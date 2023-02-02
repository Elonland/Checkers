package com.tp.Model;

import java.lang.Math;
/*
 * @author Tobiasz Jędrzejek
 * 
 * RuleSet class contains all* the generic rules about checkers. (It should contain)
 */
public abstract class RuleSet {
	/*
	 * @param move contains information about move made by a player.
	 * @param board contains information about positions of pieces.
	 * 
	 * This method is used by other classes to check if everything is correct.
	 */
	public void verifyMove(Move move, Board board) throws InvalidMoveException{
		insideBoardCheck(move.after.X, move.after.Y, board.getSize());
		
		if(move.jumped != null) {
			pieceExistsCheck(move, board);
			jumpOwnPieceCheck(move);
		}
		
		
		tileOccupiedCheck(move.after.X, move.after.Y, board);
		
		necessaryJumpCheck(move, board);
		requiredMaxJumpCheck(move, board);
		movePossibleCheck(move);
	}

	/*
	 * Checks if piece isn't moved out of the board.
	 */
	protected void insideBoardCheck(int x, int y, int size) throws InvalidMoveException {
		if(x < 0 || x > size || y < 0 || y > size) {
			throw new InvalidMoveException("Piece out of bounds");
		}
	}
	
	/*
	 * Checks if player tries to jump his own piece.
	 */
	protected void jumpOwnPieceCheck(Move move) throws InvalidMoveException {
		for(Piece piece: move.jumped) {
			if(piece.color == move.before.color) {
				throw new InvalidMoveException("Can't jump over own piece");
			}
		}
	}
	
	/*
	 * Checks if player want to jump to a place where there is a piece already.
	 */
	protected void tileOccupiedCheck(int x, int y, Board board) throws InvalidMoveException {
		if(board.getPiece(x, y) != null) {
			throw new InvalidMoveException("Tile ocuppied");
		}
	}
	
	/*
	 * Checks if a piece jumped by a player exists.
	 */
	protected void pieceExistsCheck(Move move, Board board) throws InvalidMoveException {
		for(Piece piece: move.jumped) {
			if(piece == null) {
				throw new InvalidMoveException("Jumped piece doesn't exists");
			}
		}
	}
	/*
	 * Checks generic movement rule. Without jumping over other pawns.
	 */
	protected void movePossibleCheck(Move move) throws InvalidMoveException {
		
		if(move.isJump){
            return;
        }
		
		int dx = Math.abs(move.before.X - move.after.X);
		int dy = move.after.Y - move.before.Y;

		if(dx == 0 || dy == 0) {
			throw new InvalidMoveException("Cannot move upwords or sideways");
		}
		if( (dy < 0 && move.before.color == com.tp.Model.Player.WHITE) || (dy > 0 && move.before.color == com.tp.Model.Player.BLACK) ) {
			throw new InvalidMoveException("Cannot move backwards");
		}
		if(dx > 1 || Math.abs(dy) > 1 || dx != Math.abs(dy)) {
			throw new InvalidMoveException("Cannot move that far");
		}
	}
	
	/*
	 * Checks if a player needs to jump other player's piece.
	 */
	protected void necessaryJumpCheck(Move move, Board board) throws InvalidMoveException {
		int x = move.before.X;
		int y = move.before.Y;
		
		int dx = Math.abs(x - move.after.X);
		
		int pieceY;
		
		
		Piece piece1, piece2, piece1Behind, piece2Behind;
		
		if(move.before.color == com.tp.Model.Player.WHITE) {
			piece1 = board.getPiece(x + 1, y + 1);
			piece2 = board.getPiece( x - 1, y + 1);
			piece1Behind = board.getPiece( x + 2, y + 2);
			piece2Behind = board.getPiece(x - 2, y + 2);
			
			pieceY = y + 1;
		
		} else {
			piece1 = board.getPiece(x + 1, y - 1);
			piece2 = board.getPiece( x - 1, y - 1);
			piece1Behind = board.getPiece( x + 2, y - 2);
			piece2Behind = board.getPiece(x - 2, y - 2);
			
			pieceY = y - 1;
		}
		
		if(piece1 != null && piece2 != null)
		{
		
			//Checking if player isn't already going to jump required piece.
			if( (move.after.X == piece1.X + 1 && move.after.Y == pieceY) || (move.after.X == piece2.X - 1 && move.after.Y == pieceY)) {
		
				if( (piece1 != null && piece1Behind == null) || (piece2 != null && piece2Behind == null) ) {
				throw new InvalidMoveException("You need to jump a piece");
				}
		
			}
		
		}
		
	}
	
	protected void requiredMaxJumpCheck(Move move, Board board) throws InvalidMoveException{
        if(!move.isJump){
            return;
        }
        int maxJumpLength = 0;
        for(Piece piece : board.getPieces(move.before.color)){
            int jumpLength = maxJumps(piece, board, 0);
            if(jumpLength > maxJumpLength){
                maxJumpLength = jumpLength;
            }
        }
        if(maxJumpLength > move.jumped.length){
            throw new InvalidMoveException("Longer jump possible");
        } else if(maxJumpLength < move.jumped.length){
            throw new InvalidMoveException("Jump not possible");
        }
    }
	
	private int maxJumps(Piece piece, Board board, int jumps){
        //Go further if it isn't queen.
		if(!isJumpPossible(piece, board)){
            return jumps;
        }
        int maxJumps = jumps;
        for(int x = -1; x <= 1; x += 2){
            for(int y = -1; y <= 1; y += 2){
                int diagonal = 1;
                do{
                    //find available piece.
                	Piece jumped = board.getPiece(piece.X + diagonal*x, piece.Y + diagonal*y);
                    if(jumped == null){
                        continue;
                    }
                    //check if piece can jump and if it is in correct place on board.
                    if(jumped.color == piece.color ||
                        board.getPiece(piece.X + (diagonal+1)*x, piece.Y + (diagonal+1)*y) != null ||
                        piece.X + (diagonal+1)*x < 0 || piece.X + (diagonal+1)*x >= board.getSize() ||
                        piece.Y + (diagonal+1)*y < 0 || piece.Y + (diagonal+1)*y >= board.getSize()
                    ){
                        break;
                    }
                    //Create said piece.
                    Piece moved = new Piece(
                        piece.X + (diagonal+1)*x,
                        piece.Y + (diagonal+1)*y,
                        piece.queen,
                        piece.color
                    );
                    //Recursive.
                    Board newBoard = new PolishBoard(board);
                    newBoard.makeMove(new Move(piece, moved, true, new Piece[]{jumped}));

                    int jumpLength = maxJumps(moved, newBoard, jumps + 1);
                    if(jumpLength > maxJumps){
                        maxJumps = jumpLength;
                    }
                } while( piece.queen && ++diagonal < board.getSize() );
            }
        }
        return maxJumps;
    }
	
	//Multiple jumps for queen.
	private boolean isJumpPossible(Piece piece, Board board){
        if(!piece.queen){
            if((piece.color == Player.WHITE && piece.Y == board.getSize()-1) ||
            (piece.color == Player.BLACK && piece.X == 0)){
                return false;
            }
        }
        
        for(int x = -1; x <= 1; x += 2){
            for(int y = -1; y <= 1; y += 2){
                int diagonal = 1;
                do{
                	//looking for the piece to jump
                    Piece jumped = board.getPiece(piece.X + diagonal*x, piece.Y + diagonal*y);
                    if(jumped == null){
                        continue;
                    }
                    //check if jump is possible and if it didn't leave the board.
                    if(jumped.color == piece.color ||
                        board.getPiece(piece.X + (diagonal+1)*x, piece.Y + (diagonal+1)*y) != null ||
                        piece.X + (diagonal+1)*x < 0 || piece.X + (diagonal+1)*x >= board.getSize() ||
                        piece.Y + (diagonal+1)*y < 0 || piece.Y + (diagonal+1)*y >= board.getSize()
                    ){
                        break;
                    }
                    return true;
                } while( piece.queen && ++diagonal < board.getSize() );
            }
        }
        return false;
	}
	
	
	protected void promotionCheck(Move move, Board board) throws InvalidMoveException{
        if(move.before.queen  == false && move.after.queen == true){
            if(move.after.color == Player.WHITE && move.after.Y != board.getSize() - 1){
                throw new InvalidMoveException("Promotion not possible");
            }
            if(move.after.color == Player.BLACK && move.after.Y != 0){
                throw new InvalidMoveException("Promotion not possible");
            }
        }
    }
	
}
