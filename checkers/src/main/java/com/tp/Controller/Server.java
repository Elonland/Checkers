package com.tp.Controller;


import java.io.DataInputStream;
/**
 * Class responsible for establishing connection, and handling requests from clients, the C in MVC.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import com.tp.Model.Board;
import com.tp.Model.Checkers;
import com.tp.Model.EndGame;
import com.tp.Model.ICheckersFactory;
import com.tp.Model.InvalidMoveException;
import com.tp.Model.Move;
import com.tp.Model.Piece;
import com.tp.Model.PolishCheckersFactory;

public class Server {

    public static void main(String[] args) throws Exception {
        try (var listener = createServerSocket(58901)) {
            System.out.println("Checkers server is running...");
            var pool = Executors.newFixedThreadPool(200);
            while (true) {
                Game game = new Game();
                //System.out.println("Game Created");
                pool.execute(game.new Player(listener.accept(), '1'));
                System.out.println("Player joined");
                pool.execute(game.new Player(listener.accept(), '2'));
            }
        }
    }
    
    public static ServerSocket createServerSocket(int port) throws IOException {
    	return new ServerSocket(port);
    }
}

class Game {

	ICheckersFactory factory;
    Checkers checkers;
    Board board;

    Player currentPlayer;

    class Player implements Runnable {
        char mark;
        Player opponent;
        Socket socket;
        //Scanner input;
        InputStream inputStream;
        //InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream;
        PrintWriter output;
        Move move;
       

        public Player(Socket socket, char mark) {
            this.socket = socket;
            this.mark = mark;
        }

        @Override
        public void run() {
            try {
                setup();
                //processCommands();
                processMoves();
                
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (opponent != null && opponent.output != null) {
                    opponent.output.println("OTHER_PLAYER_LEFT");
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

        public void setup() throws IOException {
        	inputStream = socket.getInputStream();
            //input = new Scanner(socket.getInputStream());
        	dataInputStream = new DataInputStream(inputStream);
            output = new PrintWriter(socket.getOutputStream(), true);
            
            output.println("WELCOME " + mark);
            
            //Only one player needs to implement checkers.
            if(mark == '1' || true) {
            	setUpCheckers();
            }
            
            //Sending to client board size
           output.println("SIZE " + getBoardSize());
            
            
            if (mark == '1') {
                currentPlayer = this;
                output.println("MESSAGE Waiting for opponent to connect");
            } else {
                opponent = currentPlayer;
                opponent.opponent = this;
                opponent.output.println("MESSAGE Your move");
            }
        }
        /*
         * Used to communicate with server.
         */
        /*
        private void processCommands() {
            while (input.hasNextLine()) {
                var command = input.nextLine();
                if (command.startsWith("QUIT")) {
                    return;
                }
            }
        }
        */
        /**
         * Read JsonObject from input to process changes.
         * Create move object for checkers to process changes.
         * If there is a InvalidMoveException send JsonObject to
         * client with error message.
         */
        private void processMoves() {
        	while(true) {
        		if(checkers.getState() instanceof EndGame) {
        			break;
        		}

				String coordinates = null;
				try {
					coordinates = dataInputStream.readUTF();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}//input.nextLine();
				String[] moves = coordinates.split(" ");
				
				int xOld = Integer.parseInt(moves[0]);
				int yOld = Integer.parseInt(moves[1]);
				Piece oldPiece = board.getPiece(xOld, yOld);
				
				int xNew = Integer.parseInt(moves[2]);
				int yNew = Integer.parseInt(moves[3]);
				boolean isQueen = Boolean.parseBoolean(moves[4]);
				
				Piece newPiece;
				
				if(mark == '1') {
					newPiece = new Piece(xNew, yNew, isQueen, com.tp.Model.Player.WHITE);
				} else {
					newPiece = new Piece(xNew, yNew, isQueen, com.tp.Model.Player.BLACK);
				}
				
				/*
				JsonArray array = obj.getJsonArray("jumped");
				Piece captured[] = new Piece[array.size()];
				int i = 0;
				
				for(Object o: array) {
					int x =((JsonObject) o).getInt("x");
					int y =((JsonObject) o).getInt("y");
					
					captured[i] = board.getPiece(x, y);
					
					i++;
				}
				*/
				Piece captured[] = new Piece[(moves.length - 5) / 2];
				int j = 0;
				for(int i = 5; i < moves.length; i += 2) {
					
					int x = Integer.parseInt(moves[i]);
					int y = Integer.parseInt(moves[i+1]);
					captured[j] = board.getPiece(x, y);
					
					j++;
					
				}
				
				move = new Move(oldPiece, newPiece, isQueen, captured);
				try {
					if(mark == '1') {
						checkers.move(move, com.tp.Model.Player.WHITE);
					} else {
						checkers.move(move, com.tp.Model.Player.BLACK);
					}
					
				} catch (InvalidMoveException e) {
					/*TODO Auto-generated catch block
					JsonObject error = Json.createObjectBuilder()
										.add("error", e.getMessage()).build();
					wrapper.setJsonObject(error , "Error");
					
					objOutput.writeObject(wrapper);
					*/
					e.printStackTrace();
				}
        	}
        }
        
        private int getBoardSize() {
        	return board.getSize();
        }
        /*
         * Using a wrapper for JsonObject because it isn't serializable.
         * Method used to deduce what type of checkers player wants to play.
         */
        public void setUpCheckers() {
        	
        	String Sfactory = null;
			try {
				Sfactory = dataInputStream.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//input.nextLine();
        	
        	if(Sfactory == "polishCheckers") {
        		factory = new PolishCheckersFactory();
        		checkers = new Checkers(factory);
        		board = checkers.getBoard();
        		System.out.println("board size: " + board);
        	}
        }

    }
}
