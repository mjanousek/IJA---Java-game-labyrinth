/**
 * Vyskakovaci dialogy pro herni plan
 * help a about o autorech
 * @author Marek Fiala xfiala46
 * @file:Dialog.java
 * @version: 1.1
 */


package ija.project.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Dialog extends JDialog {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Toolkit toolkit;
    private String msg;
	    
    /**
     * Text pro okno s napovedou pro textove prikazy
     */
    public void helpDialog(){
    	setTitle("Usage manual");
    	msg =  "<html>Help<br>" + 
    			"Use these commands: <br> <br>" + 
    			"step  - move on field before player's figure<br>" + 
    			"go    - sequence of steps<br>" + 
    			"stop  - end of sequence in go<br>" + 
    			"left  - turn player's figure to the left<br>" + 
    			"right - turn player's figure to the right<br>" + 
    			"take  - take key before player's figure<br>" + 
    			"open  - open gate before player's figure (if he has keys)<br>" + 
    			"keys  - print actual number of keys<br><br>" + 
    			"Have fun</html>";
    	initUI();
    }
    
    /**
     * Text pro okno s informacemi o autorech
     */
    public void aboutDialog(){
        setTitle("About dialog");
        msg =  "<html>Authors:<br><br>" + 
        		"Martin Janousek<br>" + 
        		"Marek Fiala<br>" + 
        		"2014 for subject IJA<br><br>" + 
        		"Thank you for plaing our game</html>";
        initUI();
    }
    
    /**
     * Vytvoreni okna 
     */
    private void initUI() {

        JPanel panel = new JPanel(); 
        panel.setLayout(new BorderLayout(10, 10));
 
        JLabel label = new JLabel(msg);
        label.setFont(new Font("Georgia", Font.PLAIN, 14));
        label.setForeground(new Color(50, 50, 25));

        panel.add(label, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(panel);
        pack();

        toolkit = getToolkit();
        Dimension screensize = toolkit.getScreenSize();
        setLocation((screensize.width - getWidth())/2, 
            (screensize.height - getHeight())/2);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
/*** end of Dialog.java **/