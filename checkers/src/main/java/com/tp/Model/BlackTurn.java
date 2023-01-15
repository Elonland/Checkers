package com.tp.Model;
/**
 * 
 * @author Tobiasz Jędrzejek
 *
 * One of game states.
 */
public class BlackTurn implements IGameState {

	private Checkers checkers;
	
	public BlackTurn(Checkers checkers) {
		this.checkers = checkers;
	}
	
	@Override
	public void verifyPlayer(Player player) throws InvalidMoveException {
		if(player != Player.BLACK) {
			throw new InvalidMoveException("Not your turn");
		}
	}

	@Override
	public Player getTurn() {
		return Player.BLACK;
	}

	@Override
	public void nextTurn() {
		// TODO Auto-generated method stub
		checkers.setState(new WhiteTurn(checkers));
	}

}
