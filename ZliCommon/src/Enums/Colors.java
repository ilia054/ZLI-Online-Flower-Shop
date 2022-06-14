package Enums;
/**
*This enum class is used for the Colors of the items that in the Orders
*we pass a Colors to the Order and return a String for the select color
* @author Shay Zak
*/
public enum Colors {
		  RED {
		    public String toString() {
		      return "RED";
		    }
		  },
		  GREEN {
		    public String toString() {
		      return "GREEN";
		    }
		  },
		  BLUE {
		    public String toString() {
		      return "BLUE";
		    }
		  },
		  PURPLE {
		    public String toString() {
		      return "PURPLE";
		    }
		  },
		  YELLOW
		  {
			  public String toString()
			  {
				  return "YELLOW";
			  }
		  },PINK
		  {
			  public String toString()
			  {
				  return "PINK";
			  }
		  },
		  MIX
		  {
			  public String toString()
			  {
				  return "MIX";
			  }
		  },
		  ORANGE
		  {
			  public String toString()
			  {
				return "ORANGE";
			  }
		  },
		  WHITE
		  {
			  public String toString()
			  {
				return "WHITE";
			  }
		  }
}