package com.tp.Model;

public class CanadianBoard extends Board {
	
	public CanadianBoard(){
		for(int x = 0; x < 12; x+=2){
			this.addPiece(new Piece(x, 0, Player.WHITE));
		    this.addPiece(new Piece(x, 2, Player.WHITE));
		    this.addPiece(new Piece(x, 4, Player.WHITE));
		    this.addPiece(new Piece(x+1, 1, Player.WHITE));
		    this.addPiece(new Piece(x+1, 3, Player.WHITE));

		    this.addPiece(new Piece(x, 9, Player.BLACK));
		    this.addPiece(new Piece(x, 11, Player.BLACK));
		    this.addPiece(new Piece(x+1, 12, Player.BLACK));
		    this.addPiece(new Piece(x+1, 10, Player.BLACK));
		    this.addPiece(new Piece(x+1, 8, Player.BLACK));
		}
	}

	public int getSize() {
		return 12;
	}

}
