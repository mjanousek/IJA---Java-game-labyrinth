/**
 * @author:Martin Janousek xjanou14, Marek Fiala, xfiala46
 * @file: Gate.java
 * @version: 1.1
 */


package ija.homework3.objects;
import ija.homework3.table.TableObject;

public class Gate extends TableObject{
	protected boolean open= false;
	char symbol = 'G';
	
	//Lze obsadit podle toho, zda je otevrena
	public boolean canSeize(){
		return open;
	}
	
	//Otevreni brany
	public boolean open(){
		//Pokud je uzavrena muzeme otevrit
		if(this.open == false){
			this.open = true; //Otevreni
			symbol = 'O'; 	  //Symbol otevrene brany
			return true;
		}
		else //Jednou otevrenou branu nelze znovu otevrit
			return false;
	}
	
	//Je mozne odemcit
	public boolean canBeOpen(){
		return (!open);
	}
	
	public boolean canBeTaken(){
		return false;
	}
	
	public char symbolPrint(){
		return symbol;
	}
}

/*** End of Gate.java ***/