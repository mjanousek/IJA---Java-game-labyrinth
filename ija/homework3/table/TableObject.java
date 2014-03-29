/*
 * @author:Martin Janousek xjanou14, Marek Fiala, xfiala46
 * @file: TapeObject.java
 */


package ija.homework3.table;
import ija.homework3.objects.*;

public abstract class TableObject {
	
	//Test, zda je mozne obsadit policko obsahujici tento objekt
	public abstract boolean canSeize();
	
	public abstract char symbolPrint();
	
	//Otevira objekt. Vraci vysledek operace (zda se podarilo objekt otevrit)
	public abstract boolean open();
	
	//Test, zda je mozne odemknout objekt. Podmínka zavisi na implementujici tride.
	public abstract boolean canBeOpen();
	
	public abstract boolean canBeTaken();
	
	/*	Vytvori a vrati objekt podle zadaného typu format. Popis retezce format:
	 * W = Wall
	 * G = Gate
	 * K = Key
	 * F = Finish
	 * ostatni pripady null
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
