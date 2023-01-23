package com.tp.Controller;

/*
 * TODO dodaj zmienna message które będzie wysyłana do klienta.
 * TODO czy podłączyłeś stany do servera?
 * TODO zasady max liczba bic trzeba dodac
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
/**
 * Class responsible for establishing connection, and handling requests from clients, the C in MVC.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
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
        try (ServerSocket listener = createServerSocket(58901)) {
            System.out.println("Checkers server is running...");
            var pool = Executors.newFixedThreadPool(2);
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
    Board board = null;
    
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
        Boolean readyRead = true;
        
    	BufferedReader reader;

        public Player(Socket socket, char mark) {
            this.socket = socket;
            this.mark = mark;
        }

        @Override
        public void run() {
            try {
            	System.out.println("Player" + mark + " is running\n");	
                setup();
                processCommands();
                //processMoves();
                
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
        	System.out.println("Setup is running\n");	
        	//inputStream = socket.getInputStream();
            //input = new Scanner(socket.getInputStream());
        	//dataInputStream = new DataInputStream(inputStream);
            output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            output.println("WELCOME " + mark);
                   
            //Sending to client board size
           //output.println("SIZE " + getBoardSize());
            
            
            if (mark == '1') {
                currentPlayer = this;
                output.println("MESSAGE Waiting for opponent to connect");
                
                //Only one player needs to implement checkers.
                setUpCheckers();
            } else {
                opponent = currentPlayer;
                opponent.opponent = this;
                opponent.output.println("MESSAGE Your move");
            }
        }
        /*
         * Used to communicate with server.
         */
        
        private void processCommands() throws IOException {
            while (true) {
                var command = reader.readLine();
                System.out.println("Command: " + command);
                if (command.startsWith("QUIT")) {
                    return;
                }
                
                if (command.startsWith("MOVE")) {
                	System.out.println("Got move request.");
                	processMoves(command);
                }
            }
        }
        
        
        
        /**
         * Read JsonObject from input to process changes.
         * Create move object for checkers to process changes.
         * If there is a InvalidMoveException send JsonObject to
         * client with error message.
         */
        private void processMoves(String command) {
        	//while(true) {
        		if(checkers.getState() instanceof EndGame) {
        			//break;
        		}

				String coordinates = null;
				coordinates = command;
				System.out.println("Read coordinates");
				String[] moves = coordinates.split(" ");
				
				//See what is inside
				for(int i = 0; i < moves.length; i++) {
					System.out.println(moves[i]);	
				}
				//System.out.println(moves.length);
				
				int xOld = Integer.parseInt(moves[1]);
				int yOld = Integer.parseInt(moves[2]);
				Piece oldPiece = board.getPiece(xOld, yOld);
				
				int xNew = Integer.parseInt(moves[3]);
				int yNew = Integer.parseInt(moves[4]);
				boolean isQueen = Boolean.parseBoolean(moves[5]);
				
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
				
				//If sth was captured create an array of these pieces
				Piece[] captured = null;
				boolean isJump = Boolean.parseBoolean(moves[6]);
				if(isJump == true) {
					captured = new Piece[(moves.length - 7) / 3];
					int j = 0;
					for(int i = 7; i < moves.length; i += 3) {
					
						int x = Integer.parseInt(moves[i]);
						int y = Integer.parseInt(moves[i+1]);
						
						//currently it isn't used anywhere
						boolean queen = Boolean.parseBoolean(moves[i+2]);
						captured[j] = board.getPiece(x, y);
					
						j++;
					
					}
				
				}
				if(oldPiece == null) {
					System.out.println("something is null");
				}
				move = new Move(oldPiece, newPiece, isQueen, captured);
				try {
					if(mark == '1') {
						checkers.move(move, com.tp.Model.Player.WHITE);
					} else {
						checkers.move(move, com.tp.Model.Player.BLACK);
					}
					
					output.println("Correct move");
					
				} catch (InvalidMoveException e) {
					
					e.printStackTrace();
					output.println(e);
				}
        	//}
        }
        
        private int getBoardSize() {
        	System.out.println("Get that board.\n");
        	return board.getSize();
        }
        /*
         * Using a wrapper for JsonObject because it isn't serializable.
         * Method used to deduce what type of checkers player wants to play.
         */
        public void setUpCheckers() {
        	String Sfactory = null;
			try {
				Sfactory = reader.readLine();
				//output.println("READY");
				//output.flush();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//input.nextLine();
        	
			
			System.out.println(Sfactory);
        	if(Sfactory.equals("polishCheckers")) {
        		System.out.println("Selected Polish checkers.\n");
        		checkers = new Checkers();
        		factory = new PolishCheckersFactory();
        		factory.setCheckers(checkers);
        		checkers.useFactory(factory);
        		
        		board = checkers.getBoard();
        		if(board != null) {
        			System.out.println("Board is not null\n");
        		} else {
        			System.out.println("Board is null.\n");
        		}
        		System.out.println("board size: " + board.getSize());
        	} else {
        		System.out.println("No factory selected: " + Sfactory + "\n");
        	}
        }

    }
}