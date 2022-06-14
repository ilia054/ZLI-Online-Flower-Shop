package common;

import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
*This class is used to change Scene's when users are clicking buttons on the screen
*each screen has a FXML file and the path is passed so we change the scene and icon depending on what scene we want to
*show to the to the user
*/
public class ChangeScene {
	/*We save two path's the the Server and User Icon for our app's screen*/
	private String userIconPath = "/GuiAssests/userIcon.png";
	private String serverIconPath = "/GuiAssests/serverIcon.jpeg";
/**
* This method is called when the we want to change the scene (lets say a user clicked "Back" or "X" or Manage Orders
* So to keep the code clean and nice we have made a method that works for all screens
* @param primaryStage a new stage entity( New Stage())
* @param path Path to the FXML file to display
* @param isUserScreen A boolean to indicate which Icon to set to the select screen
*/	
	public void changeScreen(Stage primaryStage,String path,boolean isUserScreen)
	{
		try {
			Parent root = FXMLLoader.load(getClass().getResource(path));
			Scene scene = new Scene(root);
			String iconPath = isUserScreen == true ? userIconPath :  serverIconPath;
			Image icon=new Image(getClass().getResourceAsStream(iconPath));
			primaryStage.getIcons().add(icon);
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			makeUndecoratedScreenMovable(root,primaryStage);
			primaryStage.show();
			} catch(Exception e) {
				e.printStackTrace();
			}
	}

	public void changeScreenNotMovable(Stage primaryStage,String path,boolean isUserScreen)
	{
		try {
			Parent root = FXMLLoader.load(getClass().getResource(path));
			Scene scene = new Scene(root);
			String iconPath = isUserScreen == true ? userIconPath :  serverIconPath;
			Image icon=new Image(getClass().getResourceAsStream(iconPath));
			primaryStage.getIcons().add(icon);
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.show();
			} catch(Exception e) {
				e.printStackTrace();
			}
	}
/**
*This method makes the Scene movable when a User moves the Application around his screen
*since we disable the default view of the Application we need to create a custom Function to be able
*to move it now
* @param root
* @param primaryStage
*/	
	public void makeUndecoratedScreenMovable(Parent root,Stage primaryStage)
	{
		root.setOnMousePressed(pressEvent -> {
		    root.setOnMouseDragged(dragEvent -> {
		        primaryStage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
		        primaryStage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
		    });
		});
	}
	
	public void ErrorMessage(String arg) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		Image image = new Image("/GuiAssests/WarningItemCart.png");
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(100);
		imageView.setFitWidth(100);
		alert.setGraphic(imageView);
		alert.setHeaderText(arg);
		alert.getDialogPane().setStyle("-fx-background: #168900; -fx-background-color: #168900; ");
		alert.initStyle(StageStyle.UNDECORATED);
		alert.setContentText("Please Click OK to Contuine");
		@SuppressWarnings("unused")
		Optional<ButtonType> option = alert.showAndWait();
	}
	
	public void HelpMessage(String arg) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(arg);
		alert.getDialogPane().setStyle("-fx-background: #168900; -fx-background-color: #168900; ");
		alert.initStyle(StageStyle.UNDECORATED);
		alert.setContentText("Please Click OK to Contuine");
		@SuppressWarnings("unused")
		Optional<ButtonType> option = alert.showAndWait();
	}
	
	
}
