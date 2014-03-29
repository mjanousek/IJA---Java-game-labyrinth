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
  public void openFile() {
    File file = new File("/home/marek/workspace1/IJA3/src/bludiste");
    if (!file.exists()) {
      System.out.println("neco" + " does not exist.");
      return;
    }
    if (!(file.isFile() && file.canRead())) {
      System.out.println(file.getName() + " cannot be read from.");
      return;
    }
    try {
      FileInputStream fis = new FileInputStream(file);
      Scanner s = new Scanner(fis);
      
      this.readFile(s);
      s.close();
      fis.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
   public void readFile(Scanner s){
	  if(!s.hasNextInt())
	   	  System.out.println("Missing size number\n");
	  int x = s.nextInt();
	      
	  if(!s.hasNextInt())
		  System.out.println("Missing size number\n");
	  int y = s.nextInt();
	      
	  Table table =  new Table(x,y);
	      
	  System.out.println(x + " "+ y); 
	  while(s.hasNext()){
		  table.insertLine(s.next());
	  }  
	  
      table.printTable();
   }
}

