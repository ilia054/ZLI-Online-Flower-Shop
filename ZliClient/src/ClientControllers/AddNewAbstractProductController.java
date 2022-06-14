package ClientControllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entities.AbstractProduct;
import Entities.CustomProduct;
import Entities.Product;
import Entities.ProductMyFile;
import Enums.Colors;
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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
/**
 * The class AddNewAbstractProductController is the Controller of "Add new Prodcut\Item" user Screen
 * @author Ilya
 *
 */

public class AddNewAbstractProductController implements Initializable {

	@FXML
	private Button XBtn;

	@FXML
	private FontAwesomeIconView BackBtn;

	@FXML
	private Text errorTXT;

	@FXML
	private FontAwesomeIconView HelpBtn1;

	@FXML
	private TextField nameTXT;

	@FXML
	private TextField priceTXT;

	@FXML
	private ComboBox<String> typeComboBox;

	@FXML
	private ComboBox<String> categoryComboBox;

	@FXML
	private ComboBox<String> colorComboBox;

	@FXML
	private Button photoBtn;

	@FXML
	private Button componentBtn;

	@FXML
	private Button completeBtn;

	@FXML
	private FontAwesomeIconView HelpBtn;

	@FXML
	private FontAwesomeIconView ComponenetsFXICON;

	private ChangeScene scene = new ChangeScene();
	private ObservableList<String> categories = FXCollections.observableArrayList("Boquet", "FlowerArrangment",
			"BlossomingFlower", "FlowerPot");
	private ObservableList<String> type = FXCollections.observableArrayList("Product", "Item");
	private ObservableList<String> colors = FXCollections.observableArrayList();
	private File selectedFile;
	private String Path;
	private ProductMyFile productMyFile;

	
	/**
	 * initialize()
	 * This method is called when the screen "Create new Prodcut\Item" is calleed
	 * it initializes all of our logic,buttons,combboxes etc...
	 */

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		for (Colors color : Colors.values()) {
			colors.add(color.toString());
		}
		categoryComboBox.setItems(categories); // set CategoryComboBox strings
		typeComboBox.setItems(type);// set TypeComboBox strings
		colorComboBox.setItems(colors);// set SaleComboBox strings
		errorTXT.setText("");

	}
	/**
	 * CompleteNewAbstractProduct(ActionEvent event)
	 * This method invokes Tests to verify all of the required info has been sumbited
	 * if one test does not pass, we set the ErrorTXT to let the user know what the error is
	 * if all test passed we create a new Prodcut\Item in our Catalog by runing A query
	 * @param event when "Complete" button is clicked
	 */

	@FXML
	void CompleteNewAbstractProduct(ActionEvent event) {
		if (!testInput())
			return;
		productMyFile = new ProductMyFile();
		String components = null;
		if (typeComboBox.getValue().equals("Product")) {
			components = getComponents(ZliClient.marketingEmployeeController.getNewProductComponents());
		}
		Product product = new Product(nameTXT.getText(), "/GuiAssests/" + selectedFile.getName(),
				colorComboBox.getValue(), categoryComboBox.getValue(), typeComboBox.getValue(), components,
				Float.valueOf(priceTXT.getText()), 0);
		if (!setImage())
			return;
		productMyFile.setProduct(product);
		ZliClientUI.chat.accept(new Message(Task.Add_New_Product_To_Catalog, productMyFile));
		String popup = "Successfully added new " + product.getType().toString() + "\nName: " + product.getName();
		if(product.getType().toString().equals("Product"))
		{
			popup += "\nComponents: " + components;
		}
		popup += "\nPrice: " + product.getPrice() + "\nColor: " + product.getColor().toString().toLowerCase() +
				"\nCategory: " + product.getCategory();
		scene.HelpMessage(popup);
		ZliClient.marketingEmployeeController.getNewProductComponents().clear(); // so next time it wont save the last															// components selected
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/MarketingEmployeeMenu.fxml", true);
	}
	/**
	 * boolean setImage()
	 * Method is called when user selecets an Image from his machine in order to submit it to the DB
	 * if it is succesfull we return True, on fail we return False
	 * @return boolean True\False depending on success\failiure
	 **/

	private boolean setImage() {
		MyFile msg = new MyFile(selectedFile.getName());
		productMyFile.setMyfile(msg);
		try {

			File newFile = new File(Path);
			byte[] mybytearray = new byte[(int) newFile.length()];
			FileInputStream fis = new FileInputStream(newFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			msg.initArray(mybytearray.length);
			msg.setSize(mybytearray.length);
			bis.read(msg.getMybytearray(), 0, mybytearray.length);
			bis.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * boolean testInput()
	 * Method is called when user clicks "Complete" button
	 * We test the user input, Name,Price,Picture path,Color, Category,Componenets etc...
	 * if all test passed we return True, else False
	 * @return boolean True\False depending on success\failiure
	 **/

	private boolean testInput() {
		if (nameTXT.getText().length() < 1) {
			errorTXT.setText("You MUST enter a name!!");
			return false;
		}
		if (!checkString(nameTXT.getText(), "^[ A-Za-z]+$")) {
			errorTXT.setText("Name can only contain Letters and Spaces!!");
			return false;
		}
		if (nameTXT.getText().length() > 20) {
			errorTXT.setText("Maximum name length is 20 (letters and spaces)");
			return false;
		}

		if (priceTXT.getText().length() < 1) {
			errorTXT.setText("You MUST enter a price!!");
			return false;
		}
		if (!checkString(priceTXT.getText(), "[0-9]+$")) {
			errorTXT.setText("Price can only contain Integer numbers!!");
			return false;
		}
		if (Integer.valueOf(priceTXT.getText()) > 150 || Integer.valueOf(priceTXT.getText()) < 5) {
			errorTXT.setText("Price must be in range of 5-150$");
			return false;
		}
		if (typeComboBox.getValue() == null) {
			errorTXT.setText("You MUST pick a type!!");
			return false;
		}
		if (typeComboBox.getValue().equals("Product")) {
			if (ZliClient.marketingEmployeeController.getNewProductComponents().size() < 2) {
				errorTXT.setText("You must pick at least 2 components to create a new Product");
				return false;
			}
		}
		if (categoryComboBox.getValue() == null) {
			errorTXT.setText("You MUST pick a category!!");
			return false;
		}
		if (colorComboBox.getValue() == null) {
			errorTXT.setText("You MUST pick a color!!");
			return false;
		}
		if (selectedFile == null) {
			errorTXT.setText("You MUST pick an Image!!");
			return false;
		}

		return true;
	}
	/**
	 * SelectComponenets(ActionEvent event)
	 * If user has Choosen "Prodcut" in the Type he must select up to 4 Components
	 * this method shows the user all of the items, and lets his choose up to 4 elements
	 * @param event User Clicked on Select Component button
	 * @return boolean True\False depending on success\failiure
	 **/

	@FXML
	void SelectComponenets(ActionEvent event) {
		// ((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary
		// window
		scene.changeScreenNotMovable(new Stage(), "/GuiClientScreens/ComponentsCatalog.fxml", true);
	}
	/**
	 * addNewPhoto(ActionEvent event)
	 * This method opens up a "File Chooser" and lets the user select a PNG File as a photo
	 * if user selected any file that is not a PNG we "return" from the function
	 * @param event User Clicked on Select Photo
	 * @return boolean True\False depending on success\failiure
	 **/

	@FXML
	void addNewPhoto(ActionEvent event) {
		FileChooser fc = new FileChooser();
		selectedFile = fc.showOpenDialog(null);
		if (selectedFile == null)
			return;
		Path = selectedFile.getAbsolutePath();
		String FileName = selectedFile.getName();
		String AfterDot[] = selectedFile.getName().split("\\.");
		if (!AfterDot[1].equals("png")) {
			errorTXT.setText("The Image you choose MUST be .png, try a diffrent one");
		} else {
			photoBtn.setText(FileName);
			errorTXT.setText("");
		}

	}
	/**
	 * checkString(String toCheck, String pattern)
	 * This functions checks if toCheck string is only made of String pattern elements
	 * if yes we return True, else False
	 * @param toCheck user passes us the string he wants checked
	 * @param pattern the pattern The user would like to be comapre in the toCheck string
	 * @return boolean True\False depending on success\failiure
	 **/

	private boolean checkString(String toCheck, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(toCheck);
		return m.matches();
	}
	/**
	 * typeSelected(ActionEvent event)
	 * This method is called when user has selected a value from Type comboBoxs
	 * @param event mouse clicked on Type ComboBox
	 **/

	@FXML
	void typeSelected(ActionEvent event) {
		if (typeComboBox.getValue().equals("Item")) {
			componentBtn.setDisable(true);
		} else {
			componentBtn.setDisable(false);
		}
	}
	/**
	 * Help(MouseEvent event)
	 * Displays a Prompt screen to user with Information regarding the current screen
	 * @param event mouse clicked on "?" icon
	 **/

	@FXML
	void Help(MouseEvent event) {
		scene.HelpMessage("*Name can only contain letter and spaces(up to 20)\n\n"
				+ "*Price can only be Integer number and in the range of 5-150$\n\n"
				+ "*If you create a new product, you MUST select at least 1 Component\n\n"
				+ "*Item cannot contain Components\n\n"
				+ "*If you create a new Item, it will not include Components even if chosen\n\n"
				+ "*You MUST choose Type Category and Color\n\n" + "*You MUST enter a Name and Price\n\n"
				+ "*You MUST select an image\n\n"
				+ "image MUST be .png");
	}
	/**
	 * Back(MouseEvent event)
	 * Returns the user the the previous screen while closing current screen
	 * @param event mouse clicked on Back Icon
	 **/

	@FXML
	void Back(MouseEvent event) {
		ZliClient.marketingEmployeeController.getNewProductComponents().clear(); // so next time it wont save the last															// components selected
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/MarketingEmployeeMenu.fxml", true);
	}
	/**
	 * X(ActionEvent event)
	 * Closes the applications and sends a Request_disconnected to server side
	 * @param event mouse clicked on Back Icon
	 **/

	@FXML
	void X(ActionEvent event) {
    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
		ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
		System.exit(0);
	}
	/**
	 * showComponenets(MouseEvent event)
	 * opens a pop-up screen that shows the user information about the components for the new product/item
	 * @param event mouse clicked on Info icon
	 **/
	@FXML
	void showComponenets(MouseEvent event) {
		if (ZliClient.marketingEmployeeController.getNewProductComponents().size() == 0)
			scene.HelpMessage("No Components Selected");

		else if (typeComboBox.getValue() == null || !typeComboBox.getValue().equals("Item")) {
			scene.HelpMessage("The Components you chose for the new Product are:\n"
					+ ZliClient.marketingEmployeeController.ComponentsString());
		} else
			scene.HelpMessage("No Components for creating an item");
	}
	/**
	 * getComponents(ArrayList<Product> components)
	 * builds string that represents the components the user chose for the new product
	 * @param event mouse clicked on Info ico
	 * @return String Represents components for product
	 **/
	private String getComponents(ArrayList<Product> components) {
		StringBuilder str = new StringBuilder();
		boolean flag = false;
		for (AbstractProduct p : components) {
			if (flag)
				str.append(",");
			else
				flag = true;
			if (p instanceof CustomProduct) {
				str.append(p.getComponents());
			} else
				str.append(p.getName());
		}
		return str.toString();
	}

}
