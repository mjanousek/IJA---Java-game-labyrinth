/*
 * @author: Marek Fiala, xfiala46
 * @file: TapeHead.java
 */

package ija.homework3.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ija.homework3.objects.*;
import ija.homework3.player.Player;
import ija.homework3.table.*;

public class Console{

	String id = "";
    String str = "";
    Table table;
    Player pl;
    
    public String InitGame(){
    	while (id.equals("")){
    		System.out.println("Prosim inicializujte hru, nebo ju ukoncete.");
    		this.ReadInput();
    		if(str.equals("exit"))
    			return null;
    		
    		id = str.substring(5);
    		str = str.substring(0,4);
    		if(!str.equals("game")) //to druhe nemusi byt prazdne?
    			id = "";
    		//System.out.println("str = "+str+"|");
    		
    	}
    	str = "";
    	return id;
    }
    
    public void RunGame(Table table){
    	this.table = table; 
		this.pl = table.createPlayer();
		//pl.rotateLeft();
		//pl.rotateLeft();
		
    	//------------------------------------------------
    	
    	while(!str.equals("exit")){
    		System.out.println("#-------------------------------#");
    		ReadInput();
    		if(ValidateInput()){
    			System.out.println("validateinput");
    			if(!runCommand())
    				System.out.println("Prikaz se nepodarilo provezt.");
    			
    				
    		}else{
    			System.out.println("Neplatny prikaz, -h pro napovedu.");
    		}
    		
    	}
    	
    	System.out.println("#   konec   #");
		
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



    public String GetString(){
        return str;
    }

    public boolean ValidateInput(){
        if(this.str != null){
        	  if(str != null){
                  
                  switch(str){
                      case "show":
                      case "close":
                      case "go":
                      case "stop":
                      case "left":
                    	  
                      case "right":
                      case "take":
                      case "open":
                      case "keys":
                          return true;
                      default:
                          return false;
                         
                  }
              }else{

                  System.out.println("Nebyl zadan zadny prikaz.");
                  return false;
              }

        }else{

            System.out.println("Nebyl zadan zadny prikaz.");
            return false;
        }
    }

    public boolean ValidateHeadMove(){
        if(this.str.equals("right") || this.str.equals("left")){// || move.equals("move") || move.equals("stop")){
            //System.out.println("vadini pohyb");
            return true;
        }else{
            //System.out.println("ne vadini pohyb");
            return false;
        }
    }

    public boolean ValidateHeadCommand(){
        if(this.str.equals("keys") || this.str.equals("open") || this.str.equals("take")){
            //
            return true;
        }else{
            //
            return false;
        }
    }


    public boolean runCommand(){
           	switch(str){
            		//-------------------------------------------prikazy hrace
            		case "left":
            			pl.rotateLeft();
	    				return true;
	    			case "right":
            			pl.rotateRight();
	    				return true;
	    			case "go":
	    				pl.move();
	    				return true;
	    			case "stop":
	    				
	    				//return true;
	    
	    			case "keys":
	    				System.out.println("You have:"+pl.keyCount()+"keys");
	    				return true;
	    			
	    			case "take":
	    				pl.takeKey();
	    				return true;
	    			
	    			case "open":
	    				pl.openGate();
	    				return true;
	    			//-------------------------------------------herni prikazy
	    			//case "game":
	    				
	    				//return true;
	    			case "show":
	    				table.printTable();
	    				return true;
	    			case "close":
	    				
	    				//return true;
	    			default:
	    				break;
	        }
    	    
            return false;
    }



/*
    public static void main (String[] args) {
        System.out.println("Spoustim Input.java");

        //  prompt the user to enter their name

        Tape t1 = new Tape(9, 2, "w p p p p p p p g");
        TapeHead h;
        h = t1.createHead(1);
        System.out.println("Hlava na pozici:" + h.seizedField().position());

        System.out.print("Zadat pohyb hrace: ");


        Input input = new Input();
        input.ReadInput();

        //text = ReadInput();

        while(!input.GetString().equals("q")){
            if(!input.ValidateMove()){
                System.out.println("Posim zkoste zadat znovu.");
                input.ReadInput();
            }else{
                if(input.GetString().equals("r")){
                    System.out.println("posun doprava");
                    h.moveRight();
                }else if(input.GetString().equals("l")){
                    System.out.println("posun doleva");
                    h.moveLeft();
                }
                System.out.println("Hlava na pozici:" + h.seizedField().position());
                input.ReadInput();
            }


        }

        //  open up standard input
        //System.out.println("Pohyb: " + text + "|");
    }*/
}
