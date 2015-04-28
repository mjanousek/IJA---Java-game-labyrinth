/**
 * Trida impelentujici hlidace a jeho logiku pohybu
 * @author: Martin Janousek xjanou14
 * @file:Keeper.java
 * @version: 1.1
 */

package ija.project.figure;

import ija.project.table.TableField;

import java.util.Random;

public class Keeper extends Figure{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Random rand;
	boolean cleft = false;
	boolean cright = false;
	boolean cgo = false;
	boolean nextGo = false;
	
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
				System.out.println("[server] Keeper kills a player");
			}
		}
        return false;
	}
	
	public void whereCanGo(){
		TableField myfd = seizedField();
		TableField fd1,fdl,fdr;
		
		
		int rowObsaz, colObsaz, row1, col1, deltar, deltac;
		colObsaz = myfd.positionCol();
		rowObsaz = myfd.positionRow();
		
		fd1 = fieldBeforeSight();
		if(fd1.canSeize() == true){
			cgo = true;
		}else{
			cgo = false;
		}
		col1 = fd1.positionCol();
		row1 = fd1.positionRow();
		
		
		//pomocny rozdil mezi obsazenym polickem a polickem pred hracem
		deltar = rowObsaz - row1;
		deltac = colObsaz - col1;
		
		if(deltac == 0) deltar *= -1; 
		
		//policko v pravo
		fdr = myfd.fieldOnPosition((rowObsaz - deltac), (colObsaz - deltar));
		if(fdr != null && fdr.canSeize() == true){
			cright = true;
		}else{
			cright = false;
		}
		
		//policko vlevo
		fdl = myfd.fieldOnPosition((rowObsaz + deltac), (colObsaz + deltar));
		if(fdl != null && fdl.canSeize() == true){
			cleft = true;
		}else{
			cleft = false;
		}
		if(fd1!= null && fd1.figure != null && fd1.figure instanceof Player){
			cgo = true; cleft = false; cright = false;
		}else if(fdl!= null && fdl.figure != null && fdl.figure instanceof Player){
			cgo = false; cleft = true; cright = false;
		}else if(fdr!= null && fdr.figure != null && fdr.figure instanceof Player){
			cgo = false; cleft = false; cright = true;
		}
		
		
		
		//v pripade ze je tam hrac utikej za nim!!
		//...
				
	}
	

	/**
	 * Funkce zajistujici pohyb hrace vpred. Pri nespechu zustane hrac na stejnem policku
	 * na jekm se nachazi
	 * 
	 * @return vraci true pri uspesnem pohybu vpred a false pri neuspechu
	 */
	public boolean move(){
		int nextMove = rand.nextInt(3);
		int one = rand.nextInt(2);
		int wait = rand.nextInt(10);
		one = (one == 0) ? -1 : 1;
		
		whereCanGo();
		if((cleft == false) && (cgo == false) && (cright == false)){
			//System.out.println("Jsem ve slepe ulicce.");
			nextMove = 3;
		}
		
		if(one == -1){
			if(nextMove == 2 && cgo == false){nextMove = (nextMove + one)%3;}
			if(nextMove == 1 && cright == false){nextMove = (nextMove + one)%3;}
			if(nextMove == 0 && cleft == false){nextMove = (nextMove + 2)%3;}
			if(nextMove == 2 && cgo == false){nextMove = (nextMove + one)%3;}
			if(nextMove == 1 && cright == false){nextMove = (nextMove + one)%3;}
		}	
		//aby se netocil casteji doleva - funguje i bez toho docela inteligentne
		if(one == 1){
			if(nextMove == 0 && cleft == false){nextMove = (nextMove + one)%3;}
			if(nextMove == 1 && cright == false){nextMove = (nextMove + one)%3;}
			if(nextMove == 2 && cgo == false){nextMove = (nextMove + one)%3;}
			if(nextMove == 0 && cleft == false){nextMove = (nextMove + one)%3;}  	//kdyby se vygenerovala prvni 2
			if(nextMove == 1 && cright == false){nextMove = (nextMove + one)%3;}   // -||-
		}

		if(wait < 8){//obcas neudela nic a zastavi se
			if(nextGo){
				//System.out.println("Posunuji se jako druhy tah");
				this.goKeeper();
				nextGo = false;
			}else{
				//vyhodnocuj
				switch(nextMove){
				case 0: this.rotateLeft();
						nextGo = true;
						break;
				case 1: this.rotateRight();
						nextGo = true;
						break;
				case 2: this.goKeeper();
					 	//this.move();
					 	break;
				default: this.rotateRight();
						 break;
				}
			}
		}
	return true;
	}
}
/*** end of Keeper.java ***/
