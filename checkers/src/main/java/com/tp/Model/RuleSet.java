package com.tp.Model;

import java.lang.Math;

public abstract class RuleSet {
	
	public void verifyMove(Move move, Board board) throws InvalidMoveException{
		insideBoardCheck(move.after.X, move.after.Y, board.getSize());
		
		if(move.jumped != null) {
			pieceExistsCheck(move, board);
			jumpOwnPieceCheck(move);
		}
		
		
		tileOccupiedCheck(move.after.X, move.after.Y, board);
		movePossibleCheck(move);
		necessaryJumpCheck(move, board);
	}

	protected void insideBoardCheck(int x, int y, int size) throws InvalidMoveException {
		if(x < 0 || x > size || y < 0 || y > size) {
			throw new InvalidMoveException("Piece out of bounds");
		}
	}
	
	protected void jumpOwnPieceCheck(Move move) throws InvalidMoveException {
		for(Piece piece: move.jumped) {
			if(piece.color == move.before.color) {
				throw new InvalidMoveException("Can't jump over own piece");
			}
		}
	}
	
	protected void tileOccupiedCheck(int x, int y, Board board) throws InvalidMoveException {
		if(board.getPiece(x, y) != null) {
			throw new InvalidMoveException("Tile ocuppied");
		}
	}
	
	protected void pieceExistsCheck(Move move, Board board) throws InvalidMoveException {
		for(Piece piece: move.jumped) {
			if(piece == null) {
				throw new InvalidMoveException("Jumped piece doesn't exists");
			}
		}
	}
	
	protected void movePossibleCheck(Move move) throws InvalidMoveException {
		int dx = Math.abs(move.before.X - move.after.X);
		int dy = move.after.Y - move.before.Y;
		
		if(dx == 0 || dy == 0) {
			throw new InvalidMoveException("Cannot move upwords or sideways");
		}
		if( (dy < 0 && move.before.color == com.tp.Model.Player.WHITE) || (dy > 0 && move.before.color == com.tp.Model.Player.BLACK) ) {
			throw new InvalidMoveException("Cannot move backwards");
		}
		if(dx > 3 || Math.abs(dy) > 3 || dx != Math.abs(dy)) {
			throw new InvalidMoveException("Cannot move that far");
		}
	}
	
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
	
}
