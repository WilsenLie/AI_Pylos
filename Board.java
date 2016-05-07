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
	}

	public boolean insert() { //there should be two variables passed to this function. 1-coordinates; 2-currentPlayer
		return true;
	}
}