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
	private RuleSet movement;
	private IGameState state;
	
	private static Checkers instance;
	
	/*
	 * @param factory specific type of factory which will be used to create board state and ruleSet.
	 */
	public void useFactory(ICheckersFactory factory) {
		board = factory.createBoard();
		state = factory.createState();
		movement = factory.createRules();
	}
	/*
	 * @param move contains information about made move by a player.
	 * @param player contains information about color of the player.
	 * 
	 * Crucial part of this class it verifies if movement is correct 
	 * and if everything is correct changes are applied.
	 */
	public void move(Move move, Player player) throws InvalidMoveException {
		if(move.before == null || move.after == null) {
			throw new InvalidMoveException("Move cannot be null");
		}
		if(move.before.color != player || move.after.color != player) {
			throw new InvalidMoveException("You can only move your pieces");
		}
		state.verifyPlayer(player);
		try {
			movement.verifyMove(move, board);
			board.makeMove(move);

			if(board.getPieceCount(player.BLACK) == 0) {
				state = new EndGame(this, player.WHITE);
			}
			if(board.getPieceCount(player.WHITE) == 0) {
				state = new EndGame(this, player.BLACK);
			}
			
			state.nextTurn();
			
		} catch (InvalidMoveException e) {
			throw e;
		}
		
		
	}
	//NOT USED
	public void createInstance(ICheckersFactory factory) {
		//instance = new Checkers(factory);
	}
	//NOT USED
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
