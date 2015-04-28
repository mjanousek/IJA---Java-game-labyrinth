/**
 * Hlavni soubor serveru umoznujici zadani cisla portu a pripojeni klientu 
 *  
 * @author:Martin Janousek xjanou14
 * @file: SocketServer.java
 * @version: 1.1
 */

package ija.project.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer{
    
    private ServerSocket serverSocket;
    private int port;
    private Socket ss;	//socket server

    private int playersCount;
    
    /**
     * Konstruktor, ktery provadi navazani na port
     * @param port cislo portu
     */
    public SocketServer(int port) {
        this.port = port;
    }
    
    /**
     * Zahajeni funkce serveru
     * @throws IOException
     */
    public void start() throws IOException {
        System.out.println("[server] Starting the socket server at port:" + port);
        serverSocket = new ServerSocket(port);
        serverSocket.setReuseAddress(true);
        while(true){
	        //Listen for clients. Block till one connects
	        System.out.println("[server] Waiting for clients another client...");
	        ss = serverSocket.accept();
	        
			new Thread(new PlayerThread(ss,playersCount),"Player"+playersCount).start();
			playersCount++;
        }
    }
    
       
    /**
    * Creates a SocketServer object and starts the server.
    *
    * @param args
    */
    public static void main(String[] args) {
        // Setting a default port number.
        int portNumber = 8011;
        
        try {
            // initializing the Socket Server
        	if(args.length != 1){
        		String portNum = "9500";
        		portNumber =  Integer.parseInt(portNum);
        	}else{
        		portNumber = Integer.parseInt(args[0]);
        	}
            SocketServer socketServer = new SocketServer(portNumber);
            socketServer.start();
           
        } catch (IOException e) {
        	System.err.println("[server] IOEXception");
        } catch(IllegalArgumentException error){
        	System.err.println("[server] Please write valid number of port");
        }
    }
}
/*** end of SocketServer.java ***/
