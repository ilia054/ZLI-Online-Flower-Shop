package Enums;

/**
* represent all possible status of store staff permissions
* @author Omri Cohen
*/
public enum StorestaffPermissions {
	REGULAR {
		public String toString() {
			return "REGULAR";
		}
	},
	INPUT_SURVEY {
		public String toString() {
			return "INPUT_SURVEY";
		}
	}
}
