package Enums;

/**
* represent all possible PaymentMethod
* @author Omri Cohen
*/
public enum PaymentMethod {
	Credit_Card {
	    public String toString() {
	      return "Credit_Card";
	    }
	  },
	  Store_Credit {
	    public String toString() {
	      return "Store_Credit";
	    }
	  },
	  Store_Card_Credit {
	    public String toString() {
	      return "Store_Card_Credit";
	    }
	  },
}
