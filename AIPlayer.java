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
		//if (board.isFinished() != ' ') 
			//if (board.isFinished() == mySymbol)
				//return 255 - depth;
			//else return 0 + depth; 
		//check if the depth has reached its limit or if the 
		//board is in a terminal (tie) state and return its utility value.
		if (depth == searchLimit)
			//return board.evaluateContent();
		depth = depth + 1;
		int column = 0;
		List<int[]> possib_moves = new ArrayList<int[]>();
		for (int i = 0; i<possib_moves.size(); i++) { 
				board.insert(possib_moves.get(i), mySymbol);
				//int value = minValue(board, depth, alpha, beta);
				int value = 0;
				if (value > alpha) {
					alpha = value;
					column = i;
				}
				//board.remove(i);
				if (alpha >= beta) 
					return alpha;
		}
		//maxCell = column;
		return alpha;
	}

}