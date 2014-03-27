/*
 * @author: Marek Fiala, xfiala46
 * @file:TapeField.java
 */

package ija.homework3.tape;

import ija.homework3.objects.TapeObject;

public class TapeField {

	private int position;
	private TapeObject object = null;
	private TapeHead head = null;
	protected Tape tape;
	
	//Inicializace policka p a umisteni objektu podle zadaneho formatu type
	public TapeField(Tape tape, int p, String type){
		object = TapeObject.create(type);
		this.tape = tape;
		position = p;
	}
	
	//Vrátí polícko, které je na pásce vpravo od tohoto policka. Pokud žádné není (konec pásky), vrati null.
	public TapeField rightField(){
		return tape.fieldAt((position+1));
	}
	
	//Test, zda je možné otevrít objekt na polcku. Podmínka: polícko obsahuje objekt, který lze otevrit.
	public boolean canBeOpen(){
        if((this.object) == null){
            return object.canBeOpen();
        }else{
            return false
        }

	}
	
	//Vrací pozici policka.
	public int position(){
		return position;
	}
	
	//	Obsadi policko hlavou head, pokud je to mozne. Vraci úspesnost operace (ob-
	//sazení se zdarilo/nezdarilo).
	public boolean seize(TapeHead head){
		if(canSeize()){
			this.head = head;
			return true;
		}
		return false;
	}
	
	//Uvolni policko field. Vraci hlavu, která byla na policku. Pokud bylo policko volne, vraci null.
	public TapeHead leave(){
		if(head != null){
			TapeHead tmp = head;
			head = null;
			return tmp;
		}

		return null;
	}
	
	//Test, zda je možné obsadit policko. 
	//Podminka obsazení: policko neni obsazene a obsazeni dovoluje umísteny objekt.
	public boolean canSeize(){
		
		if(object != null || head != null){
			if(head != null)
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
}
