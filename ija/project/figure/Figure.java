/**
 * Abstraktni trida ze ktere jsou odvozeny vsechny ostatni figurky
 * @author: Martin Janousek xjanou14
 * @file:Figure.java
 * @version: 1.1
 */

package ija.project.figure;

import java.io.Serializable;

import ija.project.table.TableField;

public abstract class Figure implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int row;
	protected int col;
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
	public Figure(int row,int col, TableField f,int sight){
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
	public abstract char symbolSight();
	
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
	 * Funkce zajistujici pohyb hrace vpred. Pri nespechu zustane hrac na stejnem policku
	 * na jekm se nachazi
	 * 
	 * @return vraci true pri uspesnem pohybu vpred a false pri neuspechu
	 */
	public abstract boolean move();
}
/*** end of Figure.java ***/