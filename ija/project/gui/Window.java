/**
 * Okno pro uvodni menu, pro vytvoreni nove hry a pripojeni ke hre
 * @author Marek Fiala xfiala46
 * @file:Window.java
 * @version: 1.1
 */

package ija.project.gui;

import java.awt.*;
import java.io.*;
import java.net.UnknownHostException;
import java.awt.event.*;

import ija.project.client.*;

import javax.swing.*;
import javax.swing.event.*;

public class Window extends JFrame implements ActionListener{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Tabulka s nazvy her **/
	private	JTable fileTable;
	private ListSelectionModel selectionModel;
	
    private JPanel welcomePanel;
    private JPanel choosePanel;

    /** Tlacitka */
	private JButton newGameButton;
	private JButton connectButton;
	private JButton exitButton;
	private JButton startGame;
	private JButton backbutton;
	/** Slider */
	private JSlider slider;
	private JLabel scrollLabel;
	private String[][] listOfFiles;
	private SocketClient client = null;
	private JScrollPane pane;
	private JSpinner spinner;
	/** Cas zpomaleni souvisleho pohybu **/
	private int slowtime;
	
	/**
	 * Uvodni okno ktere je spustene hned po zapnuti programu
	 */
    public void initUIWelcome(){

    	JPanel panel = new JPanel(); // Panel pro tlacitka new game,connect a exit
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	
    	welcomePanel = new JPanel();
    	welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    	
        JLabel welcomeLabel = new JLabel("Labyrint escaper");
        welcomeLabel.setFont(new Font(welcomeLabel.getName(), Font.BOLD, 20));
        panel.add(welcomeLabel);
        panel.add(Box.createRigidArea(new Dimension(10, 15)));

    	// Tlacitka
        newGameButton = new JButton("New game");
        newGameButton.addActionListener(this);
        newGameButton.setToolTipText("Start new game");

        connectButton = new JButton("Connect to game");
        connectButton.addActionListener(this);
        connectButton.setToolTipText("Connect to runing game");

        exitButton = new JButton("Exit");	
        exitButton.addActionListener(this);

        // Zarovnani velikosti
        newGameButton.setMaximumSize(welcomeLabel.getMaximumSize());
        connectButton.setMaximumSize(welcomeLabel.getMaximumSize());
        exitButton.setMaximumSize(welcomeLabel.getMaximumSize());

        // Vlozeni
        panel.add(newGameButton);
        panel.add(Box.createRigidArea(new Dimension(40, 20)));
        panel.add(connectButton);
        panel.add(Box.createRigidArea(new Dimension(40, 20)));
        panel.add(exitButton);

        welcomePanel.add(panel);
        welcomePanel.add(Box.createRigidArea(new Dimension(10, 0))); //Zarovnani

        add(welcomePanel);

        pack();
        
        setTitle("Main menu");
        setResizable(false);
        setLocationRelativeTo(null);
        
        WindowListener exitListener = new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		int confirm = JOptionPane.showOptionDialog(null, "Are You Sure to Close Application?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        		if (confirm == 0) {
        			if(client != null){
        				client.sendMessage("close");
        				client.stopSocket();
        			}
        			System.exit(0);
        		}
        	}
        };
        addWindowListener(exitListener);
    }

    /**
     * Perfomery pro tlacitka
     */
    @Override
    public void actionPerformed(ActionEvent e) {
    	
        if (e.getActionCommand().equals("New game")) {
        	enterIPAddress(true);
        }

        if (e.getActionCommand().equals("Connect to game")) {
        	enterIPAddress(false);
        }

        if (e.getActionCommand().equals("Exit")) {
        	if(client != null){
        		client.sendMessage("close");
        		client.stopSocket();
        	}
        	System.out.println("[client] Bye bye");
        	System.exit(0);
        }
        
        if (e.getActionCommand().equals("Back")) {
     	   	client.sendMessage("close");
     	   	returnToMenu();
        }
        
        //Zahajeni hry 
        if (e.getActionCommand().equals("Start game")) {
            
			client.sendMessage(listOfFiles[fileTable.getSelectedRow()][0]);
			//Pokud nebylo zpozdeni jiz zadano drive
			//Je zadan i pocet hlidacu
			if(slowtime != -1)
				client.sendMessage("SLOW:"+slowtime+"||"+"KEEP:"+spinner.getValue()+">");
			
        	Maze game = new Maze();
            game.initGame(client.readObject(),client,this);
            game.setVisible(true);
            this.setVisible(false);
        }
     }
    
    /**
     * Metoda umoznujici vlozeni, rozpoznani a nesledovne pripojeni clenta k serveru
     * @param newGame
     */
    private void enterIPAddress(boolean newGame){
		String address = JOptionPane.showInputDialog("Enter IP address and port (for example 127.0.0.1:8011)","localhost:8011");
    	int index;

		if(address != null && (index = address.indexOf(':')) != -1){
				
			String ipAddress= address.substring(0,index);
			try{
				int port =  Integer.parseInt(address.substring(index+1));
				System.out.println("[client] You entred IP: "+ipAddress+" and Port: "+port);
				
				//pripojeni na ip adresu a port zadany uzivatelelem
		        client = new SocketClient (ipAddress, port);
		        try {
		            client.connect();
		            if(newGame == true){
		            	client.sendMessage("NewGame");
		            }else{
		            	client.sendMessage("ConnectToGame");
		            }
		            	
		        } catch (UnknownHostException e) {
		            System.err.println("[client] Host unknown. Cannot establish connection");
		            JOptionPane.showMessageDialog(null, "Host unknown. Cannot establish connection","Adress connection",JOptionPane.ERROR_MESSAGE);
		            System.exit(0);
		        } catch (IOException e) {
		        	JOptionPane.showMessageDialog(null, "Cannot establish connection. Server may not be up.","Adress connection",JOptionPane.ERROR_MESSAGE);
		            System.err.println("[client] Cannot establish connection. Server may not be up."+e.getMessage());
		            System.exit(0);
		        }
				
				welcomePanel.setVisible(false);
				listOfFiles = client.readFiles();
				initUIGame(newGame);
		    	choosePanel.setVisible(true);
			}
			catch(NumberFormatException error){
				JOptionPane.showMessageDialog(null, "You have to write valuable IP address","Wrong address format",JOptionPane.ERROR_MESSAGE);
				System.err.println("[client] Wrong address format");
			}
		}
    }
    
    /**
     * Pro otevreni hry pres uz spustenou hru
     * 
     * @param newGame true jedna li se o novou hru
     */
    public void lunchGameChoose(boolean newGame){
    	choosePanel.removeAll();
    	remove(choosePanel);
    	listOfFiles = client.readFiles();
    	initUIGame(newGame);
    	setVisible(true);
    	choosePanel.setVisible(true);
    }
    
     /**
     * Dalsi okno- pri zmacknuti New game 
     * Lze v nem vybrat hry ktere jsou ve slozce examples
     * @param newGame true jedna li se o novou hru
     */
    private void initUIGame(boolean newGame){
    	
    	pane = new JScrollPane(); 
    	
    	//Tlacitko pro zapnuti hry
    	startGame = new JButton("Start game"); 
    	startGame.removeActionListener(this);
    	startGame.addActionListener(this);
    	
    	//Tlacitko zpet
    	backbutton = new JButton("Back");
    	backbutton.setMaximumSize(startGame.getMaximumSize());
    	backbutton.removeActionListener(this);
    	backbutton.addActionListener(this);
    	
    	slider = new JSlider(5, 50, 40);
    	slowtime = 40;		//Nastaveni defautlni casove prodlevy
    	scrollLabel= new JLabel("4.0"); // Defaultne nastaveno
    	choosePanel = new JPanel();

    	choosePanel.setLayout(new BorderLayout());
    	choosePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    	choosePanel.add(Box.createHorizontalGlue());
    	choosePanel.setLayout(new BoxLayout(choosePanel, BoxLayout.Y_AXIS));
    	
    	//Overeni pro pripad, ze nebyly nalezeny zadne hry
    	if(listOfFiles == null){
    		JOptionPane.showMessageDialog(choosePanel, "No games avaible","Error", JOptionPane.ERROR_MESSAGE);
    		returnToMenu();
    		return;
    	}
    	
    	//Pridani popisneho napisu
    	JPanel labelPanel = new JPanel();
        JLabel chooseLabel = new JLabel("Please choose avaible games:");
        labelPanel.add(chooseLabel,BorderLayout.CENTER);
        choosePanel.setLayout(new BoxLayout(choosePanel, BoxLayout.Y_AXIS));
        choosePanel.add(labelPanel);
        choosePanel.add(Box.createRigidArea(new Dimension(10, 7)));
        
        //Vypis tabulky moznych bludist
        String columnNames[] = { "Name", "Size", "Players" };
        fileTable = new JTable( listOfFiles, columnNames);
        selectionModel = fileTable.getSelectionModel();
        selectionModel.setSelectionInterval(0, 0);

        pane.getViewport().add(fileTable);
        pane.setPreferredSize(new Dimension(200, 200));
        choosePanel.add(pane); //Pridani oblasti pro vyber hry
        
        chooseGame(newGame);

        choosePanel.add(Box.createHorizontalGlue());

        add(choosePanel);
        pack();
        setTitle("Available games");
        
        setResizable(true);
        setLocationRelativeTo(null);

    }
    
    /**
     * Dalsi komponeny v pripade ze hrac pozaduje zahajeni nove hry
     * @param newGame true jedna li se o novou hru
     */
    private void chooseGame(boolean newGame){
    	JPanel buttonPanel = new JPanel();
    	
        buttonPanel.add(backbutton,BorderLayout.EAST);
        buttonPanel.add(Box.createRigidArea(new Dimension(60, 7)));
        buttonPanel.add(startGame,BorderLayout.WEST);
    	
    	if(newGame){
	    	JPanel panel = new JPanel();
	    	JPanel sliderPanel = new JPanel();
	    	JPanel guardPanel = new JPanel();
	    	
	        choosePanel.add(Box.createRigidArea(new Dimension(30, 7)));
	        JLabel slowLabel = new JLabel("Slow time between steps in go (in seconds)");
	        JLabel guardLabel = new JLabel("Number of guards");
	        
	        slider.setPreferredSize(new Dimension(80, 20));
	        
	        // Slider pro nastaveni zpomaleni 
	        slider.addChangeListener(new ChangeListener() {
	            @Override
	            public void stateChanged(ChangeEvent event) {
	            	if(slider.getValueIsAdjusting()) {
	            		scrollLabel.setText(Double.toString(slider.getValue()/10.0));                  
	                } else {
	                	slowtime = slider.getValue();
	                	scrollLabel.setText(Double.toString(slider.getValue()/10.0));
	                }
	            }
	        });
	        
	        SpinnerModel spinnerModel = new SpinnerNumberModel(1,0,5,1);

	        spinner = new JSpinner(spinnerModel);
	        spinner.setEditor(new JSpinner.NumberEditor(spinner, "#"));
	        spinner.setBounds(30, 30, 30, 25);

	        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
	        sliderPanel.add(slowLabel);
	        sliderPanel.add(Box.createRigidArea(new Dimension(20, 7)));
	        sliderPanel.add(slider);
	        
	        guardPanel.setLayout(new BoxLayout(guardPanel, BoxLayout.Y_AXIS));
	        guardPanel.add(guardLabel);
	        guardPanel.add(Box.createRigidArea(new Dimension(20, 7)));
	        guardPanel.add(spinner);
	        
	        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
	        panel.add(sliderPanel);
	        panel.add(scrollLabel);
	        panel.add(Box.createRigidArea(new Dimension(30, 7)));
	        panel.add(guardPanel);
	        panel.add(Box.createRigidArea(new Dimension(30, 7)));
	        
	        choosePanel.add(panel);
	        choosePanel.add(Box.createRigidArea(new Dimension(30, 7)));
	        choosePanel.add(buttonPanel);
    	}else{
        	// Slowtime je jiz nastaven
        	slowtime = -1;
        	choosePanel.add(Box.createRigidArea(new Dimension(20, 7)));
            choosePanel.add(buttonPanel,BorderLayout.CENTER);
        }
    }
   
   /**
    * Otevreni okna hlavniho menu - je potreba prenastavit velikost okna
    */
   private void returnToMenu(){
	    choosePanel.setVisible(false);
   		remove(choosePanel);
   		remove(welcomePanel);
   		add(welcomePanel);
		welcomePanel.setVisible(true);
		revalidate();
		pack();
		setLocationRelativeTo(null);
   }
   
   /**
    * Funkce main, ktera spousti GUI
    * @param args
    */
   public static void main(String[] args) {

       SwingUtilities.invokeLater(new Runnable() {

           public void run() {
	           Window game = new Window();
	           game.initUIWelcome();
	           game.setVisible(true);
		   }
		});
   }
}
/*** end of Window.java ***/