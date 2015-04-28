/**
 * Trida pro klientskou komunikaci se serverem.
 * @author: Martin Janousek xjanou14
 * @file:SocketClient.java
 * @version: 1.1
 */

package ija.project.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;


public class SocketClient {

	private String hostname;
    private int port;
    private Socket sc;		//socket client
    
    private ObjectInputStream instream = null;
    private ObjectOutputStream outstream = null;

    /**
     * Konstruktor, kterym se inicializuje port a adresa serveru
     * @param hostname
     * @param port
     */
    public SocketClient(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Metoda ve ktere probiha spojeni klienta se serverem
     * 
     * @throws UnknownHostException
     * @throws IOException
     */
    public void connect() throws UnknownHostException, IOException{
        System.out.println("[client] Attempting to connect to "+hostname+":"+port);
        sc = new Socket(hostname,port);
        System.out.println("[client] Connection established");
        
        //vytvoreni vstupniho a vystupniho streamu
        outstream = new ObjectOutputStream(sc.getOutputStream());
        instream = new ObjectInputStream(sc.getInputStream());
        
        System.out.println("[client] Waiting on welcome message");
        String welcome = readMessage();
        System.out.println("[client] "+welcome);
        
    }

    /**
     * Metoda, ktera prijima vypis bludist ze serveru
     * @return pole retezcu znacici jmena souboru
     */
    public String[][] readFiles(){
    	System.out.println("[client] Reading list of files");
    	
    	String[][] listFiles = null;
    	

    	try{
    		//Pocet souboru ktery bude zaslan
    		int filescount = instream.readInt();

    		if(filescount == 0)
    			return null;
    		
    		listFiles =  new String[filescount][3];
    		//ukladani vsech souboru bludist
    		for(int i = 0; i < filescount; i++)
    		{
    		    String fileInfo= instream.readUTF();
    		    
    		    System.out.println("[client] "+fileInfo);
    		    
    		    //Prvni pozice jmeno hry, druha velikost, treti pocet hracu
    		    listFiles[i][0] = fileInfo.substring(fileInfo.indexOf("GAME:")+5,fileInfo.indexOf("|"));
    		    listFiles[i][1] = fileInfo.substring(fileInfo.indexOf("SIZE:")+5,fileInfo.indexOf("<"));
    		    listFiles[i][2] = fileInfo.substring(fileInfo.indexOf("PLAYER:")+7,fileInfo.indexOf(">"))+"/4";
    		}

    	}catch(IOException e){
    		System.err.println("[client] Unable to read list of files");
    		return null;
    	}
    	
    	return listFiles;   
    }
    
    /**
     * Metoda pro posilani zprav z klienta na server
     * @param str pozadovana zprava na zaslani
     */
    public void sendMessage(String str) {
    	try {
    		outstream.writeUTF(str);
			outstream.flush();
		} catch (IOException e) {
			System.err.println("[client] Unable to send message");
		}
    }
    
    /**
     * Metoda pro prijimani zprav ze serveru pro klienta
     * @return prectena zprava 
     */
    public String readMessage() {
    	String str = null;
    	try {
			str = instream.readUTF();
		} catch (IOException e) {
			System.err.println("[client] Unable to read message");
			return "";
		}
    	return str;
    }
    
    /**
     * Metoda pro precteni zaslaneho objektu od serveru
     * @return precteny objekt
     */
    public Object readObject() {
    	Object o = null;
    	try {
			o = instream.readObject();
		} catch (IOException e) {
			System.err.println("[client] Unable to read object");
		} catch (ClassNotFoundException e) {
			System.err.println("[client] Unable to find class");
		}
    	return o;
    }

    /**
     * Metoda, ktera vypina socket
     */
    public void stopSocket(){
    	try{
    		sc.close();
    		System.out.println("[client] Socket was shutted down");
    	} catch (IOException e) {
    		System.err.println("[client] Unable to close socket");
        }
    }
}
/*** end of SocketClient.java ***/