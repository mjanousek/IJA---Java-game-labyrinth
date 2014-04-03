/**
 * @author:Martin Janousek xjanou14, Marek Fiala, xfiala46
 * @file: TapeField.java
 * @version: 1.1
 */

package ija.homework3.table;

import ija.homework3.objects.Finish;
import ija.homework3.player.*;

public class TableField {

	private int row;
	private int col;
	private TableObject object = null;
	private Player figure = null;
	protected Table table;
	
	/**
	 * Metoda inicializuje policko p a umisti na nej objekt podle zadaneho formatu
	 * 
	 * @param table matice hraciho pole
	 * @param row 	cislo radku na ktery se ma umistit policko
	 * @param col	cislo sloupce na ktery se ma umistit policko
	 * @param type	retezec symbolizujici typ viz (@see TapeObject.java)
	 */
	public TableField(Table table, int row,int col, String type){
		object = TableObject.create(type);
		this.table = table;
		this.row = row;
		this.col = col;
	}
		
	//Test, zda je možné otevrít objekt na polcku. Podmínka: polícko obsahuje objekt, který lze otevrit.
/*	public boolean canBeOpen(){
        if((this.object) == null)
            return object.canBeOpen();
        else
            return false;
	}*/
	
	/**
	 * Metoda vraci pozici radku policka @return cislo symbolizujici radek
	 */
	public int positionRow(){
		return row;
	}
	/**
	 * Metoda vraci pozici radku policka @return cislo symbolizujici sloupec
	 */
	public int positionCol(){
		return col;
	}
	
	/**
	 * Obsazeni policka hracem figure, pokud je to mozne
	 * 
	 * @param figure hrac ktery chce policko obsadit
	 * @return	hodnota ano ne podle toho zda byla metoda uspesna
	 */
	public boolean seize(Player figure){
		if(canSeize()){
			this.figure = figure;
			return true;
		}
		return false;
	}
	
	/**
	 * Metoda uvolneni policka.
	 * 
	 * @return Figurka hrace
	 */
	public Player leave(){
		if(figure != null){
			Player tmp = figure;
			figure = null;
			return tmp;
		}
		return null;
	}
	
	/**
	 * Metoda testuje zda je mozne obsadit dane policko
	 * Podminka obsazení: policko neni obsazene a obsazeni dovoluje umísteny objekt.
	 * 
	 * @return hodnota ano ne podle toho zda byla metoda uspesna
	 */
	public boolean canSeize(){
		
		if(object != null || figure != null){
			if(figure != null)
				return false;
			else
				return object.canSeize();
		}
		//Pokud je neobsazeno lze obsadit
		return true;
	}
	
	/**
	 * Metoda otevira objekt na policku
	 * 
	 * @return hodnota ano ne podle toho zda byla metoda uspesna
	 */
	public boolean open(){
		if(object != null)
			return object.open(); //Vraci zda bylo mozno otevrit
		//Pokud object neexistuje vracim false
		return false;
	}
	
	/**
	 * Metoda pro vypis objektu na policku
	 * 
	 * @return symbol znacici objekt na policku
	 */
	public char printObj(){

		if(figure != null)
			return figure.symbolSight();
		else if(object != null)
			return object.symbolPrint();
		else 
			return '.';
	}
	
	/**
	 * Metoda vraci policko na dane pozici
	 * 
	 * @param 	row cislo radku
	 * @param 	col cislo sloupce
	 * @return 	pozadovane policko
	 */
	public TableField fieldOnPosition(int row, int col){
		return table.fieldAt(row,col);
	}	
	
	/**
	 * Metoda se pokousi vzit klic na zadanem policku
	 * Podminka: musi na policku lezi objekt a lze vzit
	 * 
	 * @return hodnota ano ne podle toho zda byla metoda uspesna
	 */
	public boolean tryTakeKey(){
		if(object == null)
			return false;
		else if(object.canBeTaken()){
			object = null; //Simulace vziti klice
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Metoda overujici zda je policko polickem vyhernim
	 * 
	 * @return hodnota ano ne podle toho zda byla metoda uspesna
	 */
	public boolean isFinish(){
		if(object == null)
			return false;
		else
			return (object instanceof Finish);
	}
}

/*** End of TableField.java ***/