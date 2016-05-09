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
		List<int[]> possib_moves = new ArrayList<int[]>();
		possib_moves = board.possibleMoves();
		int[] test = possib_moves.get(0);
		for (int i = 0; i<possib_moves.size(); i++) { 
			int value = 0;
			if(board.isSquare(mySymbol, possib_moves.get(i)) || board.isLine(mySymbol, possib_moves.get(i))) {
					//System.out.println("GOT LINE OR SQUARE in MAX");
					//value = value + 50 - depth;
				}
				board.insert(possib_moves.get(i), mySymbol);
				//System.out.println("SIZE MAX: " + possib_moves.size());
				//board.showBoard();
				value += minValue(board, depth, alpha, beta);
				System.out.println("VALUEEEEE IN MAX: " + value);
				if (value > alpha) {
					alpha = value;
					cell = possib_moves.get(i);
				}
				int[] cell_remove = possib_moves.get(i);
				board.remove(cell_remove);
				if (alpha >= beta) 
					return alpha;
		}
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
				if(board.isSquare(opponentSymbol, possib_moves.get(i)) || board.isLine(opponentSymbol, possib_moves.get(i))) {
					System.out.println("GOT LINE OR SQUARE in MIN");
					value = value - 20 - depth;
				}
				//System.out.println("SIZE MIN: " + possib_moves.size());
				//board.showBoard();
				value += maxValue(board, depth, alpha, beta);
				//System.out.println("VALUEEEEE IN MIN: " + value);
				if (value < beta)
					beta = value;
				int[] cell_remove = possib_moves.get(i);
				board.remove(cell_remove);
				if (beta <= alpha) 
					return beta;
		}
		return beta;
	}

}