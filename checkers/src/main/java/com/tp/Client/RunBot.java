package com.tp.Client;

import java.io.IOException;

public class RunBot {

	static JavaBot bot = new JavaBot();
	
	public static void main(String[] args) {
		
		try {
			bot.main(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
