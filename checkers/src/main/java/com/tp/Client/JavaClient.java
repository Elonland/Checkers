package com.tp.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
/*
 * @author Tobiasz Jędrzejek
 * Class reads and sends messages to server.
 * At the beginning it connects to the server and gets the input and output stream to the socket.
 * Next it reads the player number if it equals 1 then the player can decide what type of checkers to play.
 * Before selecting game type server sends message to the player to wait for the other player.
 * Player cannot send moves to the server if other player hasn't joined yet.
 * 
 * In run method input from player is interpreted and it will respond.
 * 
 * if server sends message to player 1 containing "Your move" player will be able to make moves.
 */

public class JavaClient {

	Socket clientSocket;
		
	BufferedReader reader;
	
	PrintWriter write;
	
	Scanner playerInput;
	
	String playerNumber;
	
	Boolean opponentJoined = false;
	
	ArrayList<Pawn> pawns = null;
	
	Boolean boardFirstDrawn = false;
	Boolean firstTryHuh = true;
	
	int boardSize = 0;
	
	public void main(String[] args) throws IOException {
		setup();
		
		//Server sends greetings message at the beginning.
		//System.out.println("Waiting for server to send greeting.");
		playerNumber = reader.readLine();
		//System.out.println("Received greeting.");
		
		
		
		if(playerNumber.contains("1")) {
			System.out.println("You are player number 1.");
			
			//Server sends message to player 1 to wait
			respondServer();
			
			System.out.println("Select game type.\n Available: polishCheckers, englishCheckers, canadianCheckers");
			String gameType = playerInput.nextLine();
			write.println(gameType);
			write.flush();
			
			waitForOpponent();
		} else {
			System.out.println("You are player number 2.\n Wait for first player to move.");
			opponentJoined = true;
		}
		
		run();
		shutdown();
	}
	
	/*
	 * Is responsible for configuring input and output and draws initial board.
	 */
	private void setup() throws IOException {
		try {
			clientSocket = new Socket("127.0.0.1" ,58901);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		write = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
		playerInput = new Scanner(System.in);
	}
	/*
	 * Reads players input if he types QUIT loop breaks.
	 */
	private void run() throws IOException {
		while(true) {
			
			if(playerNumber.contains("1") && firstTryHuh) {
			
				System.out.println("Opponent joined: " + opponentJoined );
			}
			
			String command = null;
			String[] subCommand = {" "};
			
			
			
			if(opponentJoined == true) {
				
				//To draw initial board using first message from server.
				if(firstTryHuh == true) {
					write.println("SHOWBOARD");
					write.flush();
					drawBoard();
					firstTryHuh = false;
				}
				
				command = playerInput.nextLine();
				System.out.println(command);
				subCommand = command.split(" ");
				
				
			}
			
			if(subCommand[0] == null) {
				continue;
			}
			
			if(subCommand[0].equals("QUIT")) {
				write.println("QUIT");
				write.flush();
				break;
			}
			
			if(subCommand[0].equals("MOVE")) {
				write.println(command);
				//System.out.println(command.substring(5));
				write.flush();
				respondServer();
				//waitForOtherPlayer();
				
			}
			
			if(subCommand[0].equals("SHOW")) {
				respondServer();
			}
			
			
		}
	}
	/*
	 * Used to properly close all streams.
	 */
	private void shutdown() throws IOException {
		reader.close();
		write.close();
		clientSocket.close();
	}
	
	/*
	 * Responding to input from client [NOT USED]
	 */
	private void readMessage() {
		String message = playerInput.nextLine();
		
	}
	/*
	 * Responding to information from server
	 */
	private void respondServer() throws IOException {
		String message = reader.readLine();
		System.out.println(message);
		if(message.startsWith("MESSAGE")) {
			System.out.println(message.substring(8));
			
			if(message.substring(8).equals("Your move")) {
				opponentJoined = true;
			}
		} else if(message.equals("Correct move")) {
			//Draw board if move was correct
			
			//drawBoard();
			updateBoard();
		} else if(message.equals("UPDATE")) { 
			updateBoard();
			
		} else if(!message.equals("GOOD")){
			System.out.println(message);
		}
	}
	/*
	 * Only used for player1
	 * He needs to wait for player2 to start the game.
	 * Waits for server response.
	 */
	private void waitForOpponent() throws IOException {
		while(opponentJoined == false) {
				try {
					System.out.println("waiting for opponent in waitForOpponent");
					respondServer();
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Thread.currentThread().interrupt();
					e.printStackTrace();
				}
			}
		}
	
	private void drawBoard() throws IOException {
		
		
		if(boardFirstDrawn == false) {
			
			System.out.println("Drawing board... ");
			String details = reader.readLine();
			String[] detailsArray = details.split(" ");
			System.out.println(details);
			
			//System.out.println("Length of the command: " + detailsArray.length);
			
			//SIZE 25 PIECES WHITE BLACK
			int numberOfPieces = (detailsArray.length - 3) / 3;
		
			boardSize = Integer.parseInt(detailsArray[1]);
		
			pawns = new ArrayList<Pawn>();
			int j = 0;
			
			for(int i = 0; i < numberOfPieces; i++) {
				
				int x = Integer.parseInt(detailsArray[j + 3]); 
				int y = Integer.parseInt(detailsArray[j + 4]);
				//System.out.println("x: " + x + " y: " + y);
				String color = detailsArray[j + 5];
				Pawn pawn = new Pawn(x, y, false, color);
				pawns.add(pawn);
				j += 3;
			}
			
			boardFirstDrawn = true;
		} /*else {
			//pobieram x y queen color i zmieniam dane w tablicy
			
			int numberOfJumped = (detailsArray.length - 6) / 2;
			//int[] xTab = new int[numberOfJumped];
			//int[] yTab = new int[numberOfJumped];
			
			//getting positions of old pawn and the new position of said pawn.
			int xOld = Integer.parseInt(detailsArray[0]);
			int yOld = Integer.parseInt(detailsArray[1]);
			int xNew = Integer.parseInt(detailsArray[2]);
			int yNew = Integer.parseInt(detailsArray[3]);
			
			//if pawn at new position is a queen.
			boolean queen = Boolean.parseBoolean(detailsArray[4]);
			//color of new pawn.
			String color = detailsArray[5];
			
			//color of jumped pieces.
			String colorJumped;
			
			//determine what color they are.
			if(color == "WHITE") {
				colorJumped = "BLACK";
			} else {
				colorJumped = "WHITE";
			}
				
			//jumped pieces
			int k = 0;
			for( int j = 0;  j < numberOfJumped; j++) {
				
				//coordinates of piece to delete.
				int xDel = Integer.parseInt(detailsArray[k + 6]);
				int yDel = Integer.parseInt(detailsArray[k + 7]);
				
				//go to hell with efficiency. Looking for pawn to obliterate.
				for(int i = 0; i < pawns.size(); i++) {
					Pawn pawn = pawns.get(i);
					
					//Obliterate only once because we aren't that greedy.
					if(pawn.getX() == xDel && pawn.getY() == yDel && pawn.color.equals(colorJumped)) {
						System.out.println("Deleting pawn");
						pawns.remove(pawn);
						break;
					}
					
				}
				k += 2;
			}
			
			//go to hell with efficiency. Looking for old useless pawn to obliterate.
			for(int i = 0; i < pawns.size(); i++) {
				Pawn pawn = pawns.get(i);
				
				//if you found then get out of here.
				if(pawn.getX() == xOld && pawn.getY() == yOld && pawn.color.equals(color)) {
					System.out.println("Deleting old pawn");
					pawns.remove(pawn);
					break;
				}
				
			}

			//Adding pawn in new position.
			Pawn pawn = new Pawn(xNew, yNew, queen, color);
			pawns.add(pawn);
			
		}
		*/
		drawBoardDetails(boardSize);
		
	}
	
	private void drawBoardDetails(int boardSize) {
		//System.out.println("Preparing to draw this: " + boardSize);
		
		
		System.out.print("  ");
		for(int i = 0; i < boardSize; i++) {
			System.out.print(i);
		}
		System.out.print("\n  ");
		for(int i = 0; i < boardSize; i++) {
			System.out.print("_");
		}
		
		System.out.print("\n");
		
		for(int y1 = boardSize - 1; y1 >= 0; y1--) {
			System.out.print(y1 + " ");
			String line = "";
				for(int x1 = 0; x1 < boardSize; x1++) {
					
					
					boolean found = false;
					for(int i = 0; i < pawns.size(); i++) {
						Pawn pawn = pawns.get(i);
						if( pawn.getX() == x1 && pawn.y == y1 && pawn.queen == false && pawn.color.equals("BLACK")) {
							line += "X";
							found = true;
							break;
						} if(pawn.getX() == x1 && pawn.y == y1 && pawn.queen == true && pawn.color.equals("BLACK")) {
							line += "B";
							found = true;
							break;
						} if(pawn.getX() == x1 && pawn.y == y1 && pawn.queen == false && pawn.color.equals("WHITE")) {
							line += "O";
							found = true;
							break;
						} if(pawn.getX() == x1 && pawn.y == y1 && pawn.queen == true && pawn.color.equals("WHITE")) {
							line += "W";
							found = true;
							break;
						}
						
					}
					
					if(found == false) {
						line += " ";
					}
					
					
					
					
				
				}	
				System.out.println(line);
				//System.out.print("\n");
		
			}
		
		System.out.print("  ");
		for(int i = 0; i < boardSize; i++) {
			System.out.print("_");
		}
		System.out.print("\n");
	}
	
	public void updateBoard() throws IOException {
		
		String board;
		
		write.println("UPDATEBOARD");
		write.flush();
		
		board = reader.readLine();
		String[] boardTab = board.split(" ");
		int numberOfPieces = boardTab.length / 4;
		
		ArrayList<Pawn> pawnsTemp = new ArrayList<Pawn>();
		
		System.out.println("numberOfPieces: " + numberOfPieces);
		
		int k = 0;
		for(int i = 0; i < numberOfPieces; i++) {
			
			int x = Integer.parseInt(boardTab[k]);
			int y = Integer.parseInt(boardTab[k + 1]);
			boolean queen = Boolean.parseBoolean(boardTab[k + 2]);
			String color = boardTab[k + 3];
			
			Pawn pawn = new Pawn(x, y, queen, color);
			//System.out.println("x: " + x + " y: " + y + " color: " + color);
			pawnsTemp.add(pawn);
			
			k += 4;
		}
		
		pawns = pawnsTemp;
		
		drawBoardDetails(boardSize);
	
	}
	
	public void waitForOtherPlayer() throws IOException {
		write.println("MOVED " + playerNumber);
		write.flush();
		String message = reader.readLine();
		
		if( !message.equals("MOVED")) {
			System.out.println("That wasn't supposed to happen");
		}
	}
	
	
}

