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
		instance.drawBoard();
		/*
			while(win == false) {
				drawBoard();
				makeMove();
				checkwin();
			}
		 */	
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

				System.out.print("    ");

				if (i<3 && j<3) {
					if (layer_2[i][j] == 1) System.out.print("w ");
					else if (layer_2[i][j] == 2) System.out.print("b ");
					else System.out.print("o ");
				}

				if (i<2 && j<2) {
					if (layer_3[i][j] == 1) System.out.print("w ");
					else if (layer_3[i][j] == 2) System.out.print("b ");
					else System.out.print("o ");
				}
			}
		}

	}

	public int minimax(int alpha, int beta, int maxDepth, int playertype) { 
		long startTime = System.nanoTime();
		//TODO here
		long endTime = System.nanoTime();
		duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
		return 0;
	}
}