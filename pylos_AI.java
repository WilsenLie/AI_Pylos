/*
	*pylos_AI.java
	*Evgeny Rogozik - 21177069
	*Wilsen Lie - 21105559
	*This is the implementation of Minimax algorithm with alpha beta pruning
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
		public static int[] cell_to_remove_1 = {0,0,0}; //will store coordinates of first cell AI want to remove
		public static int[] cell_to_remove_2 = {0,0,0}; //will store coordinates of the second cell AI wants to remove
		public static boolean up_tier = false; //false if AI wants to use new ball; true if AI want to move up existing ball.
		public static int[] from = {0,0,0}; //which ball to move UP

		public static int currentPlayer;
		public static int firstPlayer;

		public static boolean win = false;

		long duration = 0; //The timing of the AI's move

		public static void main(String[] args) {
			ai_player = new AIPlayer(limit);
			pylos_AI instance = new pylos_AI();
			int[] cell;
			instance.greeting();
			board.possibleMoves();
			board.showBoard(firstPlayer);

			do {
				if(currentPlayer==1) {
					long startTime = System.nanoTime();
					cell = ai_player.alphaBetaSearch(board);
					long endTime = System.nanoTime();
					long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.

					System.out.println("UPTIER: " + up_tier);
					System.out.println("FROM: " + from[0] + " " + from[1] + " " + from[2]);
					if(up_tier == true) {
						board.remove(from);
						board.updateRemovable(from, 1, 2);
						board.insert(cell, currentPlayer);
						board.updateRemovable(cell, 1, 1);
						System.out.println("AI MOVED UP");
						up_tier = false;
						int temp[] = {0,0,0};
						from = temp;
					}
					else {
						board.insert(cell, currentPlayer);
						board.updateRemovable(cell, 1, 1);
						white_balls-=1;
						System.out.println("AI made its move: {" + cell[0] + ", " + cell[1] + ", " + cell[2] + "}");
						System.out.println(cell_to_remove_1[0] + " " + cell_to_remove_1[1] + " " + cell_to_remove_1[2]);
					}
					if(cell_to_remove_1[0] != 0) {
						board.remove(cell_to_remove_1);
						board.updateRemovable(cell_to_remove_1, 1, 2);
						System.out.println("AI REMOVED 1st ball! {" + cell_to_remove_1[0] + " " + cell_to_remove_1[1] + " " + cell_to_remove_1[2]);
						white_balls+=1;
						int[] temp = {0,0,0};
						cell_to_remove_1 = temp;
					}
					if(cell_to_remove_2[0] != 0) {
						board.remove(cell_to_remove_2);
						board.updateRemovable(cell_to_remove_2, 1, 2);
						System.out.println("AI REMOVED 2nd ball! {" + cell_to_remove_2[0] + " " + cell_to_remove_2[1] + " " + cell_to_remove_2[2]);
						white_balls+=1;
						int[] temp = {0,0,0};
						cell_to_remove_2 = temp;
					}
					System.out.println("AlphaBeta timing: " + duration/1000000 + " milliseconds\n");	
					if(instance.checkwin()) {
						win = true;
						System.out.println("AI WINS");	
					} 
				}
				else {
					instance.makeMove();
					if(instance.checkwin()) {
						win = true;
						System.out.println("HUMAN WINS");	
					}
				}
				System.out.println("AI's Balls: " + white_balls + "; Human's Balls: " + black_balls);
				board.showBoard(firstPlayer);
				changePlayer();
			}
			while(win != true);
		}

		public static void changePlayer() {
			if (currentPlayer == 1) 
				currentPlayer = 2;
			else currentPlayer = 1;
		}

		//Greeting when we run the program
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
						firstPlayer = currentPlayer;
						temp = true;
					}
				}
			}
			System.out.println("currentPlayer: " + currentPlayer);
		}
	
		public void makeMove() {
			System.out.println("Please make a move in the followin format: \n The bottom tier is squares: [a,b,c,d][1-4], \n The second tier up is squares: [e,f,g][1-3], \n The third tier up is squares: [h,i][1-2], \n The top tier is square j1.");
			System.out.println("=======\nThe move is in the format: coord1 coord2.\nIf you wish to use new ball type: 0 coord1\nif you wish to move the ball to upper tier type: coord1 coord2\n- where coord1 - ball to move, coord2 - to where you move it");
			Scanner keyboard = new Scanner(System.in);
			String move;
			int[] translated_move_1 = {0,0,0};
			int[] translated_move_2 = {0,0,0};
			char[] legalAlphabet = {'a','b','c','d','e','f','g','h','i','j'};
			char[] legalNumber = {'1','2','3','4'};
			boolean good_move = false;
			do {
				System.out.println("Your move: ");
				move = keyboard.nextLine();
				String[] splited = move.split("\\s+");
				System.out.println(splited[0] + " +++++ " + splited[1]);
				if (splited.length < 2 || splited.length > 2) {
					System.out.println("Wrong input");
					continue;
				}
				else if(splited[0].equals("0") && Arrays.binarySearch(legalAlphabet, splited[1].charAt(0)) >= 0 && Arrays.binarySearch(legalNumber, splited[1].charAt(1)) >= 0) {
					translated_move_2 = board.transCoordinate(splited[1]);

					if(board.legalMove(translated_move_2)) {
						board.insert(translated_move_2, currentPlayer);
						int[][] test = board.updateRemovable(translated_move_2, 2, 1);
						good_move = true;
						black_balls-=1;
					}
					else {
						System.out.println("Cell is occupied");
					}
				}
				else if (Arrays.binarySearch(legalAlphabet, splited[0].charAt(0)) >= 0 && Arrays.binarySearch(legalNumber, splited[0].charAt(1)) >= 0 && Arrays.binarySearch(legalAlphabet, splited[1].charAt(0)) >= 0 && Arrays.binarySearch(legalNumber, splited[1].charAt(1)) >= 0) { // move up - works
					translated_move_1 = board.transCoordinate(splited[0]);
					translated_move_2 = board.transCoordinate(splited[1]);
					board.remove(translated_move_1);
					board.updateRemovable(translated_move_1, 2, 2);
					board.insert(translated_move_2, currentPlayer);
					board.updateRemovable(translated_move_2, 2, 1);
					System.out.println("YOU MOVED BALL TO UPPER TIER");
					good_move = true;
				}
				else {
					System.out.println("Wrong input");
					continue;
				}
			}
			while(good_move != true);

		
			//Check isLine() and isSquare if player puts in 1st or 2nd tier
			if (move.charAt(0) != 'h' && move.charAt(0) != 'i' && move.charAt(0) != 'j'){
				if (board.isLine(currentPlayer, translated_move_2) || board.isSquare(currentPlayer, translated_move_2)) {
					//remove balls here
					int balls_to_remove = 0;
					System.out.println("FOUND a line or square!");
					boolean temp1 = false;
					while (temp1 != true) {
						System.out.println("How many balls you want to remove (1 or 2): ");
						balls_to_remove = keyboard.nextInt();
						if (balls_to_remove == 1 || balls_to_remove == 2) {
							temp1 = true;
							continue;
						}
						System.out.println ("Wrong input");
					}
					for(int i = 1; i<=balls_to_remove; i++) {
						System.out.println("Ball to remove number " + i + ": ");
						String remove = keyboard.next();
						if (Arrays.binarySearch(legalAlphabet,remove.charAt(0)) < 0 || Arrays.binarySearch(legalNumber,remove.charAt(1)) < 0) {
							System.out.println("Wrong input");
							i--;
							continue;
						}
						int[] translated_remove = board.transCoordinate(remove);
						board.remove(translated_remove);
						board.updateRemovable(translated_remove, 2, 2);
						black_balls+=1;

					}
				}
			}
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
