/**
 * @author:Martin Janousek xjanou14
 * @file: Table.java
 * @version: 1.1
 */

package ija.project.table;
import ija.project.figure.*;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Table implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected TableField[][] objectFD;
	protected Player[] figureFD;
	protected int sizeRow,sizeCol;
	protected int line = 0;
	ArrayList<Color> colorPlayerList = new ArrayList<Color>();
	
	protected int playerCount = 0;
	private String message;
	private String messageForAll;
	private int slowTime = 400;
	long start;
	long end;

	
	/**
	 * Konstruktor ktery inicializuje pole na danou velikost 
	 * @param row velikost radku
	 * @param col velikost sloupcu
	 */
	public Table(int row,int col){
		objectFD = new TableField[row][col];
		sizeRow = row;
		sizeCol = col;
		colorPlayerList.add(new Color(17,0,255));
		colorPlayerList.add(new Color(250, 0, 0));
		colorPlayerList.add(new Color(0, 197, 205));
		colorPlayerList.add(new Color(255, 0, 255));
		start = System.currentTimeMillis();
	}
	
	/**
	 * Vlozeni objetku do daneho radku
	 * @param format retezec znaku znacici objekty
	 * @return true pokud se operace podarila, false pokud nekoliv
	 */
	public boolean insertLine(String format){
		int i = 0;
		int position = 0;
		
		if(format.length() != sizeCol){
			System.err.println("[server] Wrong size of columns\n");
			return false;
		}
		
		if(line >= sizeRow){
			System.err.println("[server] Wrong size of rows\n");
			return false;
		}
		
		while(i < format.length()){
			if(format.charAt(i) != ' '){
				String buffer = "" +format.charAt(i);
				objectFD[line][position] = new TableField(this,line,position, buffer);
				position++;
			}
			i++;
		}	
		line++;
		return true;
	}
	
	/**
	 * Vraci cislo ktere znaci pocet skutecne nactenych radku
	 * @return pocet radku
	 */
	public int lineSize(){
		return line;
	}
	
	/**
	 * Vytvoreni hrace na nahodne pozici
	 * @return postava hrace
	 */
	public Player createPlayer(int idPlayer){
		Random rand = new Random();
		TableField fd;			
		for(int i = rand.nextInt(sizeRow); i < sizeRow;i++){		//Pokus o nahodne generovani polohy
			for(int j = rand.nextInt(sizeCol); j < sizeCol; j++){
				fd = objectFD[i][j];
				if((fd.canSeize()) == true && !fd.isFinish()){	//nahodne policko a at to neni finish
					Player pl = new Player(fd.positionRow(),fd.positionCol(), fd,rand.nextInt(3),colorPlayerList.remove(0),idPlayer);
					messageForAll = "Player "+pl.getID()+" has join the game";
					playerCount++;
					fd.seize(pl);
					return pl;
				}
			}
		}				
		return createPlayer(idPlayer);		//Pokud poloha nenalezena dalsi zanoreni rekurze
	}
	
	/**
	 * Vytvoreni hlidace na nahodne pozici
	 * @return postava hlidace
	 */
	public Keeper createKeeper(){
		Random rand = new Random();
		TableField fd;			
		for(int i = rand.nextInt(sizeRow); i < sizeRow;i++){		//Pokus o nahodne generovani polohy
			for(int j = rand.nextInt(sizeCol); j < sizeCol; j++){
				fd = objectFD[i][j];
				if((fd.canSeize()) == true && !fd.isFinish()){	//nahodne policko a at to neni finish
					Keeper pl = new Keeper(fd.positionRow(),fd.positionCol(), fd,rand.nextInt(3));
					fd.seize(pl);
					return pl;
				}
			}
		}				
		return createKeeper();		//Pokud poloha nenalezena dalsi zanoreni rekurze
	}
	
	/**
	 * Nastaveni zpomaleni pro vsechny postavicky
	 * @param s
	 */
	public void setSlowTime(int s){
		slowTime = s;
	}
	
	/**
	 * Ziskani zpomaleni
	 * @return cas v ms 
	 */
	public int getSlowTime(){
		return slowTime;
	}
	

	/**
	 * Vypis bludiste
	 */
	public void printTable(){
		for(int i = 0; i < sizeRow;i++){
			for(int j = 0; j < sizeCol; j++){
				System.out.print(objectFD[i][j].printObj());
			}
			System.out.print('\n');
		}	
	}
	
	/**
	 * Odstraneni hrace
	 * @param pl hrac k odebrani
	 */
	public void leavePlayer(Player pl){
		colorPlayerList.add(pl.getColor());
		playerCount--;
		
		messageForAll = "Player "+pl.getID()+" has left the game";
	}
	
	/**
	 * Metoda vracejici pocet hracu
	 * @return aktualni pocet hracu
	 */
	public int getPlayerCount(){
		return playerCount;
	}

	/*Vrati policko na indexu i. Pokud je index mimo rozsah, vrati null*/
	//tady to nevratilo null i kdyz to bylo mimo rozmer!!
	public TableField fieldAt(int row, int col){
		//chybi osetreni hranic - zmeneno > na =>
		if(col >= sizeCol || col < 0 || row >= sizeRow || row < 0)
			return null;
		return objectFD[row][col];
	}
	
	/**
	 * Ziskani hrace na pozici row,col
	 * @param row pozice v radku 
	 * @param col pozice v sloupci
	 * @return hrac
	 */
	public Player getPlayer(int row, int col){
		return fieldAt(row,col).getPlayerFigure();
	}
	
	public int getSizeRow(){
		return sizeRow;
	}
	public int getSizeCol(){
		return sizeCol;
	}
	
	/**
	 * Prepsani zpavy k odeslani 
	 * @param msg zprava k odeslani
	 */
	public void rewriteMsg(String msg){
		message = msg;
	}
	
	/**
	 * Ziskani zpravu
	 * @return zprava
	 */
	public String getMsg(){
		return message;
	}
	
	/**
	 * Ziskani zpravy pro vsechny hrace
	 * @return zprava
	 */
	public String getMsgForAll(){
		return messageForAll;
	}
	
	/**
	 * Prepsani zpravy pro vsechny hrace 
	 * @param msg zprava
	 */
	public void rewriteMsgForAll(String msg){
		messageForAll = msg;
	}
	
	/**
	 * Ukonceni casu hry
	 */
	public void stopTime(){
		end = System.currentTimeMillis();
	}
	
	/**
	 * Ziskani casu konce hry
	 * @return cas konce hry
	 */
	public long getEndTime(){
		return end;
	}
	
	/**
	 * Ziskani casu pocatku hry
	 * @return cas pocatku hry
	 */
	public long getStartTime(){
		return start;
	}
}

/*** End of Table.java ***/