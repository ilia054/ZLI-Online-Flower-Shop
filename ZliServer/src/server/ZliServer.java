package server;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import Entities.ConnectedClient;
import Entities.DataBase;
import common.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 */

public class ZliServer extends AbstractServer {
	DataBase DataBasecontroller;
	// Class variables *************************************************
	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	// This holds the list of the connected clients to the server and their status
	private static ObservableList<ConnectedClient> clientList = FXCollections.observableArrayList();

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 * @param DBAddress DataBase Address
	 * @param username the username of the database
	 * @param password database password 
	 */
	public ZliServer(int port, String DBAddress, String username, String password) {
		super(port);
		DataBasecontroller = new DataBase(username, password, DBAddress);
	}

	// Instance methods ************************************************

	public static ObservableList<ConnectedClient> getClientList() {
		return clientList;
	}

	public static void setClientList(ObservableList<ConnectedClient> clientList) {
		ZliServer.clientList = clientList;
	}

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg instanceof String)
			System.out.println("Message received: " + msg + " from " + client);
		else if (msg instanceof Message)
			System.out.println("Message received: " + ((Message) msg).getTask().toString() + " from " + client);
		try {
			MessageHandlerServer.HandleMessage(msg, client);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
//the method creates connection between our sql and our server.
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
		try {
			MySqlConnection.connectToDb(DataBasecontroller.getUsername(), DataBasecontroller.getPassword(),
					DataBasecontroller.getDBAddress());
		} catch (Exception ex) {
			System.out.println("Error! DataBase Connection Failed");
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	@Override
	protected void clientConnected(ConnectionToClient client) {

	}

}
//End of ZliServer class
