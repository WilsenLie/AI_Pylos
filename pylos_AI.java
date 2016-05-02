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
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Please choose who will make the move first (1-AI, 2-You): ");
		playertype = keyboard.nextInt();
		System.out.println("playertype: " + playertype);
		while(playertype!=1 && playertype!=2) {
			System.out.println("Wrong input! Try again: ");
			playertype = keyboard.nextInt();
		}
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
			for (int j = 0; j<4; j++) {
				if (layer_1[i][j] == 1) System.out.print("w ");
				else if (layer_1[i][j] == 2) System.out.print("b ");
				else System.out.print("o ");
			}

			System.out.print("    ");

			if (i<3) {
				for (int j = 0; j<3; j++) {
						if (layer_2[i][j] == 1) System.out.print("w ");
						else if (layer_2[i][j] == 2) System.out.print("b ");
						else System.out.print("o ");
				}
			}

			System.out.print("    ");

			if (i<2) {
				for (int j=0; j<2; j++) {
					if (layer_3[i][j] == 1) System.out.print("w ");
					else if (layer_3[i][j] == 2) System.out.print("b ");
					else System.out.print("o ");
				}
			}

			System.out.print("    ");

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

		//Here we manually enter the coordinates for the move.
		//Then based on player type we either do run Minimax (in case of AI's move) or we do not (in case of Human move - just place the ball)
	}

	public int minimax(int alpha, int beta, int maxDepth, int playertype) { 
		long startTime = System.nanoTime();
		//TODO here
		long endTime = System.nanoTime();
		duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
		return 0;
	}

	public void checkwin() {
		//Check for wining combinations here
	}
}