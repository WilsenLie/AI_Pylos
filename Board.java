import java.util.*;
import java.io.*;

public class Board{
	public final int tier1_dim = 4;
	public final int tier2_dim = 3;
	public final int tier3_dim = 2;

	public int tier1_board[][] = new int[tier1_dim][tier1_dim];
	public int tier2_board[][] = new int[tier2_dim][tier2_dim];
	public int tier3_board[][] = new int[tier3_dim][tier3_dim];
	public int tier_4 = 0;

	private static int[][] tier1_evaluationTable = {{3, 4, 4, 3,}, 
		  				  					  		{4, 6, 6, 4},
		  				  					  		{4, 6, 6, 4}, 
		  				  					 		{3, 4, 4, 3}};

    private static int[][] tier2_evaluationTable = {{8, 9, 8}, 
		  				  					  		{9, 10, 9},
		  				  					  		{8, 9, 8}};
   
    private static int[][] tier3_evaluationTable = {{12, 12}, 
		  				  					  		{12, 12}};

	//Board constructor
	public Board() {
	}

	//draw the all the tiers
	public void showBoard() {
		
		for (int i = 0; i<4; i++) {
			
			//4x4
			for (int j = 0; j<4; j++) {
				if (tier1_board[i][j] == 1) System.out.print("w ");
				else if (tier1_board[i][j] == 2) System.out.print("b ");
				else System.out.print("o ");
			}
			
			System.out.print("    ");
			
			//3x3
			if (i<3) {
				for (int j = 0; j<3; j++) {
					if (tier2_board[i][j] == 1) System.out.print("w ");
					else if (tier2_board[i][j] == 2) System.out.print("b ");
					else System.out.print("o ");
				}
			}
			
			System.out.print("    ");
			
			//2x2
			if (i<2) {
				for (int j=0; j<2; j++) {
					if (tier3_board[i][j] == 1) System.out.print("w ");
					else if (tier3_board[i][j] == 2) System.out.print("b ");
					else System.out.print("o ");
				}
			}
			
			System.out.print("    ");
			
			//Top of pyramid
			if(i==0){
				if (tier_4 == 1) System.out.print("w ");
				else if (tier_4 == 2) System.out.print("b ");
				else System.out.print("o ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public void insert(int[] pos, int whichPlayer) { //there should be two variables passed to this function. 1-coordinates; 2-currentPlayer
		//int[] pos = transCoordinate(coordinate);
		
		switch(pos[0]) {
			case 1 :
				tier1_board[pos[1]][pos[2]] = whichPlayer;
				break;
			case 2 :
				tier2_board[pos[1]][pos[2]] = whichPlayer;
				break;
			case 3 :
				tier3_board[pos[1]][pos[2]] = whichPlayer;
				break;
			case 4 :
				tier_4 = whichPlayer;
				break;
			default :
				System.out.println ("error_insert");
		}
	}
	
	public int[] transCoordinate (String coordinate) {
		//[0] = which tier
		//[1] = which row in that tier
		//[2] = which column in that tier
		int[] result = {0,0,0};
		char[] rows = {'a', 'b', 'c', 'd','e','f','g','h','i','j'};
		char tempRow = coordinate.charAt(0);
		int tempCol = Character.getNumericValue(coordinate.charAt(1))-1;
		int findTier =  Arrays.binarySearch(rows, tempRow);
		
		if (findTier >=0 && findTier <=3) { //1st tier
			result[0] = 1;
			result[1] = tempRow - 97; //97 is an int value of char 'a'
		}
		else if (findTier >=4 && findTier <=6) { //2nd tier
			result[0] = 2;
			result[1] = tempRow - 101; //101 is an int value of char 'e'
		}
		else if (findTier ==7 || findTier ==8) { //3rd tier
			result[0] = 3;
			result[1] = tempRow - 104; //104 is an int value of char 'h'
		}
		else { //top pyramid
			result[0] = 4;
			result[1] = tempRow - 106; //97 is an int value of char 'j'
		}
		result[2] = tempCol;
		return result;
	}

	public List<int[]> possibleMoves() {
		List<int[]> result = new ArrayList<int[]>();
		int num_moves = 0;
		for (int i = 0; i<4; i++) {
			for (int j=0; j<4; j++) {
				if (tier1_board[i][j] == 0) {
					int[] each_move = {1, i, j};
					result.add(each_move);
				}
			}
		}

		for (int i = 0; i<3; i++) {
			for (int j = 0; j<3; j++) {
				if (tier2_board[i][j] == 0 && tier1_board[i][j] != 0 && tier1_board[i][j+1] !=0 && tier1_board[i+1][j] != 0 && tier1_board[i+1][j+1] != 0) {
					int[] each_move = {2, i, j};
					result.add(each_move);
				}
			}
		}

		for (int i = 0; i<2; i++) {
			for (int j = 0; j<2; j++) {
				if (tier3_board[i][j] == 0 && tier2_board[i][j] != 0 && tier2_board[i][j+1] !=0 && tier2_board[i+1][j] != 0 && tier2_board[i+1][j+1] != 0) {
					int[] each_move = {3, i, j};
					result.add(each_move);
				}
			}
		}
		//int l = result.size();
		//System.out.println("SIIIIIZZZZEEEEEEEEEE: " + l);
		return result;
	}

	public int isWin() {
		return tier_4;
	}

	public int evaluateContent() {
		int utility = 128;
		int sum = 0;
		for (int i = 0; i < 4; i++)
			for (int j = 0; j <4; j++) {
				if (tier1_board[i][j] == 1)
					sum += tier1_evaluationTable[i][j];
				else if (tier1_board[i][j] == 2)
					sum -= tier1_evaluationTable[i][j];
			}
		
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j <3; j++)
				
				if (tier2_board[i][j] == 1)
					sum += tier2_evaluationTable[i][j];
				else if (tier2_board[i][j] == 2)
					sum -= tier2_evaluationTable[i][j];
			}

				for (int i = 0; i < 2; i++) {
					for (int j = 0; j <2; j++)
				if (tier3_board[i][j] == 1)
					sum += tier3_evaluationTable[i][j];
				else if (tier3_board[i][j] == 2)
					sum -= tier3_evaluationTable[i][j];
			}

		return utility + sum;
	}

	public void remove(int[] pos) {
		switch(pos[0]) {
			case 1 :
				tier1_board[pos[1]][pos[2]] = 0;
				break;
			case 2 :
				tier2_board[pos[1]][pos[2]] = 0;
				break;
			case 3 :
				tier3_board[pos[1]][pos[2]] = 0;
				break;
			case 4 :
				tier_4 = 0;
				break;
			default :
				System.out.println ("error_remove");
		}
	}

	public boolean legalMove (int[] pos) {
		switch(pos[0]) {
			case 1 :
				if(tier1_board[pos[1]][pos[2]] == 0)
					return true;
				else return false;
			case 2 :
				if(tier2_board[pos[1]][pos[2]] == 0)
					return true;
				else return false;
			case 3 :
				if(tier3_board[pos[1]][pos[2]] == 0)
					return true;
				else return false;
			case 4 :
				if(tier_4 == 0)
					return true;
				else return false;
		}
		return false;
	}
}