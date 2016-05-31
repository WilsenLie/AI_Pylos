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
		//return the maximum or minimum utility value (2505 - depth or 
		//0 + depth) if the max player or min player is winning.
		//System.out.println("MAX started");
		if (board.isWin() != 0) {
			if (board.isWin() == mySymbol)
				return 2505 - depth;
			else return 0 + depth; 
		}
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
			int value = 0;
			int[] current_move = possib_moves.get(i);
//MOVE UP CHECK===================================================================================================================================
			if (current_move[0] == 2 || current_move[0] == 3) {//only if move is on tier 2 or tier 3
				value-=80;
				board.insert(current_move, mySymbol);
				int[][] temp_rem = board.updateRemovable(current_move, 1, 1);
				board.remove(current_move);
				board.updateRemovable(current_move, 1, 2);
				for(int k = 0; k<29; k++) {
					board.insert(current_move, mySymbol);
					board.updateRemovable(current_move, 1, 1);//insert and update the removeable balls first.
					int[] current_ball = board.intToCoord(k);
					if(board.removable[k][1] == 1 && board.removable[k][0] == 0 && current_ball[0] == current_move[0]-1) {//check which balls are removeable
						current_ball = board.intToCoord(k);
						board.remove(current_ball);//remove the ball (so it looks like it is moved up) and do the rest of the algorithm to get the value of the board state.
						int[][] toremove = board.updateRemovable(current_ball, 1, 2);
						if(board.isSquare(mySymbol, current_move) || board.isLine(mySymbol, current_move)) {//if found line or square
							for(int n = 0; n<29; n++) {//check all possible removals of ONE ball
								int[] removed_cell = board.intToCoord(n);
								if(toremove[n][1] == 1 && toremove[n][0]==0) {
									value = 5;
									value = value + 15- depth; //AI creating a its own square or line
									board.remove(removed_cell);
									board.updateRemovable(removed_cell, 1, 2);
									pylos_AI.white_balls+=1;
									value+=5;
									value += minValue(board, depth, alpha, beta);
									if (value > alpha) {
										alpha = value;
										cell = current_move;
										if(depth == 1) {//if get highest value in depth 1 that means this remove is the best choice for the next move.
											pylos_AI.up_tier = true;
											pylos_AI.from = current_ball;
											pylos_AI.cell_to_remove_1 = removed_cell;
										}
									}
									board.insert(removed_cell, 1);
									board.updateRemovable(removed_cell, 1, 1);
									pylos_AI.white_balls-=1;
								}
							}

							for(int n = 0; n<28; n++) {//check removals of all possible combinations of TWO balls
								for(int j = n+1; j<29; j++) {
									int[] removed_cell_1 = board.intToCoord(n);
									int[] removed_cell_2 = board.intToCoord(j);
									if(toremove[n][1] == 1 && toremove[n][0]==0 && toremove[j][1] == 1 && toremove[j][0] == 0) {
										value = 10;
										value = value + 20- depth; //if for AI creating a its own square or line is more important
										board.remove(removed_cell_1);
										board.updateRemovable(removed_cell_1, 1, 2);
										board.remove(removed_cell_2);
										board.updateRemovable(removed_cell_2, 1, 2);
										pylos_AI.white_balls+=2;
										value+=10;
										value += minValue(board, depth, alpha, beta);
										if (value > alpha) {
											alpha = value;
											cell = current_move;
											if(depth == 1) {//if get highest value in depth 1 that means these removes are the best choice for the next move.
												pylos_AI.up_tier = true;
												pylos_AI.from = current_ball;
												pylos_AI.cell_to_remove_1 = removed_cell_1;
												pylos_AI.cell_to_remove_2 = removed_cell_2;
											}
										}
										board.insert(removed_cell_1, 1);
										board.updateRemovable(removed_cell_1, 1, 1);
										board.insert(removed_cell_2, 1);
										board.updateRemovable(removed_cell_2, 1, 1);
										pylos_AI.white_balls-=2;
									}
								}
							}
							board.insert(current_ball, mySymbol);
							board.updateRemovable(current_ball, 1, 1);
							board.remove(current_move);
							board.updateRemovable(current_move, 1, 2);
							if (alpha >= beta) 
								return alpha;
						}
						else {
							value=10;
							value += minValue(board, depth, alpha, beta);
							if (value > alpha) {
								alpha = value;
								cell = current_move;
								if(depth == 1) {
									pylos_AI.up_tier = true;
									pylos_AI.from = current_ball;
								}
							}
							board.insert(current_ball, mySymbol);
							board.updateRemovable(current_ball, 1, 1);
							board.remove(current_move);
							board.updateRemovable(current_move, 1, 2);
							if (alpha >= beta) 
								return alpha;
						}
					}
					else {
						board.remove(current_move);
						board.updateRemovable(current_move, 1, 2);
					}
				}
			}
//====================================================================================================================================
			board.insert(possib_moves.get(i), mySymbol);
			int[][] toremove = board.updateRemovable(possib_moves.get(i), 1, 1);
//BOARD SQUARE CHECK==================================================================================================================
			if(board.isSquare(mySymbol, possib_moves.get(i)) || board.isLine(mySymbol, possib_moves.get(i))) {
				pylos_AI.white_balls-=1;
				for(int k = 0; k<29; k++) {
					int[] removed_cell = board.intToCoord(k);
					if(toremove[k][1] == 1 && toremove[k][0]==0) {
						value = 25;
						value = value + 15- depth; //AI creating a its own square or line
							board.remove(removed_cell);
							board.updateRemovable(removed_cell, 1, 2);
							pylos_AI.white_balls+=1;
							value+=5;
							value += minValue(board, depth, alpha, beta);
							if (value > alpha) {
								alpha = value;
								cell = possib_moves.get(i);
								if(depth == 1) {
									pylos_AI.up_tier = false;
									pylos_AI.cell_to_remove_1 = removed_cell;

								}
							}
							board.insert(removed_cell, 1);
							board.updateRemovable(removed_cell, 1, 1);
							pylos_AI.white_balls-=1;
						}
					}
					
					for(int k = 0; k<28; k++) {
						for(int j = k+1; j<29; j++) {
							int[] removed_cell_1 = board.intToCoord(k);
							int[] removed_cell_2 = board.intToCoord(j);
							if(toremove[k][1] == 1 && toremove[k][0]==0 && toremove[j][1] == 1 && toremove[j][0] == 0) {
								value = 30;
								value = value + 20- depth; //AI creating a its own square or line
								board.remove(removed_cell_1);
								board.updateRemovable(removed_cell_1, 1, 2);
								board.remove(removed_cell_2);
								board.updateRemovable(removed_cell_2, 1, 2);
								pylos_AI.white_balls+=2;
								value+=10;
								value += minValue(board, depth, alpha, beta);
								if (value > alpha) {
									alpha = value;
									cell = possib_moves.get(i);
									if(depth == 1) {
										pylos_AI.up_tier = false;
										pylos_AI.cell_to_remove_1 = removed_cell_1;
										pylos_AI.cell_to_remove_2 = removed_cell_2;
									}
								}
								board.insert(removed_cell_1, 1);
								board.updateRemovable(removed_cell_1, 1, 1);
								board.insert(removed_cell_2, 1);
								board.updateRemovable(removed_cell_2, 1, 1);
								pylos_AI.white_balls-=2;
							}
						}
					}
					int[] cell_remove = possib_moves.get(i);
					board.remove(cell_remove);
					board.updateRemovable(cell_remove, 1, 2);
					pylos_AI.white_balls+=1;
					if (alpha >= beta) 
						return alpha;
				}
//-=================================================================================================================================================
				else {
					pylos_AI.white_balls-=1;
					value += minValue(board, depth, alpha, beta);
					if (value > alpha) {
						alpha = value;
						cell = possib_moves.get(i);
					}
					int[] cell_remove = possib_moves.get(i);
					board.remove(cell_remove);
					board.updateRemovable(cell_remove, 1, 2);
					pylos_AI.white_balls+=1;
					if (alpha >= beta) 
						return alpha;
				}
			}
			maxCell = cell;
			return alpha;
		}

		private int minValue(Board board, int depth, int alpha, int beta) {
		//check if the board is in a terminal (winning) state and
		//return the maximum or minimum utility value (2505 - depth or 
		//0 + depth) if the max player or min player is winning.
			if (board.isWin() != 0)
				if (board.isWin() == mySymbol)
					return 2505 - depth;
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
					int[] current_move = possib_moves.get(i);
//MOVE UP CHECK================================================================================================================================
					if(current_move[0] == 2 || current_move[0] == 3) {
						value-=10;
						board.insert(current_move, opponentSymbol);
						int[][] temp_rem = board.updateRemovable(current_move, 2, 1);
						board.remove(current_move);
						board.updateRemovable(current_move, 2, 2);
						for(int k = 0; k<29; k++) {
							board.insert(current_move, opponentSymbol);
							board.updateRemovable(current_move, 2, 1);
							int[] current_ball = board.intToCoord(k);
							if(board.removable[k][1] == 2 && board.removable[k][0] == 0 && current_ball[0] == current_move[0]-1) {
								current_ball = board.intToCoord(k);
								board.remove(current_ball);
								int[][] toremove = board.updateRemovable(current_ball, 2, 2);
								if(board.isSquare(opponentSymbol, possib_moves.get(i)) || board.isLine(opponentSymbol, possib_moves.get(i))) {
									for (int n = 0; n<29; n++) {
										int[] removed_cell = board.intToCoord(n);
										if(toremove[n][1] == 2 && toremove[n][0] == 0) {
											value = 0;
											value = value - 14 - depth;
											board.remove(removed_cell);
											board.updateRemovable(removed_cell, 2, 2);
											pylos_AI.black_balls+=1;
											value+=5;
											value += maxValue(board, depth, alpha, beta);
											if (value < beta) {
												beta = value;
											}
											board.insert(removed_cell, 2);
											board.updateRemovable(removed_cell, 2, 1);
											pylos_AI.black_balls-=1;
										}
									}

									for(int n = 0; n<28; n++) {
										for(int j = k+1; j<29; j++) {
											int[] removed_cell_1 = board.intToCoord(n);
											int[] removed_cell_2 = board.intToCoord(j);
											if(toremove[n][1] == 2 && toremove[n][0] == 0 && toremove[j][1] == 2 && toremove[j][0] == 0) {
												value = 0;
												value = value - 15 - depth;
												board.remove(removed_cell_1);
												board.updateRemovable(removed_cell_1, 2, 2);
												board.remove(removed_cell_2);
												board.updateRemovable(removed_cell_2, 2, 2);
												pylos_AI.black_balls+=2;
												value-=10;
												value+=maxValue(board, depth, alpha, beta);
												if(value<beta) {
													beta = value;
												}
												board.insert(removed_cell_1, 2);
												board.updateRemovable(removed_cell_1, 2, 1);
												board.insert(removed_cell_2, 2);
												board.updateRemovable(removed_cell_2, 2, 1);
												pylos_AI.black_balls-=2;
											}
										}
									}
									board.insert(current_ball, opponentSymbol);
									board.updateRemovable(current_ball, 2, 1);
									board.remove(current_move);
									board.updateRemovable(current_move, 2, 2);
									if (beta <= alpha)
										return beta;
								}
								else {
									value-=10;
									value += maxValue(board, depth, alpha, beta);
									if (value < beta)
										beta = value;
									board.insert(current_ball, opponentSymbol);
									board.updateRemovable(current_ball, 2, 1);
									board.remove(current_move);
									board.updateRemovable(current_move, 2, 2);
									if (beta <= alpha) 
										return beta;
								}
							}
							else {
								board.remove(current_move);
								board.updateRemovable(current_move, 2, 2);
							}
						}	
					}

//=============================================================================================================================================
					board.insert(possib_moves.get(i), opponentSymbol);
					int[][] toremove = board.updateRemovable(possib_moves.get(i), 2,1);
//BOARD SQUARE CHECK===========================================================================================================================
					if(board.isSquare(opponentSymbol, possib_moves.get(i)) || board.isLine(opponentSymbol, possib_moves.get(i))) {
						pylos_AI.black_balls-=1;
						for (int k = 0; k<29; k++) {
							int[] removed_cell = board.intToCoord(k);
							if(toremove[k][1] == 2 && toremove[k][0] == 0) {
								value = -25;
								value = value - 40 + depth;
								board.remove(removed_cell);
								board.updateRemovable(removed_cell, 2, 2);
								pylos_AI.black_balls+=1;
								value += maxValue(board, depth, alpha, beta);
								if (value < beta) {
									beta = value;
								}
								board.insert(removed_cell, 2);
								board.updateRemovable(removed_cell, 2, 1);
								pylos_AI.black_balls-=1;
							}
						}

						for(int k = 0; k<28; k++) {
							for(int j = k+1; j<29; j++) {
								int[] removed_cell_1 = board.intToCoord(k);
								int[] removed_cell_2 = board.intToCoord(j);
								if(toremove[k][1] == 2 && toremove[k][0] == 0 && toremove[j][1] == 2 && toremove[j][0] == 0) {
									value = -30;
									value = value - 50 + depth;
									board.remove(removed_cell_1);
									board.updateRemovable(removed_cell_1, 2, 2);
									board.remove(removed_cell_2);
									board.updateRemovable(removed_cell_2, 2, 2);
									pylos_AI.black_balls+=2;
									value+=maxValue(board, depth, alpha, beta);
									if(value<beta) {
										beta = value;
									}
									board.insert(removed_cell_1, 2);
									board.updateRemovable(removed_cell_1, 2, 1);
									board.insert(removed_cell_2, 2);
									board.updateRemovable(removed_cell_2, 2, 1);
									pylos_AI.black_balls-=2;
								}
							}
						}
						int[] cell_remove = possib_moves.get(i);
						board.remove(cell_remove);
						board.updateRemovable(cell_remove, 2, 2);
						pylos_AI.black_balls+=1;
						if (beta <= alpha)
							return beta;
					}
					else {
						pylos_AI.black_balls-=1;
						value += maxValue(board, depth, alpha, beta);
						if (value < beta)
							beta = value;
						int[] cell_remove = possib_moves.get(i);
						board.remove(cell_remove);
						board.updateRemovable(cell_remove, 2, 2);
						pylos_AI.black_balls+=1;
						if (beta <= alpha) 
							return beta;
					}
				}
				return beta;
			}
		}