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
				objectFD[position][line] = new TableField(this, position,line, buffer);
				position++;
			}
			i++;
		}	
		line++;
	}
	
	
	//Vytvoreni hrace na urcite pozici
	public Player createPlayer(){
		Random rand = new Random();
		TableField fd;			
		for(int i = rand.nextInt(sizeY); i < sizeY;i++){		//Pokus o nahodne generovani polohy
			for(int j = rand.nextInt(sizeX); j < sizeX; j++){
				fd = objectFD[j][i];
				if((fd.canSeize()) == true){
					Player pl = new Player(fd.positionX(),fd.positionY(), fd);
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
