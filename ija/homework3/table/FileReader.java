/*
 * @author:Martin Janousek xjanou14, Marek Fiala, xfiala46
 * @file: FileReader.java
 */


package ija.homework3.table;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;



public class FileReader {
	public Table openFile(String filename) {
		
		File file = new File(System.getProperty("user.dir")+"/src/ija/homework3/maze/"+filename);
		//Zjisteni existence
		if (!file.exists()) {
			System.out.println(filename + " does not exist.");
			return null;
		}
		if (!(file.isFile() && file.canRead())) {
			System.out.println(filename + " cannot be read from.");
			return null;
		}
		try{
			FileInputStream fis = new FileInputStream(file);
			Scanner s = new Scanner(fis);
			Table tab = this.readFile(s);
			s.close();
			fis.close();
			return tab;
		}catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
  
	//Naplni tabulku objekty
   public Table readFile(Scanner s){
	   int row,col;
	   
	   try{
		   if(!s.hasNext()){	//Pokud neexistuje prvni radek chyba
			   System.out.println("No size was found\n");
			   return null;
		   }
			   
		   String numbStr = s.next();
		   row = Integer.parseInt(numbStr.substring(0, 2)); // X souradnice
		   if(!numbStr.substring(2,3).equals("x"))
			   System.out.println("Missing size number devider x insted"+numbStr.substring(2,3));
		   col = Integer.parseInt(numbStr.substring(3)); // Y souradnice
		   
		   //Prevod na pseudo matematicke souradnice x - sloupce y - radky
		   Table table =  new Table(row,col);
		   while(s.hasNext()){
			   if(!table.insertLine(s.next()))
				   return null;
		   }  
		   
		   if(table.lineSize() != row){
			   System.out.println("Wrong size rows\n");
			   return null;		   
		   }
		   System.out.println("Maze size ROW:"+row+" COLOMNS:"+col);

		   return table;
	   }catch(NumberFormatException e){
		   System.out.println("Wrong number format\n");
		   return null;
	   }
   }
}

