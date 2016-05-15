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
	public static int white_balls = 15;
	public static int black_balls = 15;
	public static int[] cell_to_remove = {0,0,0};
	
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
				board.updateRemovable(cell, 1, 1);
				white_balls-=1;
				System.out.println("AI made its move: {" + cell[0] + ", " + cell[1] + ", " + cell[2] + "}");
				if(cell_to_remove[0] != 0) {
					board.remove(cell_to_remove);
					board.updateRemovable(cell_to_remove, 1, 2);
					System.out.println("AI REMOVED!!!!!! {" + cell_to_remove[0] + " " + cell_to_remove[1] + " " + cell_to_remove[2]);
					white_balls+=1;
					int[] temp = {0,0,0};
					cell_to_remove = temp;
				}
			}
			else {
				instance.makeMove();
				black_balls-=1;
			}
			System.out.println("White Balls: " + white_balls + "; Black Balls: " + black_balls);
			board.showBoard();
			//for (int i =0; i<29; i++) {
			//for (int j=0; j<3; j++) {
				//System.out.print(board.removable[i][j] + ",");
				//}
			//	System.out.println(" ");
		//}
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
				board.updateRemovable(translated_move, 2, 1);
				good_move = true;
			}
			else {
				System.out.println("Cell is occupied");
			}
		}
		while(good_move != true);

		
		//Check isLine() and isSquare if player puts in 1st or 2nd tier
		if (move.charAt(0) != 'h' && move.charAt(0) != 'i' && move.charAt(0) != 'j'){
			if (board.isLine(currentPlayer, translated_move) || board.isSquare(currentPlayer, translated_move)) {
				//remove balls here
				System.out.println("FOUND a line or square!!");
				System.out.println("How many balls you want to remove (1 or 2): ");
				int balls_to_remove = keyboard.nextInt();
				for(int i = 1; i<=balls_to_remove; i++) {
					System.out.println("Ball to remove number " + i + ": ");
					String remove = keyboard.next();
					int[] translated_remove = board.transCoordinate(remove);
					board.remove(translated_remove);
					board.updateRemovable(translated_remove, 2, 2);
					black_balls+=1;

				}
			}
		}
		
		//Here we manually enter the coordinates for the move.
		//Then based on player type we either do run Minimax (in case of AI's move) or we do not (in case of Human move - just place the ball)
	}
	
	
	public boolean checkwin() {
		//Check for wining here - it is either when one of the players has no balls left or the one who put his ball on top of the pyramid
		if (black_balls==0 || white_balls==0) {
			return true;
		}
		if (board.tier_4[0][0]!=0) return true;

		return false;
	}
}	
