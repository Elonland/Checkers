package com.tp.Model;

public class CanadianCheckersFactory implements ICheckersFactory {

	Checkers checkers;
	Board board;
	IGameState state;
	
	@Override
	public Board createBoard() {
		// TODO Auto-generated method stub
		board = new CanadianBoard();
		return board;
	}

	@Override
	public IGameState createState() {
		// TODO Auto-generated method stub
		return new WhiteTurn(checkers);
	}

	@Override
	public RuleSet createRules() {
		// TODO Auto-generated method stub
		return new CanadianRuleSet();
	}

	@Override
	public void setCheckers(Checkers checkers) {
		// TODO Auto-generated method stub
		this.checkers = checkers;
	}

}
