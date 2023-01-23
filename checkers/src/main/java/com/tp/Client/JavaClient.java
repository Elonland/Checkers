package com.tp.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
/*
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
			
			System.out.println("Select game type.\n Available: polishCheckers");
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
	
	private void run() throws IOException {
		while(true) {
			
			System.out.println("Opponent joined: " + opponentJoined );
			String command = null;
			String[] subCommand = null;
			if(opponentJoined == true) {
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
				
			}
			
		}
	}
	
	private void shutdown() throws IOException {
		reader.close();
		write.close();
		clientSocket.close();
	}
	
	/*
	 * Responding to input from client
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
		} else if(!message.equals("GOOD")){
			System.out.println(message);
		}
	}
	
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
	}

