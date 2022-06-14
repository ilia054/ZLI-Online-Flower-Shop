package ClientControllersTest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ClientControllers.LoginZLIController;
import Entities.User;
import EntityControllers.UserController;
import Enums.UserType;
import javafx.event.ActionEvent;

class LoginZLIControllerTest {
	private LoginZLIController loginZLIController;
	private ActionEvent event;
	private Method loginTestMethod;
	
	/** 
	 * We use loginZLIController as mock to do nothing with methods related to javaFX function that used in login method.
	 */
	
	@BeforeEach
	void setUp() throws Exception {
		loginTestMethod = LoginZLIController.class.getDeclaredMethod("privateLogin", ActionEvent.class);
		loginTestMethod.setAccessible(true);
		loginZLIController = Mockito.mock(LoginZLIController.class);
	}

	/**
	 * Description: checking that the method step into the correct "if" statment for NotExistUser.
	 * Input data: event.
	 * Expected result: String "UserNotExists" and verify that setErrorTxtUserNotExists called once.
	 */  
	@Test
	void loginTesjtForNotExistUser() throws Exception {
		doNothing().when(loginZLIController).setErrorTxtUserNotExists();
		doNothing().when(loginZLIController).userLoginAddAndSet();
		loginZLIController.userController = new UserController();
		String result = (String)loginTestMethod.invoke(loginZLIController, event);
		String expected = "UserNotExists"; 
		verify(loginZLIController, atLeastOnce()).setErrorTxtUserNotExists();
		assertEquals(expected, result);
	}
	
	/**
	 * Description: checking that the method step into the correct "if" statment for NotExistUser.
	 * Input data: event.
	 * Expected result: String "AlreadyLoggedIN" and verify that setErrorTxtAlreadyLoggedIN called once.
	 */  
	@Test
	void loginTestForAlreadyLoggedINUser() throws Exception {
		doNothing().when(loginZLIController).setErrorTxtAlreadyLoggedIN();
		doNothing().when(loginZLIController).userLoginAddAndSet();
		loginZLIController.userController = new UserController();
		loginZLIController.userController.setUser(new User("costumer6", "123456", "Prim", "Brand", UserType.Costumer, "zlibraude@gmail.com", "0523215682", true, "7"));
		String result = (String)loginTestMethod.invoke(loginZLIController, event);
		String expected = "AlreadyLoggedIN"; 
		verify(loginZLIController, atLeastOnce()).setErrorTxtAlreadyLoggedIN();
		assertEquals(expected, result);
	}
	
	/**
	 * Description: checking that the method step into the correct "if" statment for Exist CEO User.
	 * Input data: event.
	 * Expected result: String "showRelevanceGui" + UserType and verify that showRelevanceGui called once.
	 */  
	@Test
	void loginTestForExistCEOUser() throws Exception {
		doNothing().when(loginZLIController).showRelevanceGui(event, UserType.CEO.toString());
		doNothing().when(loginZLIController).userLoginAddAndSet();
		loginZLIController.userController = new UserController();
		loginZLIController.userController.setUser(new User("ceo", "123456", "Eliav", "Shabat", UserType.CEO, "zlibraude@gmail.com", "0543320955", false, "1"));
		String result = (String)loginTestMethod.invoke(loginZLIController, event);
		String expected = "showRelevanceGui " + UserType.CEO.toString(); 
		verify(loginZLIController, atLeastOnce()).showRelevanceGui(event, UserType.CEO.toString());
		assertEquals(expected, result);
	}
	
	/**
	 * Description: checking that the method step into the correct "if" statment for Exist Costumer User.
	 * Input data: event.
	 * Expected result: String "showRelevanceGui" + UserType and verify that showRelevanceGui called once.
	 */  
	@Test
	void loginTestForExistCostumerUser() throws Exception {
		doNothing().when(loginZLIController).showRelevanceGui(event, UserType.Costumer.toString());
		doNothing().when(loginZLIController).userLoginAddAndSet();
		loginZLIController.userController = new UserController();
		loginZLIController.userController.setUser(new User("Costumer", "123456", "Eliav", "Shabat", UserType.Costumer, "zlibraude@gmail.com", "0543320955", false, "1"));
		String result = (String)loginTestMethod.invoke(loginZLIController, event);
		String expected = "showRelevanceGui " + UserType.Costumer.toString(); 
		verify(loginZLIController, atLeastOnce()).showRelevanceGui(event, UserType.Costumer.toString());
		assertEquals(expected, result);
	}
	
	/**
	 * Description: checking that the method step into the correct "if" statment for Exist Costumer_Service_Employee User.
	 * Input data: event.
	 * Expected result: String "showRelevanceGui" + UserType and verify that showRelevanceGui called once.
	 */  
	@Test
	void loginTestForExistCostumerServiceEmployeeUser() throws Exception {
		doNothing().when(loginZLIController).showRelevanceGui(event, UserType.Costumer_Service_Employee.toString());
		doNothing().when(loginZLIController).userLoginAddAndSet();
		loginZLIController.userController = new UserController();
		loginZLIController.userController.setUser(new User("Costumer_Service_Employee", "123456", "Eliav", "Shabat", UserType.Costumer_Service_Employee, "zlibraude@gmail.com", "0543320955", false, "1"));
		String result = (String)loginTestMethod.invoke(loginZLIController, event);
		String expected = "showRelevanceGui " + UserType.Costumer_Service_Employee.toString(); 
		verify(loginZLIController, atLeastOnce()).showRelevanceGui(event, UserType.Costumer_Service_Employee.toString());
		assertEquals(expected, result);
	}
	
	
	/**
	 * Description: checking that the method step into the correct "if" statment for Exist Costumer_Service_Expert User.
	 * Input data: event.
	 * Expected result: String "showRelevanceGui" + UserType and verify that showRelevanceGui called once.
	 */  
	@Test
	void loginTestForExistCostumerServiceExpertUser() throws Exception {
		doNothing().when(loginZLIController).showRelevanceGui(event, UserType.Costumer_Service_Expert.toString());
		doNothing().when(loginZLIController).userLoginAddAndSet();
		loginZLIController.userController = new UserController();
		loginZLIController.userController.setUser(new User("Costumer_Service_Expert", "123456", "Eliav", "Shabat", UserType.Costumer_Service_Expert, "zlibraude@gmail.com", "0543320955", false, "1"));
		String result = (String)loginTestMethod.invoke(loginZLIController, event);
		String expected = "showRelevanceGui " + UserType.Costumer_Service_Expert.toString(); 
		verify(loginZLIController, atLeastOnce()).showRelevanceGui(event, UserType.Costumer_Service_Expert.toString());
		assertEquals(expected, result);
	}
	
	/**
	 * Description: checking that the method step into the correct "if" statment for Exist Marketing_Employee User.
	 * Input data: event.
	 * Expected result: String "showRelevanceGui" + UserType and verify that showRelevanceGui called once.
	 */  
	@Test
	void loginTestForExistMarketingEmployeeUser() throws Exception {
		doNothing().when(loginZLIController).showRelevanceGui(event, UserType.Marketing_Employee.toString());
		doNothing().when(loginZLIController).userLoginAddAndSet();
		loginZLIController.userController = new UserController();
		loginZLIController.userController.setUser(new User("Marketing_Employee", "123456", "Eliav", "Shabat", UserType.Marketing_Employee, "zlibraude@gmail.com", "0543320955", false, "1"));
		String result = (String)loginTestMethod.invoke(loginZLIController, event);
		String expected = "showRelevanceGui " + UserType.Marketing_Employee.toString(); 
		verify(loginZLIController, atLeastOnce()).showRelevanceGui(event, UserType.Marketing_Employee.toString());
		assertEquals(expected, result);
	}
	
	/**
	 * Description: checking that the method step into the correct "if" statment for Exist Store_Manager User.
	 * Input data: event.
	 * Expected result: String "showRelevanceGui" + UserType and verify that showRelevanceGui called once.
	 */  
	@Test
	void loginTestForExistStoreManagerUser() throws Exception {
		doNothing().when(loginZLIController).showRelevanceGui(event, UserType.Store_Manager.toString());
		doNothing().when(loginZLIController).userLoginAddAndSet();
		loginZLIController.userController = new UserController();
		loginZLIController.userController.setUser(new User("Store_Manager", "123456", "Eliav", "Shabat", UserType.Store_Manager, "zlibraude@gmail.com", "0543320955", false, "1"));
		String result = (String)loginTestMethod.invoke(loginZLIController, event);
		String expected = "showRelevanceGui " + UserType.Store_Manager.toString(); 
		verify(loginZLIController, atLeastOnce()).showRelevanceGui(event, UserType.Store_Manager.toString());
		assertEquals(expected, result);
	}
	
	/**
	 * Description: checking that the method step into the correct "if" statment for Exist Store_Employee User.
	 * Input data: event.
	 * Expected result: String "showRelevanceGui" + UserType and verify that showRelevanceGui called once.
	 */  
	@Test
	void loginTestForExistUser() throws Exception {
		doNothing().when(loginZLIController).showRelevanceGui(event, UserType.Store_Employee.toString());
		doNothing().when(loginZLIController).userLoginAddAndSet();
		loginZLIController.userController = new UserController();
		loginZLIController.userController.setUser(new User("Store_Employee", "123456", "Eliav", "Shabat", UserType.Store_Employee, "zlibraude@gmail.com", "0543320955", false, "1"));
		String result = (String)loginTestMethod.invoke(loginZLIController, event);
		String expected = "showRelevanceGui " + UserType.Store_Employee.toString(); 
		verify(loginZLIController, atLeastOnce()).showRelevanceGui(event, UserType.Store_Employee.toString());
		assertEquals(expected, result);
	}
	
	/**
	 * Description: checking that the method step into the correct "if" statment for Exist Delivery_Man User.
	 * Input data: event.
	 * Expected result: String "showRelevanceGui" + UserType and verify that showRelevanceGui called once.
	 */  
	@Test
	void loginTestForExistDeliveryManUser() throws Exception {
		doNothing().when(loginZLIController).showRelevanceGui(event, UserType.Delivery_Man.toString());
		doNothing().when(loginZLIController).userLoginAddAndSet();
		loginZLIController.userController = new UserController();
		loginZLIController.userController.setUser(new User("Delivery_Man", "123456", "Eliav", "Shabat", UserType.Delivery_Man, "zlibraude@gmail.com", "0543320955", false, "1"));
		String result = (String)loginTestMethod.invoke(loginZLIController, event);
		String expected = "showRelevanceGui " + UserType.Delivery_Man.toString(); 
		verify(loginZLIController, atLeastOnce()).showRelevanceGui(event, UserType.Delivery_Man.toString());
		assertEquals(expected, result);
	}
	
}
