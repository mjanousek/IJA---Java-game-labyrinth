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
				objectFD[position][line] = new TableField(this, position,line, buffer);
				position++;
			}
			i++;
		}	
		line++;
	}
	
	public Player createPlayer(){
		TableField fd = objectFD[0][0];			
		for(int i = 0; i < sizeY;i++){
			for(int j = 0; j < sizeX; j++){
				fd = objectFD[j][i];
				if((fd.canSeize()) == true){
					Player pl = new Player(fd.positionX(),fd.positionY(), fd);
					fd.seize(pl);
					return pl;
				}
			}
		}				
		return null;
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
		return objectFD[x][y];
	}

}
