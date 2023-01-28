package com.tp.Client;

public class Pawn {
	int x;
	int y;
	boolean queen;
	String color;
	
	public Pawn(int x, int y, boolean queen, String color) {
		this.x = x;
		this.y = y;
		this.queen = queen;
		this.color = color;
	}
	
	public boolean isQueen() {
		return queen;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public String getColor() {
		return color;
	}
	
}
