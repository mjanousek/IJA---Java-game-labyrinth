/*
 * @author: Marek Fiala, xfiala46
 * @file: TapeHead.java
 */

package ija.homework3.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ija.homework3.objects.*;
import ija.homework3.table.*;




class Input{

    String str;

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
            if(this.str.equals("game") || this.str.equals("show") || this.str.equals("close")
                    || this.str.equals("go") || this.str.equals("stop") || this.str.equals("left") || this.str.equals("right")
                    || this.str.equals("take") ||this.str.equals("open") || this.str.equals("keys")){
                //kontrola vsech prikazu, ktere je moznost zadat do prikazove radky


                return true;
            }else{
                //vstup neni validni
                return false;
            }

        }else{

            System.out.println("Nebyl zdan zadny prikaz.");
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
        if(ValidateInput()){
            if(this.ValidateHeadMove()){
                //poslat prikaz hlave na vykonani pohybu

            }else if(this.ValidateHeadCommand()){
                //poslat prikaz hlave na vykonani akce
            }else{
                //vykonat prikaz hry
            }
            return true;
        }else{
            return false;
        }
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
