/**
 * @author:Martin Janousek xjanou14
 * @file: TapeField.java
 * @version: 1.1
 */

package ija.project.table;

import java.awt.Color;
import java.io.Serializable;

import ija.project.objects.Finish;
import ija.project.figure.*;

public class TableField implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int row;
	private int col;
	private TableObject object = null;
	public Figure figure = null;
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
	public boolean seize(Figure figure){
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
	public Figure leave(){
		if(figure != null){
			Figure tmp = figure;
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
	 * Ziskani barvy hrace
	 * @return barva pokud na poli stoji hrac, null pokud ne 
	 */
	public Color paintObj(){
		if(figure != null && figure instanceof Player)
			return ((Player)figure).getColor();
		else
			return null;
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
	
	/**
	 * Lze zabit objekt na policku 
	 * @return uspesnost zabiti
	 */
	public boolean couldKill(){
		if(figure != null && figure instanceof Player){
			Player pl = (Player)figure;
			table.leavePlayer(pl);
			table.rewriteMsgForAll("Player "+pl.getID()+" was KILLED!!!");
			pl.kill();
			this.leave();
			return true;
		}
		return false;
	}
	
	/**
	 * Ziskani hrace na policku
	 * @return figurka hrace, pokud se nenachazi tak null
	 */
	public Player getPlayerFigure(){
		if(figure != null && figure instanceof Player)
			return (Player) figure;
		else
			return null;
	}
}

/*** End of TableField.java ***/