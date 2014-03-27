/*
 * @author: Marek Fiala, xfiala46
 * @file:Gate.java
 */

package ija.homework3.objects;

public class Gate extends TapeObject{
	protected boolean open= false;
	
	//Lze obsadit podle toho, zda je otevrena
	public boolean canSeize(){
		return open;
	}
	
	//Otevreni brany
	public boolean open(){
		//Pokud je uzavrena muzeme otevrit
		if(this.open == false){
			this.open = true; //Otevreni
			return true;
		}
		else //Jednou otevrenou branu nelze znovu otevrit
			return false;
	}
	
	//Je mozne odemcit
	public boolean canBeOpen(){
		return (!open);
	}
}
