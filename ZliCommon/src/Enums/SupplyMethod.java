package Enums;

/**
* represent all possible SupplyMethod
* @author Omri Cohen
*/
public enum SupplyMethod {
	Store_PickUp {
	    public String toString() {
	      return "Store_PickUp";
	    }
	  },
	  Standard_Delivery {
	    public String toString() {
	      return "Standard_Delivery";
	    }
	  },
	  Priority_Delivery {
	    public String toString() {
	      return "Priority_Delivery";
	    }
	  },
}
