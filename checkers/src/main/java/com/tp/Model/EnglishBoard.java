package com.tp.Model;

public class EnglishBoard extends Board{

	@Override
	public int getSize() {
		return 8;
		
	}public EnglishBoard(){
		for(int x = 0; x < 8; x+=2){
			this.addPiece(new Piece(x, 0, Player.WHITE));
		    this.addPiece(new Piece(x, 2, Player.WHITE));
		    this.addPiece(new Piece(x+1, 1, Player.WHITE));

		    this.addPiece(new Piece(x, 6, Player.BLACK));
		    this.addPiece(new Piece(x+1, 7, Player.BLACK));
		    this.addPiece(new Piece(x+1, 5, Player.BLACK));
		}
	}
	
}
