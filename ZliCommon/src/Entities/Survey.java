package Entities;
import java.io.Serializable;


/**
*This class is a class that stores survey info
*we use it to store the surveys that are passed from the DB to the Server and to the User
*or from the user to the DB 
* @author Shay Zak
*/

public class Survey implements Serializable {
	private static final long serialVersionUID = 1L;
	private String user,store,question1,question2,question3,question4,question5,question6,Type;
	public Survey(String user,
			String question1,
			String question2,
			String question3,
			String question4,
			String question5,
			String question6,
			String store,String Type) {
		this.user = user;
		this.question1 = question1;
		this.question2 = question2;
		this.store = store;
		this.question3 = question3;
		this.question4 = question4;
		this.question5 = question5;
		this.question6 = question6;
		this.Type = Type;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}

	public String getQuestion1() {
		return question1;
	}

	public void setQuestion1(String question1) {
		this.question1 = question1;
	}

	public String getQuestion2() {
		return question2;
	}

	public void setQuestion2(String question2) {
		this.question2 = question2;
	}

	public String getQuestion3() {
		return question3;
	}

	public void setQuestion3(String question3) {
		this.question3 = question3;
	}

	public String getQuestion4() {
		return question4;
	}

	public void setQuestion4(String question4) {
		this.question4= question4;
	}


	public String getQuestion5() {
		return question5;
	}

	public void setQuestion5(String question5) {
		this.question5= question5;
	}

	public String getQuestion6() {
		return question6;
	}

	public void setQuestion6(String question6) {
		this.question6= question6;
	}
	public String getType() {
		return Type;
	}

	public void setType(String Type) {
		this.Type= Type;
	}
}
