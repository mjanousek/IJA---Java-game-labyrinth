/**
 * Okno pro bludiste, horni listu a terminalovy panel 
 * @author Marek Fiala xfiala46
 * @file:Maze.java
 * @version: 1.1
 */


package ija.project.gui;

import ija.project.client.SocketClient;
import ija.project.table.Table;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.DefaultCaret;


public class Maze extends JFrame implements ActionListener{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JMenuBar menubar;		/** Panel menu **/
	private JTextField textfield;	/** Textove pole pro zadani prikazu **/
	private JTextArea statusArea;	/** Textove pole pro vypis a ulozeni zpetne vazby **/
	private JPanel panel;
	private Table table;
	private SocketClient client;	/** Klient pro komunikaci se serverem **/
	private boolean pause = true;	/** Moznost zadavat sipkamy prikazy **/
	private Board board;
	private Window parent;
	
    
	/**
	 * Inicializace instancnich promennych
	 * @param obj prvni prijmuty hraci plan
	 * @param client klient pro komunikaci pres sit
	 * @param parent rodicovske okno
	 */
    public void initGame(Object obj, SocketClient client,Window parent){
    	this.parent = parent;
    	this.client = client;
    	this.table = (Table) obj;
    	initUI(table);
    }
    
    /**
     * Vrchni menu panel
     */
    private void addMenuBar(){
	   menubar = new JMenuBar();

	   /****************** Nabidka game ******************/
       JMenu game = new JMenu("Game");
       game.setMnemonic(KeyEvent.VK_G);

       //Nova hra
       JMenuItem gameNew = new JMenuItem("New");
       gameNew.setMnemonic(KeyEvent.VK_N);
       gameNew.addActionListener(this);

       //Pripojeni k jine hre 
       JMenuItem gameOpen = new JMenuItem("Join");
       gameOpen.setMnemonic(KeyEvent.VK_O);
       gameOpen.addActionListener(this);

       //Ukonceni
       JMenuItem gameExit = new JMenuItem("Exit");
       gameExit.setMnemonic(KeyEvent.VK_C);
       gameExit.setToolTipText("Exit application");
       gameExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
           ActionEvent.CTRL_MASK));
       gameExit.addActionListener(this);

       game.add(gameNew);
       game.add(gameOpen);
       game.add(gameExit);

       menubar.add(game);
       
       /****************** Nabidka help ******************/
       JMenu help = new JMenu("Help");
       help.setMnemonic(KeyEvent.VK_M);
       
       //Tisk napovedy
       JMenuItem helpPrint = new JMenuItem("Help");
       helpPrint.setToolTipText("How to use");
       helpPrint.setMnemonic(KeyEvent.VK_H);
       helpPrint.addActionListener(this);

       //Tisk about
       JMenuItem aboutPrint = new JMenuItem("About");
       aboutPrint.setMnemonic(KeyEvent.VK_H);
       aboutPrint.addActionListener(this);
       
       help.add(helpPrint);
       help.add(aboutPrint);
       
       menubar.add(help);

       setJMenuBar(menubar);
   }
    
    /**
     * Spodni panel - slouzici jako terminal a vypis validace prikazu
     */
    private void addTerminalBar(){
    	
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Scrolovaci panel na vypis spravnosti prikazu
        JScrollPane scrollPane = new JScrollPane();
        statusArea = new JTextArea("WELCOME! Please write your first command:\n");
        
        // Nastaveni automatickeho scrolovani
        DefaultCaret caret = (DefaultCaret)statusArea.getCaret();  
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);  
        
        statusArea.setEditable(false);
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);
        statusArea.setFocusable(false);	//Nelze zamerit

        scrollPane.getViewport().add(statusArea);
        
        // Vstupni "terminal"
        textfield = new JTextField(">>>",14);
        textfield.setFont(new Font("Verdana", Font.BOLD, 12));
        textfield.addKeyListener(new TAdapter()); //Bonus klavesove zkratky
        textfield.setFocusable(true);

        panel.add(scrollPane);
        panel.add(textfield);

        textfield.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent e) {

	    	String text = textfield.getText();
	    	// Vypsani znaku terminalu plus
	    	textfield.setText(">>>");
	    	setStatusArea(">->"+text.substring(3));
	    	client.sendMessage(text.substring(3));
		    }
		});
        
        add(panel,BorderLayout.SOUTH);
    }
    
    /**
     * Pridani textu na status Area
     * @param status text pozadovany na pripsani
     */
    public void setStatusArea(String status){
    	statusArea.append("\n"+status);
    	statusArea.setCaretPosition(statusArea.getDocument().getLength());
    }
    
    /**
     * Vypnuti hry zablokovanim panelu
     * @param str posledni text
     */
    public void stopGame(String str){
    	textfield.setEditable(false);
    	pause = true; //Vypnuti klaves
    	JOptionPane.showMessageDialog(board, str,"End of Game", JOptionPane.INFORMATION_MESSAGE);
    }

    
    /**
     * Performery pro tlacitka v horni liste
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    	//Nova hra
    	if (e.getActionCommand().equals("New")) {
    		client.sendMessage("NewGame");
    		board.deleteBoard();
    		parent.lunchGameChoose(true);
    		dispose();
    	}
    	//Pripojeni k jine rozehrane hre
    	if (e.getActionCommand().equals("Join")) {
    		client.sendMessage("ConnectToGame");
    		board.deleteBoard();
    		parent.lunchGameChoose(false);
    		dispose();
    	}
    	//Ukonceni programu
        if (e.getActionCommand().equals("Exit")) {
        	client.sendMessage("close");
        	client.stopSocket();
        	System.out.println("[client] Bye bye");
        	System.exit(0);
        }
        //Vypis about
        if (e.getActionCommand().equals("About")) {
        	Dialog hl =  new Dialog();
        	hl.aboutDialog();
        	hl.setVisible(true);
        }
        //Vypis help
        if (e.getActionCommand().equals("Help")) {
        	
        	Dialog hl =  new Dialog();
     	   	hl.helpDialog();
     	   	hl.setVisible(true);
        }
     }
    
    /**
     * Inicializace okna pro hru
     * @param table vstupni hraci plan
     */
    private void initUI(Table table) {
        
        addTerminalBar();
        addMenuBar();
        
        board = new Board(this,table,client);
        board.setLayout(null);
        
        // Uprava velikosti okna
        Dimension expectedDimension = new Dimension(900,700);

        board.setPreferredSize(expectedDimension);

        Box box = new Box(BoxLayout.Y_AXIS);

        box.add(Box.createVerticalGlue());
        box.add(board);     
        box.add(Box.createVerticalGlue());    


        this.add(box);

        setMinimumSize(getMinimumSize());   // cannot be resized-

        pack();
        setLocationRelativeTo(null);       
		setVisible(true);
        setTitle("Labyrint escaper");
        // Informativni okno s barvou hrace 
        JOptionPane.showMessageDialog(null,table.getMsg() ,
                "Player info", JOptionPane.INFORMATION_MESSAGE);
        
        WindowListener exitListener = new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		int confirm = JOptionPane.showOptionDialog(null, "Are You Sure to Close Application?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        		if (confirm == 0) {
        			client.sendMessage("close");
        			client.stopSocket();
        			System.exit(0);
        		}
        	}
        };
        addWindowListener(exitListener);
    }
    
    /**
     * Nadstavbova trida slouzici k ovladani za pomoci sipek
     * a klaves shift ctrl alt a space
     * caps lock vypnuti
     * 
     * @author Martin Janousek xjanou14, Marek Fiala xfiala46
     *
     */
    class TAdapter extends KeyAdapter {
        
        @Override
        public void keyPressed(KeyEvent e) {

        	String msg = "";
            int keycode = e.getKeyCode();
            
            if(keycode == KeyEvent.VK_CAPS_LOCK){
            	if(pause == true)
            		pause = false;
            	else
            		pause = true;
            	return;
            }
          
            if(pause == true)
            	return;

            switch (keycode) {
                
            case KeyEvent.VK_LEFT:
            	msg = "left";
                break;
                
            case KeyEvent.VK_RIGHT:
            	msg = "right";
                break;
                
            case KeyEvent.VK_DOWN:
            	msg = "stop";
            	break;
                
            case KeyEvent.VK_UP:
            	msg = "step";
                break;
                
            case KeyEvent.VK_ALT:
            	msg = "keys";
                break;
                
            case KeyEvent.VK_CONTROL:
            	msg = "open";
                break;
                
            case KeyEvent.VK_SPACE:
            	msg = "take";
                break;
                
            case KeyEvent.VK_SHIFT:
            	msg = "go";
                break;
            }
            
            client.sendMessage(msg);
            setStatusArea(">->"+msg);
        }
    }
}
/*** end of Maze.java ***/