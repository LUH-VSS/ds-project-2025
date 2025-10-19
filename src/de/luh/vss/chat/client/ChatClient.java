package de.luh.vss.chat.client;

import java.io.IOException;

public class ChatClient {

	public static void main(String... args) {
		try {
			new ChatClient().start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() throws IOException {
		System.out.println("Congratulation for successfully setting up your environment for Distributed Systems Exercises!\n");
		// Use your own user ID (ErgebnisPIN) exclusively to initiate and pass the tests; otherwise the actions will be performed for a different user.

		// implement your chat client logic here
	}

}
