package Entities;
/**
*This class is a class that stores Data base User name Password and Address
*we use it to store the Info that is passed the by server GUI
* @author Biran Fridman
*/
public class DataBase {
	private String username;
	private String password;
	private String DBAddress;
	public DataBase(String username, String password, String DBAddress) {
		this.username = username;
		this.password = password;
		this.DBAddress = DBAddress;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setDBAddress(String DBAddress) {
		this.DBAddress = DBAddress;
	}
	public String getDBAddress() {
		return DBAddress;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
