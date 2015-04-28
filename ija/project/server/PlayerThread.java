/**
 * Vlakno ve kterem bezi komunace serveru s jednim klientem
 *  
 * @author:Martin Janousek xjanou14
 * @file: PlayerThread.java
 * @version: 1.1
 */

package ija.project.server;

import ija.project.controler.Console;
import ija.project.controler.FileReader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.Timer;

import ija.project.table.Table;

public class PlayerThread implements ActionListener,Runnable{
	
    private ObjectInputStream instream = null;
    private ObjectOutputStream outstream = null;
    private Socket con;
    private Timer timer;	/** Casovac pro zasilani herniho planu po urcitem intervalu **/
    private Console console = null;
    private Table table = null;
    /** Mapa rozehranych her **/
    private static Map<String, Table> runningGames = new HashMap<String, Table>();
    private int idPlayer;
    private String respond = "";
    private String gameName;
    private boolean aLive = true;
    private enum GameStatus {NEWGAME,CONNECT,END,ERROR};
  
    /**
     * Inicializace socketu
     * 
     * @param sock
     */
    public PlayerThread (Socket sock,int id){
    	idPlayer = id;
    	con = sock;
    }
    
    /**
     * Metoda pro vyber hry
     * @param command retezec znacici zda je pozadovana nova hra nebo pripojeni ke stavajici
     */
    private GameStatus chooseGame(String command){
        //zacatek hrani
        if(command.equals("NewGame")){
            System.out.println("[server] New game - sending files");
        	
            sendAvaibleGames();
            
            command = readMessage();
            if(command.equals("close"))
            	return GameStatus.END;
            
            FileReader fr = new FileReader();
            if((table = fr.openFile(command))==null)
            	return GameStatus.ERROR;
            
    		runningGames.put(command, table);
    		//Prirazeni jmena rozehrane hry
    		gameName = command;
    		
            command = readMessage();
            if(command.equals("close"))
            	return GameStatus.END;
            
            int number;
            //Vyparsovani cisla znacici zpomaleni
            try{
            	number = Integer.parseInt(command.substring(command.indexOf("SLOW:")+5,command.indexOf("||")));
                table.setSlowTime(number);
                System.out.println("[server] Slowtime: "+number+" in game: "+gameName);
	        }catch(StringIndexOutOfBoundsException e){
	        	System.err.println("[server] Wrong message format");
	        }
    		
    		//vytvoření consoly pokud je potřeba
    		//console = new Console(table,table.getSlowTime());
            int slowTime = table.getSlowTime();
            console = new Console(table,slowTime);
            
            try{
            	number = Integer.parseInt(command.substring(command.indexOf("KEEP:")+5,command.indexOf(">")));
        		//Spustni hlidace 
        		System.out.println("[server] Count of guards "+number);
        		console.startKeeper(number);
            }catch(StringIndexOutOfBoundsException e){
            	System.err.println("[server] Wrong message format");
            }

    	    return playGame();
        }
        else if(command.equals("ConnectToGame")){
        	System.out.println("[server] Connect to game - sending games");
        	
            sendActualGames();
            
            command = readMessage();
            if(command.equals("close"))
            	return GameStatus.END;
            
            table = runningGames.get(command);
            //Ziskani jmena prirazene hry
            gameName = command;
            
            // pokud bude 4 musim oddelat -> mas 4 hraci
            if(table.getPlayerCount() >= 3)
            	runningGames.remove(command);
            
    		//vytvoření consoly pokud je potřeba
    		console = new Console(table,table.getSlowTime());
    		
            return playGame();
        }
        else if(command.equals("close")){
        	return GameStatus.END;
        }
        else
        	System.err.println("[server] Wrong order");
        	System.exit(0);
        	
        return GameStatus.ERROR;
    }
    
    
    /**
     * Spusteni hry 
     * @return stav hry
     */
    public GameStatus playGame(){
    	
		console.AddPlayer(idPlayer);
		timer = new Timer(500, this);
		System.out.println("[server] Player "+idPlayer+" has join the game"+gameName);
		respond = "You are the "+console.getPlayerColor()+ " player";
	    timer.start();
	    
    	String command;
		//dokud command neni close tak pokracuj
        while(!((command = readMessage()).equals("close"))){
        	
        	//V pripade, ze hrac si uz nepreje hrat tuto hru a chce hrat jinou
        	if(command.equals("NewGame"))
        		return leftGame(GameStatus.NEWGAME);
        	if(command.equals("ConnectToGame"))
        		return leftGame(GameStatus.CONNECT);
        	
        	System.out.println("[server] Player: "+idPlayer+" command " + command);
        	respond = console.MoveFigure(command); //Provedeni prikazu
        	
        	//Pokud je vitez
        	if(respond.contains("WINNER")){
        		timer.stop();
        		sendTable();
        		table.stopTime();
        		System.out.println("[server] Game "+gameName+" was finished");
        		if(runningGames.containsKey(gameName))
        			runningGames.remove(gameName);
        		return GameStatus.END;
        	}
        	
        	// Pokud konec
        	if(table.getMsgForAll().contains("END")){
        		timer.stop();
        		return GameStatus.END;
        	}
        }
        
    	if(aLive == true){
    		return leftGame(GameStatus.END);
    	}
    	
    	return GameStatus.END;
    }
    
    /**
     * Dobrovolne opusteni hry, status je promenna znacici,
     * zda si hrac preje ukoncit celou hru nebo zda chce zapnout novou
     * @param status
     * @return status 
     */
    private GameStatus leftGame(GameStatus status){
        timer.stop();
        console.removePlayer();
        if(table.getPlayerCount() == 3)
        	runningGames.put(gameName,table);
        System.out.println("[server] Player "+idPlayer+" has left the game");
        return status;
    }

    /**
     * Po uplynuti casu v timeru posila aktualni stav bludiste
     */
    @Override
    public void actionPerformed(ActionEvent e) {
    	if(aLive == true){
    		if(console.amIAlive() != true){
    			aLive = false;
    			respond = "YOU was MURDERED!";
    		}
    	}
    	
    	sendTable();

    	//Pokud je hra dohrana
    	if(table.getPlayerCount() == 0){
			timer.stop();
			table.stopTime();
    		table.rewriteMsgForAll("ALL players has been killed\nEND");
    		System.out.println("[server] Game "+gameName+" was finished");
    		if(runningGames.containsKey(gameName))
    			runningGames.remove(gameName);
    		sendTable();
    		return;
    	}
    }
    
    /**
     * Posilani vsech nazvu souboru obsahujici bludiste
     */
    public void sendAvaibleGames(){
    	String directory = System.getProperty("user.dir")+"/examples/";
    	File[] files = new File(directory).listFiles();
    	
    	try{
    		//Posilani poctu nazvu souboru
	    	outstream.writeInt(files.length);
	    	outstream.flush();
	    	
	    	//posilani nazvu
	    	for(File file : files)
	    	{
	    		FileInputStream fis = new FileInputStream(file);
	    		Scanner s = new Scanner(fis);
	    		
	    	    sendMessage("GAME:"+file.getName()+"|SIZE:"+getSize(s)+
    	    			"<PLAYER:0>");
	    	    s.close();
	    	    fis.close();
	    	}
	    	
		}catch(IOException e){
			System.err.println("[server] Unable to send list of game");
		}
	}
    
    public String getSize(Scanner s){
    	String numbStr = s.next();
	   if(!numbStr.substring(2,3).equals("x")){			// Oddelovac x
		   System.out.println("[server] Missing size number devider x insted"+numbStr.substring(2,3));
		   return "Unknown";	
	   }
	   return numbStr;
    }
    
    /**
     * Odesila jmena her ktere jsou aktualne rozehraty
     */
    public void sendActualGames(){
    	
    	try{
	    	outstream.writeInt(runningGames.size());
	    	outstream.flush();
	    	
	    	//posilani souboru
	    	for(String name : runningGames.keySet())
	    	{
	    	    sendMessage("GAME:"+name+"|SIZE:"+runningGames.get(name).getSizeRow()+
	    	    			"x"+runningGames.get(name).getSizeCol()+
	    	    			"<PLAYER:"+runningGames.get(name).getPlayerCount()+">");
	    	}
	
		}catch(IOException e){
			System.err.println("[server] Unable to send list of game");
		}
    }
    
    /**
     * Metoda pro posilani zprav
     * @param str zprava k zaslani
     */
    private void sendMessage(String str) {
    	try {
    		outstream.writeUTF(str);
			outstream.flush();
		} catch (IOException e) {
			System.err.println("[server] Unable to send message");
		}
    }

    /**
     * Metoda pro precteni zpravy
     * @return prectena zprava
     */
    private String readMessage() {
    	String str = "";
    	try {
			str = instream.readUTF();
		} catch (IOException e) {
			System.err.println("[server] Unable to read message");
		}
    	return str;
    }
    
    /**
     * Metoda pro zaslani bludiste
     * a take zprav pro vsechny hrace i pro jednotlivce
     */
    private void sendTable() {
    	try {
			table.rewriteMsg(respond);
			respond = "";
    		outstream.writeObject(table);
			outstream.flush();
			outstream.reset();
		} catch (IOException e) {
			System.err.println("[server] Unable to send table");
			timer.stop();
		}
    }
    
	@Override
	public void run() {
        try {
        	GameStatus stat;
        	instream = new ObjectInputStream(con.getInputStream());
			outstream = new ObjectOutputStream(con.getOutputStream());
			sendMessage("Welcome on our server!");
			String command = "";
	        command = readMessage();
	        // Prvni hra
	        stat = chooseGame(command);
	        
	        // Moznost zahajeni z jine hry nove hry
			while(true){
				if(stat == GameStatus.END){
					System.out.println("[server] Client "+idPlayer+" has disconected");
					break;
				}
				else if(stat == GameStatus.ERROR){
					System.err.println("[server] Error has appiered");
					break;
				}
				else if(stat == GameStatus.NEWGAME)
					stat = chooseGame("NewGame");
				else if(stat == GameStatus.CONNECT)
					stat = chooseGame("ConnectToGame");
			}
			
			// Uzavreni socketu
			con.close();
		} catch (IOException|NullPointerException e) {
			System.err.println("[server] Unspecified execption has appiered\n (IO or NullPointer Exception)");
		}
	}
}
/*** end of PlayerThread.java ***/