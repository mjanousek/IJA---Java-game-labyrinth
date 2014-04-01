/*
 * @author:Martin Janousek xjanou14, Marek Fiala, xfiala46
 * @file: Table.java
 */

package ija.homework3.table;
import ija.homework3.player.*;

public class Table {
	
	protected TableField[][] objectFD;
	protected Player[] figureFD;
	int sizeX,sizeY;
	int line = 0;
	
	//Konstruktor ktery inicializuje pole na danou velikost 
	public Table(int x,int y){
		objectFD = new TableField[x][y];
		sizeX = x;
		sizeY = y;
	}
	
	//Vlozeni objetku do daneho radku
	public void insertLine(String format){
		int i = 0;
		int position = 0;
		
		if(format.length() > sizeX){
			System.out.println("Pretahl jsi po X!\n");
			return;
		}
		
		if(line >= sizeY){
			System.out.println("Pretahl jsi po Y!\n");
			return;
		}
		
		while(i < format.length()){
			if(format.charAt(i) != ' '){
				String buffer = "" +format.charAt(i);
				objectFD[position][line] = new TableField(this, position, buffer);
				position++;
			}
			i++;
		}	
		line++;
	}
	

	//Vypis bludiste
	public void printTable(){
		for(int i = 0; i < sizeY;i++){
			for(int j = 0; j < sizeX; j++){
				System.out.print(objectFD[j][i].printObj());
			}
			System.out.print('\n');
		}	
	}

	/*Vrati policko na indexu i. Pokud je index mimo rozsah, vrati null*/
/*	public TapeField fieldAt(int i){
		if(i > (size-1) || i < 0)
			return null;

		return objectFD[i];
	}
	
		Vytvori a vraci hlavu s identifikatorem i a umístí ji na prvni volne policko zleva (volne podle operace
	canSeize()). Pokud nelze vlozit vraci null.
	public TapeHead createHead(int i){
		TapeField fd = objectFD[0];
		
		do{
			if((fd.canSeize()) == true){
				TapeHead head = new TapeHead(fd.position(), fd);
				fd.seize(head);
				return head;
			}
				
			
		}while((fd = fd.rightField())!= null);
			
		return null;
	}	*/
}
