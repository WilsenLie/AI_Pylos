import java.util.*;
import java.io.*;

public class Board{
	public final int tier1_dim = 4;
	public final int tier2_dim = 3;
	public final int tier3_dim = 2;
	
	public int tier1_board[][] = new int[tier1_dim][tier1_dim];
	public int tier2_board[][] = new int[tier2_dim][tier2_dim];
	public int tier3_board[][] = new int[tier3_dim][tier3_dim];
	public int tier_4[][] = new int[1][1];
	
	private static int[][] tier1_evaluationTable = {{3, 4, 4, 3,}, 
	{4, 15, 15, 4},
	{4, 15, 15, 4}, 
	{3, 4, 4, 3}};
	
	private static int[][] tier2_evaluationTable = {{10, 11, 10}, 
	{11, 50, 11},
	{10, 11, 10}};
	
	private static int[][] tier3_evaluationTable = {{12, 12}, 
	{12, 12}};
	
	//Board constructor
	public Board() {
	}
	
	//draw the all the tiers
	public void showBoard(int x) {

		//if AI moves first
		if (x==1) {

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
					if (tier_4[0][0] == 1) System.out.print("w ");
					else if (tier_4[0][0] == 2) System.out.print("b ");
					else System.out.print("o ");
				}
				System.out.println();
			}
		}

	//if human move first
		else {
			for (int i = 0; i<4; i++) {

			//4x4
				for (int j = 0; j<4; j++) {
					if (tier1_board[i][j] == 1) System.out.print("b ");
					else if (tier1_board[i][j] == 2) System.out.print("w ");
					else System.out.print("o ");
				}

				System.out.print("    ");

			//3x3
				if (i<3) {
					for (int j = 0; j<3; j++) {
						if (tier2_board[i][j] == 1) System.out.print("b ");
						else if (tier2_board[i][j] == 2) System.out.print("w ");
						else System.out.print("o ");
					}
				}

				System.out.print("    ");

			//2x2
				if (i<2) {
					for (int j=0; j<2; j++) {
						if (tier3_board[i][j] == 1) System.out.print("b ");
						else if (tier3_board[i][j] == 2) System.out.print("w ");
						else System.out.print("o ");
					}
				}

				System.out.print("    ");

			//Top of pyramid
				if(i==0){
					if (tier_4[0][0] == 1) System.out.print("b ");
					else if (tier_4[0][0] == 2) System.out.print("w ");
					else System.out.print("o ");
				}
				System.out.println();
			}
		}
		System.out.println();
	}
	
	//Insert the 'whichPlayer' ball into the cell specified by pos.
	public void insert(int[] pos, int whichPlayer) {
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
			tier_4[0][0] = whichPlayer;
			break;
			default :
			System.out.println ("error_insert");
		}
		int[] temp = {0,0,0};
	}
	
	//Trastlates the coordinates that human types manually (a2, b3, d4) into
	//an array of 3 values: tier, row, col. 
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
	
	//Gets the array list of cells where the ball can be put. If the cell value
	//is 0 then the ball can be put into that cell.
	//Also for tier 2,3,4 it checks whether there are 4 balls under that cell.
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
		
		if(tier3_board[0][0] != 0 && tier3_board[0][1] != 0 && tier3_board[1][0] != 0 && tier3_board[1][1] != 0) {
			int[] each_move = {4,0,0};
			result.add(each_move);
		}
		return result;
	}
	
	//Return the values of the tier4 cell.
	public int isWin() {
		return tier_4[0][0];
	}
	
	//Evaluation function that is being used in the MIN and MAX functions.
	//Evaluates the current board state using three evaluation tables declared above.
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

	//Set the value of the pos cell to 0.
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
				default :
				System.out.println ("error_remove");
		}
	}

	//If the cell is occupied return false.
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
				if(tier_4[0][0] == 0)
					return true;
				else return false;
		}
					return false;
	}

	//Once the 'currentMove' is done check whether the square was created by 'whichPlayer'.
	public boolean isSquare(int whichPlayer, int[] currentMove) {
		//There are 14 possible squares (4x4, 3x3, 2x2)
		//We don't need to check the whole board
		//Just check after the last move and surround
		int tempRow = currentMove[1];
		int tempCol = currentMove[2];
		int findTier =  currentMove[0];
		if (findTier == 1) { //1st tier
			//Top left corner
			if (tempRow == 0 && tempCol == 0) {
				if (tier1_board[tempRow][tempCol+1] == whichPlayer && tier1_board[tempRow+1][tempCol] == whichPlayer && tier1_board[tempRow+1][tempCol+1] == whichPlayer) {
					return true;
				}
			}
			//top right corner
			else if (tempRow == 0 && tempCol == 3) {
				if (tier1_board[tempRow][tempCol-1] == whichPlayer && tier1_board[tempRow+1][tempCol] == whichPlayer && tier1_board[tempRow+1][tempCol-1] == whichPlayer) {
					return true;
				}
			}
			//bottom left corner
			else if (tempRow == 3 && tempCol == 0) {
				if (tier1_board[tempRow-1][tempCol] == whichPlayer && tier1_board[tempRow][tempCol+1] == whichPlayer && tier1_board[tempRow-1][tempCol+1] == whichPlayer) {
					return true;
				}
			}
			//bottom right corner
			else if (tempRow == 3 && tempCol == 3) {
				if (tier1_board[tempRow][tempCol-1] == whichPlayer && tier1_board[tempRow-1][tempCol] == whichPlayer && tier1_board[tempRow-1][tempCol-1] == whichPlayer) {
					return true;
				}
			}
			else if (tempRow == 0) {
				if (tier1_board[tempRow][tempCol-1] == whichPlayer && tier1_board[tempRow+1][tempCol] == whichPlayer && tier1_board[tempRow+1][tempCol-1] == whichPlayer) {
					return true;
				}
				else if (tier1_board[tempRow][tempCol+1] == whichPlayer && tier1_board[tempRow+1][tempCol] == whichPlayer && tier1_board[tempRow+1][tempCol+1] == whichPlayer) {
					return true;
				}
			}
			else if (tempCol == 0) {
				if (tier1_board[tempRow][tempCol+1] == whichPlayer && tier1_board[tempRow+1][tempCol] == whichPlayer && tier1_board[tempRow+1][tempCol+1] == whichPlayer) {
					return true;
				}
				else if (tier1_board[tempRow-1][tempCol] == whichPlayer && tier1_board[tempRow][tempCol+1] == whichPlayer && tier1_board[tempRow-1][tempCol+1] == whichPlayer) {
					return true;
				}
			}
			else if (tempCol == 3) {
				if (tier1_board[tempRow][tempCol-1] == whichPlayer && tier1_board[tempRow+1][tempCol] == whichPlayer && tier1_board[tempRow+1][tempCol-1] == whichPlayer) {
					return true;
				}
				else if (tier1_board[tempRow][tempCol-1] == whichPlayer && tier1_board[tempRow-1][tempCol] == whichPlayer && tier1_board[tempRow-1][tempCol-1] == whichPlayer) {
					return true;
				}
			}
			else if (tempRow == 3) {
				if (tier1_board[tempRow][tempCol-1] == whichPlayer && tier1_board[tempRow-1][tempCol] == whichPlayer && tier1_board[tempRow-1][tempCol-1] == whichPlayer) {
					return true;
				}
				else if (tier1_board[tempRow-1][tempCol] == whichPlayer && tier1_board[tempRow][tempCol+1] == whichPlayer && tier1_board[tempRow-1][tempCol+1] == whichPlayer) {
					return true;
				}
			}
			else {
				if (tier1_board[tempRow][tempCol-1] == whichPlayer && tier1_board[tempRow+1][tempCol] == whichPlayer && tier1_board[tempRow+1][tempCol-1] == whichPlayer) {
					return true;
				}
				else if (tier1_board[tempRow][tempCol-1] == whichPlayer && tier1_board[tempRow+1][tempCol] == whichPlayer && tier1_board[tempRow+1][tempCol-1] == whichPlayer) {
					return true;
				}
				else if (tier1_board[tempRow-1][tempCol] == whichPlayer && tier1_board[tempRow][tempCol+1] == whichPlayer && tier1_board[tempRow-1][tempCol+1] == whichPlayer) {
					return true;
				}
				else if (tier1_board[tempRow][tempCol-1] == whichPlayer && tier1_board[tempRow-1][tempCol] == whichPlayer && tier1_board[tempRow-1][tempCol-1] == whichPlayer) {
					return true;
				}
			}
		}
		
		else if(findTier == 2) { //2nd tier
			//Top left corner
			if (tempRow == 0 && tempCol == 0) {
				if (tier2_board[tempRow][tempCol+1] == whichPlayer && tier2_board[tempRow+1][tempCol] == whichPlayer && tier2_board[tempRow+1][tempCol+1] == whichPlayer) {
					return true;
				}
			}
			//top right corner
			else if (tempRow == 0 && tempCol == 2) {
				if (tier2_board[tempRow][tempCol-1] == whichPlayer && tier2_board[tempRow+1][tempCol] == whichPlayer && tier2_board[tempRow+1][tempCol-1] == whichPlayer) {
					return true;
				}
			}
			//bottom left corner
			else if (tempRow == 2 && tempCol == 0) {
				if (tier2_board[tempRow-1][tempCol] == whichPlayer && tier2_board[tempRow][tempCol+1] == whichPlayer && tier2_board[tempRow-1][tempCol+1] == whichPlayer) {
					return true;
				}
			}
			//bottom right corner
			else if (tempRow == 2 && tempCol == 2) {
				if (tier2_board[tempRow][tempCol-1] == whichPlayer && tier2_board[tempRow-1][tempCol] == whichPlayer && tier2_board[tempRow-1][tempCol-1] == whichPlayer) {
					return true;
				}
			}
			else if (tempRow == 0) {
				if (tier2_board[tempRow][tempCol-1] == whichPlayer && tier2_board[tempRow+1][tempCol] == whichPlayer && tier2_board[tempRow+1][tempCol-1] == whichPlayer) {
					return true;
				}
				else if (tier2_board[tempRow][tempCol+1] == whichPlayer && tier2_board[tempRow+1][tempCol] == whichPlayer && tier2_board[tempRow+1][tempCol+1] == whichPlayer) {
					return true;
				}
			}
			else if (tempCol == 0) {
				if (tier2_board[tempRow][tempCol+1] == whichPlayer && tier2_board[tempRow+1][tempCol] == whichPlayer && tier2_board[tempRow+1][tempCol+1] == whichPlayer) {
					return true;
				}
				else if (tier2_board[tempRow-1][tempCol] == whichPlayer && tier2_board[tempRow][tempCol+1] == whichPlayer && tier2_board[tempRow-1][tempCol+1] == whichPlayer) {
					return true;
				}
			}
			else if (tempCol == 2) {
				if (tier2_board[tempRow][tempCol-1] == whichPlayer && tier2_board[tempRow+1][tempCol] == whichPlayer && tier2_board[tempRow+1][tempCol-1] == whichPlayer) {
					return true;
				}
				else if (tier2_board[tempRow][tempCol-1] == whichPlayer && tier2_board[tempRow-1][tempCol] == whichPlayer && tier2_board[tempRow-1][tempCol-1] == whichPlayer) {
					return true;
				}
			}
			else if (tempRow == 2) {
				if (tier2_board[tempRow][tempCol-1] == whichPlayer && tier2_board[tempRow-1][tempCol] == whichPlayer && tier2_board[tempRow-1][tempCol-1] == whichPlayer) {
					return true;
				}
				else if (tier2_board[tempRow-1][tempCol] == whichPlayer && tier2_board[tempRow][tempCol+1] == whichPlayer && tier2_board[tempRow-1][tempCol+1] == whichPlayer) {
					return true;
				}
			}
			else {
				if (tier2_board[tempRow][tempCol-1] == whichPlayer && tier2_board[tempRow+1][tempCol] == whichPlayer && tier2_board[tempRow+1][tempCol-1] == whichPlayer) {
					return true;
				}
				else if (tier2_board[tempRow][tempCol-1] == whichPlayer && tier2_board[tempRow+1][tempCol] == whichPlayer && tier2_board[tempRow+1][tempCol-1] == whichPlayer) {
					return true;
				}
				else if (tier2_board[tempRow-1][tempCol] == whichPlayer && tier2_board[tempRow][tempCol+1] == whichPlayer && tier2_board[tempRow-1][tempCol+1] == whichPlayer) {
					return true;
				}
				else if (tier2_board[tempRow][tempCol-1] == whichPlayer && tier2_board[tempRow-1][tempCol] == whichPlayer && tier2_board[tempRow-1][tempCol-1] == whichPlayer) {
					return true;
				}
			}
		}
		else {
			if(tier3_board[0][0] == whichPlayer && tier3_board[0][1] == whichPlayer && tier3_board[1][0] == whichPlayer && tier3_board[1][1] == whichPlayer)
				return true;
		}
		return false;
	}
	
	//Once the 'currentMove' is done check whether the line was created by 'whichPlayer'.
	public boolean isLine(int whichPlayer, int[] currentMove) {
		//There are 14 possible lines
		int tempRow = currentMove[1];
		int tempCol = currentMove[2];
		int findTier =  currentMove[0];
		int i = 0;
		boolean hor = true;
		boolean ver = true;
		
		if (findTier == 1) {
			//in 1st tier 4x4
			for (i=0; i<4; i++) { //horizontal
				if (tier1_board[tempRow][i] != whichPlayer) {
					hor = false;
					break;
				}
			}
			for (i=0; i<4; i++) { //vertical
				if (tier1_board[i][tempCol] != whichPlayer) {
					ver = false;
					break;
				}
			}
			return (hor || ver);
		}
		else if (findTier == 2){
			//in 2nd tier 3x3
			for (i=0; i<3; i++) { //horizontal
				if (tier2_board[tempRow][i] != whichPlayer) {
					hor = false;
					break;
				}
			}
			for (i=0; i<3; i++) { //vertical
				if (tier2_board[i][tempCol] != whichPlayer) {
					ver = false;
					break;
				}
			}
			return (hor || ver);
		}
		else if (findTier == 3){
			//in 2nd tier 3x3
			for (i=0; i<2; i++) { //horizontal
				if (tier3_board[tempRow][i] != whichPlayer) {
					hor = false;
					break;
				}
			}
			for (i=0; i<2; i++) { //vertical
				if (tier3_board[i][tempCol] != whichPlayer) {
					ver = false;
					break;
				}
			}
			return (hor || ver);
		}
		else if (findTier > 3) {
			return false;
		}
		return false;
	}
	/*
	  Array will contain 30 boards cells with 3 values for each one:
	  [0][0] - which player: 0 none; 1 AI; 2 human
	  [0][1] - how many balls are on top of the cell
	  [0][2] - 1 if the ball in this cell is removeable, 
	*/
	int[][] removable = new int[30][3]; //values, whichplayer
	
	//1 = insert
	//2 = delete
	public int[][] updateRemovable(int[] coord1, int whichPlayer, int id) {
		
		int tier1 = coord1[0];
		//kinda silly, don't need to use temp
		int cti1 = coordToInt (tier1, coord1[1], coord1[2]);
		int[] temp = intToCoord(cti1); //back to coordinates
		
		//Insert
		if (id==1) {
			removable[cti1][1] = whichPlayer;
			removable[cti1][2] = 1;
			
			if (tier1 == 2) {
				removable[coordToInt(1,temp[1], temp[2])][0]++;
				removable[coordToInt(1,temp[1]+1, temp[2])][0]++;
				removable[coordToInt(1,temp[1], temp[2]+1)][0]++;
				removable[coordToInt(1,temp[1]+1, temp[2]+1)][0]++;
			}
			else if (tier1 == 3) {
				removable[coordToInt(2,temp[1], temp[2])][0]++;
				removable[coordToInt(2,temp[1]+1, temp[2])][0]++;
				removable[coordToInt(2,temp[1], temp[2]+1)][0]++;
				removable[coordToInt(2,temp[1]+1, temp[2]+1)][0]++;
			}
		}
		//remove
		else if (id == 2) {
			removable[cti1][1] = 0;
			removable[cti1][2] = 0;
			
			if (tier1 == 2) {
				removable[coordToInt(1,temp[1], temp[2])][0]--;
				removable[coordToInt(1,temp[1]+1, temp[2])][0]--;
				removable[coordToInt(1,temp[1], temp[2]+1)][0]--;
				removable[coordToInt(1,temp[1]+1, temp[2]+1)][0]--;
				
				if (removable[coordToInt(1,temp[1], temp[2])][0] == 0) 
					removable[coordToInt(1,temp[1], temp[2])][2] = 1;
				if (removable[coordToInt(1,temp[1]+1, temp[2])][0] == 0)
					removable[coordToInt(1,temp[1]+1, temp[2])][2] = 1;
				if (removable[coordToInt(1,temp[1], temp[2]+1)][0] == 0)
					removable[coordToInt(1,temp[1], temp[2]+1)][2] = 1;
				if (removable[coordToInt(1,temp[1]+1, temp[2]+1)][0] == 0)
					removable[coordToInt(1,temp[1]+1, temp[2]+1)][2] = 1;
			}
			else if (tier1 == 3) {
				removable[coordToInt(2,temp[1], temp[2])][0]--;
				removable[coordToInt(2,temp[1]+1, temp[2])][0]--;
				removable[coordToInt(2,temp[1], temp[2]+1)][0]--;
				removable[coordToInt(2,temp[1]+1, temp[2]+1)][0]--;
				
				if (removable[coordToInt(2,temp[1], temp[2])][0] == 0) 
					{removable[coordToInt(2,temp[1], temp[2])][2] = 1;}
				if (removable[coordToInt(2,temp[1]+1, temp[2])][0] == 0)
					{removable[coordToInt(2,temp[1]+1, temp[2])][2] = 1;}
				if (removable[coordToInt(2,temp[1], temp[2]+1)][0] == 0)
					{removable[coordToInt(2,temp[1], temp[2]+1)][2] = 1;}
				if (removable[coordToInt(2,temp[1]+1, temp[2]+1)][0] == 0)
					{removable[coordToInt(2,temp[1]+1, temp[2]+1)][2] = 1;}
			}
		}
		return removable;
	}
	

	//Translate the 3 value coordinate to an integer value.
	public int coordToInt (int z, int x, int y) {
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

	//Translate an integer value to 3 value coordinate.
	public int[] intToCoord (int i) {
		int[] result = new int[3];
		if (i >= 0 && i <= 15) {
			result[0] = 1;
			result[1]= i/4;
			result[2]= i%4;
		}
		else if (i >= 16 && i <= 24) {
			result[0] = 2;
			result[1]= (i-16)/3;
			result[2]= (i-16)%3;
		}
		else if (i >=25 && i <= 28) {
			result[0] = 3;
			result[1]= (i-25)/2;
			result[2]= (i-25)%2;
		}
		return result;
	}
}