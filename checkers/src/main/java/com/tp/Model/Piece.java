package com.tp.Model;

/**
 * 
 * @author Tobiasz Jędrzejek
 * 
 * Class representing basic piece.
 *
 */
public class Piece {
	
	int X;
	int Y;
	public Player color;
	boolean queen = false;
	
	public Piece (int x, int y, Player color) {
		X = x;
		Y = y;
		this.color = color;
	}
	
	public Piece (int x, int y, boolean queen, Player color) {
		X = x;
		Y = y;
		this.queen = queen;
		this.color = color;
	}
	

}
