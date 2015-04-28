/**
 * Trida reprezentujici hrace a celou jeho funkcionalitu.
 * @author: Martin Janousek xjanou14
 * @file:Player.java
 * @version: 1.1
 */

package ija.project.figure;

import java.awt.Color;

import ija.project.table.*;

public class Player extends Figure{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int key = 0;
	protected boolean alive;
	private Color myColor;
	private int moveCount = 0;
	private int ID;
	long start;
	
	/**
	 * Metoda pro inicializaci hrace na policko a ulozeni jeho souradnic 
	 * 
	 * @param row 	cislo radku na ktere je hrac vytvoren
	 * @param col 	cislo sloupce na kterem je hrac vytvoren
	 * @param f 	policko na kterem je hrac vytvoren
	 * @param sight	pocatecni pohled hrace
	 */
	public Player(int row,int col, TableField f,int sight,Color color,int ID){
		super(row,col,f,sight);
		this.ID = ID;
		myColor = color;
		alive = true;
		start = System.currentTimeMillis();
	}

	/**
	 * Prida urcity pocet klidu hraci.
	 * 
	 * @param n		- udava pocet klicu
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
	
	public Color getColor(){
		return myColor;
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
				moveCount++; //Pocitadlo kroku
				return true;
			}	
			return false;
		}
        return true;
	}
	
	/**
	 * Overeni zda postavicka nestoji na miste cile
	 * @return true hrac vyhral, false hrac dosud nevyhral
	 */
	public boolean isWinner(){
		return field.isFinish();
	}
	
	/**
	 * Overeni zda postavicka zije
	 * @return truezije, false hrac nezije
	 */
	public boolean amIAlive(){
		return alive;
	}
	
	/**
	 * Zabiti hrace
	 */
	public void kill(){
		alive = false;
	}
	
	/**
	 * Ziskani ID postavicky
	 * @return ID
	 */
	public int getID(){
		return ID;
	}
	
	/**
	 * Pocet projitych policek
	 * @return pocet policek
	 */
	public int getMoveCount(){
		return moveCount;
	}
	
	/**
	 * Ziskani pocatecniho casu kdy se hrac pripojil
	 * @return pocatecni cas
	 */
	public long getStartTime(){
		return start;
	}
}

/*** End of Player.java ***/
