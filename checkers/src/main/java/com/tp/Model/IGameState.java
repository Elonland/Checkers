package com.tp.Model;

/**
 * 
 * @author Tobiasz Jędrzejek
 * 
 * Interface for the games states.
 *
 */
public interface IGameState {

	void verifyPlayer(Player player) throws InvalidMoveException;
	
	Player getTurn();
	
	void nextTurn();

}
