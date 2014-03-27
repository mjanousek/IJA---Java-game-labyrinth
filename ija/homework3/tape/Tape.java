/*
 * @author: Marek Fiala, xfiala46
 * @file:Tape.java
 */

package ija.homework3.tape;

public class Tape {
	
	protected TapeField[] objectFD;
	protected TapeHead[] headFD;
	int size;
	
	//Inicializace pasky a vlozeni objektu dle zadaneho formatu
	public Tape(int f, int h, String format){
		objectFD = new TapeField[f];
		size = f;
		headFD = new TapeHead[h];
		int lenght = format.length();
		int i = 0;
		int position = 0;
		
		while(i < lenght){
			if(format.charAt(i) != ' '){
				String buffer = "" +format.charAt(i);
				objectFD[position] = new TapeField(this, position, buffer);
				position++;
			}		
			i++;
		}	
	}


	/*Vrati policko na indexu i. Pokud je index mimo rozsah, vrati null*/
	public TapeField fieldAt(int i){
		if(i > (size-1) || i < 0)
			return null;

		return objectFD[i];
	}
	
	/*	Vytvori a vraci hlavu s identifikatorem i a umístí ji na prvni volne policko zleva (volne podle operace
	canSeize()). Pokud nelze vlozit vraci null.*/
	public TapeHead createHead(int i){
		TapeField fd = objectFD[0];
		
		do{
			if((fd.canSeize()) == true){
				TapeHead head = new TapeHead(fd.position(), fd);
				fd.seize(head);
				return head;
			}
				
			
		}while((fd = fd.rightField())!= null);
			
		return null;
	}	
}
