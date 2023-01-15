package com.tp.Model;

/**
 * 
 * @author Tobiasz Jędrzejek
 *
 *Contains board and states. It processes moves.
 *It is a place where everything happens.
 */
public class Checkers {
	
	private Board board;
	//private Ruleset rules;
	private IGameState state;
	
	private static Checkers instance;
	
	public Checkers(ICheckersFactory factory) {
		board = factory.createBoard();
		state = factory.createState();
	}
	
	public void move(Move move, Player player) throws InvalidMoveException {
			
		state.verifyPlayer(player);
		//movement.verifyMove(move, board);
		board.makeMove(move);
	}
	
	public void createInstance(ICheckersFactory factory) {
		instance = new Checkers(factory);
	}
	
	public Checkers getInstance() {
		return instance;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public IGameState getState() {
		return state;
	}
	
	public void setState(IGameState state) {
		this.state = state;
	}
}
