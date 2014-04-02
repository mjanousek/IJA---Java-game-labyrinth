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
	   int x,y;
	   

	   String numbStr = s.next();
	   x = Integer.parseInt(numbStr.substring(0, 2)); // 1st part
	   if(!numbStr.substring(2,3).equals("x"))
		   System.out.println("Missing size number X\n"+numbStr.substring(2,3));
	   y = 	Integer.parseInt(numbStr.substring(3)); // 2nd part
	   
	   System.out.println("X:"+x+" Y:"+y+"\n");
	      
	   Table table =  new Table(x,y);
	      
	   System.out.println(x + " "+ y); 
	   while(s.hasNext()){
		   table.insertLine(s.next());
	   }  
	  
	   return table;
   }
}

