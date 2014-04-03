/**
 * Trida reprezentujici hrace a celou jeho funkcionalitu.
 * @author: Martin Janousek xjanou14, Marek Fiala, xfiala46
 * @file:Player.java
 * @version: 1.1
 */

package ija.homework3.player;

import ija.homework3.table.*;

public class Player {
	protected int row;
	protected int col;
	protected int key = 0;
	protected TableField field = null;
	protected int sight = 0; //inicializovano nahoru

	
	/**
	 * Metoda pro inicializaci hrace na policko a ulozeni jeho souradnic 
	 * 
	 * @param row 	cislo radku na ktere je hrac vytvoren
	 * @param col 	cislo sloupce na kterem je hrac vytvoren
	 * @param f 	policko na kterem je hrac vytvoren
	 * @param sight	pocatecni pohled hrace
	 */
	public Player(int row,int col, TableField f,int sight){
		this.row = row;
		this.col = col;
		field = f;
		this.sight = sight; 
	}

	/**
	 * Vraci policko na kterem se hrac aktualne nachazi
	 * 
	 * @return policko na kterem se hrac nachazi
	 */
	public TableField seizedField(){
		return field;
	}

	/**
	 * Prida urcity pocet klidu hraci.
	 * 
	 * @param int n		- udava pocet klicu
	 */
	public void addKeys(int n){
		key += n;
	}
	
	/**
	 * Vrati pocet klicu.
	 * 
	 * @return pocet klicu
	 */
	public int keyCount(){
		return key;
	}

	/**
	 * Zajistuje rotaci hrace vlevo
	 */
	public void rotateLeft(){
		if(--sight < 0)
			sight = 3;
	}
	
	/**
	 * Zajistuje rotaci hrace vpravo
	 */
	public void rotateRight(){
		if(++sight > 3)
			sight = 0;
	}
	
	/**
	 * Vraci symbol podle toho, kam se hrac diva
	 * 
	 * 0	- ^
	 * 1	- >
	 * 2	- v
	 * 3	- <	
	 * 
	 * @return znak, kam se hrac diva
	 */
	public char symbolSight(){
		switch(sight){
		case 0: return '^';
		case 1: return '>';
		case 2: return 'v';
		case 3: return '<';
		default: return 'X';		
		}
	}
	
	
	/**
	 * Vrati polecko kam se hrac diva. (Policko pred nim.)
	 * @return	policka pred hracem
	 */
	public TableField fieldBeforeSight(){
		if(sight == 0) 		//nahoru
			return field.fieldOnPosition(row-1,col);
		else if(sight == 1)	//doprava
			return field.fieldOnPosition(row,col+1);
		else if(sight == 2)	//dolu
			return field.fieldOnPosition(row+1,col);
		else if(sight == 3)	//doleva
			return field.fieldOnPosition(row,col-1);
		
		return null;
	}
	
	/**
	 * Metoda, ktera sebere klic z policka.
	 * @return true pri uspechu, false pri neuspechu
	 */
	public boolean takeKey(){
		TableField fd = fieldBeforeSight();
		if(fd.tryTakeKey()){
			key++;		//Podarilo se vzit klic
			return true;
		}
		else
			return false;		
	}
	
	/**
	 * Metoda, ktera otevre branu, pokud hrac vlastni alespon jeden klic
	 * @return true pri uspesnem otevreni a flase pri neuspechu
	 */
	public boolean openGate(){
		TableField fd = fieldBeforeSight();
		if(key > 0 && fd.open()){
			key--;
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Funkce zajistujici pohyb hrace vpred. Pri nespechu zustane hrac na stejnem policku
	 * na jekm se nachazi
	 * 
	 * @return vraci true pri uspesnem pohybu vpred a false pri neuspechu
	 */
	public boolean move(){
		TableField fd = fieldBeforeSight();
        if(fd != null){
			if((fd.canSeize()) == true){
				fd.seize(this);		//Obsazeni policka
				field.leave();      //Uklid stareho
				row= fd.positionRow();	//Nahrani novych souradnic
				col = fd.positionCol();
				field = fd;
				return true;
			}				
		}
        return false;
	}
	
	//Overeni vyhry
	public boolean isWinner(){
		return field.isFinish();
	}
}

/*** End of Player.java ***/