package com.tp.Model;

public class WhiteTurn implements IGameState{

	Checkers checkers;
	
	public WhiteTurn(Checkers checkers) {
		this.checkers = checkers;
	}
	
	@Override
	public void verifyPlayer(Player player) throws InvalidMoveException {
		if(player != Player.WHITE) {
			throw new InvalidMoveException("Not your turn");
		}
	}

	@Override
	public Player getTurn() {
		// TODO Auto-generated method stub
		return Player.WHITE;
	}

	@Override
	public void nextTurn() {
		// TODO Auto-generated method stub
		checkers.setState( new BlackTurn(checkers) );
	}

}
