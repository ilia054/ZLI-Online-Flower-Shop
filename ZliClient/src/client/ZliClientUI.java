package client;

import common.ChangeScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class ZliClientUI extends Application {
	/**
	 * chat is the instance of our "Socket" and the com·mu·ni·ca·tion with the
	 * Server All of the Controller's are the variables which will hold our Entities
	 * data from the DataBase
	 */
	public static ClientCommu chat; // only one instance
	

	public static void main(String args[]) throws Exception {
		launch(args);
	} 

	/**
	 * This method Creates a socket connection between the server and the client
	 * opening a two way communication between the 2 sides
	 * @param ip is the Ip server address of the server
	 * @param port the port to connect on
	 */
	public static void setChat(String ip, int port) {
		chat = new ClientCommu(ip, port);
	}

	/**
	 * This method creates the first instance of our GUI Screen to the user
	 * @param primaryStage the stage to switch to
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		ChangeScene scene = new ChangeScene();
		scene.changeScreen(primaryStage, "/GuiClientScreens/UserInputIPConnect.fxml", true);
		
	}
}
