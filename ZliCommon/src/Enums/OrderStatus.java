package Enums;
/**
*This enum class is used for the Colors of the items that in the Orders
*we pass a Colors to the Order and return a String for the select color
* @author Shay Zak
*/
public enum OrderStatus {
		  CANCELED {
		    public String toString() {
		      return "CANCELED";
		    }
		  },
		  REQUEST_TO_CANCELED {
		    public String toString() {
		      return "REQUEST_TO_CANCELED";
		    }
		  },
		  APPROVED {
		    public String toString() {
		      return "APPROVED";
		    }
		  },
		  NOT_APPROVED {
		    public String toString() {
		      return "NOT_APPROVED";
		    }
		  },
		  ARRIVED {
			    public String toString() {
			      return "ARRIVED";
			    }
			  }
}