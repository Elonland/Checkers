package com.tp.Model;

/**
 * 
 * @author Tobiasz Jędrzejek
 * 
 * Interface for the checkers factory for different types of checkers.
 *
 */
public interface ICheckersFactory {

	public Board createBoard();
	public IGameState createState();

}
