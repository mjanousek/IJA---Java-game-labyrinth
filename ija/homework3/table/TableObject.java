/** 
 * Abstraktni trida slouzici k implementaci trid Finish, Gate, Key, Wall,
 * ktere od teto tridy dedi.
 * @author:Martin Janousek xjanou14, Marek Fiala, xfiala46
 * @file: TapeObject.java
 */


package ija.homework3.table;
import ija.homework3.objects.*;

public abstract class TableObject {
	
	/**
	 * Metoda zjistujici zda lze objekt obsadit.
	 * @return boolean		- vraci true, nebo false podle toho zda lze objekt obsadit
	 */
	public abstract boolean canSeize();
	
	/**
	 * Metoda slouzici pro vypis znaku, ktery reprezentuje dany objekt.
	 * @return char			- vraci char reprezentujici dany objekt
	 */
	public abstract char symbolPrint();
	
	/**
	 * Metoda otevirajici objekt.
	 * @return boolean 		- vraci true, nebo false v pripade, ze objekt nelze otevrit
	 */
	public abstract boolean open();
	
	/**
	 * Metoda zjistujici, zda objekt lze otevrit.
	 * @return boolean 		- vraci true nebo false podle toho zda objekt lze nebo nelze otevrit
	 */
	public abstract boolean canBeOpen();
	
	/**
	 * Metoda zjistujici, zda objekt lze otevrit.
	 * @return boolean 		- vraci true nebo false podle toho zda objekt lze nebo nelze sebrat.
	 */
	public abstract boolean canBeTaken();
	
	/*	Vytvori a vrati objekt podle zadaného typu format. Popis retezce format:
	 * W = Wall
	 * G = Gate
	 * K = Key
	 * F = Finish
	 * ostatni pripady null
	 */
	/**
	 * Metoda vytvarejici instanci objektu podle zadaneho parametru.
	 * @param format		- urcuje objekt, ktery ma byt vytvoren
	 * W = Wall
	 * G = Gate
	 * K = Key
	 * F = Finish
	 * @return TableObject 	- vraci instanci vytvoreneho objektu
	 */
	public static TableObject create(String format){
		if((format.compareTo("W")) == 0){
			TableObject wall = new Wall();
			return wall;
		}
		else if((format.compareTo("G")) == 0){
			TableObject gate = new Gate();
			return gate;
		}
		else if((format.compareTo("K")) == 0){
			TableObject key =  new Key();
			return key;
		}
		else if((format.compareTo("F")) == 0){
			TableObject finish = new Finish();
			return finish;
		}
		else
			return null;	
	}
}
