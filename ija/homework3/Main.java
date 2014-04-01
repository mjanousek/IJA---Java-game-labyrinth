/*
 * @author:Martin Janousek xjanou14, Marek Fiala, xfiala46
 * @file: Main.java
 */


package ija.homework3;

import ija.homework3.player.Player;
import ija.homework3.io.Console;
import ija.homework3.table.*;


public class Main {
	public static void main(String[] args){

		Console con = new Console();
		//zde se ceka dokud se neinicializuje hra
		if(!con.InitGame()) //ukonceni hry
			return;
		
		
		FileReader fr = new FileReader();
		Table table = fr.openFile("maze1");
		if(table == null)
			return;

		con.RunGame(table);
		
	}	
}
