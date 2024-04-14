package com.hotelhub.controllers;

import java.io.IOException;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.hibernate.SessionFactory;
import java.util.regex.Pattern;

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
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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


public class AdminPortalController {
	
	
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
	private TextField passwordTxt;
	
	@FXML
	private TextField confirmPasswordTxt;
	
	
	@FXML
	public void handleAddUser(ActionEvent event) throws IOException {
		try {
		    // Load the admin dashboard page
		    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotelhub/views/AdminUserAdd.fxml"));
		    Parent adminDashboard = loader.load();
		    Scene adminDashboardScene = new Scene(adminDashboard);

		    // Get the current stage and set the new scene
		    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		    stage.setScene(adminDashboardScene);
		    stage.show();
		} catch(Exception e) {
		    e.printStackTrace();
		    // Show an error message if loading the admin dashboard fails
		    Alert alert = new Alert(Alert.AlertType.ERROR);
		    alert.setTitle("Error");
		    alert.setHeaderText("Failed to load admin dashboard");
		    alert.setContentText("An error occurred while loading the admin dashboard. Please try again.");
		    alert.showAndWait();
		}
	}
	
	
	@FXML
	public void handleAddHotel(ActionEvent event) throws IOException {
		
	}
	
	
	@FXML
	public void handleAddHotelToDb(ActionEvent event) throws IOException {
		
	}
	
	@FXML
	public void handleAddUserToDb(ActionEvent event) throws IOException {
		
		  if (!validateFields()) {
	            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields correctly.");
	            return;
	        }
		SessionFactory sessionFactory = SessionManager.getSessionFactory();
		String firstName = firstNameTxt.getText();
		String lastName = lastNameTxt.getText();
		String address = addressTxt.getText();
		String phoneNumber = phoneNumberTxt.getText();
		String dateOfBirth = dobTxt.getText();
		String gender = genderTxt.getText();
		String email = emailTxt.getText();
		String password = passwordTxt.getText();
		
		java.sql.Date dob = convertToSqlDate(dateOfBirth);
		
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setAddress(address);
		user.setPhoneNo(phoneNumber);
		user.setGender(gender);
		user.setEmail(email);
		user.setPassword(password);
		user.setDateOfBirth(dob);
		
		UserDao userDao = new UserDao(sessionFactory);
		userDao.save(user);
		System.out.println("Successfully added to Database");
		
	    showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully!");
	}
	
    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        // Add your event handling logic here
        System.out.println("Back button clicked!");
        Parent adminPortal = FXMLLoader.load(getClass().getResource("/com/hotelhub/views/AdminPortal.fxml"));
        Scene adminPortalScene = new Scene(adminPortal);

        // Get the current stage and set the new scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(adminPortalScene);
        stage.show();
    }
	
	@FXML
	public void handleBackBtnHotel(ActionEvent event) throws IOException {
		
	}
	
	
	public static java.sql.Date convertToSqlDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        
        try {
        	java.util.Date utilDate = dateFormat.parse(dateString);
            return new java.sql.Date(utilDate.getTime());
        } catch (ParseException e) {
            // Handle the case when the date string is in an invalid format
            e.printStackTrace();
            return null;
        }
    }
	
    @FXML
    private void handleBackButtonToLogin(ActionEvent event) throws IOException {
        // Add your event handling logic here
        System.out.println("Back button clicked!");
        Parent adminPortal = FXMLLoader.load(getClass().getResource("/com/mainview/Login.fxml"));
        Scene adminPortalScene = new Scene(adminPortal);

        // Get the current stage and set the new scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(adminPortalScene);
        stage.show();
    }
	
	
    private boolean validateFields() {
        return validateFirstName() && validateLastName() && validateAddress() && validatePhoneNumber()
                && validateDateOfBirth() && validateGender() && validateEmail() && validatePassword() && validateConfirmPassword();
    }

    private boolean validateFirstName() {
        String firstName = firstNameTxt.getText();
        if (firstName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter the first name.");
            return false;
        }
        return true;
    }

    private boolean validateLastName() {
        String lastName = lastNameTxt.getText();
        if (lastName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter the last name.");
            return false;
        }
        return true;
    }

    private boolean validateAddress() {
        String address = addressTxt.getText();
        if (address.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter the address.");
            return false;
        }
        return true;
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = phoneNumberTxt.getText();
        if (!Pattern.matches("\\d{10}", phoneNumber)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid phone number.");
            return false;
        }
        return true;
    }

    private boolean validateDateOfBirth() {
        String dob = dobTxt.getText();
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

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
