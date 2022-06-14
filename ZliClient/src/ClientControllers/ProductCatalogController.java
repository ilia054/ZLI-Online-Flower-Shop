package ClientControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import Entities.AbstractProduct;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * This class is the Product Catalog controller, it holds all of the logic behind the Product Catalog screen
 * and order proccess
 * @author Ilya
 *
 */
public class ProductCatalogController implements Initializable {

	@FXML
	private FontAwesomeIconView iconTest;

	@FXML
	private FontAwesomeIconView iconTest1;

	@FXML
	private ScrollPane scroll;

	@FXML
	private ComboBox<String> CategoryComboBox;

	@FXML
	private ComboBox<String> TypeComboBox;

	@FXML
	private ComboBox<String> SaleComboBox;

	@FXML
	private ScrollPane cartScrollPane;

	@FXML
	private VBox cartVBOX;

	@FXML
	private GridPane grid;

	@FXML
	private Button CompleteOrderBtn;

	@FXML
	private Label orderTotalTXT;

	private String itemCategory, itemType;
	private float totalprice;
	private List<Product> products = new ArrayList<>();
	private int productInCartCnt = 0;
	ArrayList<ProductInCartController> List = new ArrayList<>();
	private ChangeScene scene = new ChangeScene();
	private HashMap<Integer, Sale> salesMap;
	private ObservableList<String> categories = FXCollections.observableArrayList("Boquet", "FlowerArrangment",
			"BlossomingFlower", "FlowerPot", "All Categories");
	private ObservableList<String> type = FXCollections.observableArrayList("Product", "Item", "All Types");
	private ObservableList<String> salesComboList = FXCollections.observableArrayList("On Sale", "Not On Sale",
			"No Filter");
	private ArrayList<ProductController> ProductControllerArray = new ArrayList<>(); // holds controllers of all items
																						// in grid
	/**
	 * initialize()
	 * This method is called when the screen "Create new order" is clicked in menu costumer menu screen
	 * it initializes all of our Logic\Buttons\etc...
	 */ 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Set transparent color for the scroll pane in the Shopping cart
		cartScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
		CategoryComboBox.setItems(categories); // set CategoryComboBox strings
		TypeComboBox.setItems(type);// set TypeComboBox strings
		SaleComboBox.setItems(salesComboList);// set SaleComboBox strings
		ZliClientUI.chat.accept(new Message(Task.Get_ProductCatalog_table, null));
		products = ZliClient.catalogProductController.getProductCatalog();
		ZliClientUI.chat.accept(new Message(Task.Get_ActiveSales, null));
		salesMap = ZliClient.salesController.getActiveSalesMap();
		SetGrid(null, null, 0); // no filter at all
		ProductController.setInstance(this);
		ObservableList<AbstractProduct> cart = ZliClient.cartController.getCart();
		if (cart.size() != 0) {
			for (int i = 0; i < cart.size(); i++)
				addToCart(cart.get(i), false);
		} else
			CompleteOrderBtn.setDisable(true);

	}

	/**
	 * SetGrid(String category, String type, int FilterType)
	 * This function displays the Items\Products in our catalog, it also uses the
	 * Filter() method we will only display items the that the user has "Filtered",
	 * we add children to our GridPane, the "Children" are "Item.FXML" and they have
	 * ItemController, for each AbstractProduct (Product,Item) item\product in his
	 * cart
	 * @param category
	 * @param type
	 * @param FilterType
	 **/
	private void SetGrid(String category, String type, int FilterType) // setup catalog
	{
		int column = 0;
		int row = 1;
		try {
			for (int i = 0; i < products.size(); i++) {
				itemCategory = products.get(i).getCategory();
				itemType = products.get(i).getType();
				// If item has passed the Filter we add it to the Catalog
				if (ApplyFilter(category, type, FilterType)) {
					FXMLLoader fxmlLoader = new FXMLLoader();
					fxmlLoader.setLocation(getClass().getResource("/GuiClientScreens/Product.fxml"));
					AnchorPane anchorPane = fxmlLoader.load();
					ProductController productController = fxmlLoader.getController();
					ProductControllerArray.add(productController);
					productController.setData(products.get(i));

					if (salesMap.containsKey(products.get(i).getId()))
						productController.isOnSale(salesMap.get(products.get(i).getId()));
					else
						productController.notOnSale();

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
	 * addToCart(AbstractProduct product, boolean addToCart)
	 * Method is called when user clicks "add to cart" or "+" in the cart
	 * we pass the product we want to add to the cart, and a boolean to indicate wether to add it to CartController or not
	 * @param product the product to add to the cart
	 * @param addToCart indicates if to add the product to the cart in cartController or not
	 */
	public void addToCart(AbstractProduct product, boolean addToCart) {
		if (List.size() == 0)
			CompleteOrderBtn.setDisable(false);

		for (ProductInCartController Element : List) { // if product already exists in cart, add one to amount and //
														// return
			if (Element.getProductID() == product.getId()) {
				Element.incrementProductAmount(addToCart);
				return;
			}
		}
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("/GuiClientScreens/ProductInCart.fxml"));
		try {
			cartVBOX.getChildren().add(fxmlLoader.load());
			List.add(fxmlLoader.getController());
			List.get(productInCartCnt).setData(product, productInCartCnt++);
		} catch (IOException e) {
			e.printStackTrace();
		}
		updateOrderTotal();
		if (addToCart)
			ZliClient.cartController.addToCart(product);
	}
	/**
	 * removeFromCart(int index)
	 * we remove the Prodcut\Custom Prodcut\Item that is in Index (from the cart)
	 * @param index index of the product to remove in the hbox of the cart
	 */ 
	public void removeFromCart(int index) {
		cartVBOX.getChildren().remove(index); // remove from vbox
		for (int i = index + 1; i < List.size(); i++)// dec all index's for products in vbox that are beneath the item
														// removed
			List.get(i).decIndex();
		List.remove(index);
		productInCartCnt--;
		if (List.size() == 0)
			CompleteOrderBtn.setDisable(true);
		updateOrderTotal();

	}
	/**
	 * updateOrderTotal()
	 * We update the cart order total by iterating over the cart and summing all of our products
	 */ 
	public void updateOrderTotal() {
		float totalPrice = 0;
		for (ProductInCartController tmp : List)
			totalPrice += tmp.getTotalPrice();
		totalprice = (float) Math.round(totalPrice);
		String formatString = String.format("%.02f", totalprice);
		orderTotalTXT.setText("Order Total: " + "$" + formatString);
	}
	/**
	 * CreateCustomProduct(ActionEvent event)
	 * This method is called when user clicks "Create Custom Product"
	 * We "switch" screen to "Custom Product Catalog" and call clearBeforeExit(boolean false)
	 * @param event
	 */ 
	@FXML
	void CreateCustomProduct(ActionEvent event) {
		clearBeforeExit(false);
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/ItemCatalog.fxml", true);
	}
	/**
	 * CompleteOrder(ActionEvent event)
	 * This method is called when user clicks "Complete order"
	 * we calculate the order total, set the correct items\producst in cart and change screen
	 * @param event
	 */ 
	@FXML
	void CompleteOrder(ActionEvent event) {

		ZliClient.cartController.setOrderTotalPrice(totalprice);
		; // set order price in cart controller
		if (ZliClient.cartController.getInstance() == null)
			ZliClient.cartController.setInstance(this);
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/GreetingCardController.fxml", true);

	}

	@FXML
	void Back(MouseEvent event) {
		clearBeforeExit(true);
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/CostumerMenu.fxml", true);
	}
	/**
	 * clickedX(MouseEvent event)
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
	 * This method is used to ensure that we "wipe" all of our data that we can resue the screen again
	 * @param deleteFromMyCart if true delete from cart else dont delete
	 **/
	public void clearBeforeExit(boolean deleteFromMyCart) {
		if (deleteFromMyCart) {
			ZliClient.cartController.getCart().clear();
		} else {
			ObservableList<AbstractProduct> cart = ZliClient.cartController.getCart();

			for (int i = 0; i < List.size(); i++) {
				if (!cart.contains(List.get(i).getProduct())) {
					for (int j = 0; j < List.get(i).getAmountInCart(); j++)
						cart.add(List.get(i).getProduct());
				}
			}
			ZliClient.cartController.setCart(cart);
		}
		cartVBOX.getChildren().clear();
		List.clear();
		// products.clear();
		productInCartCnt = 0;
		ProductController.deleteInstance();
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
	 * @param category user category choosen
	 * @param type user type choosen
	 * @param filterType, case to filter by
	 * @return boolean true if to add the AbstractProdcut else false
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
		ProductControllerArray.clear();
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
		for (int i = 0; i < ProductControllerArray.size(); i++) {
			if ((filterType.equals("On Sale") && ProductControllerArray.get(i).getSale() == null)
					|| filterType.equals("Not On Sale") && ProductControllerArray.get(i).getSale() != null) {
				grid.getChildren().remove(i);
				ProductControllerArray.remove(i);
				i--;
			}
		}
	}

}
