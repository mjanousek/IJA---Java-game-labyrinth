/*
 * @author:Martin Janousek xjanou14, Marek Fiala, xfiala46
 * @file: TapeField.java
 */

package ija.homework3.table;

import ija.homework3.player.*;

public class TableField {

	private int position;
	private TableObject object = null;
	private Player figure = null;
	protected Table tape;
	
	//Inicializace policka p a umisteni objektu podle zadaneho formatu type
	public TableField(Table tape, int p, String type){
		object = TableObject.create(type);
		this.tape = tape;
		position = p;
	}
	
	//Vrátí polícko, které je na pásce vpravo od tohoto policka. Pokud žádné není (konec pásky), vrati null.
/*	public TapeField rightField(){
		return tape.fieldAt((position+1));
	}*/
	
	//Test, zda je možné otevrít objekt na polcku. Podmínka: polícko obsahuje objekt, který lze otevrit.
	public boolean canBeOpen(){
        if((this.object) == null){
            return object.canBeOpen();
        }else{
            return false;
        }

	}
	
	//Vrací pozici policka.
	public int position(){
		return position;
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
		if(object == null)
			return '.';
		else
			return object.symbolPrint();
	}
}
