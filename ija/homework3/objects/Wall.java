/*
 * @author: Marek Fiala, xfiala46
 * @file: Wall.java
 */

package ija.homework3.objects;

public class Wall extends TapeObject{
	
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
}
