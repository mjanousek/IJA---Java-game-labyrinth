/**
 * @author:Martin Janousek xjanou14
 * @file:Wall.java
 * @version: 1.1
 */


package ija.project.objects;
import java.io.Serializable;

import ija.project.table.TableObject;

public class Wall extends TableObject implements Serializable{
	
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
		return false;
	}
	
	public char symbolPrint(){
		return 'W';
	}
}

/*** End of Wall.java ***/