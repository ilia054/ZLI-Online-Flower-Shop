package Entities;

import java.io.Serializable;
import java.util.Objects;

import Enums.UserType;

/**
 * This class is stores data of User that imported or will be saved in database 
 */
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private UserType role;
	private String email;
	private String phoneNumber;
	private String store;
	private boolean isLoggedIn;
	private String id;

	public User(String username, String password, String firstName, String lastName, UserType role, String email, String phoneNumber, boolean isLoggedIn, String id) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.isLoggedIn = isLoggedIn;
		this.id = id;
	}
	
	public User(User user) {
		this.username = user.username;
		this.password = user.password;
		this.firstName = user.firstName;
		this.lastName = user.lastName;
		this.email = user.email;
		this.phoneNumber = user.phoneNumber;
		this.role = user.role;
		this.isLoggedIn = user.isLoggedIn;
		this.id = user.id;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public UserType getRole() {
		return role;
	}
	public void setRole(UserType role) {
		this.role = role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public boolean getLoginStatus() {
		return isLoggedIn;
	}
	public void setLoginStatus(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public String getId() {
		return id;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setStore(String store) {
		this.store = store;
		
	}
	public String getStore() {
		return store;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, firstName, id, isLoggedIn, lastName, password, phoneNumber, role, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(id, other.id) && isLoggedIn == other.isLoggedIn
				&& Objects.equals(lastName, other.lastName) && Objects.equals(password, other.password)
				&& Objects.equals(phoneNumber, other.phoneNumber) && role == other.role
				&& Objects.equals(username, other.username);
	}
	
	
}