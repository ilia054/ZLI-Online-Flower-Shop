package client;

import java.io.IOException;

import common.ChatIF;
//import message.Message;
import common.Message;

/**
 * This class constructs the UI for a chat client. It implements the chat
 * interface in order to activate the display() method. Warning: Some of the
 * code here is cloned in ServerConsole
 **/

public class ClientCommu implements ChatIF {
	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	ZliClient client;

	/**
	 * Constructs an instance of the ClientConsole UI.
	 * @param ip The ip of the Client
	 * @param port The port to connect on.
	 */
	public ClientCommu(String ip, int port) {
		try {
			client = new ZliClient(ip, port, this);
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
			System.exit(1);
		}
	}

	/**
	 * This method waits for input from the console. Once it is received, it sends
	 * it to the client's message handler.
	 * @param message recived from Server
	 */
	public void accept(Message message) {
		client.handleMessageFromClientUI(message);
	}

	/**
	 * same as above only diffrence is that message is type of string and not Message
	 * @param message message Recived from server
	 */
	public void accept(String message) {
		client.handleMessageFromClientUI(message);
	}

	/**
	 * This method overrides the method in the ChatIF interface. It displays a
	 * message onto the screen.
	 * @param message The string to be displayed.
	 */
	@Override
	public void display(String message) {
		System.out.println("> " + message);
	}

}
