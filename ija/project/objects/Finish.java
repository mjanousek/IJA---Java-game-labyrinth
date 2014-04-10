/**
 * @author: Martin Janousek xjanou14, Marek Fiala, xfiala46
 * @file:Finish.java
 * @version: 1.1
 */


package ija.project.objects;
import ija.project.table.TableObject;

public class Finish extends TableObject{
	
	//Nelze obsadit
	public boolean canSeize(){
		return true;
	}
	
	//Nelze otevrit
	public boolean open(){
		return false;
	}

	public boolean canBeOpen(){
		return false;
	}
	
	public boolean canBeTaken(){
		return false;
	}
	
	public char symbolPrint(){
		return 'F';
	}
}
/*** End of Finish.java ***/