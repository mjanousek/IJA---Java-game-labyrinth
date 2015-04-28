/**
 * Trida ovladajici samotnou hru, nacitani samotnych prikazu a jejich spousteni 
 * @author:Martin Janousek xjanou14
 * @file: Console.java
 * @version: 1.1
 */

package ija.project.controler;

import java.awt.Color;

import ija.project.figure.*;
import ija.project.table.*;

public class Console implements Runnable{

    private String str = "";	/** Retezec obsahujici hodnotu ze prectenou ze vstupniho terminalu */
    private Table table; 		/** Matice reprezentujici hraci plochu */
    private Player pl;		/** Figurka hrace */
    private GameState status = GameState.VALIDINPUT;
    private Keeper[]kp;
    private Thread keeperThread;
    private int slowTimeKeeper;
    GameState gs = GameState.VALIDINPUT;
    private int slowTime = 500;
    private boolean pGo = false;
    
    public Console(){}
    
    /**
     * Metoda vola metodu ReadInput pro nacteni prikazu, metodu runCommand pro spusteni prikazu a
     * kontroluje, zda nebyl zadan prikaz close
     * 
     * @param table 		- zde je predano nactene bludiste
     */
    public Console(Table table,int s){
    	this.table = table; 
    	slowTime = s*100;
    }
    
    
    /**
     * Nasazeni hlidace
     */
    public void startKeeper(int number){
    	if(number == 0)
    		return;
    	kp = new Keeper[number];
    	for(int i = 0; i < number;i++){
			kp[i] = table.createKeeper();	
			keeperThread = new Thread(this);
			keeperThread.start();
    	}
    	//Aby se kazdy hlidac pohyboval v jiny cas
    	slowTimeKeeper = slowTime/number;
    }
    
    /**
     * Nasazeni hrace
     */
    public void AddPlayer(int id){
    	this.pl = table.createPlayer(id);
    }
    
    /**
     * Odstraneni hrace 
     */
    public void removePlayer(){
		pl.seizedField().leave();
		pl.kill();
		table.leavePlayer(pl);
    }
    
    /**
     * Metoda pro informovani hrace o jeho barve figurky
     * @return nazev barvy
     */
    public String getPlayerColor(){
    	Color test = pl.getColor();
    	
    	if		(test.equals(new Color( 17,  0,255))) return "Blue";
    	else if	(test.equals(new Color(250,  0,  0))) return "Red";
    	else if	(test.equals(new Color(  0,197,205))) return "Cyan";
    	else if	(test.equals(new Color(255, 0, 255))) return "Violate";
    	else	return "Unknown";
    }
    
    /**
     * Zjisteni zda je hrac nazivu
     * @return true pokud je ziv false pokud nikoliv
     */
    public boolean amIAlive(){
    	return pl.amIAlive();
    }
    
    /**
     * Metoda pro overeni pred a po pohybu hrace
     * @param str prikaz
     * @return zpetna vazba
     */
    public String MoveFigure(String str){
    	if(pl.amIAlive() != true)
    		return "Dead man can't speak!";
    	if(pGo && !(str.equals("stop"))){
    		return GameState.INVALIDINPUT.toString();
    	}
    	
    	
    	status = runCommand(str);
    	
		if(status == GameState.WINNER){
			table.rewriteMsgForAll("Player "+ pl.getID()+" is WINNER!!!\nEND");
			table.stopTime();
		}
			
		if(status == GameState.KEYSCOUNT)
			return ("You have: "+pl.keyCount()+((pl.keyCount() == 1)?" key":" keys"));
		
		return status.toString();	
    }


    /**
     * Metoda foUntilStop() slouzi pro chuzi pomoci sekvence go-stop.
     * @return GameState 		- vraci uspesnost prikazu
     */
    public GameState goUntilStop(){
    	new SimpleThread("go").start();
    	return gs;    	
    }
    /**
     * Metoda spousti prikaz nacteny ze standartniho vstupu a ulozeny v instancni promenne str
     * 
     * @return GameState stav hry po provedeni prikazu		-
     */
    public GameState runCommand(String str){
           	switch(str){
            		//-------------------------------------------prikazy hrace
            		case "left":
            			pl.rotateLeft();
	    				return GameState.VALIDINPUT;
	    			case "right":
            			pl.rotateRight();
	    				return GameState.VALIDINPUT;
	    			case "step":
	    				if(pl.move()){
	    					if(pl.isWinner())
	    						return GameState.WINNER;
	    					else
	    						return GameState.VALIDINPUT;
	    				}
	    				else
	    					return GameState.CANTMOVE;
	    				
	    			case "go":
	    				return goUntilStop();	    					
	    			
	    			case "stop":
	    				this.str = "stop";
	    				return GameState.VALIDINPUT;
	    				
	    			case "keys":
	    				return GameState.KEYSCOUNT;
	    			
	    			case "take":
	    				if(pl.takeKey())
	    					return GameState.VALIDINPUT;
	    				else
	    					return GameState.NOKEY;
	    			
	    			case "open":
	    				if(pl.keyCount() <= 0)
	    					return GameState.NULLKEY;
	    				if(pl.openGate())
	    					return GameState.VALIDINPUT;
	    			//-------------------------------------------herni prikazy
	    				
	    			case "show":
	    				table.printTable();
	    				return GameState.VALIDINPUT;
	    				
	    			default:
	    				break;
	        }
    	    
            return GameState.INVALIDINPUT;
    }
    
    /**
     * Vlakno pro pohyb hlidace
     */
	@Override
	public void run() {
		try { 
			Thread.sleep(slowTime);
		} catch (InterruptedException e) { }
		
		while(true){
			for(Keeper p :kp){
				//p.move(slowTimeKeeper);
				p.move();
				try {
					Thread.sleep(slowTimeKeeper);
				} catch (InterruptedException e) { }
			}
		}
	}
    
    /**
     * Enumerace GameState je vycet stavu hry, do kterych se da dostat.
     */
    public enum GameState {
    	INTRO {
    		@Override
    		public String toString() {
    			return "   Welcome in Labyrint escape v1.1\n Before start choose the game you want.";
    		}
    	},
    	WELCOME {
    		@Override
    		public String toString() {
    			return "Let the game begin!";
    		}
    	},
        CANTMOVE {
            @Override
            public String toString() {
                return "You can't move, field before you is blocked!";
            }
        },
        NOKEY {
        	@Override
        	public String toString() {
        		return "You can't take key, key's not found!";
        	}
        },
        NULLKEY {
        	@Override
        	public String toString() {
        		return "You can't unlock, you don't have keys!";
        	}
        },
        VALIDINPUT {
        	@Override
        	public String toString() {
        		return "Valid input";
        	}
        },
        WINNER {
            @Override
            public String toString() {
            	return "You are the WINNER!!!!";
            }
        },
        INVALIDINPUT {
            @Override
            public String toString() {
            	return "Invalid input";
            }
        },
        KEYSCOUNT,
        PLAYERKILLER {
            @Override
            public String toString() {
            	return "You was KILLED!!!! :-(";
            }
        };
    }
    
    /**
     * Vlakno pro sekvenci prikazu go-stop. Po zdani prikazu GO hrac pokracuje dokun nenarazi, nebo dokud se nezada prikaz STOP.
     * Pri zdani jineho prikazu než STOP zahlási console neplatny vtupni prikaz.
     * 
     * @author martin
     *
     */
    public class SimpleThread extends Thread {
    	Console console = null;
    	
    	public SimpleThread(String str) {
    		super(str);
    	}
   
    	public void run() {
    		str = "";
        	pGo = true;
    		while(pGo){
    			//Pokud neni zivy nemuze se hybat
    			if(pl.amIAlive() != true)
    				break;
    			
    			System.out.println("[server] Player: "+ pl.getID()+" Move for one position");

    			if(pl.move() == false){
	    			gs = GameState.CANTMOVE;
	    			pGo=false;
	    			break;
	    		}
	    		
				if(pl.isWinner()){
					gs = GameState.WINNER;
					table.rewriteMsgForAll("Player "+ pl.getID()+" is WINNER!!!\nEND");
					table.stopTime();
					pGo=false;
					break;
				}
				
				try {
					Thread.sleep(slowTime);
						if(str.equals("stop")){
							gs = GameState.VALIDINPUT;
							pGo = false;
						}
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
        	}
	    } 
    }
}

/*** End of Console.java ***/