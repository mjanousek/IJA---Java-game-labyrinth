package ija.project.figure;

import ija.project.table.TableField;
import java.util.Random;

public class Keeper extends Figure{

	Random rand;
	/**
	 * Metoda pro inicializaci hrace na policko a ulozeni jeho souradnic 
	 * 
	 * @param row 	cislo radku na ktere je hrac vytvoren
	 * @param col 	cislo sloupce na kterem je hrac vytvoren
	 * @param f 	policko na kterem je hrac vytvoren
	 * @param sight	pocatecni pohled hrace
	 */
	public Keeper(int row,int col, TableField f,int sight){
		super(row,col,f,sight);
		rand = new Random();
	}
	
	/**
	 * Vraci symbol podle toho, kam se hrac diva
	 * 
	 * 0	- ^
	 * 1	- >
	 * 2	- v
	 * 3	- <	
	 * 
	 * @return znak, kam se hrac diva
	 */
	public char symbolSight(){
		switch(sight){
		case 0: return '-';
		case 1: return '\\';
		case 2: return '_';
		case 3: return '/';
		default: return 'X';		
		}
	}
	
	public boolean goKeeper(){
		TableField fd = fieldBeforeSight();
        if(fd != null){
			if((fd.canSeize()) == true){
				fd.seize(this);		//Obsazeni policka
				field.leave();      //Uklid stareho
				row= fd.positionRow();	//Nahrani novych souradnic
				col = fd.positionCol();
				field = fd;
				return true;
			}
			if(fd.couldKill() == true){
				System.out.println("Keeper kills player");
			}
		}
        return false;
	}
	
	/**
	 * Funkce zajistujici pohyb hrace vpred. Pri nespechu zustane hrac na stejnem policku
	 * na jekm se nachazi
	 * 
	 * @return vraci true pri uspesnem pohybu vpred a false pri neuspechu
	 */
	public boolean move(){
		int nextMove = rand.nextInt(8);
		switch(nextMove){
		case 0: this.rotateLeft(); break;
		case 1: this.rotateRight(); break;
		case 3: break;
		default: if(this.goKeeper() != true) //Pro docileni pochodu 
				 this.move();
				 break;
		}
		
	return true;
	}
}
