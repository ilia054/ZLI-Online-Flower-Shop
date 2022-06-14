package EntityControllers;

import Entities.User;

/**
* The class UserController represent the data structure that save users that imported from data base.
* @author Omri Cohen
*/
public class UserController {
	private User user = null;
	//Constructors
	public UserController() {}
	public UserController(User givenUser) {user = givenUser;}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public boolean isExist() {
		return !(user == null);
	}
	
}
