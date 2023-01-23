package com.tp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.json.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.tp.Controller.Server;
/*
 * Class used to test server's communication with client via JsonObjects.
 */
public class ServerTest {
	
	String obj;
	String factory;
	@Before
	public void setup() {
		 //old x    y  new x     y      queen    isJump jumped[]
		obj = "1 " + "0 " + "0 " + "1 " + "false " + "true " +  "4 " + "4 " + "false " + "6 " + "6 " + "false"; 
		factory = "polishCheckers";
	}
	
	/*
	@Test
	public void testServerSocketIsCreated() throws IOException {
		assertNotNull(createServerSocket(58901));
	}
	*/
	
	@Test
	public void testServerSocketIsCreatedWithSpecificPort() throws IOException {
		final int testPort = 58902;
		ServerSocket testServerSocket = createServerSocket(testPort);
		
		assertEquals(testServerSocket.getLocalPort(), testPort);
		
	}
	
	/*
	@Test
	public void testClientSocketGetsCreated() throws IOException {
		ServerSocket mockServerSocket = mock(ServerSocket.class);
		when(mockServerSocket.accept()).thenReturn(new Socket());
		
		assertNotNull(createClientSocket(mockServerSocket));
	}
	*/
	
	public static ServerSocket createServerSocket(int port) throws IOException {
	    return new ServerSocket(port);
	}
	
	public static Socket createClientSocket(ServerSocket socket) throws IOException {
	    return socket.accept();
	}
	
	Socket clientSocket;
	
	Scanner inputStream;
	
	BufferedReader reader;
	
	PrintWriter write;
	
	public static Scanner createSocketReader(Socket socket) throws IOException {
		return new Scanner(new InputStreamReader(socket.getInputStream()));
	}
	
	public static PrintWriter createSocketWriter(Socket socket) throws IOException {
		return new PrintWriter(socket.getOutputStream(), true);
	}
	
	
	/**
	 * 
	 * @param input read from input stream.
	 * @return Read messages from server.
	 */
	public static String readFromInputStream(Scanner input) {
	    return input.nextLine();
	}
	/**
	 * 
	 * @param output write to output stream.
	 * @param data data to send to server.
	 */
	public static void writeToOutputStream(PrintWriter output, String data) {
	    output.println(data);
	}
		
	@Test
	public void testServerResponse() throws UnknownHostException, IOException {
		//Server server = new Server();
		setup();
		/*
		try {
			try {
				Server.main(null);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			*/
			Socket socket = new Socket("127.0.0.1" ,58901);
			inputStream = new Scanner(socket.getInputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//outputStream = new PrintWriter(socket.getOutputStream());
			write = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			
			//Socket socket2 = new Socket("127.0.0.1", 58901);
			
			//write.write(factory);
			//write.flush();
			
			System.out.println("Factory Written");
			
			//WELCOME 1
			String signal = reader.readLine();
			System.out.println(signal);
			
			write.println(factory);
			write.flush();
			
			System.out.println("Factory Written");
			
			signal = reader.readLine();
			System.out.println("Read signal " + signal);
			
			//board size and "waiting for opponent message"
			//signal = reader.readLine();
			//System.out.println(signal);
			//signal = reader.readLine();
			//System.out.println(signal);
			
			
			
			System.out.println("Writing to Server");
			write.println(obj);
			write.flush();
			
			
			
			System.out.println("Waiting for response");
			//Read error message.
			String out = reader.readLine();
			System.out.println(out);
			
			assertNotNull(out);
			
			write.close();
			//dataOutputStream.close();
			socket.close();
		}
	}
	
