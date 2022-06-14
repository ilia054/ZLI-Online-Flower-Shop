package ClientControllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Entities.Product;
import Enums.Task;
import client.ZliClient;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
/**
 * The class ComponentsCatalogController is the Controller of "Component Catalog" user Screen
 * @author Biran Fridman
 *
 */

public class ComponentsCatalogController {

	@FXML
	private Button CompleteProductBtn;

	@FXML
	private ScrollPane cartScrollPane;

	@FXML
	private VBox cartVBOX;

	@FXML
	private Button XBtn;

	@FXML
	private FontAwesomeIconView XIcon;

	@FXML
	private ScrollPane scroll;

	@FXML
	private GridPane grid;

	@FXML
	private FontAwesomeIconView BackBtn;

	@FXML
	private FontAwesomeIconView HelpBtn;

	private List<Product> items = new ArrayList<>();
	private ChangeScene scene = new ChangeScene();
	private int itemInCartCnt = 0; // amount of items in cart
	private ArrayList<ComponentInCartController> ItemInCartControllerArray = new ArrayList<>(); // holds controllers of all
	ArrayList<Product> components = new ArrayList<>();


	/**
	 * initialize()
	 * This method is called when the screen "Create new Prodcut\Item" is calleed
	 * it initializes all of our logic,buttons,combboxes etc...
	 */
	@FXML
	void initialize() {
		cartScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
		ZliClientUI.chat.accept(new Message(Task.Get_ProductCatalog_table, null));
		items = ZliClient.catalogProductController.getProductCatalog(); // get the AbstractCatalog
		SetGrid();
		ComponentController.setInstance(this);
		CompleteProductBtn.setDisable(true);
	}
	/**
	 * CompleteProduct(ActionEvent event)
	 * This method is called when the CompleteProductBtn is pressed
	 * sets the components selected by user in the marketingEmployeeController and closes the screen
	 * @param event
	 */
	@FXML
	void CompleteProduct(ActionEvent event) {
		if(ItemInCartControllerArray.size() < 2)
		{
			scene.HelpMessage("You MUST choose at least 2 components in order to continue");
			return;
		}
		for (ComponentInCartController Element : ItemInCartControllerArray) { 
					components.add(Element.getComponent());
			}
		ZliClient.marketingEmployeeController.setNewProductComponents(components);
		scene.HelpMessage("Components for product saved successfully\nComponents Chosen: " + ZliClient.marketingEmployeeController.ComponentsString());
		clearBeforeExit();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		}
	/**
	 * Back(MouseEvent event)
	 * Returns the user the the previous screen while closing current screen
	 * @param event mouse clicked on Back Icon
	 **/
	@FXML
	void Back(MouseEvent event) {
		clearBeforeExit();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
	}
	/**
	 * clickedX(ActionEvent event)
	 * Closes the applications and sends a Request_disconnected to server side
	 * @param event mouse clicked on Back Icon
	 **/

	@FXML
	void clickedX(ActionEvent event) {
		clearBeforeExit();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
	}
	/**
	 * SetGrid()
	 * sets all components in the catalog on the greed, loads all the fxml's files of all components
	 **/
	private void SetGrid() // setup catalog
	{
		int column = 0;
		int row = 1;
		try {
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).getType().equals("Item")) {
					FXMLLoader fxmlLoader = new FXMLLoader();
					fxmlLoader.setLocation(getClass().getResource("/GuiClientScreens/Component.fxml"));
					AnchorPane anchorPane = fxmlLoader.load();
					ComponentController componentController = fxmlLoader.getController();
					componentController.setData(items.get(i));
					grid.add(anchorPane, column, row++);// (child column,row)
					// Set grid width
					grid.setMinWidth(Region.USE_COMPUTED_SIZE);
					grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
					grid.setMaxWidth(Region.USE_COMPUTED_SIZE);
					// Set grid height
					grid.setMinHeight(Region.USE_COMPUTED_SIZE);
					grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
					grid.setMaxHeight(Region.USE_COMPUTED_SIZE);
					GridPane.setMargin(anchorPane, new Insets(5));
				}
			}
		} catch (

		IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * addToCart(Product item)
	 * if the cart doesn't contain 4 items and doesn't contain the item in the cart, it adds it to the cart
	 * @param item The component added to the cart
	 **/
	public void addToCart(Product item) { // add item to cart
		if(ItemInCartControllerArray.size() == 3)
		{
			scene.ErrorMessage("You cannot pick more than 3 components\n");
			return;
		}
		for (ComponentInCartController Element : ItemInCartControllerArray) { // if item already exists in cart, give client message return
			if (Element.getItemID() == item.getId()) {
				scene.ErrorMessage(item.getName() + " is already in your cart");
				return;
			}
		}
		// Element was not found in our "Cart" we will added it to the
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("/GuiClientScreens/ComponentInCart.fxml"));
		try {
			cartVBOX.getChildren().add(fxmlLoader.load());
			ItemInCartControllerArray.add(fxmlLoader.getController());
			ItemInCartControllerArray.get(itemInCartCnt).setData(item, itemInCartCnt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// If we added the first item in the cart we will allow the user to "Complete"
		// the Custom Product
		if (itemInCartCnt == 0)
			CompleteProductBtn.setDisable(false); // Set Complete Product button to click able
		itemInCartCnt++;
	}
	/**
	 * removeFromCart(int index)
	 * removes the item that is in index from the cart
	 * @param index the index of the item in the cart to be removed
	 **/
	public void removeFromCart(int index) { // each time an items is removed from cart, decrease index of all items that
		// have higher index.
		// if cart is empty enable all categories and show all item catalog
		cartVBOX.getChildren().remove(index);
		ItemInCartControllerArray.remove(index);
		for (int i = index; i < ItemInCartControllerArray.size(); i++)// Looping through Controller array and decreasing index in VBOX
			ItemInCartControllerArray.get(i).decIndex();
		itemInCartCnt--; // Deduct 1 from our cart counter
		if (itemInCartCnt == 0) // count of items in cart is 0 set the "Complete Custom Product" to disabled
			CompleteProductBtn.setDisable(true);
	}
	/**
	 * clearBeforeExit()
	 * clears resets neccessary variables before exiting the screen
	 **/
	public void clearBeforeExit() {
		cartVBOX.getChildren().clear();
		itemInCartCnt = 0;
		ComponentController.deleteInstance();
	}


}
