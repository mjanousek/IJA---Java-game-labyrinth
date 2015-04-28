/**
 * Trida slouzici pro otevreni a nacteni zadane hry
 * 
 * @author:Martin Janousek xjanou14
 * @file: FileReader.java
 * @version: 1.1
 */


package ija.project.controler;

//Import
import ija.project.table.*;
import java.io.*;
import java.util.Scanner;


public class FileReader {
	
	/**
	 * Otevreni souboru s bludistem dle zadaneho jmena
	 * 
	 * @param 	filename jmeno bludiste ktere se ma otevrit
	 * @return 	vraci vytvorenou matici obsahujici bludiste, null v pripade chyby
	 */
	public Table openFile(String filename) {
		
		File file = new File(System.getProperty("user.dir")+"/examples/"+filename);
		//Zjisteni existence souboru
		if (!file.exists()) {
			System.err.println("[server] "+filename + " does not exist.");
			return null;
		}
		if (!(file.isFile() && file.canRead())) {
			System.err.println("[server] "+filename + " cannot be read from.");
			return null;
		}
		try{
			FileInputStream fis = new FileInputStream(file);
			Scanner s = new Scanner(fis);
			Table tab = this.readFile(s);	// Precteni obsahu souboru
			s.close();
			fis.close();
			return tab;
		}catch (IOException e) {
			System.err.println("[server] Cannot read the file");
		}
		return null;
	}
  
	/**
	 * Vytvoreni matice a jeji naplneni objekty
	 * 
	 * @param s Popisovac vstupniho souboru
	 * @return	Matice obsahujici herni pole, null v pripade chyby
	 */
	public Table readFile(Scanner s){
	   int row,col;	/** Velikost radku a sloupcu */
	   
	   try{
		   if(!s.hasNext()){	//Pokud neexistuje prvni radek chyba
			   System.err.println("[server] No size was found\n");
			   return null;
		   }
			   
		   String numbStr = s.next();
		   row = Integer.parseInt(numbStr.substring(0, 2)); // X souradnice
		   if(!numbStr.substring(2,3).equals("x")){			// Oddelovac x
			   System.err.println("[server] Missing size number devider x insted"+numbStr.substring(2,3));
			   return null;	
		   }
		   col = Integer.parseInt(numbStr.substring(3)); 	// Y souradnice
		   
		   Table table =  new Table(row,col);
		   while(s.hasNext()){
			   if(!table.insertLine(s.next()))
				   return null;
		   }  
		   
		   if(table.lineSize() != row){
			   System.err.println("[server] Wrong size rows\n");
			   return null;		   
		   }
		   System.out.println("[server] Maze size ROW:"+row+" COLOMNS:"+col);

		   return table;
	   }catch(NumberFormatException e){
		   System.err.println("[server] Wrong number format\n");
		   return null;
	   }
   }
}

/*** End of FileReader.java ***/