package Enums;

/**
* represent all possible type of user accounts
* @author Omri Cohen
*/
public enum UserType {
	CEO{
		public String toString() {
			//the string will be the exact name of the FXML we would like to show
			return "CEOmenu.fxml";
		}
	},
	
	Costumer_Service_Employee{
		public String toString() {
			//the string will be the exact name of the FXML we would like to show
			return "CostumerServiceEmployeeMenu.fxml";
		}
	},
	
	Costumer_Service_Expert{
		public String toString() {
			//the string will be the exact name of the FXML we would like to show
			return "CostumerServiceExpertMenu.fxml";
		}
	},
	
	Costumer{
		public String toString() {
			//the string will be the exact name of the FXML we would like to show
			return "CostumerMenu.fxml";
		}
	},
	
	Marketing_Employee{
		public String toString() {
			//the string will be the exact name of the FXML we would like to show
			return "MarketingEmployeeMenu.fxml";
		}
	},
	
	Store_Manager{
		public String toString() {
			//the string will be the exact name of the FXML we would like to show
			return "StoreManagerMenu.fxml";
		}
	},
	Store_Employee{
		public String toString() {
			//the string will be the exact name of the FXML we would like to show
			return "StoreEmployeeMenu.fxml";
		}
	},
	Delivery_Man{
		public String toString() {
			//the string will be the exact name of the FXML we would like to show
			return "DeliveryManMenu.fxml";
		}
	}
}
