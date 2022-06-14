package Enums;

/**
* represent all possible tasks (in massage) that sent to server or client
* @author Omri Cohen
*/
public enum Task {
	Update_orders_table {
		public String toString() {
			return "Update orders table";
		}
	},
	Request_Login {
		public String toString() {
			return "Request Login";
		}
	},
	Request_logout {
		public String toString() {
			return "User asks to logout";
		}
	},
	Logged_out_Successful {
		public String toString() {
			return "User was logged out successfuly";
		}
	},
	Logged_Successful {
		public String toString() {
			return "Logged was successful";
		}
	},
	Logged_UnSuccessful {
		public String toString() {
			return "Logged was unsuccessful";
		}
	},
	Logged_UnSuccessful_already_logged_in {
		public String toString() {
			return "User is already logged in!";
		}
	},
	Get_ProductCatalog_table {
		public String toString() {
			return "Get ProductCatalog table";
		}
	},
	Update_surveys_table {
		public String toString() {
			return "Update surveys table";
		}

	},
	Update_complaints_table {
		public String toString() {
			return "Update complaints table";
		}
	},
	Import_order_data {
		public String toString() {
			return "Import order data";
		}
	},
	Order_data_imported {
		public String toString() {
			return "Order data imported";
		}
	},
	Import_order_data_by_store {
		public String toString() {
			return "Import order data by store";
		}
	},
	Import_orderstocancel_data_by_store {
		public String toString() {
			return "Import orderstocancel data by store";
		}
	},
	Request_to_cancel_order {
		public String toString() {
			return "Request to cancel order";
		}
	},
	Cancel_request_successfully {
		public String toString() {
			return "Cancel request successfully";
		}
	},
	Get_user_store {
		public String toString() {
			return "Get user store";
		}

	},
	Got_store_user {
		public String toString() {
			return "Got store user";
		}
	},
	Request_connect {
		public String toString() {
			return "Request connect";
		}
	},
	Request_disconnected {
		public String toString() {
			return "Request disconnected";
		}
	},
	Connected {
		public String toString() {
			return "Connected";
		}
	},
	Disconnected {
		public String toString() {
			return "Disconnected";
		}
	},
	Update_orders_successfully {
		public String toString() {
			return "Update orders successfully";
		}
	},
	Update_surveys_successfully {
		public String toString() {
			return "Update surveys successfully";
		}
	},
	Update_complaints_successfully {
		public String toString() {
			return "Update complaints successfully";
		}
	},
	Get_ItemCatalog_table {
		public String toString() {
			return "Get ItemCatalog table";
		}
	},
	Import_costumer_data_by_username {
		public String toString() {
			return "Import costumer data by username";
		}
	},
	Costumer_data_was_imported {
		public String toString() {
			return "Costumer data was imported successfully";
		}
	},
	Import_user_by_username {
		public String toString() {
			return "Import user by username";
		}
	},
	User_data_was_imported {
		public String toString() {
			return "User data was imported successfully";
		}
	},
	Update_costumer_creditcard {
		public String toString() {
			return "Update costumer creditcard";
		}
	},
	Costumer_creditcard_was_updated {
		public String toString() {
			return "Costumer creditcard was updated successfully";
		}
	},
	Import_data_costumers_NotApproved_permissions {
		public String toString() {
			return "Import data costumers NotApproved permissions";
		}
	},
	Imported_NotApproved_usernames_of_costumers {
		public String toString() {
			return "Imported NotApproved usernames of costumers successfully";
		}
	},
	Get_OrdersBy_id {
		public String toString() {
			return "Get Orders By ID successfully";
		}
	},
	OrdersBy_id_imported {
		public String toString() {
			return "Order data imported";
		}
	},
	Get_Complaints_table {
		public String toString() {
			return "Complaints data imported";
		}
	},
	Import_data_costumers_approved_and_frozen {
		public String toString() {
			return "Import data costumers approved and frozen";
		}
	},
	Costumers_approved_and_frozen_was_imported {
		public String toString() {
			return "Costumers approved and frozen was imported successfully";
		}
	},
	Update_costumer_table {
		public String toString() {
			return "Update costumer table";
		}
	},
	Update_costumer_successfully {
		public String toString() {
			return "Update costumer successfully";
		}
	},
	Import_data_storestaff_of_the_same_store {
		public String toString() {
			return "Import data storestaff of the same store";
		}
	},
	Storestaff_of_the_same_store_was_imported {
		public String toString() {
			return "Storestaff of the same store was imported successfully";
		}
	},
	Update_storestaff_table {
		public String toString() {
			return "Update storestaff table";
		}
	},
	Update_storestaff_successfully {
		public String toString() {
			return "Update storestaff successfully";
		}
	},
	Get_nextProductID {
		public String toString() {
			return "Get Next Product ID";
		}
	},
	Get_nextOrderID {
		public String toString() {
			return "Get Next Order ID";
		}
	},
	Update_Complaints_DB {
		public String toString() {
			return "Update Complaints DB";
		}
	},
	Got_price_by_order_id_for_complaint {
		public String toString() {
			return "Got price by order id for complaint";
		}
	},
	Get_price_for_comaplaint_by_order_id {
		public String toString() {
			return "Get price for comaplaint by order id";
		}
	},
	Update_Complaints_successfully {
		public String toString() {
			return " Update Complaints successfully";
		}
	},
	Insert_newDelivery {
		public String toString() {
			return "Insert New Delivery";
		}
	},
	Insert_newOrder {
		public String toString() {
			return "Insert New Order";
		}
	},
	Get_Costumer_By_User_Name {
		public String toString() {
			return "Get Costumer By User Name";
		}
	},
	Updated_Customer_Store_Credit {
		public String toString() {
			return "Updated Customer Store Credit";
		}
	}, Get_MonthsBy_Year{
		public String toString() {
			return "Months By Years asked ";
		}
	}, Get_Store_Names{
		public String toString() {
			return "Get stores by name asked";
		}
	}, MonthsBy_Year_imported{
		public String toString() {
			return "Months By Years returned";
		}
	}, Survey_Stores_Imported{
		public String toString() {
			return "Survey stores names are imported";
		}
	}, Get_Sale_Analyze{
		public String toString() {
			return "Analyze sales requsted";
		}
	}, Sale_Analyze_Imported{
		public String toString() {
			return "Sale anayzle returned";
		}	
	}, Get_Month_Year_Analyze{
		public String toString() {
			return "Requested to generate Month and Year Analyze";
		}	
	}, Year_And_Month_Analyze_Imported{
		public String toString() {
			return "generated Month and Year Analyze";
		}	
	}, Get_User_Permission{
		public String toString() {
			return "Requst to get user name permissions";
		}	
	}, User_Permission_Imported{
		public String toString() {
			return "User Permission has been imported";
		}	
	},
	Change_Order_Status_To_Arrived{
		public String toString() {
			return "Change Order Status To Arrived";
		}	
	},
	Order_Status_Changed_To_Arrived{
		public String toString() {
			return "Order Status Changed To Arrived";
		}	
	},
	Update_orders_table_to_approved{
		public String toString() {
			return "Update orders table to approved";
		}	
	},
	orders_table_to_approved_updated{
		public String toString() {
			return "orders table to approved updated";
		}	
	},
	Send_Email {
		public String toString() {
			return "Send Email";
		}
	},
	Get_monthly_incomes_report{
		public String toString() {
			return "Get monthly incomes report";
		}
	},
	Monthly_incomes_report_was_got{
		public String toString() {
			return "monthly incomes report was got";
		}
	},
	Get_quarterly_costumer_satisfaction_report{
		public String toString() {
			return "Get quarterly costumer satisfaction report";
		}
	},
	Quarterly_costumer_satisfaction_report_was_got{
		public String toString() {
			return "quarterly costumer satisfaction report was got";
		}
	},
	Get_monthly_orders_report{
		public String toString() {
			return "Get monthly orders report";
		}
	},
	Monthly_orders_report_was_got{
		public String toString() {
			return "monthly orders report was got";
		}
	},
	Get_quarterly_incomes_report{
		public String toString() {
			return "Get quarterly incomes report";
		}
	},
	Quarterly_incomes_report_was_got{
		public String toString() {
			return "Quarterly incomes report was got";
		}
	},
	Get_Customer_first_order_status
	{
		public String toString() {
			return "Get Customer first order stauts";
		}
	}
	,
	Get_Product_By_ID
	{
		public String toString() {
			return "Get Product by ID";
		}
	},
	Delete_Product_By_ID
	{
		public String toString() {
			return "Delete Product by ID";
		}
	},
	Add_New_Product_To_Catalog
	{
		public String toString() {
			return "Add New Product To Catalog";
		}
	},
	Update_Price {
		public String toString() {
			return "Update Price";
		}
	},
	Get_ActiveSales {
		public String toString() {
			return "Get Active Sales";
		}
	},
	Add_New_Sale {
		public String toString() {
			return "Add New Sale";
		}
	},
	Check_Active_Sale_By_ID {
		public String toString() {
			return "Check Active Sale By ID";
		}
	},
	Update_orders_successfully_and_Send_email{
		public String toString() {
			return "Update orders successfully and Send email";
		}
	},
	Update_orders_cancelation{
		public String toString() {
			return "Update orders cancelation";
		}
	},
	orders_successfully_canceled{
		public String toString() {
			return "orders successfully canceled";
		}
	},
	Send_PDF{
		public String toString() {
			return "Send PDF";
		}
	}
	
}
