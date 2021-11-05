import java.util.ArrayList;  

public class MoveChooser {
  
    public static Move chooseMove(BoardState boardState){
   	//gets search depth value
	int searchDepth= Othello.searchDepth;

    ArrayList<Move> moves= boardState.getLegalMoves();
    if(moves.isEmpty()){
        return null;
	}


	//gets the first legal move
	Move m= moves.get(0);
	//makes a copy of the current boardstate
	BoardState bs= boardState.deepCopy();
	//makes that legal move on the copy of the board
	bs.makeLegalMove(m.x, m.y);
	//calls minimax function for value of the first move
	int moveValue= minimax(bs, searchDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
	//itteration through rest of moves
	for(int i=1; i<moves.size(); i++){
		//makes boardstate copies for each of the moves 
		bs= boardState.deepCopy();
		//gets next move in arraylist
		Move m1= moves.get(i);
		//makes the leagal move for next move
		bs.makeLegalMove(m1.x, m1.y);
		//evaluates the value of the next move, using minimax function
		int eval= minimax(bs, searchDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		//if current evaluation is larger (larger means better), then it replaces the value of the previous move, as well as evaluation for the move
		if(eval> moveValue){
			moveValue= eval;
			m=m1;
		}

	}
		//returns computer executed move at end
        return m;
    }

    //method to iniitialise the static evaluation function
    public static int board (BoardState boardState){

    	int moveScore= 0;

    	//2d array for the value of each of the places on the board
    	int board[][]= {
    					{120, -20, 20, 5, 5, 20, -20, 120}, 
    					{-20, -40, -5, -5, -5, -5, -40, -20},
    					{20, -5, 15, 3, 3, 15, -5, 20},
    					{5, -5, 3, 3, 3, 3, -5, 5}, 
    					{5, -5, 3, 3, 3, 3, -5, 5}, 
    					{20, -5, 15, 3, 3, 15, -5, 20}, 
    					{-20, -40, -5, -5, -5, -5, -40, -20}, 
    					{120, -20, 20, 5, 5, 20, -20, 120}
    				};
    	//loops through board, adding to the total count if the piece is white (computer) or subtracting if it is a black piece, i.e. non-computer player
   		for (int i=0; i<8; i++){
   			for(int j=0; j<8; j++){
   				int pieceColour = boardState.getContents(i,j);
   				if(pieceColour == 1){
   					moveScore= moveScore + board[i][j]; 

   				}
   				else if (pieceColour == -1){
   					moveScore= moveScore - board[i][j];

   				}
   			}
   		}
   		//returns static evaluation 
   		return moveScore;
    }

    //minimax method
    public static int minimax(BoardState boardState, int searchDepth, int alpha, int beta){
    	//gets legal moves for the current board state
    	ArrayList<Move> moves= boardState.getLegalMoves();
        if(moves.isEmpty() || boardState.gameOver() || searchDepth== 0){
        	//returns current boardstate if the game is over, there are no possible moves, or the search depth is 0
            return board(boardState);
		}

		//checks if maximising node
		boolean maximising= boardState.colour== 1;
		//System.out.println(maximising);
		if (maximising){
			//iniitial max eval of alpha, which is negative inifinity
			int maxEval= Integer.MIN_VALUE;
			for(int i=0; i<moves.size(); i++){
				//creates copy of current board state, so we can get daughteres of each vertex
				BoardState bs= boardState.deepCopy();
				Move m= moves.get(i);
				//executes the legal move on new board state
				bs.makeLegalMove(m.x, m.y);
				//calls minimax again, reducing the search depth, as it is a recursive algorithm
				int eval= minimax(bs, searchDepth-1, alpha, beta);
				//compares max evaluation with the current evaluation
				maxEval= Math.max(maxEval, eval);
				//alpha beta pruning, if evaluation is larger than alpha, alpha will get that value 
				alpha= Math.max(alpha, eval);
				if(beta <= alpha){
					break;
				}
			}
			//System.out.println(maxEval);

			//returns maximum evaluation
			return maxEval;
		}
		else{
			//initial min eval of beta, which is positive infinity
			int minEval= Integer.MAX_VALUE;
			for(int i=0; i<moves.size(); i++){
				//creates copy of current board state, so we can get daughters of each vertex
				BoardState bs= boardState.deepCopy();
				Move m= moves.get(i);
				//executes the legal move on new board state
				bs.makeLegalMove(m.x, m.y);
				//calls minimax again, this time reducing the search depth by 1, as it is recursive
				int eval= minimax(bs, searchDepth-1, alpha, beta);
				//compare min evaluation with current evaluation
				minEval= Math.min(minEval, eval);
				//alpha beta pruning, if beta is smaller than the evaluation, beta will get that value
				beta= Math.min(beta, eval);
				if(beta <= alpha){
					break;
				}
			}
			//System.out.println(minEval);
			//returns the minimum evaluation
			return minEval;
		}
    }

}