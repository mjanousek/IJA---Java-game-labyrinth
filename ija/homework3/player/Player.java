/*
 * @author:Martin Janousek xjanou14, Marek Fiala, xfiala46
 * @file: TapeHead.java
 */

package ija.homework3.player;

import ija.homework3.table.TableField;

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
	
	public boolean move(){
		TableField fd = field;
		
		
			if(sight == 0){
				fd = fd.frontField();
			}else if(sight == 1){
				fd = fd.rightField();
			}else if(sight == 2){
				fd = fd.behindField();
			}else if(sight == 3){
				fd = fd.leftField();
			}
			
	        if(fd != null){
				if((fd.canSeize()) == true){
					fd.seize(this);
					field.leave();
					field = fd;
					return true;
				}
				else if(key > 0){
					if((fd.open()) == true){
					fd.seize(this);
					field.leave();
					field = fd;
					key--;
					return true;
					}
				}				
			}else{
				return false; //toto sem placl
			}
		
		return false;
	}
}
