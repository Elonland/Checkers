package com.tp.Model;

/**
 * 
 * @author Tobiasz Jędrzejek
 * 
 * Class that allows to track moves.
 * When server wants to register a move it sends object of this class to checkers class.
 *
 */
public class Move {

	public Move(Piece oldPiece, Piece newPiece, boolean isJump, Piece[] captured) {
		before = oldPiece;
		after = newPiece;
		jumped = captured;
		this.isJump = isJump;
	}
	public Piece before;
	public Piece after;
	public boolean isJump;
	public Piece[] jumped;

}
