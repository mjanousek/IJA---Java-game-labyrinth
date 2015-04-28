/**
 * @author:Martin Janousek xjanou14
 * @file:Key.java
 * @version: 1.1
 */


package ija.project.objects;
import java.io.Serializable;

import ija.project.table.TableObject;


public class Key extends TableObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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