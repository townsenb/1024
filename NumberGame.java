package game1024;

import java.util.*;

/*******************************************************************
 * Game Logic for 1024 game
 * @author Ben Townsend
 * @version 2/9/17
 ******************************************************************/
public class NumberGame implements NumberSlider{

	/** define dimensions and winning value */
	private int height, width, winningValue;
	
	/**define a 2D array for the grid */
	private int[][] game;
	
	/** make a current score counter */
	private int score;
	
	/** make a new random for placing random tiles */
	private Random random = new Random();

	/*******************************************************************
	 * Changes the game dimensions and the winning value
	 * @param height Number of rows
	 * @param width Number of columns
	 * @param winningValue Changes the winning max value
	 ******************************************************************/
	@Override
	public void resizeBoard(int height, int width, int winningValue) {
		this.height = height;
		this.width = width;
		game = new int[width][height];

		//default the winning score to 1024 if its not 2^something
		if(!isBaseTwo(winningValue) || winningValue <= 0){
			this.winningValue = 1024;
		}else{
			this.winningValue = winningValue;
		}
	}

	/*******************************************************************
	 * sets the score to zero and erases the game board
	 ******************************************************************/
	@Override
	public void reset(){
		score = 0;
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				game[i][j] = 0;
			}
		}

	}

	/*******************************************************************
	 * @param ref Sets the game values to a specific 2D int array
	 ******************************************************************/
	@Override
	public void setValues(int[][] ref) {
		game = ref;

	}

	/*******************************************************************
	 * @return if successfully placed a value
	 ******************************************************************/
	@Override
	public boolean placeRandomValue() {

		int[] tempArray = getEmptySpace();
		
		//if the first part is -1, then there are no more spaces left
		if(tempArray[0] >= 0){
			game[tempArray[0]][tempArray[1]] = random.nextInt(2) + 1;
			return true;
		}else{
			return false;
		}
	}

	/*******************************************************************
	 * This method just prepares the array for sliding left
	 * @return if game successfully slid
	 ******************************************************************/
	@Override
	public boolean slide(SlideDirection dir) {

		//flip game horizontally, slide and combine
		if(dir == SlideDirection.RIGHT){
			int[][] slideArray = flipHorizontal(game);
			for(int i=0; i<slideArray.length;i++){
				for(int j=0;j<slideArray[i].length;j++){
					game[i][j] = slideArray[i][j];
				}
			}
			slideWork(game);
			combine(game);
			slideWork(game);

			//return the game to its original orientation
			game = flipHorizontal(game);
			return true;
		}

		//turn game 270degrees, slide, and combine
		else if(dir == SlideDirection.DOWN){
			int[][] slideArray = clockwise90(game);
			slideArray = clockwise90(slideArray);
			slideArray = clockwise90(slideArray);
			for(int i=0;i<slideArray.length;i++){
				for(int j=0;j<slideArray[i].length;j++){
					game[i][j] = slideArray[i][j];				
				}
			}
			slideWork(game);
			combine(game);
			slideWork(game);

			//return the game to its original orientation
			game = clockwise90(game);
			return true;
		}  

		//turn game 90degrees, flip vertically, slide and combine
		else if(dir == SlideDirection.UP){
			int[][] slideArray = clockwise90(game);
			slideArray = flipVertical(slideArray);
			for(int i=0;i<slideArray.length;i++){
				for(int j=0;j<slideArray[i].length;j++){
					game[i][j] = slideArray[i][j];				
				}
			}
			slideWork(game);
			combine(game);
			slideWork(game);

			//return the game to its original orientation
			game = flipVertical(game);
			game = clockwise90(game);
			game = clockwise90(game);
			game = clockwise90(game); 
			return true;
		}

		//nothing to prepare
		else if(dir == SlideDirection.LEFT){
			slideWork(game);
			combine(game);
			slideWork(game);
			return true;
		}

		//slide didn't work
		else{
			return false;
		}

	}

	/*******************************************************************
	 * @param nums 2D array to slide
	 ******************************************************************/
	public void slideWork(int[][] nums){
		int[] workArray = new int[width];
		int[] newArray = new int[width];
		int count = 0;
		flipVertical(game);

		//cycle through all the rows
		for(int r = 0; r < game.length; r++){

			//reset newArray so values don't carry over
			newArray = new int[width];

			//take out a single row of game and assign it to workArray
			for(int c=0;c<workArray.length;c++){
				workArray[c] = game[c][r];
			}

			count = 0;

			//left-justifies the 1D array
			for(int c = 0;c < workArray.length;c++){
				if (workArray[c] > 0) {
					newArray[count] = workArray[c];
					count++;
				}
			}
			for(int c=0;c<game[r].length;c++){
				game[c][r] = newArray[c];
			}
		}
		flipVertical(game);
	}


	/*******************************************************************
	 * @param nums 2D array to combine
	 ******************************************************************/
	public void combine(int[][] nums){
		for(int r=0;r<nums.length;r++){
			for (int i = 0; i < nums.length-1; i++) {

				//combine like values, and add them to score
				if (nums[i][r] == nums[i + 1][r]) {
					nums[i][r] += nums[i + 1][r];
					score += nums[i][r];
					if(nums[i][r] == winningValue){
						System.out.println("GOAL REACHED! Great Job!");
					}
					nums[i + 1][r] = 0;
				}
			}

			//assign the working array to game
			for(int c=0;c<game[r].length;c++){
				game[c][r] = nums[c][r];
			}

		}
	}

	/*******************************************************************
	 * @param i Integer to test if it is 2^something
	 * @return the result of baseTwo
	 ******************************************************************/
	@Override
	public boolean isBaseTwo(int i){
		double temp = Math.log(i)/Math.log(2);  //hey look! high-school math payed off!
		temp = temp - Math.floor(temp);

		//the product of log2(number) is an integer 
		if(temp == 0){
			return true;
		}else{
			return false;

		}
	}	

	/*******************************************************************
	 * @return an array of size 2 with coords of an empty space
	 ******************************************************************/
	public int[] getEmptySpace(){
		int[] temp = new int[2];
		int count = 0;
		int randRow,randCol = 0;
		
		//make sure the randomness hits all the spots
		while(count < 100){
			count++;
			randRow = random.nextInt(width);
			randCol = random.nextInt(height);
			if(game[randRow][randCol] == 0){
				temp[0] = randRow;
				temp[1] = randCol;
				//System.out.println(temp[0] + " " + temp[1]);
				return temp;
			}
		}

		//-1 signifies that the game has no empty spaces left
		temp[0] = -1;
		temp[1] = 0;
		return temp;

	}

	/*******************************************************************
	 * I mainly just used this for testing
	 * @param nums 1D array to print
	 ******************************************************************/
	public void print1D(int[] nums){
		for(int n: nums){
			System.out.print(n + " ");
		}
		System.out.println();

	}

	/*******************************************************************
	 * again, mainly for testing
	 * @param nums 2D array to print
	 ******************************************************************/
	public void printArray(int[][] nums){
		for(int c=0;c<nums.length;c++){
			for(int r=0;r<nums[c].length;r++){
				System.out.print(nums[c][r] + " ");
			}
			System.out.println("");
		}
		System.out.println("\n");
	}

	/*******************************************************************
	 * rotates a 2D array clockwise 90 degrees
	 * @param array The array to rotate
	 * @return the result of rotation
	 ******************************************************************/
	public int[][] clockwise90(int[][] array){
		int[][] tempArray = new int[width][height];
		for(int r=0;r<width;r++){
			for(int c=0;c<height;c++){
				tempArray[c][width - 1 -r] = array[r][c];
			}
		}
		return tempArray;
	}

	/*******************************************************************
	 * flips a 2D array over the x-axis 
	 * @param array The array to flip
	 * @return the result of flippage
	 ******************************************************************/
	public int[][] flipVertical(int[][] array){
		int[][] tempArray = new int[width][height];
		for(int r = 0; r<height;r++){
			for(int c=0;c<width;c++){
				tempArray[r][width - c - 1] = array[r][c];
			}
		}
		return tempArray;
	}

	/*******************************************************************
	 * flips a 2D array over the y-axis
	 * @param array The array to flip
	 * @return the result of flippage
	 ******************************************************************/
	public int[][] flipHorizontal(int[][] array){
		int[][] tempArray = new int[width][height];
		for(int r = 0; r<height;r++){
			for(int c=0;c<width;c++){
				tempArray[height - r - 1][c] = array[r][c];
			}
		}
		return tempArray;
	}


	/*******************************************************************
	 * @return The current score
	 ******************************************************************/
	public int getScore(){
		return score;
	}

	/*******************************************************************
	 * @return the game used in the UI
	 ******************************************************************/
	public int[][] getGame(){
		return game;
	}

}