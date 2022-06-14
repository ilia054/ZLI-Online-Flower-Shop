package Enums;
/**
*This enum class is used for the Complaint Refund amount of the complaint that in the complaints Table
*we pass a complaint Refund cases to the complaints table at
*"Manage Complaints" and return a String for the select color
* @author Shay Zak
*/
public enum Refund {
	  NO_REFUND {
	    public String toString() {
	      return "NO_REFUND";
	    }
	  },
	  QUARTER_REFUND{
	    public String toString() {
	      return "QUARTER_REFUND";
	    }
	  },
	  HALF_REFUND {
	    public String toString() {
	      return "HALF_REFUND";
	    }
	  },
	  FULL_REFUND {
	    public String toString() {
	      return "FULL_REFUND";
	    }
	  }

	  
}