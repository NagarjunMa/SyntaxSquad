package com.hotelhub.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import org.hibernate.SessionFactory;

import com.hotelhub.dao.UserDao;
import com.hotelhub.hibernate.SessionManager;
import com.hotelhub.models.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

public class UserSignUpController {
	
	@FXML
	private TextField firstNameTxt;
	
	@FXML
	private TextField lastNameTxt;
	
	@FXML
	private TextField addressTxt;
	
	@FXML
	private TextField dobTxt;
	
	@FXML
	private TextField genderTxt;
	
	@FXML
	private TextField phoneNumberTxt;
	
	@FXML
	private TextField emailTxt;
	
	@FXML
	private PasswordField passwordTxt;

	@FXML
	private PasswordField confirmPasswordTxt;
	
	@FXML
	public void handleSignupUserToDb(ActionEvent event) throws IOException {
		
		
		  // Validation for each field
	    if (!validateFields()) {
	        showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields correctly.");
	        return;
	    }

	    // Get the session factory
	    SessionFactory sessionFactory = SessionManager.getSessionFactory();

	    // Retrieve user input from text fields
	    String firstName = firstNameTxt.getText();
	    String lastName = lastNameTxt.getText();
	    String address = addressTxt.getText();
	    String phoneNumber = phoneNumberTxt.getText();
	    String dateOfBirth = dobTxt.getText();
	    String gender = genderTxt.getText();
	    String email = emailTxt.getText();
	    String password = passwordTxt.getText();

	 // Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
	    
	    // Convert date of birth to SQL date
	    java.sql.Date dob = convertToSqlDate(dateOfBirth);

	    // Create a new User object with the retrieved data
	    User user = new User();
	    user.setFirstName(firstName);
	    user.setLastName(lastName);
	    user.setAddress(address);
	    user.setPhoneNo(phoneNumber);
	    user.setGender(gender);
	    user.setEmail(email);
	    user.setPassword(hashedPassword);
	    user.setDateOfBirth(dob);

	    // Save the user data to the database
	    UserDao userDao = new UserDao(sessionFactory);
	    userDao.save(user);
	    System.out.println("Successfully added to Database");
		
	    // Show a success message
	    showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful", "User registered successfully!");

	    // Return to the login page
	    goToLoginPage(event);
	}
	
	// Method to navigate to the login page
	private void goToLoginPage(ActionEvent event) throws IOException {
	    Parent loginParent = FXMLLoader.load(getClass().getResource("/com/mainview/Login.fxml"));
	    Scene loginScene = new Scene(loginParent);
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setScene(loginScene);
	    stage.show();
	}
	
    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        // Add your event handling logic here
        System.out.println("Back button clicked!");
        Parent adminPortal = FXMLLoader.load(getClass().getResource("/com/mainview/Login.fxml"));
        Scene adminPortalScene = new Scene(adminPortal);

        // Get the current stage and set the new scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(adminPortalScene);
        stage.show();
    }
	


    // Validate the input fields
    private boolean validateFields() {
        return validateFirstName() && validateLastName() && validateAddress() && validatePhoneNumber()
                && validateDateOfBirth() && validateGender() && validateEmail() && validatePassword() && validateConfirmPassword();
    }
    
    private boolean validateFirstName() {
        String firstName = firstNameTxt.getText();
        if (firstName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter your first name.");
            return false;
        }
        return true;
    }

    private boolean validateLastName() {
        String lastName = lastNameTxt.getText();
        if (lastName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter your last name.");
            return false;
        }
        return true;
    }

    private boolean validateAddress() {
        String address = addressTxt.getText();
        if (address.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter your address.");
            return false;
        }
        return true;
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = phoneNumberTxt.getText();
        // Validate phone number using regex
        if (!Pattern.matches("\\d{10}", phoneNumber)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid phone number.");
            return false;
        }
        return true;
    }

    private boolean validateDateOfBirth() {
        String dob = dobTxt.getText();
        // Validate date of birth using regex or other methods as needed
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(dob);
            return true;
        } catch (ParseException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid date of birth (YYYY-MM-DD).");
            return false;
        }
    }
	

    private boolean validateGender() {
        String gender = genderTxt.getText();
        // You can add specific validations for gender if needed
        return true;
    }

    private boolean validateEmail() {
        String email = emailTxt.getText();
        // Validate email using regex
        if (!Pattern.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}", email)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid email address.");
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        String password = passwordTxt.getText();
        if (password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a password.");
            return false;
        }
        return true;
    }
    
    private boolean validateConfirmPassword() {
        String password = passwordTxt.getText();
        String confirmPassword = confirmPasswordTxt.getText();
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Passwords do not match.");
            return false;
        }
        return true;
    }
    
    // Method to display an alert
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
	public static java.sql.Date convertToSqlDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        
        try {
        	java.util.Date utilDate = dateFormat.parse(dateString);
            return new java.sql.Date(utilDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	
	
}
