/*
 * @author:Martin Janousek xjanou14, Marek Fiala, xfiala46
 * @file: TapeHead.java
 */

package ija.homework3.player;

import ija.homework3.table.*;

public class Player {
	protected int x;
	protected int y;
	protected int key = 0;
	protected TableField field = null;
	protected int sight = 0; //inicializovano nahoru

	
	//Inicializace hlavy, nastavení jeho identifikatoru id a informace o umistení na policku f.
	public Player(int x,int y, TableField f){
		this.x = x;
		this.y = y;
		field = f;
	}

	//Vraci policko obsazene hlavou.
	public TableField seizedField(){
		return field;
	}

	//Prida hlave n klicu
	public void addKeys(int n){
		key += n;
	}
	
	public int keyCount(){
		return key;
	}

	//rotace vlevo
	public void rotateLeft(){
		if(--sight < 0)
			sight = 3;
	}
	
	//rotace vpravo
	public void rotateRight(){
		if(++sight > 3)
			sight = 0;
	}
	
	//Vraci symbol podle toho kam se hrac diva
	//0^ 1> 2V 3< 4^
	public char symbolSight(){
		switch(sight){
		case 0: return '^';
		case 1: return '>';
		case 2: return 'V';
		case 3: return '<';
		default: return 'X';		
		}
	}
	
	//Vrati policko na ktere se figurka diva
	public TableField fieldBeforeSight(){
		if(sight == 0) 		//nahoru
			return field.fieldOnPosition(x,y-1);
		else if(sight == 1)	//doprava
			return field.fieldOnPosition(x+1,y);
		else if(sight == 2)	//dolu
			return field.fieldOnPosition(x,y+1);
		else if(sight == 3)	//doleva
			return field.fieldOnPosition(x-1,y);
		
		return null;
	}
	
	// Pokus o vezmuti klice
	public boolean takeKey(){
		TableField fd = fieldBeforeSight();
		if(fd.tryTakeKey()){
			key++;		//Podarilo se vzit klic
			return true;
		}
		else
			return false;		
	}
	
	//Pokus o otevreni brany
	public boolean openGate(){
		TableField fd = fieldBeforeSight();
		if(key > 0 && fd.open()){
			key--;
			return true;
		}
		else
			return false;
	}
	
	//Pohyb
	public boolean move(){
		TableField fd = fieldBeforeSight();
        if(fd != null){
			if((fd.canSeize()) == true){
				fd.seize(this);		//Obsazeni policka
				field.leave();      //Uklid stareho
				x = fd.positionX();	//Nahrani novych souradnic
				y = fd.positionY();
				field = fd;
				return true;
			}				
		}
        return false;
	}
	
	
}
