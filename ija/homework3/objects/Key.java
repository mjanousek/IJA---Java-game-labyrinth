/**
 * @author:Martin Janousek xjanou14, Marek Fiala, xfiala46
 * @file:Key.java
 * @version: 1.1
 */


package ija.homework3.objects;
import ija.homework3.table.TableObject;


public class Key extends TableObject{
	
	//Nelze obsadit
	public boolean canSeize(){
		return false;
	}
	
	//Nelze otevrit
	public boolean open(){
		return false;
	}
	
	public boolean canBeOpen(){
		return false;
	}
	
	public boolean canBeTaken(){
		return true;
	}
	
	public char symbolPrint(){
		return 'K';
	}
}

/*** End of Key.java ***/