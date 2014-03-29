/*
 * @author:Martin Janousek xjanou14, Marek Fiala, xfiala46
 * @file: TapeHead.java
 */

package ija.homework3.table;

public class TableHead {
	protected int id;
	protected int key = 0;
	protected TableField field = null;
	
	//Inicializace hlavy, nastavení jeho identifikatoru id a informace o umistení na policku f.
	public TableHead(int id, TableField f){
		this.id = id;
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

	//Posune hlavu na pasce doprava na nejblizsi volne policko. Pokud ma klic tak muze otevrit branu.
/*	public boolean moveRight(){
		TapeField fd = field;
        while((fd = fd.rightField())!= null){
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
		}
		
		return false;
	}*/
}
