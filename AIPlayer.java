import java.util.*;
import java.io.*;

public class AIPlayer {
	private int mySymbol = 1;
	private int opponentSymbol = 2;
	private int searchLimit;
	private int[] maxCell;
	
	
	public AIPlayer(int limit) {
		searchLimit = limit;
	}

	public int[] alphaBetaSearch(Board board) {
		maxValue(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
		return maxCell;
	}

	private int maxValue(Board board, int depth, int alpha, int beta) {
		//check if the board is in a terminal (winning) state and
		//return the maximum or minimum utility value (255 - depth or 
		//0 + depth) if the max player or min player is winning.
		//System.out.println("MAX started");
		if (board.isWin() != 0) 
			if (board.isWin() == mySymbol)
				return 255 - depth;
			else return 0 + depth; 
		//check if the depth has reached its limit or if the 
		//board is in a terminal (tie) state and return its utility value.
			if (depth == searchLimit)
				return board.evaluateContent();
			depth = depth + 1;
			int[] cell = {0,0,0};
			int[] rem_cell = {0,0,0};
			List<int[]> possib_moves = new ArrayList<int[]>();
			possib_moves = board.possibleMoves();
			int[] test = possib_moves.get(0);
			for (int i = 0; i<possib_moves.size(); i++) { 
				//System.out.println("STARTER LOOPING POSSIBLE MOVES");
				int value = 0;
				board.insert(possib_moves.get(i), mySymbol);
				int[][] toremove = board.updateRemovable(possib_moves.get(i), 1, 1);
				if(board.isSquare(mySymbol, possib_moves.get(i)) || board.isLine(mySymbol, possib_moves.get(i))) {
					if (depth==1) {
						System.out.println("AI CAN REMOVE NEXT MOVE");
					}
					//single loop here to check removeable list
					//board.insert(possib_moves.get(i), mySymbol);
					pylos_AI.white_balls-=1;
					for(int k = 0; k<29; k++) {
						int[] removed_cell = board.intToCoord(k);
						if(toremove[k][1] == 1 && toremove[k][2]==1) {
							value = 0;
							value = value + 100- depth; //if for AI creating a its own square or line is more important
							board.remove(removed_cell);
							board.updateRemovable(removed_cell, 1, 2);
							pylos_AI.white_balls+=1;
							value+=5;
							value += minValue(board, depth, alpha, beta);
							if (value > alpha) {
								//System.out.println("SET ALPHA TO: " + value);
								alpha = value;
								cell = possib_moves.get(i);
								if(depth == 1) {
									pylos_AI.cell_to_remove = removed_cell;
								}
							}
							board.insert(removed_cell, 1);
							board.updateRemovable(removed_cell, 1, 1);
							pylos_AI.white_balls-=1;
						}
						//System.out.println("I WANT TO REMOVE: {" + removed_cell[0] + " " + removed_cell[1] + " " + removed_cell[2]);
					}
					int[] cell_remove = possib_moves.get(i);
					board.remove(cell_remove);
					board.updateRemovable(cell_remove, 1, 2);
					pylos_AI.white_balls+=1;
					if (alpha >= beta) 
						return alpha;
					System.out.println("VALUE IN AI SQUARE: " + value);
					//nested loop here to check possible remove combos of two balls
				}
				else {
					//board.insert(possib_moves.get(i), mySymbol);
					//board.updateRemovable(possib_moves.get(i), 1, 1);
					pylos_AI.white_balls-=1;
					value += minValue(board, depth, alpha, beta);
					if (value > alpha) {
						alpha = value;
						cell = possib_moves.get(i);
						int[] blah = {0,0,0};
						pylos_AI.cell_to_remove = blah;
					}
					int[] cell_remove = possib_moves.get(i);
					board.remove(cell_remove);
					board.updateRemovable(cell_remove, 1, 2);
					pylos_AI.white_balls+=1;
					if (alpha >= beta) 
						return alpha;
				}
			}
			System.out.println("FINAL ALPHA: " + alpha);
			maxCell = cell;
			return alpha;
		}

		private int minValue(Board board, int depth, int alpha, int beta) {
		//check if the board is in a terminal (winning) state and
		//return the maximum or minimum utility value (255 - depth or 
		//0 + depth) if the max player or min player is winning.
		//System.out.println("MIN started");
			if (board.isWin() != 0)
				if (board.isWin() == mySymbol)
					return 255 - depth;
				else return 0 + depth;
		//check if the depth has reached its limit or if the 
		//board is in a terminal (tie) state and return its utility value.
				if (depth == searchLimit)
					return board.evaluateContent();
				depth = depth + 1;
				List<int[]> possib_moves = new ArrayList<int[]>();
				possib_moves = board.possibleMoves();
				for (int i = 0; i < possib_moves.size(); i++) {
					int value = 0;
					board.insert(possib_moves.get(i), opponentSymbol);
					board.updateRemovable(possib_moves.get(i), 2, 1);
					pylos_AI.black_balls-=1;
					if(board.isSquare(opponentSymbol, possib_moves.get(i)) || board.isLine(opponentSymbol, possib_moves.get(i))) {
					value = value - 80 - depth; //AI will try to block our lines and squares
					}
				//System.out.println("SIZE MIN: " + possib_moves.size());
				//board.showBoard();
					value += maxValue(board, depth, alpha, beta);
				//System.out.println("VALUEEEEE IN MIN: " + value);
					if (value < beta)
						beta = value;
					int[] cell_remove = possib_moves.get(i);
					board.remove(cell_remove);
					board.updateRemovable(cell_remove, 2, 2);
					pylos_AI.black_balls+=1;
					if (beta <= alpha) 
						return beta;
				}
				return beta;
			}

		}