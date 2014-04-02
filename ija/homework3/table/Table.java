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
	protected int sizeX,sizeY;
	protected int line = 0;
	
	//Konstruktor ktery inicializuje pole na danou velikost 
	public Table(int x,int y){
		objectFD = new TableField[x][y];
		sizeX = x;
		sizeY = y;
	}
	
	//Vlozeni objetku do daneho radku
	public boolean insertLine(String format){
		int i = 0;
		int position = 0;
		
		if(format.length() != sizeX){
			System.out.println("Spatna velikost souradnic X\n");
			return false;
		}
		
		if(line >= sizeY){
			System.out.println("Spatna velikost souradnic Y\n");
			return false;
		}
		
		while(i < format.length()){
			if(format.charAt(i) != ' '){
				String buffer = "" +format.charAt(i);
				objectFD[position][line] = new TableField(this, position,line, buffer);
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
		for(int i = rand.nextInt(sizeY); i < sizeY;i++){		//Pokus o nahodne generovani polohy
			for(int j = rand.nextInt(sizeX); j < sizeX; j++){
				fd = objectFD[j][i];
				if((fd.canSeize()) == true){	//Pozice x, y, policko a dale nahodne cislo reprezentujici pohled
					Player pl = new Player(fd.positionX(),fd.positionY(), fd,rand.nextInt(3));
					fd.seize(pl);
					return pl;
				}
			}
		}				
		return createPlayer();		//Pokud poloha nenalezena dalsi zanoreni rekurze
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
	public TableField fieldAt(int x, int y){
		//chybi osetreni hranic
		if(x > sizeX || x < 0 || y > sizeY || y < 0)
			return null;
		return objectFD[x][y];
	}

}
