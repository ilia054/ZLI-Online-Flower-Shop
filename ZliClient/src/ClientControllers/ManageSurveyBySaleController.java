package ClientControllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import Enums.Task;
import client.ZliClient;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;
import common.MyFile;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * GUI Controller of ManageSurveyBySale
 */
public class ManageSurveyBySaleController {

    @FXML
    private ComboBox<String> SaleNameComboBox;

    @FXML
    private FontAwesomeIconView BackBtn;

    @FXML
    private FontAwesomeIconView analayzeBTN;

    @FXML
    private Label ErrorSurvey;

    @FXML
    private Button XBtn;
    @FXML
    private Button SelectPDFBtn;

    @FXML
    private Label errorTXT;



    @FXML
    private Label filenameLabel;


    private ChangeScene scene = new ChangeScene();
	private ChangeScene popup= new ChangeScene();
	private File selectedFile;
    private String Path;
	private MyFile msg;
	private FileInputStream fis;
	private ObservableList<String> surveysId = FXCollections.observableArrayList();
	/**
	 * before the GUI in popped up to the user
	 * it initialize the combobox "SaleNameComboBox" with the sells that we have 
	 * decided to create for our project  
	 * 
	 *the combobox will be set to empty ("") and only after the user chooses an selection
	 *the selectedFile button will be enabled
	 */
    public void initialize() {

    	SaleNameComboBox.setItems(FXCollections.observableArrayList("Spring Sale","Summer Sale","Fall Sale","Winter Sale"));
    	SaleNameComboBox.setValue("Winter Sale");
    	analayzeBTN.setDisable(true);
    	errorTXT.setText("");
    	SelectPDFBtn.setDisable(true);
    	SaleNameComboBox.setValue("");
    	filenameLabel.setText("");
    	SaleNameComboBox.setOnAction(e -> {
    		if(!SaleNameComboBox.getValue().equals("")) {
        	SelectPDFBtn.setDisable(false);
        	analayzeBTN.setDisable(false);}

    	}

           );

    }
	/**
	 * this method activates when the user presses Select PDF creates
	 * a PDF file from a specific file that the user chooses from his pc
	 *in case the file is not PDF an error label will show up and the 
	 *GUI wont go further and the user will have to re-choose a file
	 * @param event
	 */
    @FXML
    void AddPdfFile(ActionEvent event) {
    	FileChooser fc = new FileChooser();
		selectedFile = fc.showOpenDialog(null);
		if (selectedFile == null)
		{
			SelectPDFBtn.setText("Select PDF");
			analayzeBTN.setDisable(true);
			errorTXT.setText("");
			filenameLabel.setText("");
			return;
		}
		Path = selectedFile.getAbsolutePath(); //PATH
		String FileName = selectedFile.getName(); //NAME
		String AfterDot[] = selectedFile.getName().split("\\.");
		if (!AfterDot[1].equals("pdf")) {
			errorTXT.setText("The Image you choose MUST be .pdf, try a diffrent one");
			filenameLabel.setText("");
			analayzeBTN.setDisable(true);
			SelectPDFBtn.setText("Select PDF");
		} else {
			filenameLabel.setText(FileName);
			SelectPDFBtn.setText(FileName);
			analayzeBTN.setDisable(false);
			errorTXT.setText("");
		}
    }
    @FXML
    void Back(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		// scene.BackBtnOnManageOrders(new Stage());
		scene.changeScreen(new Stage(), "/GuiClientScreens/ManageSurveyMenu.fxml", true);
    }


	public void X(ActionEvent event) throws Exception {
		ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
		ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
		System.exit(0);
	}
	/**
	 * this method is activated when the button submit analyze is clicked 
	 * it inserts new PDF from the path that has been chosen into the analyzesurvey DB and gives it a 
	 * submission id "analyzeId" ,the survey that are connected to this survey analyze PDF will be updated
	 * with the analyzeId in the survey DB 
	 * as in order to create a PDF file we use the sendFile method in order to convert the path we 
	 * Received into MyFile variable
	 * @param event
	 */
    @SuppressWarnings("static-access")
	@FXML
    void analyzeClicked(MouseEvent event) {
    	String saleName= SaleNameComboBox.getValue();
    	ArrayList<String> input = new ArrayList<>();
    	input.add(saleName);
    	if(!sendFile())
    	{
			errorTXT.setText("Not Working");
    	}
    	ZliClientUI.chat.accept(new Message(Task.Send_PDF,msg));
    	surveysId=ZliClient.analyzeID.getString();
    	input.add(surveysId.get(0).toString());
    	ZliClientUI.chat.accept(new Message(Task.Get_Sale_Analyze, input));
    	popup.HelpMessage("Analyze document for "+saleName+" sale has been created!");
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/ManageSurveyMenu.fxml", true);

    }
    private boolean sendFile()
    {
    	 msg = new MyFile(selectedFile.getName());
		try {

			File newFile = new File(Path);
			byte[] mybytearray = new byte[(int) newFile.length()];
			fis = new FileInputStream(newFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			msg.initArray(mybytearray.length);
			msg.setSize(mybytearray.length);
			bis.read(msg.getMybytearray(), 0, mybytearray.length);
		} catch (Exception e) {
			return false;
		}
		return true;
    }

}
