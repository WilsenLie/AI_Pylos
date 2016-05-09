/*
	*pylos_AI.java
	*@author Evgeny Rogozik and Wilsen Lie
	*This is the implementation of Minimax (or Negamax may be) algorithm with alpha beta pruning
*/

import java.util.*;
import java.io.*;

public class pylos_AI {

	public static AIPlayer ai_player;
	public static Board board = new Board();
	public static int limit = 4;
	
	//global variables here
	int white_balls = 15;
	int black_balls = 15;
	
	public static int currentPlayer;
	
	String moveType = "add";
	
	String color = "white";
	int position = 20;
	
	public static boolean win = false;
	
	long duration = 0; //The timing of the AI's move
	
	public static void main(String[] args) {
		ai_player = new AIPlayer(limit);
		pylos_AI instance = new pylos_AI();
		int[] cell;
		instance.greeting();
		board.possibleMoves();
		board.showBoard();

		do {
			if(currentPlayer==1) {
				cell = ai_player.alphaBetaSearch(board);
				board.insert(cell, currentPlayer);
				System.out.println("AI made its move!!");
			}
			else instance.makeMove();

			board.showBoard();
			changePlayer();
			instance.checkwin();
		}
		while(win != true);
	}

	public static void changePlayer() {
		if (currentPlayer == 1) 
				currentPlayer = 2;
		else currentPlayer = 1;
	}
	
	public void greeting() {
		System.out.println("This is the terminal based AI for pylos game implemented using" + "\n" + "Minimax algorithm with alpha beta pruning.");
		boolean temp = false;
		
		
		while (temp == false) {
			Scanner keyboard = new Scanner(System.in);
			System.out.println("Please choose who will make the move first (1-AI, 2-You): ");
			if (!keyboard.hasNextInt()) {
				System.out.println("Wrong input! Try again: ");
				continue;
			}
			else {
				currentPlayer = keyboard.nextInt();
				if(currentPlayer!=1 && currentPlayer!=2) { 
					System.out.println("Wrong input! Try again: ");
				}
				else {
					temp = true;
				}
			}
		}
		System.out.println("currentPlayer: " + currentPlayer);
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

		board.showBoard();
		
		System.out.println();
		System.out.print("Balls remaining: WHITE: " + white_balls + ", BLACK: " + black_balls);
		System.out.println(newLine);
		System.out.println("Some algorithm statistics here: ");
		System.out.println();
	}
	
	public void makeMove() {
		System.out.println("Please make a move in the followin format: \n The bottom tier is squares: [a,b,c,d][1-4], \n The second tier up is squares: [e,f,g][1-3], \n The third tier up is squares: [h,i][1-2], \n The top tier is square j1.");
		Scanner keyboard = new Scanner(System.in);
		String move;
		int[] translated_move;
		boolean good_move = false;
		do {
		System.out.println("Your move: ");
		move = keyboard.next();
		translated_move = board.transCoordinate(move);
		
			if(board.legalMove(translated_move)) {
				board.insert(translated_move, currentPlayer);
				good_move = true;
			}
			else {
				System.out.println("Cell is occupied");
			}
		}
		while(good_move != true);
		updateRemovable(translated_move, currentPlayer);
		
		//Check isLine() and isSquare if player puts in 1st or 2nd tier
		if (move.charAt(0) != 'h' && move.charAt(0) != 'i' && move.charAt(0) != 'j'){
			if (isLine(currentPlayer, move) || isSquare(currentPlayer, move)) {
				//remove balls here
				System.out.println("FOUND a line or square!!");
			}
		}
		
		//Here we manually enter the coordinates for the move.
		//Then based on player type we either do run Minimax (in case of AI's move) or we do not (in case of Human move - just place the ball)
	}
	
	//For isSquare() and isLine()
	char[] rows = {'a', 'b', 'c', 'd','e','f','g','h','i','j'};
	int[] removable = new int[29];
	
	//0 for non-removable
	//1 for removable by player 1, 2 for removable by player 2
	public void updateRemovable(int[] currentMove, int whichPlayer) {
		if (currentMove[0] == 1) {
			removable[coordToInt(currentMove[0], currentMove[1], currentMove[2])] = whichPlayer;
		}
		else if (currentMove[0] == 2) {
			int temp = coordToInt(currentMove[0], currentMove[1], currentMove[2]);
			removable[temp] = whichPlayer;
			
			//if ball is in 2nd tier, 4 balls below it cannot be removed
			int[] temp2 = {(temp-16)/3, (temp-16)%3}; //back to coordinates
			removable[coordToInt(1,temp2[0], temp2[1])] = 0;
			removable[coordToInt(1,temp2[0]+1, temp2[1])] = 0;
			removable[coordToInt(1,temp2[0], temp2[1]+1)] = 0;
			removable[coordToInt(1,temp2[0]+1, temp2[1]+1)] = 0;
		}
		else if (currentMove[0] == 3) {
			int temp = coordToInt(currentMove[0], currentMove[1], currentMove[2]);
			removable[temp] = whichPlayer;
			
			//if ball is in 3rd tier, 4 balls below it cannot be removed
			int[] temp2 = {(temp-25)/2, (temp-25)%2}; //back to coordinates
			removable[coordToInt(2,temp2[0], temp2[1])] = 0;
			removable[coordToInt(2,temp2[0]+1, temp2[1])] = 0;
			removable[coordToInt(2,temp2[0], temp2[1]+1)] = 0;
			removable[coordToInt(2,temp2[0]+1, temp2[1]+1)] = 0;
		}
	}
	
	private int coordToInt (int z, int x, int y) {
		//x = row
		//y = col
		//z = which tier
		
		if (z==1) {
			return ((x*4) + y);
		}
		else if (z==2) {
			return (16 + (x*3) + y);
		}
		else if (z==3) {
			return (25+(x*2)+y);
		}
		else {
			return 29;
		}
	}
	
	
	
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
				if (board.tier1_board[temp-1][tempCol-1] == whichPlayer && board.tier1_board[temp-1][tempCol] == whichPlayer && board.tier1_board[temp][tempCol-1] == whichPlayer) {
					return true;
				}
			}
			//top right corner
			else if (temp-1 >= 0 && tempCol+1 <= 3) {
				if (board.tier1_board[temp-1][tempCol+1] == whichPlayer && board.tier1_board[temp-1][tempCol] == whichPlayer && board.tier1_board[temp][tempCol+1] == whichPlayer) {
					return true;
				}
			}
			//bottom left corner
			else if (temp+1 <= 3 && tempCol-1 >= 0) {
				if (board.tier1_board[temp+1][tempCol-1] == whichPlayer && board.tier1_board[temp+1][tempCol] == whichPlayer && board.tier1_board[temp][tempCol-1] == whichPlayer) {
					return true;
				}
			}
			//bottom right corner
			else if (temp+1 <= 3 && tempCol+1 <= 3) {
				if (board.tier1_board[temp+1][tempCol+1] == whichPlayer && board.tier1_board[temp+1][tempCol] == whichPlayer && board.tier1_board[temp][tempCol+1] == whichPlayer) {
					return true;
				}
			}
		}
		
		else { //2nd tier
			int temp = tempRow - 101; //101 is an int value of char 'e'
			
			//Top left corner
			if (temp-1 >= 0 && tempCol-1 >= 0) {
				if (board.tier2_board[temp-1][tempCol-1] == whichPlayer && board.tier2_board[temp-1][tempCol] == whichPlayer && board.tier2_board[temp][tempCol-1] == whichPlayer) {
					return true;
				}
			}
			//top right corner
			else if (temp-1 >= 0 && tempCol+1 <= 2) {
				if (board.tier2_board[temp-1][tempCol+1] == whichPlayer && board.tier2_board[temp-1][tempCol] == whichPlayer && board.tier2_board[temp][tempCol+1] == whichPlayer) {
					return true;
				}
			}
			//bottom left corner
			else if (temp+1 <= 2 && tempCol-1 >= 0) {
				if (board.tier2_board[temp+1][tempCol-1] == whichPlayer && board.tier2_board[temp+1][tempCol] == whichPlayer && board.tier2_board[temp][tempCol-1] == whichPlayer) {
					return true;
				}
			}
			//bottom right corner
			else if (temp+1 <= 2 && tempCol+1 <= 2) {
				if (board.tier2_board[temp+1][tempCol+1] == whichPlayer && board.tier2_board[temp+1][tempCol] == whichPlayer && board.tier2_board[temp][tempCol+1] == whichPlayer) {
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
				if (board.tier1_board[temp][i] != whichPlayer) {
					hor = false;
					break;
				}
			}
			for (i=0; i<4; i++) { //vertical
				if (board.tier1_board[(temp+i)%4][tempCol] != whichPlayer) {
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
				if (board.tier2_board[temp][i] != whichPlayer) {
					hor = false;
					break;
				}
			}
			for (i=0; i<3; i++) { //vertical
				if (board.tier2_board[(temp+i)%3][tempCol] != whichPlayer) {
					ver = false;
					break;
				}
			}
			return (hor || ver);
		}
	}
	
	public boolean checkwin() {
		//Check for wining here - it is either when one of the players has no balls left or the one who put his ball on top of the pyramid
		if (black_balls==0 || white_balls==0) {
			return true;
		}
		if (board.tier_4!=0) return true;

		return false;
	}
}	
