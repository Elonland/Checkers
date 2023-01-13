package com.tp.Model;

public interface ICheckersFactory {

	public Board createBoard();
	public IGameState createState();

}
