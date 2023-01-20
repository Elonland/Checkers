package com.tp.Model;

/**
 * 
 * @author Tobiasz Jędrzejek
 * 
 * Specific factory for Polish checkers.
 *
 */
public class PolishCheckersFactory implements ICheckersFactory {

	Checkers checkers;
	Board board;
	//Board board;
	IGameState state;
	@Override
	public Board createBoard() {
		
		board = new PolishBoard();
		return board;
	}

	@Override
	public IGameState createState() {
		// TODO Auto-generated method stub
		return new WhiteTurn(checkers);
	}
	
	public void setCheckers(Checkers checkers ) {
		this.checkers = checkers;
	}

}
