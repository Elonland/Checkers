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
	
	public int getX() {
		return X;
	}
	
	public int getY() {
		return Y;
	}
	
	public boolean isQueen() {
		return queen;
	}
	
	public String getColorString() {
		if(color == com.tp.Model.Player.BLACK) {
			return "BLACK";
		} else {
			return "WHITE";
		}
	}
	

}
