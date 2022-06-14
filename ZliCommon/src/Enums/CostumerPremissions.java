package Enums;

/**
* represent all possible status of costumer account permissions
* @author Omri Cohen
*/
public enum CostumerPremissions {
	FROZEN {
		public String toString() {
			return "FROZEN";
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
	}
}
