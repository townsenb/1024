package gameGUI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import javax.swing.*;
import game1024.*;

public class GameGUI extends JPanel implements KeyListener{
	
	private NumberSlider game;
	
	private int[][] grid;
	
	private int width, height, maxValue, score;
	
	private Stack<int[][]> history;
	
	 /** define menu things */
    private JMenuBar menus;
    private JMenu fileMenu;
    private JMenuItem quitItem, clearItem;
    
    public static void main(String[] args){
    	GameGUI gui = new GameGUI(4,4,1024);
    	gui.setSize(600,600);
    	gui.setVisible(true);
    }
    
    public GameGUI(int w, int h, int winVal){
    	JFrame frame = new JFrame("1024 Game");
    	Panel panel = new Panel();
    	frame.setLayout(new BorderLayout());
    	panel.setLayout(new GridBagLayout());
    	frame.add(panel,BorderLayout.CENTER);
    	
    	game = new NumberGame();
    	width = w;
    	height  = h;
    	maxValue = winVal;
    	score = 0;
    	history = new Stack<int[][]>();
    	
    }
    
    public void keyPressed(KeyEvent e){
    	System.out.println(e + " was pressed");
    }

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0){
	}
}
