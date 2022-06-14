package Enums;
/**
*This enum class is used for the Complaint Status of the complaint that in the complaints Table
*we pass a complaint statuses to the complaints table at
*"Manage Complaints" and return a String for the select color
* @author Shay Zak
*/
public enum ComplaintStatus {
	APPROVED {
		    public String toString() {
		      return "APPROVED";
		    }
		  },
	  UNHANDLED {
		   public String toString() {
		      return "UNHANDLED";
		    }
		  }
	
}
