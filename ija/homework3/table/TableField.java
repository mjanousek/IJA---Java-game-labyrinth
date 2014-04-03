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
	
	//Inicializace policka p a umisteni objektu podle zadaneho formatu type
	public TableField(Table table, int row,int col, String type){
		object = TableObject.create(type);
		this.table = table;
		this.row = row;
		this.col = col;
	}
		
	//Test, zda je možné otevrít objekt na polcku. Podmínka: polícko obsahuje objekt, který lze otevrit.
	public boolean canBeOpen(){
        if((this.object) == null)
            return object.canBeOpen();
        else
            return false;
	}
	
	//Vrací pozici policka.
	public int positionRow(){
		return row;
	}
	public int positionCol(){
		return col;
	}
	
	//	Obsadi policko hlavou figure, pokud je to mozne. Vraci úspesnost operace (ob-
	//sazení se zdarilo/nezdarilo).
	public boolean seize(Player figure){
		if(canSeize()){
			this.figure = figure;
			return true;
		}
		return false;
	}
	
	//Uvolni policko field. Vraci hlavu, která byla na policku. Pokud bylo policko volne, vraci null.
	public Player leave(){
		if(figure != null){
			Player tmp = figure;
			figure = null;
			return tmp;
		}
		return null;
	}
	
	//Test, zda je možné obsadit policko. 
	//Podminka obsazení: policko neni obsazene a obsazeni dovoluje umísteny objekt.
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
	
	//Otevreni objektu na policku
	public boolean open(){
		if(object != null)
			return object.open(); //Vraci zda bylo mozno otevrit
		//Pokud object neexistuje vracim false
		return false;
	}
	
	// Vypis objektu na policku dle symbolu
	public char printObj(){

		if(figure != null)
			return figure.symbolSight();
		else if(object != null)
			return object.symbolPrint();
		else 
			return '.';
	}
	
	// Funkce pro ziskani policka na dane pozici
	public TableField fieldOnPosition(int row, int col){
		return table.fieldAt(row,col);
	}	
	
	// Pokus o vezmuti klice 
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
	
	public boolean isFinish(){
		if(object == null)
			return false;
		else
			return (object instanceof Finish);
	}
}

/*** End of TableField.java ***/