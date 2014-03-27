/*
 * @author: Marek Fiala, xfiala46
 * @file: TapeObject.java
 */


package ija.homework3.objects;

public abstract class TapeObject {
	
	//Test, zda je mozne obsadit policko obsahujici tento objekt
	public abstract boolean canSeize();
	
	//Otevira objekt. Vraci vysledek operace (zda se podarilo objekt otevrit)
	public abstract boolean open();
	
	//Test, zda je mozne odemknout objekt. Podmínka zavisi na implementujici tride.
	public abstract boolean canBeOpen();
	
	/*	Vytvori a vrati objekt podle zadaného typu format. Popis retezce format:
	 * w = Wall
	 * g = Gate
	 * ostatni pripady null
	 */
	public static TapeObject create(String format){
		if((format.compareTo("w")) == 0){
			TapeObject wall = new Wall();
			return wall;
		}
		else if((format.compareTo("g")) == 0){
			TapeObject gate = new Gate();
			return gate;
		}
		else
			return null;	
	}
}
