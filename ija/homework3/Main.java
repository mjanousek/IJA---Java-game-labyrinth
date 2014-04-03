/*
 * @author:Martin Janousek xjanou14, Marek Fiala, xfiala46
 * @file: Main.java
 */

// Overeni nepovolene operaci napr. open na zed
// Pri nalezeni chybovyho stavu zacit jasat a ukoncit hru
// Fakt bych dal jako hlavu te konzoly neco trosku sympatictejsiho ale to je jen kosmeticka blbost
// Cely to zacit poradne okomentovavat


package ija.homework3;

//import ija.homework3.player.Player;
import java.io.IOException;

import ija.homework3.io.Console;
import ija.homework3.table.*;


public class Main {
	public static void main(String[] args){
		
        try {	
			Console con = new Console();
			String filename;
			//zde se ceka dokud se neinicializuje hra
			if((filename = con.InitGame()) == null ) //ukonceni hry
				return;
			
			FileReader fr = new FileReader();
			Table table = fr.openFile(filename);
			if(table == null)
				return;
	
			con.RunGame(table);
	    } catch (IOException ioe) {
	        System.out.println("Cannot read order");
	        System.exit(1);
	    }
	System.out.println("#THE END#");
	}	
}
