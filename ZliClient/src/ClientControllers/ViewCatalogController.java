package ClientControllers;
/**
 * This is the View Catalog Controller it holds all of the logic for View Catalog screen (without buying option)
 * @author Ilya
 */
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class ViewCatalogController implements Initializable {

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

	private String itemCategory, itemType;
	private List<Product> products = new ArrayList<>();
	private ChangeScene scene = new ChangeScene();
	private HashMap<Integer, Sale> salesMap;
	private ObservableList<String> categories = FXCollections.observableArrayList("Boquet", "FlowerArrangment",
			"BlossomingFlower", "FlowerPot", "All Categories");
	private ObservableList<String> type = FXCollections.observableArrayList("Product", "Item", "All Types");
	private ObservableList<String> salesComboList = FXCollections.observableArrayList("On Sale", "Not On Sale",
			"No Filter");
	private ArrayList<ViewProductController> ProductControllerArray = new ArrayList<>(); // holds controllers of all
																							// items in grid
	/**
	 * initialize()
	 * This method is called when the screen "View Catalog (No Buying option)" is calleed
	 * it initializes all of our Logic\Buttons\etc...
	 */  
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Set transparent color for the scroll pane in the Shopping cart
		CategoryComboBox.setItems(categories); // set CategoryComboBox strings
		TypeComboBox.setItems(type);// set TypeComboBox strings
		SaleComboBox.setItems(salesComboList);
		ZliClientUI.chat.accept(new Message(Task.Get_ProductCatalog_table, null));
		products = ZliClient.catalogProductController.getProductCatalog();
		ZliClientUI.chat.accept(new Message(Task.Get_ActiveSales, null));
		salesMap = ZliClient.salesController.getActiveSalesMap();
		SetGrid(null, null, 0); // no filter at all

	}
	/**
	 * Back(MouseEvent event)
	 * Returns the user the the previous screen while closing current screen
	 * @param event mouse clicked on Back Icon
	 **/
	@FXML
	void Back(MouseEvent event) {
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
			for (int i = 0; i < products.size(); i++) {
				itemCategory = products.get(i).getCategory();
				itemType = products.get(i).getType();
				// If item has passed the Filter we add it to the Catalog
				if (ApplyFilter(category, type, FilterType)) {
					FXMLLoader fxmlLoader = new FXMLLoader();
					fxmlLoader.setLocation(getClass().getResource("/GuiClientScreens/ViewProduct.fxml"));
					AnchorPane anchorPane = fxmlLoader.load();
					ViewProductController viewproductController = fxmlLoader.getController();
					ProductControllerArray.add(viewproductController);
					viewproductController.setData(products.get(i));

					if (salesMap.containsKey(products.get(i).getId()))
						viewproductController.isOnSale(salesMap.get(products.get(i).getId()));
					else
						viewproductController.notOnSale();

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
