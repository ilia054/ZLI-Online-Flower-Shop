package ClientControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import Entities.Product;
import Entities.Sale;
import Enums.Task;
import client.ZliClient;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
 * This is t he Item Catalog Controller, it holds all of the logic for this screen.
 * @author Ilya
 *
 */
public class ItemCatalogController implements Initializable {

	@FXML
	private ScrollPane cartScrollPane;

	@FXML
	private Button CompleteProductBtn;

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
	private HBox CategoryHBOX;

	@FXML
	private ComboBox<String> CategoryComboBox;

	@FXML
	private ComboBox<String> TypeComboBox;

	@FXML
	private ComboBox<String> SaleComboBox;

	@FXML
	private FontAwesomeIconView HelpBtn;

	private String itemCategory, itemType;
	private HashMap<Integer, Sale> salesMap;

	private ChangeScene scene = new ChangeScene();
	private ObservableList<String> categories = FXCollections.observableArrayList("Boquet", "FlowerArrangment",
			"BlossomingFlower", "FlowerPot", "All Categories");
	private ObservableList<String> type = FXCollections.observableArrayList("Product", "Item", "All Types");
	private ObservableList<String> salesComboList = FXCollections.observableArrayList("On Sale", "Not On Sale",
			"No Filter");
	private List<Product> items = new ArrayList<>();
	private ArrayList<ItemInCartController> ItemInCartControllerArray = new ArrayList<>(); // holds controllers of all
																							// items in cart
	private ArrayList<ItemController> ItemControllerArray = new ArrayList<>(); // holds controllers of all items in grid
	private int itemInCartCnt = 0; // amount of items in cart
	/**
	 * initialize()
	 * This method is called when the screen "Item Catalog" is calleed
	 * it initializes all of our Logic\Buttons\etc...
	 */ 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cartScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
		CategoryComboBox.setItems(categories); // set CategoryComboBox strings
		TypeComboBox.setItems(type);// set TypeComboBox strings
		SaleComboBox.setItems(salesComboList);
		items = ZliClient.catalogProductController.getProductCatalog(); // get the AbstractCatalog
		ZliClientUI.chat.accept(new Message(Task.Get_ActiveSales, null));
		salesMap = ZliClient.salesController.getActiveSalesMap();
		SetGrid(null, null, 0); // no filter at all
		ItemController.setInstance(this);
		CompleteProductBtn.setDisable(true);
	}

	/**
	 * ApplyFilter(String category, String type, int filterType)
	 * This function applies the filter on the "Catalog" by returning whether toAdd
	 * or !toAdd a certain AbstractProduct based on the users filtering options, if
	 * True we add the item if False we don't we receive 3 parameters, String
	 * category, String Type, Integer FilterType we check the cases on the Switch
	 * with Filter type, and then do some logic checking case 0: no filter case 1:
	 * only Category filter case 2: only Type filter case 3: both Category and Type
	 * filter 
	 * @param category , Cateogry user has selected ComboBox
	 * @param type, Type user has selected from ComboBox
	 * @param filterType, Case to filter by
	 **/
	private boolean ApplyFilter(String category, String type, int filterType) {
		boolean toAdd = true;
		switch (filterType) {
		// Case 0 is initiation, we want to display the WHOLE catalog to the user, so we
		// just add all the items
		case 0:
			toAdd = true;
			break;
		// Case 1 User has not selected Type but selected a Category, we filter by
		// Category unless the category is "All Categories"
		case 1:
			if (category.equals("All Categories"))
				toAdd = true;
			else if (!itemCategory.equals(category))
				toAdd = false;
			else
				toAdd = true;
			break;
		// Case 2 User has not selected Category but selected a type, we filter by type
		// unless the type is "All Types"
		case 2:
			if (type.equals("All Types"))
				toAdd = true;
			else if (!itemType.equals(type))
				toAdd = false;
			else
				toAdd = true;
			break;
		// Case 3 User Has selected both Category and Type, we filter by his demands
		case 3:
			if ((itemCategory.equals(category) || category.equals("All Categories"))
					&& (itemType.equals(type) || type.equals("All Types")))
				toAdd = true;
			else
				toAdd = false;
			break;

		}
		return toAdd;
	}

	/**
	 * SetGrid(String category, String type, int FilterType)
	 * This function displays the Items\Products in our catalog, it also uses the
	 * Filter() method we will only display items the that the user has "Filtered",
	 * we add children to our GridPane, the "Children" are "Item.FXML" and they have
	 * ItemController, for each AbstractProduct (Product,Item) item\product in his
	 * cart
	 * @param category, Category user selected user selected from ComboBox
	 * @param type, Type user selected from ComboBox
	 * @param FilterType, the case we want to filter by
	 **/
	private void SetGrid(String category, String type, int FilterType) // setup catalog
	{
		int column = 0;
		int row = 1;
		try {
			for (int i = 0; i < items.size(); i++) {
				itemCategory = items.get(i).getCategory();
				itemType = items.get(i).getType();
				// If item has passed the Filter we add it to the Catalog
				if (ApplyFilter(category, type, FilterType)) {
					FXMLLoader fxmlLoader = new FXMLLoader();
					fxmlLoader.setLocation(getClass().getResource("/GuiClientScreens/Item.fxml"));
					AnchorPane anchorPane = fxmlLoader.load();
					ItemController itemController = fxmlLoader.getController();
					ItemControllerArray.add(itemController);
					itemController.setData(items.get(i));

					if (salesMap.containsKey(items.get(i).getId()))
						itemController.isOnSale(salesMap.get(items.get(i).getId()));
					else
						itemController.notOnSale();

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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * addToCart(Product item)
	 * This function add's an item to the cart, if the item is already selected we
	 * will pop up A error window , letting the user know he already has the
	 * item\product in his cart
	 * @param item the item to add to the cart (which the user selected)
	 **/
	public void addToCart(Product item) { // add item to cart
		for (ItemInCartController Element : ItemInCartControllerArray) { // if item already exists in cart, add one to
																			// amount and
			// return
			if (Element.getItemID() == item.getId()) {
				scene.ErrorMessage(item.getName() + " is already in your cart");
				return;
			}
		}
		// Element was not found in our "Cart" we will added it to the
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("/GuiClientScreens/ItemInCart.fxml"));
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
	 * filter()
	 * This function Filters both by Category and by Type, it is attached to the two
	 * ComboBoxes we have 3 Conditions case 1: we only filtered by Category case 2:
	 * we only filtered by Type case 3: we filtered by both options So we use a
	 * switch case in the SetGrid method in order to check which case is currently
	 **/
	@FXML
	private void filter() // function to filter items shown in catalog by category
	{
		grid.getChildren().clear(); // filter grid to show only item category
		ItemControllerArray.clear();
		String category, type, sale;
		category = CategoryComboBox.getValue();
		type = TypeComboBox.getValue();
		sale = SaleComboBox.getValue();
		// If user has only selected Category from ComboBox
		if (type == null && category != null) {
			SetGrid(category, null, 1);
		}
		// If user only selected Type from ComboBox
		else if (type != null && category == null) {
			SetGrid(null, type, 2);
		}
		// User has selected Both Type and Category from Combo box
		else if (type != null && category != null) {
			SetGrid(category, type, 3);
		} else
			SetGrid(null, null, 0); // no filter at all

		if (sale != null)
			filterSale(sale);

	}
	/**
	 * filterSale(String filterType)
	 * This function "filters" the item by the Sale ComboBox value selected,"On Sale", "Not On Sale","No Filter"
	 * @param filterType string to filter by
	 **/
	private void filterSale(String filterType) {
		if (filterType.equals("Not On Sale")) {
		}
		for (int i = 0; i < ItemControllerArray.size(); i++) {
			if ((filterType.equals("On Sale") && ItemControllerArray.get(i).getSale() == null)
					|| filterType.equals("Not On Sale") && ItemControllerArray.get(i).getSale() != null) {
				grid.getChildren().remove(i);
				ItemControllerArray.remove(i);
				i--;
			}
		}
	}

	/**
	 * removeFromCart(int index)
	 * This function removes and item from the cart, we iterate over the CarVBOX
	 * "Cart" to remove the element and deduct 1 from every element in the VBOX so
	 * that we can add new Items\Product in the correct index
	 * @param index The index of the item in cart
	 **/
	public void removeFromCart(int index) { // each time an items is removed from cart, decrease index of all items that
											// have higher index.
											// if cart is empty enable all categories and show all item catalog
		cartVBOX.getChildren().remove(index);
		ItemInCartControllerArray.remove(index);
		for (int i = index; i < ItemInCartControllerArray.size(); i++)// Looping through Controller array and decreasing
																		// index in
			// VBOX
			ItemInCartControllerArray.get(i).decIndex();
		itemInCartCnt--; // Deduct 1 from our cart counter
		if (itemInCartCnt == 0) // count of items in cart is 0 set the "Complete Custom Product" to disabled
			CompleteProductBtn.setDisable(true);
	}

	/**
	 * Help(MouseEvent event)
	 * Displays a Prompt screen to user with Information regarding the current screen
	 * @param event mouse clicked on "?" icon
	 **/
	@FXML
	void Help(MouseEvent event) {
		scene.HelpMessage("*Use the The Category Filter to filter Items by Category!"
				+ "\n\nYou can Filter Product type by Complete Product Or Item!"
				+ "\n\nYou can Use the Sale Filter to see which items are on sale/not on sale!"
				+ "\n\nYou must choose at least ONE product in order to create a Custom Product!");
	}

	/**
	 * CompleteProduct(ActionEvent event)
	 * This function Completes the users "Custom Item" and opens the
	 * "FinishCreatingCustomItem" window we Give the User an option from one of the
	 * Colors of the items he is selected we also Pass data to the
	 * FinishCreatingCustomItem Controller such as
	 * [Colors,Category,Components,screen] "this" in line 300 refers to the instance
	 * of itemCatalogController so we can go back to it and call "hide" method
	 **/
	@FXML
	void CompleteProduct(ActionEvent event) {
		double price = 0;
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("/GuiClientScreens/FinishCreatingCustomItem.fxml"));
		ObservableList<String> colors = FXCollections.observableArrayList();
		String componenets = "";
		// Iterating over the AbstractProducts selected and Building "components" string
		// of them
		for (ItemInCartController Element : ItemInCartControllerArray) {
			if (ItemInCartControllerArray.indexOf(Element) == 0)
				componenets += Element.getItemName();
			else
				componenets = componenets + "," + Element.getItemName();
			if (colors.contains(Element.getItemColor()))
				continue;
			colors.add(Element.getItemColor());
			price += Element.getItemPrice();
		}
		Parent root = null;
		try {
			root = (Parent) fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FinishCreatingCustomItemController finishCreatingCustomItemController = fxmlLoader.getController();
		finishCreatingCustomItemController.setData(colors, componenets, price);
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		scene.makeUndecoratedScreenMovable(root, stage);
		clearBeforeExit();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		stage.show();
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
		scene.changeScreen(new Stage(), "/GuiClientScreens/ProductCatalog.fxml", true);
	}
	/**
	 * X(ActionEvent event)
	 * Closes the applications and sends a Request_disconnected to server side
	 * @param event mouse clicked on Back Icon
	 **/
	@FXML
	void clickedX(MouseEvent event) {
    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
		ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
		System.exit(0);
	}
	/**
	 * clearBeforeExit()
	 * This method is used to ensure that we "wipe" all of our data that we can reuse the screen again
	 **/
	public void clearBeforeExit() {
		cartVBOX.getChildren().clear();
		// items.clear();
		itemInCartCnt = 0;
		ItemController.deleteInstance();
	}

}
