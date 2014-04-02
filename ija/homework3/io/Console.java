/*
 * @author: Marek Fiala, xfiala46
 * @file: TapeHead.java
 */

package ija.homework3.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ija.homework3.player.Player;
import ija.homework3.table.*;

public class Console{

	String id = "";
    String str = "";
    Table table;
    Player pl;
    
    public String InitGame(){
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
    
    public void RunGame(Table table){
    	this.table = table; 
		this.pl = table.createPlayer();
    	//------------------------------------------------
		GameState status;
    	while(!str.equals("close")){
    		System.out.print(">>>");
    		ReadInput();
    		status = runCommand();
    		System.out.println(status);
    		if(status == GameState.CLOSE || status == GameState.WINNER){
    			break;
    		}	
    	}
    	System.out.println("#THE END#");
    }
    

    public void ReadInput(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        this.str = null;

        try {
            this.str = br.readLine();

        } catch (IOException ioe) {
            System.out.println("Nepodarilo se nacist prikaz.");
            System.exit(1);
        }
    }

    public GameState runCommand(){
           	switch(str){
            		//-------------------------------------------prikazy hrace
            		case "left":
            			pl.rotateLeft();
	    				return GameState.VALIDINPUT;
	    			case "right":
            			pl.rotateRight();
	    				return GameState.VALIDINPUT;
	    			case "go":
	    				if(pl.move()){
	    					if(pl.isWinner())
	    						return GameState.WINNER;
	    					else
	    						return GameState.VALIDINPUT;
	    				}
	    				else
	    					return GameState.CANTMOVE;
	    					
	    			case "stop":
	    				
	    				//return true;
	    
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
    
    // hlasky o stavu hry
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
    
    //chybove hlasky
    public enum GameError {
        WALL {
            @Override
            public String toString() {
                return "Nemuzete jit dopredu, je pred vami zed.";
            }
        },
        WELCOME {
        	@Override
        	public String toString() {
                return "Vitejte ve hre!";
            }
        },
        NULLKEYS {
            @Override
            public String toString() {
            	return "Nemate zadne klice.";
            }
        };
    }
}
