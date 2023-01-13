package com.tp.Model;

public interface IGameState {

	void verifyPlayer(Player player) throws InvalidMoveException;
	
	Player getTurn();
	
	void nextTurn();

}
