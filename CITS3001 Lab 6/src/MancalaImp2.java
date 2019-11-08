import java.util.ArrayList;

public class MancalaImp2 implements MancalaAgent{

	static int MAX = Integer.MAX_VALUE; 
	static int MIN = Integer.MIN_VALUE; 
	 
	private class Play
	{
		int move;
		int score;
		Play(int M, int S)
		{
			this.move = M;
			this.score = S;
		}
	}
	
	private int depth;
	String agentName = "Jabbo";
	ArrayList<Play> bestMoves;
	
	public MancalaImp2()
	{
		this.depth = 200;
		this.agentName = "Zabbo";
		this.bestMoves = new ArrayList<Play>();
	}
	
	// Returns optimal value for either min or max player 
	int abMiniMax(int depth, Boolean maxPlayer, 
			int board[], int alpha, int beta) 
	{ 
		//-1 = game active, 1 = player, 2 = enemy, 3 = tie.
		int winner = winCheck(board); 
		if(winner != -1){
			if(winner == 1) {
				return MAX;
			}
			else if(winner == 2) {
				return MIN;
			}
			else {
				return 0;
			}
		}
		
		if(depth == 0) {
			return eval(board);
		}
		
		int boardClo[];
		
	    if (maxPlayer) { 
	        int best = MIN; 
	        
	        // Recur for all children
	        for (int i = 0; i < 6; i++) 
	        { 
	        	if(board[i] == 0) {
	        		continue;
	        	}
	        	boardClo = board.clone();
	        	boolean moveAgain = playerMove(boardClo, i);
	        	if(moveAgain)
					best = Math.max(abMiniMax(depth-1, true, boardClo, alpha, beta), best);
				else
					best = Math.max(abMiniMax(depth-1, false, boardClo, alpha, beta), best);

				alpha = Math.max(best, alpha);

				if(depth == this.depth) {
					this.bestMoves.add(new Play(i, best));
				}
				
				// Pruning 
	            if (beta <= alpha) {
	                break; 
	            }
	        } 
	        return best; 
	    } 
	    else
	    { 
	        int best = MAX; 
	        
	        // Recur for all children 
	        for (int i = 0; i < 6; i++) 
	        { 
	            if(board[i] == 0) {
	            	continue;
	            }
	            
	            boardClo = board.clone();
				boolean moveAgain = oppMove(boardClo, i); 
				if(moveAgain)
					best = Math.min(abMiniMax(depth-1, false, boardClo, alpha, beta), best);
				else
					best = Math.min(abMiniMax(depth-1, true, boardClo, alpha, beta), best);

				beta = Math.min(best, beta);
	  
	            // Pruning 
	            if (beta <= alpha) 
	                break; 
	        } 
	        return best; 
	    } 
	}
	
	private static boolean playerMove(int[] board, int move) 
	{
		int i = move;
	    while(board[move]>0){
	    	if(i == 12) {
	    	   i = 0;
	   	   } else {
	   		   i++;
	   	   }
	   	   board[i]++; board[move]--;
       }
	   if(i==6) {
		   return true;
	   }
	   if(i<6 && board[i]==1 && board[12-i]>0){
	   	   board[6]+=board[12-i]; 
	   	   board[12-i]=0;
	   	   board[6]+=board[i];
    	   board[i]=0;
	   	}
		return false;
	}
	
	private static boolean oppMove(int[] board, int move) {
		int i = move;
	    while(board[move]>0){
	   	   if(i == 5) {
	   		   i = 7;	    	   
    		   } else if(i == 13) {
	    		   i = 0;
	    	   } else {
	    		   i++;
	    	   }
	    	   board[i]++; board[move]--;
	       }
	       if(i == 13) {
	    	   return true;
	       }
	       if(i<13 && i>6 && board[i]==1 && board[12-i]>0){
	    	   board[13]+=board[12-i]; 
	    	   board[12-i]=0;
	    	   board[13]+=board[i];
	    	   board[i] = 0;
	       }
	       return false;
	}
	
	private static int eval(int[] b)
	{
		int sum = 0;
	    for (int value : b) {
	        sum += value;
	    }
	    
		int weight = 0;
		
		int seedsP1 = 0;
		int seedsP2 = 0;
		
		//give weight to having majority
		if(b[6] > (sum/2)) {
			weight+=50;
		}
		
		if(b[13] > (sum/2)) {
			weight-=50;
		}
		
		//checks for opportunities for extra turns
		for(int i = 5; i >= 0; i--) {
			if(b[i] == 6 - (5-i)) {
				weight+=10;
			}
		}
		
		//checks for potential capture moves by either player.
		for(int i = 0; i < 6; i++)
		{
			if(b[i] == 0){
				//number of seeds that can be captured by player, 
				//x2 to give priority to defensive play given the
				//protective nature of minimax
				weight+=2*b[12-i]; 
			}
			seedsP1+=b[i];
		}

		for(int j = 7; j < 13; j++)
		{
			if(b[j] == 0){
				//number of seeds that can be captured by player, 
				//x3 to give priority to defensive play given the
				//protective nature of minimax
				weight-=3*b[12-j];
			}
			seedsP2+=b[j];
		}	
		
		//Checks each players current score, x3 to emphasise better score
		weight+=3*(b[6] - b[13]);
		
		//Checks each players current standing, 
		//how many seeds they have on their side
		for(int i = 0; i < 6; i++) {
			seedsP1 += b[i];
		}
		
		for(int i = 7; i < 14; i++) {
			seedsP2 += b[i];
		}
		
		//which player has more seeds in their storage
		weight+=(seedsP1 - seedsP2);

		return weight;
	}
	
	private static int getWinner(int[] board)
	{
		if(board[6] > board[13]) {
			return 1;
		}
		else if(board[13] > board[6]) {
			return 2;
		}
		else {
			return 0;
		}
	}
	
	private int winCheck(int[] board) {
		
		int gameState = -1;
		
		boolean gameOver = true;
		
		for(int i = 0; i < 6; i++){
			if(!(board[i] == 0)) {
				gameOver = false;
				break;
			}
		}
		if(gameOver){
			for(int j = 7; j < 13; j++)
			{
				board[13]+=board[j];
				board[j] = 0;
			}
			gameState = getWinner(board);	
			return gameState;
		}
		
		gameOver = true;
		
		for(int i = 7; i < 13; i++)	{
			if(!(board[i] == 0)) {
				gameOver = false;
				break;
			}
		}
		
		if(gameOver){
			for(int j = 0; j < 6; j++)
			{
				board[6]+=board[j];
				board[j] = 0;
			}
			gameState = getWinner(board);	
			return gameState;
		}
		
		return gameState;
	}

	public int move(int[] board) {

		bestMoves.clear();
		int bestScore = abMiniMax(depth, true, board, MIN, MAX);
		int bestMove = 0;
		for(int i = 0; i < bestMoves.size(); i++)
		{
			if(bestMoves.get(i).score == bestScore) {
				bestMove = bestMoves.get(i).move;
				break;
			}
		}
		return bestMove;
	}

	public String name() {

		return this.agentName;
	}

	public void reset() {
		
	}

}
