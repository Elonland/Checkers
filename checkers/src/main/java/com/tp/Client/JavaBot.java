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

public class JavaBot {

	Socket clientSocket;
	
	BufferedReader reader;
	
	PrintWriter write;
	
	String playerNumber;
	
	ArrayList<Pawn> pawns = new ArrayList<Pawn>();
	
	int numberOfPieces;
	
	String command = null;
	
	String input = null;
	
	int boardSize;
	
	int numberOfBlack = 0;
	
	
	
	public void main(String[] args) throws IOException {
		
		setup();
		run();
		shutdown();
	}
	
	private void setup() throws IOException {
		
		try {
			
			clientSocket = new Socket("127.0.0.1" ,58901);
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			write = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/*
	 * Jak ma wyglądać ta jebana metoda?
	 * 
	 * 1.Bot chce tylko się ruszać i czytać czy może wkońcu się ruszyć.
	 * 	W takim razie będzie brał losowego pionka z planszy. 
	 * 		Będzie losować liczbę.
	 * 	Do skutku aż trafi na dobrego.
	 * 
	 * 	Po wylosowaniu pionka losuje dostępne x i y.
	 * 
	 *  Czyta czy ruch był wykonany dobrze jak nie to powtórz
	 */
	private void run() throws IOException {
		
		//Bot musi się spytać ile jest pionków na planszy
		
		while(true) {
			//Zaktualizuj aktulany stan tablicy.
			//updateBoard();
			
			//Czy gracz wykonał już ruch.
			
			//Read greetings.
			System.out.println(readFromServer());
			
			whiteMoved();


		}
		
	}
	
	private String readFromServer() throws IOException {
		
		return reader.readLine();
		
	}
	
	private void writeToServer(String message) {
		
		write.println(message);
		
	}
	
	private void whiteMoved() throws IOException {
		input = readFromServer();
		System.out.println(input);
		if(input.equals("UPDATE")) {
			
			input = readFromServer();
			updateBoard();
			
			System.out.println(input + " numberOfBlack: " + numberOfBlack);
			numberOfBlack = 20;
		}
		if(input.equals("WHITEMOVED")) {
			
			boolean correctMove = false;
			
			while(!correctMove) {
				int pawnNum = (int) (Math.random() * numberOfBlack);
				Pawn pawn = pawns.get(pawnNum);
				int count = 0;
				int xOld = pawn.getX();
				int yOld = pawn.getY();
			
				do
				{
				
					int xNew = xOld - 1 + (count * 2);
					int yNew = yOld - 1 + (count * 2);
			
					command = "MOVE " + xOld + " " + yOld + " " + xNew + " " + yNew + " false " + "false";
			
					//Wyslij ruch do servera
					writeToServer(command);
					input = readFromServer();
					if(input.equals("Correct move")) {
						correctMove = true;
						break;
					}
			
					count++;
					//Moze jednak inny warunek
			} while( count <= 1);
			}
			
			
			
			
		} else {
			
			System.out.println("This wasn't supposed to happen");
			
		}
	}
	
	//NOT READY TO USE
	private void guessCords(int x, int y) {
		
		//odwracanie symetryczne
		for(int k = 0; k <= 1; k++) {
			
			int guessX = (x - 1) + (2 * k);
			
			//jak zly to
			guessX--;
			
			//jak ponownie zly to
			guessX -= 2;
			
		}
		
		//BoardSize nie jest pobierane z servera!
		for(int i = 0; i < boardSize; i++) {
			
		}
	}
	
	private void updateBoard() throws IOException {
		//Pobierz dane o pionkach i ich kordach swoich
		//w serverze musi on ogarnąć o co chodzi z tym black.
		command = "UPDATEBOARD";
		writeToServer(command);
		
		
		//x y queen color ...
		input = readFromServer();
		System.out.println("Got it");
		System.out.println(input);
		
		String[] inputTab = input.split(input);
		
		numberOfPieces = inputTab.length/ 4;
		
		int k = 0;
		ArrayList<Pawn> temp = new ArrayList<Pawn>();
		
		for(int i = 0; i < numberOfPieces; i++) {
			
			int x = Integer.parseInt(inputTab[k]);
			int y = Integer.parseInt(inputTab[k + 1]);
			Boolean isQueen = Boolean.parseBoolean(inputTab[k + 3]);
			String color = inputTab[k + 2];
			
			System.out.println("x: " + x + " y: " + y + " isQueen: " + isQueen + " color: " + color);
			
			if(color.equals("WHITE")) {
				
				k += 4;
				continue;
				
			}
			
			numberOfBlack++;
			
			Pawn pawn = new Pawn(x, y, isQueen,color);
			temp.add(pawn);
			
			k += 4;
		}
		
		pawns = temp;
		
	}
	
	private void shutdown() throws IOException {
		// TODO Auto-generated method stub
		write.close();
		reader.close();
		clientSocket.close();
	}

	

	

}
