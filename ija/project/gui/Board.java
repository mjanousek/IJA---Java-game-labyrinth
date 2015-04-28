/**
 * Herni plan a jeho vykreslovani.
 * @author Marek Fiala xfiala46
 * @file:Board.java
 * @version: 1.1
 */

package ija.project.gui;

import ija.project.client.SocketClient;
import ija.project.figure.Player;
import ija.project.table.Table;
import ija.project.table.TableField;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.imageio.ImageIO;

public class Board extends JPanel implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int BoardWidth;  	/** Sirka herniho planu */ 
    private int BoardHeight;	/** Vyska herniho planu */

    private Timer timer;
    private Table board; /** Hraci plan */
    private SocketClient client;
    private Maze parent;
    private String lastMsg;	/** Posledni prectena zprava - zamezeni duplicit **/
    private boolean allowToolTip = false;

    /**
     * Promenne pro uchovani obrazku 
     */
    private BufferedImage imageKey;
    private BufferedImage imageCloseGate;
    private BufferedImage imageOpenGate;
    private BufferedImage imageFinish;
    private BufferedImage imagePlayerUp;
    private BufferedImage imagePlayerLeft;
    private BufferedImage imagePlayerDown;
    private BufferedImage imagePlayerRight;
    private BufferedImage imageKeeperUp;
    private BufferedImage imageKeeperLeft;
    private BufferedImage imageKeeperDown;
    private BufferedImage imageKeeperRight;
    
    /**
     * Konstruktor, ktery nacte hraci plochu a vykresli ji do okna 
     * 
     * @param parent Okno do ktereho se bludiste vykresli
     * @param table Nactena hraci plocha
     */
    public Board(Maze parent,Table table,SocketClient client) {
    	this.parent = parent;
    	this.client = client;
    	board = table;
    	BoardWidth = table.getSizeCol();
    	BoardHeight = table.getSizeRow();
    	try { 	
    		initPictures();
        } catch (IOException ex) {
            System.err.println("[client] Can not find pictures, please add them into lib");
            JOptionPane.showMessageDialog(null, "Can not find pictures, please add them into lib","No pictures found", JOptionPane.ERROR_MESSAGE);
            return;
       }
    	initBoard(parent);
    }
    
    /**
     * Metoda nacita obrazky pro figurky hlidace a hrace ze slozky examples
     * @throws IOException vyjimka pro pripad, ze se nepodarilo nalezt nektery z obrazku
     */
    private void initPictures() throws IOException{
                       
        	imageKey = ImageIO.read(new File(System.getProperty("user.dir")+"/lib/key.png"));
        	imageCloseGate = ImageIO.read(new File(System.getProperty("user.dir")+"/lib/closeGate.png"));
        	imageOpenGate = ImageIO.read(new File(System.getProperty("user.dir")+"/lib/openGate.png"));
        	imageFinish= ImageIO.read(new File(System.getProperty("user.dir")+"/lib/finish.png"));
        	
        	imagePlayerUp = ImageIO.read(new File(System.getProperty("user.dir")+"/lib/playerUp.png"));
        	imagePlayerLeft = ImageIO.read(new File(System.getProperty("user.dir")+"/lib/playerLeft.png"));
        	imagePlayerDown = ImageIO.read(new File(System.getProperty("user.dir")+"/lib/playerDown.png"));
        	imagePlayerRight=  ImageIO.read(new File(System.getProperty("user.dir")+"/lib/playerRight.png"));
        	
        	imageKeeperUp = ImageIO.read(new File(System.getProperty("user.dir")+"/lib/keeperUp.png"));
        	imageKeeperLeft = ImageIO.read(new File(System.getProperty("user.dir")+"/lib/keeperLeft.png"));
        	imageKeeperDown = ImageIO.read(new File(System.getProperty("user.dir")+"/lib/keeperDown.png"));
        	imageKeeperRight=  ImageIO.read(new File(System.getProperty("user.dir")+"/lib/keeperRight.png"));
    }
    
    /**
     * Inicializace bludiste, naplneni timeru slouziciho pro aktualizaci obrazu
     */
    private void initBoard(Maze parent) {
        
       setFocusable(false);
       timer = new Timer(200, this);
       timer.start(); 
    }
    
    /**
     * Vymazani hraci plochy vypnutim timeru
     */
    public void deleteBoard(){
    	 timer.stop();
    }

    /**
     * Funkce, ktera je zavolana po uplynuti casu v timeru
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
    	//Prijmuti objektu
        board = (Table)client.readObject();
        
        //Pokud zprava pro vsechy neni prazdna a neni to posledni prectena zprava
        if(board.getMsgForAll().length() != 0 && !board.getMsgForAll().equals(lastMsg)){
        	//Pokud konec 
        	if(board.getMsgForAll().contains("END")){
        		timer.stop();
        		repaint(); //Posledni vykresleni
        		stopGame();
        		return;
        	}
        	parent.setStatusArea("|>|>"+board.getMsgForAll());
        	lastMsg = board.getMsgForAll();
        }
        
        //Pokud je zde zprava pro hrace 
        if(board.getMsg().length() != 0){
        	//Pokud jsem byl prave zabit tak mam moznost zkustat jako pozorovatel
        	if(board.getMsg().contains("MURDERED")){
        		parent.stopGame(board.getMsg()); //Rodicovskyho okna !
        		int confirm = JOptionPane.showOptionDialog(null, "Do you want to stay watching the game?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        		if (confirm == 1) {
        			client.sendMessage("close");
        			System.exit(0);
        		}
        	}
        	
        	parent.setStatusArea("|>|>"+board.getMsg());
        }
        
    	repaint();
    }
    
    /**
     * Pro konci hry je potreba vypnout zadavani prikazu a zapnout moznost informaci po najeti mysi
     */
    private void stopGame(){
		if(board.getMsg().length() != 0)
        	parent.setStatusArea("|>|>"+board.getMsg());
		parent.setStatusArea("|>|>"+board.getMsgForAll());
		parent.stopGame(board.getMsgForAll().substring(0, board.getMsgForAll().length()-3));
		allowToolTip = true;
		repaint();
    }
    
    /**
     * Ziskani sirky ctverce, ktery se ma vykreslit
     * @return velikost sirky ctverce
     */
    private int squareWidth() { 
    	return (int) getSize().getWidth() / BoardWidth; 
    }
    
    /**
     * Ziskani vysky ctverce, ktery se ma vykreslit
     * @return velikost vysky ctverce
     */
    private int squareHeight() {
    	return (int) getSize().getHeight() / BoardHeight; 
    }
    
    /**
     * Vykresleni hraciho planu
     * @param g Graficky element
     */
    private void doDrawing(Graphics g) {
        
        for (int i = 0; i < BoardHeight; ++i) {
            for (int j = 0; j < BoardWidth; ++j) {
                TableField field = board.fieldAt(i, j);
                	// Posun od okna
                 drawSquare(g, j * squareWidth(),i * squareHeight(), field);
            }
        }   
    }
    
    /**
     * Prepsani defalutni vykreslovaci funkce
     */
    @Override
    public void paintComponent(Graphics g) { 

        super.paintComponent(g);
        doDrawing(g);
    }
    
    /**
     * Metoda pro vypis informaci najetim na hrace po ukonceni hry
     * 
     * @param col
     * @param row
     * @param object
     */
    private void addInfoToPlayer(int col, int row,TableField object){
		JLabel label = new JLabel();     
		label.setSize(squareWidth(), squareHeight());
		Player pl = object.getPlayerFigure();
	    DateFormat formatter = new SimpleDateFormat("mm:ss");
		label.setLocation(col,row);

		//Informace o hre
		label.setToolTipText("<html>Player: "+pl.getID()+"<br>"+"Move count:"+pl.getMoveCount()+"<br>"+
				 			"Time in game: "+formatter.format(board.getEndTime()-pl.getStartTime())+"<br>"+
				 		   "Game time: "+formatter.format(board.getEndTime()-board.getStartTime())+"</html>");
		
		add(label);
    }
    
    /**
     * Vykresleni ctverce na hraci plose
     * 
     * @param g	Graficky utvar
     * @param x	souradnice ctverce
     * @param y souradnice ctverce
     * @param object objekt, ktery se ma na dane misto vykreslit
     */
    private void drawSquare(Graphics g, int x, int y, TableField object)  {     

        Color color;
        Color playersCol;
        char symbol = object.printObj();
        
        if(symbol == 'W')
        	color = new Color(0, 120, 0);
        else
        	color = new Color(238,221,130);
        
        	
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                         x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                         x + squareWidth() - 1, y + 1);
        
        // Pokud zed nebo policko nic dalsiho se nevykresluje
        if(symbol == 'W'){
        	g.setColor(color.brighter());
        	g.drawLine(x+squareWidth(),y+squareHeight(),x,y);
        	return;
        }
        	
        if(symbol == '.')
        	return;
        
        //Zvyrazneni hrace
        if(symbol == '<' || symbol == '>' || symbol == '^' || symbol == 'v'){
        	playersCol = object.paintObj();
        	// Pokud je konec a je potreba pridat i info nakonci hry
        	if(allowToolTip){
        		addInfoToPlayer(x,y,object);
        	}
        	g.setColor(playersCol);
        	g.fillOval(x, y, squareWidth(), squareHeight());
        }
        
        //Zvyrazneni strazneho
        if(symbol == '/' || symbol == '\\' || symbol == '_' || symbol == '-'){
        	g.setColor(new Color(0, 0, 0));
        	g.fillOval(x, y, squareWidth(), squareHeight());
        	g.setColor(new Color(255, 255, 255));
        	g.fillOval(x+4, y+4, squareWidth()-8, squareHeight()-8);
        }
        
        //Zvyrazneni klice
        if(symbol == 'K'){
        	g.setColor(new Color(255, 51, 51));
        	g.fillOval(x, y, squareWidth(), squareHeight());
        	g.setColor(new Color(102, 0, 0));
        	g.fillOval(x+4, y+4, squareWidth()-8, squareHeight()-8);
        }
        	
        //Naneseni obrazku
        switch(symbol){
	        case 'G':	g.drawImage(imageCloseGate, x, y,squareWidth(),squareHeight(), null); return;
	        case 'F':	g.drawImage(imageFinish, x, y,squareWidth(),squareHeight(), null); return;
	        case 'O':	g.drawImage(imageOpenGate, x, y,squareWidth(),squareHeight(), null); return;
	        case 'K':	g.drawImage(imageKey, x, y,squareWidth(),squareHeight(), null); return;
	        case '<':	g.drawImage(imagePlayerLeft, x, y,squareWidth(),squareHeight(), null); return;
	        case '>':	g.drawImage(imagePlayerRight, x, y,squareWidth(),squareHeight(), null); return;
	        case 'v':	g.drawImage(imagePlayerDown, x, y,squareWidth(),squareHeight(), null); return;
	        case '^':	g.drawImage(imagePlayerUp, x, y,squareWidth(),squareHeight(), null); return;
	        case '/':	g.drawImage(imageKeeperLeft, x, y,squareWidth(),squareHeight(), null); return;
	        case '\\':	g.drawImage(imageKeeperRight, x, y,squareWidth(),squareHeight(), null); return;
	        case '_':	g.drawImage(imageKeeperDown, x, y,squareWidth(),squareHeight(), null); return;
	        case '-':	g.drawImage(imageKeeperUp, x, y,squareWidth(),squareHeight(), null); return;        
        }
    }
}

/*** End of Board.java ***/