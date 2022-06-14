package ClientControllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.util.ArrayList;
import Enums.Task;
import client.ZliClient;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;

/**
 * GUI Controller of SubmitSurvey
 */
public class SubmitSurveyController {


   // @FXML
   // private Button XBtn;
    @FXML
    private TextField userName;

    @FXML
    private ComboBox<String> question1;

    @FXML
    private ComboBox<String> question2;

    @FXML
    private ComboBox<String> question3;

    @FXML
    private ComboBox<String> question4;

    @FXML
    private ComboBox<String> question5;

    @FXML
    private ComboBox<String> question6;

    @FXML
    private FontAwesomeIconView BackBtn;

    @FXML
    private FontAwesomeIconView submitBTN;

    @FXML
    private ComboBox<String> Type;
    @FXML
    private Label ErrorSurvey;
    
    private ArrayList<String> surveys = new ArrayList<String>();

	private ChangeScene scene = new ChangeScene();
	private ChangeScene popup= new ChangeScene();
	/** 
     * This method sets the values in the answers combo box and the month combo box.
     * @author Shay Zak
     */

	public void initialize() {
		question1.setItems(FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10"));
		question2.setItems(FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10"));
		question3.setItems(FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10"));
		question4.setItems(FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10"));
		question5.setItems(FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10"));
		question6.setItems(FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10"));
		Type.setItems(FXCollections.observableArrayList("Monthly","Store Name","Store Sale"));
		question1.setValue("");
		question2.setValue("");
		question3.setValue("");
		question4.setValue("");
		question5.setValue("");
		question6.setValue("");
		Type.setValue("Monthly");
		ErrorSurvey.setVisible(false);
	}

	public void X(ActionEvent event) throws Exception {
		ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
		ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
		System.exit(0);
	}
	

    @FXML
    void Back(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/StoreEmployeeMenu.fxml", true);
    }
    
    /** 
     * This method initiates when the button save is clicked.
     * this method saves all the answers of the survey questions into the DB.
     * It inserts the line into the "survey" DB that the store employee has submitted as a new survey.
     * @author Shay Zak
     * @param mouse event(click)
     */

    @FXML
    void saveClicked(MouseEvent event) {
    	if(question1.getValue().equals("")||question2.getValue().equals("")||question3.getValue().equals("")
    			||question4.getValue().equals("")||question5.getValue().equals("")||question6.getValue().equals("")) {
    		popup.HelpMessage("One or  more questions has not been filled.\nplease fill all 6 questions in order to submit the survey!");
    		return;
    	}
    	while(userName.getText().isEmpty()) {
    		ErrorSurvey.setVisible(true);
    		return;
    	}
    	
    	String username=userName.getText();
    	surveys.add(question1.getValue());
    	surveys.add(question2.getValue());
    	surveys.add(question3.getValue());
    	surveys.add(question4.getValue());
    	surveys.add(question5.getValue());
    	surveys.add(question6.getValue());
    	surveys.add(ZliClient.userController.getUser().getUsername());
    	surveys.add(username);
    	surveys.add(Type.getValue());

		Message message = new Message(Task.Update_surveys_table, surveys);
		ZliClientUI.chat.accept(message);
		popup.HelpMessage("Survey Result for Customer ID: "+username+" has been saved successfully!" );
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/StoreEmployeeMenu.fxml", true);
    }

}

