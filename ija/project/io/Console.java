/**
 * Trida ovladajici samotnou hru, nacitani samotnych prikazu a jejich spousteni 
 * @author:Martin Janousek xjanou14, Marek Fiala, xfiala46
 * @file: Console.java
 * @version: 1.1
 */

package ija.project.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ija.project.figure.*;
import ija.project.table.*;

public class Console{

    String str = "";	/** Retezec obsahujici hodnotu ze prectenou ze vstupniho terminalu */
    Table table; 		/** Matice reprezentujici hraci plochu */
    Player pl;			/** Figurka hrace */
    BufferedReader br;	/** Popisovac souboru pro cteni ze standardniho vstupu
    
    /**
     * Metoda vytvori prostredi pro nacitani prikazu. (konstruktor)
     */
    public Console(){
    	br = new BufferedReader(new InputStreamReader(System.in));
    }
    
    /**
     * Metodainicializuje hru. Ceka na prikaz game name, kde name je idintifikatorm hry.
     * 
     * @return Vraci identifikator hry typu String.
     * @throws IOException
     */
    public String InitGame() throws IOException{
    	String id = "";
    	while (id.equals("")){
    		System.out.println(GameState.INTRO);
    		System.out.print(">>>");
    		this.ReadInput();
    		if(str.equals("close")){
    			System.out.println(GameState.CLOSE);
    			return null;
    		}
    			
    		id = str.substring(5);
    		if(!str.substring(0,4).equals("game")) 
    			id = "";    		
    	}
    	System.out.println(GameState.WELCOME);
    	str = "";
    	return id;
    }
    
    /**
     * Metoda vola metodu ReadInput pro nacteni prikazu, metodu runCommand pro spusteni prikazu a
     * kontroluje, zda nebyl zadan prikaz close
     * 
     * @param table 		- zde je predano nactene bludiste
     * @throws IOException
     */
    public void RunGame(Table table) throws IOException {
    	this.table = table; 
		this.pl = table.createPlayer();
		Keeper kp = table.createKeeper();
    	//------------------------------------------------
		GameState status;

    	while(!str.equals("close")){
    		System.out.print(">>>");
    		if(pl.amIAlive() != true){
    			System.out.println("I was Killed :(");
    			return;
    		}
    		ReadInput();
    		status = runCommand();
    		System.out.println(status);
    		kp.move();
    		if(status == GameState.CLOSE || status == GameState.WINNER){
    			break;
    		}	
    	}
    }
    
    /**
     * Metoda nacte radek ze standartniho vstupu
     * @throws IOException
     */
    public void ReadInput() throws IOException{
        str = null;
        str = br.readLine();
    }

    /**
     * Metoda foUntilStop() slouzi pro chuzi pomoci sekvence go-stop.
     * @return GameState 		- vraci uspesnost prikazu
     */
    public GameState goUntilStop(){
    	while(true){
    		if(pl.move() == false)
    			return GameState.CANTMOVE;
    		
			if(pl.isWinner())
				return GameState.WINNER;
			try {
				Thread.sleep(1000);
				if(br.ready()){
					ReadInput();
					if(str.equals("stop"))
						return GameState.VALIDINPUT;
				}
	        } catch (IOException ioe) {
	            System.out.println("Cannot read order");
	            System.exit(1);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			System.out.println("Move for one position");
    	}
    }
    /**
     * Metoda spousti prikaz nacteny ze standartniho vstupu a ulozeny v instancni promenne str
     * 
     * @return GameState 		-
     */
    public GameState runCommand(){
           	switch(str){
            		//-------------------------------------------prikazy hrace
            		case "left":
            			pl.rotateLeft();
	    				return GameState.VALIDINPUT;
	    			case "right":
            			pl.rotateRight();
	    				return GameState.VALIDINPUT;
	    			case "step":
	    				if(pl.move()){
	    					if(pl.isWinner())
	    						return GameState.WINNER;
	    					else
	    						return GameState.VALIDINPUT;
	    				}
	    				else
	    					return GameState.CANTMOVE;
	    				
	    			case "go":
	    				return goUntilStop();	    					
	    
	    			case "keys":
	    				System.out.println("You have: "+pl.keyCount()+((pl.keyCount() == 1)?" key":" keys"));
	    				return GameState.VALIDINPUT;
	    			
	    			case "take":
	    				if(pl.takeKey())
	    					return GameState.VALIDINPUT;
	    				else
	    					return GameState.NOKEY;
	    			
	    			case "open":
	    				if(pl.keyCount() <= 0)
	    					return GameState.NULLKEY;
	    				if(pl.openGate())
	    					return GameState.VALIDINPUT;
	    			//-------------------------------------------herni prikazy
	    				
	    			case "show":
	    				table.printTable();
	    				return GameState.VALIDINPUT;
	    				
	    			case "close":
	    				return GameState.CLOSE;
	    			default:
	    				break;
	        }
    	    
            return GameState.INVALIDINPUT;
    }
    
    /**
     * Enumerace GameState je vycet stavu hry, do kterych se da dostat.
     */
    public enum GameState {
    	INTRO {
    		@Override
    		public String toString() {
    			return "   Welcome in Labyrint escape v1.1\n Before start choose the game you want.";
    		}
    	},
    	WELCOME {
    		@Override
    		public String toString() {
    			return "Let the game begin!";
    		}
    	},
        CANTMOVE {
            @Override
            public String toString() {
                return "You can't move, field before you is blocked!";
            }
        },
        NOKEY {
        	@Override
        	public String toString() {
        		return "You can't take key, key's not found!";
        	}
        },
        NULLKEY {
        	@Override
        	public String toString() {
        		return "You can't unlock, you don't have keys!";
        	}
        },
        VALIDINPUT {
        	@Override
        	public String toString() {
        		return "Valid input";
        	}
        },
        WINNER {
            @Override
            public String toString() {
            	return "You are the WINNER!!!!";
            }
        },
        CLOSE {
        	@Override
        	public String toString() {
        		return "See you later aligator...";
        	}
        },
        INVALIDINPUT {
            @Override
            public String toString() {
            	return "Invalid input";
            }
        };
    }
}

/*** End of Console.java ***/
