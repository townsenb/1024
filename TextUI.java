package game1024;

import java.util.*;

/***********************************************************************
 * Created by Hans Dulimarta on Jun 27, 2016.
 * @author  Ben Townsend
 * @version 2/15/17
 **********************************************************************/

public class TextUI {
	
	/** define a new type NumberSlider */
    private NumberSlider game;
    
    /** define a new grid */
    private int[][] grid;
    
    /** make a history bank for undo-ing */
    private Stack<int[][]> history;
    
    /** define the size of the gameBoard */
    private static int CELL_WIDTH = 3;
    
    /** define the outputs for the UI */
    private static String NUM_FORMAT, BLANK_FORMAT;
    
    /** input for movement buttons */
    private Scanner inp;

    /*******************************************************************
	 * Constructor initializes the UI
	 ******************************************************************/
    public TextUI() {
        game = new NumberGame();
        history = new Stack<int[][]>();
        if (game == null) {
            System.err.println ("*---------------------------------------------*");
            System.err.println ("| You must first modify the UI program.       |");
            System.err.println ("| Look for the first TODO item in TextUI.java |");
            System.err.println ("*---------------------------------------------*");
            System.exit(0xE0);
        }
        game.resizeBoard(4, 4, 64);
        grid = new int[4][4];

        /* Set the string to %4d */
        NUM_FORMAT = String.format("%%%dd", CELL_WIDTH + 1);

        /* Set the string to %4s, but without using String.format() */
        BLANK_FORMAT = "%" + (CELL_WIDTH + 1) + "s";
        inp = new Scanner(System.in);
    }

    /*******************************************************************
	 * outputs the content of grid to the console
	 ******************************************************************/
    private void renderBoard() {
        /* reset all the 2D array elements to ZERO */
        for (int k = 0; k < grid.length; k++)
            for (int m = 0; m < grid[k].length; m++)
                grid[k][m] = 0;
        /* fill in the 2D array */
        for (int i=0;i < grid.length; i++){
        	for(int j=0;j< grid[i].length;j++){
        		grid[j][i] = game.getGame()[i][j];
        	}
        }
   
        /* Print the 2D array using dots and numbers */
        for (int k = 0; k < grid.length; k++) {
            for (int m = 0; m < grid[k].length; m++)
                if (grid[k][m] <= 0)
                    System.out.printf (BLANK_FORMAT, ".");
                else
                    System.out.printf (NUM_FORMAT, grid[k][m]);
            System.out.println();
        }
    }

    /**
     * The main loop for playing a SINGLE game session. Notice that
     * the following method contains NO GAME LOGIC! Its main task is
     * to accept user input and invoke the appropriate methods in the
     * game engine.
     */
    public void playLoop() {
    	game.reset();
    	
        /* Place the first two random tiles */
        game.placeRandomValue();
        game.placeRandomValue();
        
        renderBoard();
        saveState();
        
        /* To keep the right margin within 75 columns, we split the
           following long string literal into two lines
         */
        System.out.print ("Slide direction (W, A, S, D), " +
        		"[U]ndo or [Q]uit? ");
        String resp = inp.next().trim().toUpperCase();
        while (!resp.equals("Q")) {
        	saveState();
        	switch (resp) {
        	case "W": /* Up */
        		game.slide(SlideDirection.UP);
        		if(!game.placeRandomValue()){
        			System.out.println("Out of space! Thanks for playing!");
        			resp = "Q";
        		}
        		renderBoard();
        		break;
        	case "D":
        		game.slide(SlideDirection.RIGHT);
        		if(!game.placeRandomValue()){
        			System.out.println("Out of space! Thanks for playing!");
        			resp = "Q";
        		}
        		renderBoard();
        		break;
        	case "S":
        		game.slide(SlideDirection.DOWN);
        		if(!game.placeRandomValue()){
        			System.out.println("Out of space! Thanks for playing!");
        			resp = "Q";
        		}
        		renderBoard();
        		break;
        	case "A":
        		game.slide(SlideDirection.LEFT);
        		if(!game.placeRandomValue()){
        			System.out.println("Out of space! Thanks for playing!");
        			resp = "Q";
        		}
        		renderBoard();
        		break;
        	case "U":
        		try {
        			undo();
        			renderBoard();
        		} catch (IllegalStateException exp) {
        			System.err.println ("Can't undo that far");
        		}
        	}
        	/* To keep the right margin within 75 columns, we split the
                following long string literal into two lines
        	 */
        	System.out.print ("Slide direction (W, A, S, D), " +
        			"[U]ndo or [Q]uit? ");
        	resp = inp.next().trim().toUpperCase();
        }



    }

    /*******************************************************************
	 * saves the current state of grid
	 ******************************************************************/
    public void saveState(){
    	int [][] aux = new int [grid.length][grid[0].length];
    	for(int i = 0;i < grid.length;i++) {
    		for(int j = 0;j < grid[0].length;j++) {
    			aux [i][j] = grid[i][j];
    		}
    	}
		history.push(aux);
		
	}
    
    /*******************************************************************
	 * sets the grid to one previous
	 ******************************************************************/
    public void undo(){
    	history.pop();
    	int [][] aux = history.pop();
    	game.printArray(aux);
    	for(int i = 0;i < grid.length;i++) {
    		for(int j = 0;j < grid[0].length;j++) {
    			grid[i][j] = aux[i][j];
    		}
    	}
    	

    }

    /*******************************************************************
	 * Main Method runs the UI
	 ******************************************************************/
    public static void main(String[] arg) {
        TextUI t = new TextUI();
        t.playLoop();
    }
}


