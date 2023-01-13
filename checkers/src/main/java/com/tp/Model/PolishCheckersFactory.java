package com.tp.Model;

public class PolishCheckersFactory implements ICheckersFactory {

	Checkers checkers;
	
	public PolishCheckersFactory(Checkers checkers) {
		this.checkers = checkers;
	}
	
	//Board board;
	IGameState state;
	@Override
	public Board createBoard() {
		
		return new PolishBoard();
	}

	@Override
	public IGameState createState() {
		// TODO Auto-generated method stub
		return new WhiteTurn(checkers);
	}

}
