package server;

import java.io.IOException;
import common.ChangeScene;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 */
public class ZliServerUi extends Application {
	final public static int DEFAULT_PORT = 5555;
	static ZliServer Zliserver;

	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main

	@Override
	public void start(Stage primaryStage) throws Exception {
		ChangeScene scene = new ChangeScene();
		scene.changeScreen(primaryStage, "/GuiServerScreens/ZliServerUI.fxml", false);
	}

	public static void runServer(String port, String DBAddress, String username, String password) {
		int zliPort = 0; // Port to listen on

		try {
			zliPort = Integer.parseInt(port); // Set port to 5555

		} catch (Throwable t) {
			System.out.println("ERROR - Could not connect!");
			return;
		}

		Zliserver = new ZliServer(zliPort, DBAddress, username, password);

		try {
			Zliserver.listen(); // Start listening for connections
		} catch (Exception e) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}

	public static void disconnect() {
		if (Zliserver == null)
			Zliserver.stopListening();
		else
			try {
				Zliserver.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		System.out.println("Server Disconnected");
	}

}
