/*
	*pylos_AI.java
	*@author Evgeny Rogozik and Wilsen Lie
	*This is the implementation of Minimax (or Negamax may be) algorithm with alpha beta pruning
*/

import java.util.*;
import java.io.*;

public class pylos_AI {
	
	//global variables here
	int white_balls = 15;
	int black_balls = 15;
	
	int playertype = 0;
	
	String moveType = "add";
	
	String color = "white";
	int position = 20;
	
	boolean win = false;
	
	int[][] layer_1 = new int[4][4];
	int[][] layer_2 = new int[3][3];
	int[][] layer_3 = new int[2][2];
	int layer_4 = 0;
	
	long duration = 0; //The timing of the AI's move
	
	public static void main(String[] args) {
		pylos_AI instance = new pylos_AI();
		instance.greeting();
		instance.drawBoard();
		instance.makeMove();
		/*
			while(win == false) {
			drawBoard();
			makeMove();
			checkwin();
			}
		*/	
	}
	
	public void greeting() {
		System.out.println("This is the terminal based AI for pylos game implemented using" + "\n" + "Minimax algorithm with alpha beta pruning.");
		boolean temp = false;
		
		
		while (temp == false) {
			Scanner keyboard = new Scanner(System.in);
			System.out.println("Please choose who will make the move first (1-AI, 2-You): ");
			if (!keyboard.hasNextInt()) {
				System.out.println("Error!!");
				continue;
			}
			else {
				playertype = keyboard.nextInt();
				if(playertype!=1 && playertype!=2) { 
					System.out.println("Wrong input! Try again: ");
				}
				else {
					temp = true;
				}
			}
		}
		System.out.println("playertype: " + playertype);
	}
	
	public void drawBoard() {
		System.out.println("==========================");
		System.out.println("Current game board");
		System.out.println("==========================");
		
		String newLine = System.getProperty("line.separator");//This will retrieve line separator dependent on OS.
		
		System.out.println(newLine);
		System.out.println("Move: {type: " + moveType + ", position: " + position + ", color: " + color + "}");
		System.out.println("Time: " + duration);
		System.out.println();
		
		
		for (int i = 0; i<4; i++) {
			
			//4x4
			for (int j = 0; j<4; j++) {
				if (layer_1[i][j] == 1) System.out.print("w ");
				else if (layer_1[i][j] == 2) System.out.print("b ");
				else System.out.print("o ");
			}
			
			System.out.print("    ");
			
			//3x3
			if (i<3) {
				for (int j = 0; j<3; j++) {
					if (layer_2[i][j] == 1) System.out.print("w ");
					else if (layer_2[i][j] == 2) System.out.print("b ");
					else System.out.print("o ");
				}
			}
			
			System.out.print("    ");
			
			//2x2
			if (i<2) {
				for (int j=0; j<2; j++) {
					if (layer_3[i][j] == 1) System.out.print("w ");
					else if (layer_3[i][j] == 2) System.out.print("b ");
					else System.out.print("o ");
				}
			}
			
			System.out.print("    ");
			
			//Top of pyramid
			if(i==0){
				if (layer_4 == 1) System.out.print("w ");
				else if (layer_4 == 2) System.out.print("b ");
				else System.out.print("o ");
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.print("Balls remaining: WHITE: " + white_balls + ", BLACK: " + black_balls);
		System.out.println(newLine);
		System.out.println("Some algorithm statistics here: ");
		System.out.println();
	}
	
	public void makeMove() {
		System.out.println("Please make a move in the followin format: \n The bottom tier is squares: [a,b,c,d][1-4], \n The second tier up is squares: [e,f,g][1-3], \n The third tier up is squares: [h,i][1-2], \n The top tier is square j1.");
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Your move: ");
		String move = keyboard.next();
		System.out.println(move);
		
		//Check isLine() and isSquare if player puts in 1st or 2nd tier
		if (move.charAt(0) != 'h' && move.charAt(0) != 'i' && move.charAt(0) != 'j'){
			if (isLine(playertype, move) || isSquare(playertype, move)) {
				//remove balls here
			}
		}
		
		//Here we manually enter the coordinates for the move.
		//Then based on player type we either do run Minimax (in case of AI's move) or we do not (in case of Human move - just place the ball)
	}
	
	//For isSquare() and isLine()
	char[] rows = {'a', 'b', 'c', 'd','e','f','g','h','i','j'};
	char[][] rows2 = {{'a', 'b', 'c', 'd'},{'e','f','g'},{'h','i'},{'j'}};
	char[][] cols = {{'1','2','3','4'},{'1','2','3'}, {'1','2'},{'1'}};
	
	public boolean isSquare(int whichPlayer, String currentMove) {
		//There are 14 possible squares (4x4, 3x3, 2x2)
		//We don't need to check the whole board
		//Just check after the last move and surround
		char tempRow = currentMove.charAt(0);
		int tempCol = Character.getNumericValue(currentMove.charAt(1))-1;
		int findTier =  Arrays.binarySearch(rows, tempRow);
		if (findTier >=0 && findTier <=3) { //1st tier
			int temp = tempRow - 97; //97 is an int value of char 'a'
			
			//Top left corner
			if (temp-1 >= 0 && tempCol-1 >= 0) {
				if (layer_1[temp-1][tempCol-1] == whichPlayer && layer_1[temp-1][tempCol] == whichPlayer && layer_1[temp][tempCol-1] == whichPlayer) {
					return true;
				}
			}
			//top right corner
			else if (temp-1 >= 0 && tempCol+1 <= 3) {
				if (layer_1[temp-1][tempCol+1] == whichPlayer && layer_1[temp-1][tempCol] == whichPlayer && layer_1[temp][tempCol+1] == whichPlayer) {
					return true;
				}
			}
			//bottom left corner
			else if (temp+1 <= 3 && tempCol-1 >= 0) {
				if (layer_1[temp+1][tempCol-1] == whichPlayer && layer_1[temp+1][tempCol] == whichPlayer && layer_1[temp][tempCol-1] == whichPlayer) {
					return true;
				}
			}
			//bottom right corner
			else if (temp+1 <= 3 && tempCol+1 <= 3) {
				if (layer_1[temp+1][tempCol+1] == whichPlayer && layer_1[temp+1][tempCol] == whichPlayer && layer_1[temp][tempCol+1] == whichPlayer) {
					return true;
				}
			}
		}
		
		else { //2nd tier
			int temp = tempRow - 101; //101 is an int value of char 'e'
			
			//Top left corner
			if (temp-1 >= 0 && tempCol-1 >= 0) {
				if (layer_2[temp-1][tempCol-1] == whichPlayer && layer_2[temp-1][tempCol] == whichPlayer && layer_2[temp][tempCol-1] == whichPlayer) {
					return true;
				}
			}
			//top right corner
			else if (temp-1 >= 0 && tempCol+1 <= 2) {
				if (layer_2[temp-1][tempCol+1] == whichPlayer && layer_2[temp-1][tempCol] == whichPlayer && layer_2[temp][tempCol+1] == whichPlayer) {
					return true;
				}
			}
			//bottom left corner
			else if (temp+1 <= 2 && tempCol-1 >= 0) {
				if (layer_2[temp+1][tempCol-1] == whichPlayer && layer_2[temp+1][tempCol] == whichPlayer && layer_2[temp][tempCol-1] == whichPlayer) {
					return true;
				}
			}
			//bottom right corner
			else if (temp+1 <= 2 && tempCol+1 <= 2) {
				if (layer_2[temp+1][tempCol+1] == whichPlayer && layer_2[temp+1][tempCol] == whichPlayer && layer_2[temp][tempCol+1] == whichPlayer) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isLine(int whichPlayer, String currentMove) {
		//There are 14 possible lines
		char tempRow = currentMove.charAt(0);
		int tempCol = Character.getNumericValue(currentMove.charAt(1))-1;
		int i = 0;
		boolean hor = true;
		boolean ver = true;
		
		int findTier =  Arrays.binarySearch(rows, tempRow);
		if (findTier >=0 && findTier <=3) {
			//in 1st tier 4x4
			int temp = tempRow - 97; //97 is an int value of char 'a'
			for (i=0; i<4; i++) { //horizontal
				if (layer_1[temp][i] != whichPlayer) {
					hor = false;
					break;
				}
			}
			for (i=0; i<4; i++) { //vertical
				if (layer_1[(temp+i)%4][tempCol] != whichPlayer) {
					ver = false;
					break;
				}
			}
			return (hor || ver);
		}
		else {
			//in 2nd tier 3x3
			int temp = tempRow - 101; //101 is an int value of char 'e'
			for (i=0; i<3; i++) { //horizontal
				if (layer_2[temp][i] != whichPlayer) {
					hor = false;
					break;
				}
			}
			for (i=0; i<3; i++) { //vertical
				if (layer_2[(temp+i)%3][tempCol] != whichPlayer) {
					ver = false;
					break;
				}
			}
			return (hor || ver);
		}
	}
	
	public int minimax(int alpha, int beta, int maxDepth, int playertype) { 
		long startTime = System.nanoTime();
		//TODO here
		long endTime = System.nanoTime();
		duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
		return 0;
	}
	
	public boolean checkwin() {
		//Check for wining here - it is either when one of the players has no balls left or the one who put his ball on top of the pyramid
		if (black_balls==0 || white_balls==0) {
			return true;
		}
		if (layer_4!=0) return true;

		return false;
	}
}	
