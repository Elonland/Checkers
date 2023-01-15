package com.tp.Model;

/**
 * 
 * @author Tobiasz Jędrzejek
 * 
 * One of the states in game.
 *
 */
public class EndGame implements IGameState {
	
	Checkers checkers;
	Player player;
	
	public EndGame(Checkers checkers, Player player) {
		this.checkers = checkers;
		this.player = player;
	}
	
	@Override
	public void verifyPlayer(Player player) throws InvalidMoveException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Player getTurn() {
		return null;
	}

	@Override
	public void nextTurn() {
		
	}
	
	public Player getWinner() {
		return player; 
	}

}
