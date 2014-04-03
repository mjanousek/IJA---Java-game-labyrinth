/**
 * Trida slouzici jako rozhrani pro dalsi funkcnost 
 * @author:Martin Janousek xjanou14, Marek Fiala, xfiala46
 * 
 **/



// Cely to zacit poradne okomentovavat


package ija.homework3;

//import ija.homework3.player.Player;
import java.io.IOException;

import ija.homework3.table.Table;
import ija.homework3.io.*;


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
