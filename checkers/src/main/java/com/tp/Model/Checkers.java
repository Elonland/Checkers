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
	
	public void useFactory(ICheckersFactory factory) {
		board = factory.createBoard();
		state = factory.createState();
		movement = factory.createRules();
	}
	
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
			System.out.println(e);
		}
		
		
	}
	
	public void createInstance(ICheckersFactory factory) {
		//instance = new Checkers(factory);
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
