package ClientControllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import EntityControllers.StringController;
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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * GUI Controller of ManageSurveyByTimePeriod
 */
public class ManageSurveyByTimePeriodController {

    @FXML
    private TextField yearTxtField;

    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private FontAwesomeIconView BackBtn;

    @FXML
    private FontAwesomeIconView analayzeBTN;

    @FXML
    private Label ErrorSurvey;

    @FXML
    private FontAwesomeIconView searchYearBtn;

    @FXML
    private Label ErrorSurvey2;

    @FXML
    private Label ErrorSurvey1;


    @FXML
    private Button XBtn;

    @FXML
    private Button SelectPDFBtn;

    @FXML
    private Label errorTXT;


    @FXML
    private Label filenameLabel;


	private ObservableList<String> monthsYear = FXCollections.observableArrayList();
	private ChangeScene scene = new ChangeScene();
	private ArrayList<String> yearMonth=new ArrayList<String>();
	private File selectedFile;
    private String Path;
	private MyFile msg;
	private FileInputStream fis;
	private ObservableList<String> surveysId = FXCollections.observableArrayList();
	/**
	 * We set some FXML parts as not visible and disabled and only in case 
	 * some error accords they will show up 
	 */
	public void initialize() {
		monthComboBox.setDisable(true);//added
		analayzeBTN.setDisable(true);
		ErrorSurvey2.setVisible(false);
		ErrorSurvey1.setVisible(false);
    	analayzeBTN.setDisable(true);
    	errorTXT.setText("");
    	filenameLabel.setText("");
    	SelectPDFBtn.setDisable(true);
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
    /**
     * this method is activated when the user press the search icon 
     * this method will search if there is a survey in our survey DB that holds this survey type "Monthly"
     * by the year that has been written in the text box , in case no such data exists an error label will pop up
     * in case they were found in the survey DB they will be returned into the comboxbox 
     * and  the Select PDF button will be enabled 
     * @param event
     * @throws IOException
     */
    @FXML
    void SearchYearClicked(MouseEvent event) throws IOException {
    	String Year=yearTxtField.getText();
    	ObservableList<String> months= FXCollections.observableArrayList();
		ZliClientUI.chat.accept(new Message(Task.Get_MonthsBy_Year, Year));
		monthsYear=StringController.getString();

		if(monthsYear==null)return;

		int j=monthsYear.size();
		for(int i=0;i<j;i++) {

			months.add(monthsYear.get(i));

		}
		if(months.size()<1) {
			ErrorSurvey2.setVisible(true);
			ErrorSurvey1.setVisible(true);
			return;
		}
		ErrorSurvey2.setVisible(false);
		ErrorSurvey1.setVisible(false);

		monthComboBox.setItems(months);
		monthComboBox.setDisable(false);//added
		analayzeBTN.setDisable(false);
    	SelectPDFBtn.setDisable(false);


    }

	public void X(ActionEvent event) throws Exception {
		ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
		ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
		System.exit(0);
	}
	/**
	 * this method is activated when the button submit analyze is clicked 
	 * it inserts new pdf from the path that has been chosen into the analyzesurvey DB and gives it a 
	 * submission id "analyzeId" ,the survey that are connected to this survey analyze PDF will be updated
	 * with the analyzeId in the survey DB 
	 * as in order to create a pdf file we use the sendFile method in order to convert the path we 
	 * Received into MyFile variable
	 * @param event
	 */
    @SuppressWarnings("static-access")
	@FXML
    void analyzeClicked(MouseEvent event) {
    	String year=yearTxtField.getText();
    	String month=monthComboBox.getValue();
    	yearMonth.add(year);
    	yearMonth.add(month);
    	if(!sendFile())
    	{
			errorTXT.setText("Not Working");
    	}
    	ZliClientUI.chat.accept(new Message(Task.Send_PDF,msg));
    	surveysId=ZliClient.analyzeID.getString();
    	yearMonth.add(surveysId.get(0).toString());
    	ZliClientUI.chat.accept(new Message(Task.Get_Month_Year_Analyze, yearMonth));
    	scene.HelpMessage("Analyze document for "+year+"And"+month+" sale has been created!");
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
