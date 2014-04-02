/*
 * @author:Martin Janousek xjanou14, Marek Fiala, xfiala46
 * @file: Table.java
 */

package ija.homework3.table;
import ija.homework3.player.*;

import java.util.Random;

public class Table {
	
	protected TableField[][] objectFD;
	protected Player[] figureFD;
	protected int sizeRow,sizeCol;
	protected int line = 0;
	
	//Konstruktor ktery inicializuje pole na danou velikost 
	public Table(int row,int col){
		objectFD = new TableField[row][col];
		sizeRow = row;
		sizeCol = col;
	}
	
	//Vlozeni objetku do daneho radku
	public boolean insertLine(String format){
		int i = 0;
		int position = 0;
		
		if(format.length() != sizeCol){
			System.out.println("Spatna velikost souradnic X\n");
			return false;
		}
		
		if(line >= sizeRow){
			System.out.println("Spatna velikost souradnic Y\n");
			return false;
		}
		
		while(i < format.length()){
			if(format.charAt(i) != ' '){
				String buffer = "" +format.charAt(i);
				objectFD[line][position] = new TableField(this,line,position, buffer);
				position++;
			}
			i++;
		}	
		line++;
		return true;
	}
	
	//Vraci cislo ktere znaci pocet skutecne nactenych radku
	public int lineSize(){
		return line;
	}
	
	//Vytvoreni hrace na urcite pozici
	public Player createPlayer(){
		Random rand = new Random();
		TableField fd;			
		for(int i = rand.nextInt(sizeRow); i < sizeRow;i++){		//Pokus o nahodne generovani polohy
			for(int j = rand.nextInt(sizeCol); j < sizeCol; j++){
				fd = objectFD[i][j];
				if((fd.canSeize()) == true && !fd.isFinish()){	//Pozice x, y, policko a dale nahodne cislo reprezentujici pohled
					Player pl = new Player(fd.positionRow(),fd.positionCol(), fd,rand.nextInt(3));
					fd.seize(pl);
					return pl;
				}
			}
		}				
		return createPlayer();		//Pokud poloha nenalezena dalsi zanoreni rekurze
	}
	

	//Vypis bludiste
	public void printTable(){
		for(int i = 0; i < sizeRow;i++){
			for(int j = 0; j < sizeCol; j++){
				System.out.print(objectFD[i][j].printObj());
			}
			System.out.print('\n');
		}	
	}

	/*Vrati policko na indexu i. Pokud je index mimo rozsah, vrati null*/
	public TableField fieldAt(int x, int y){
		//chybi osetreni hranic
		if(x > sizeCol || x < 0 || y > sizeRow || y < 0)
			return null;
		return objectFD[x][y];
	}

}
