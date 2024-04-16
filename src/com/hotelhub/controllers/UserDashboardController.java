package com.hotelhub.controllers;

import java.io.IOException;

import com.hotelhub.config.NavigationManager;
import com.hotelhub.config.SessionHandler;
import com.hotelhub.config.UserSession;
import com.hotelhub.config.UserSessionManager; // Correct import statement

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import com.hotelhub.models.User;
import javafx.scene.control.TextField;
import com.hotelhub.hibernate.SessionManager;
import org.hibernate.Session;

public class UserDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private Label username;
    
    @FXML
    private TextField fieldFirstName;

    @FXML
    private TextField fieldLastName;

    @FXML
    private TextField fieldPhone;

    @FXML
    private TextField fieldEmail;

    @FXML
    private TextField fieldAddress;

    @FXML
    private PasswordField fieldPassword;
    
    @FXML
    private PasswordField fieldConfirmPassword;
    
    private User user;
    
    // Method to initialize the user dashboard view
    @FXML
    public void initialize() {
        // Get the current session ID
        String sessionId = SessionHandler.getCurrentSessionId();

        // Check if session ID is not null
        if (sessionId != null) {
            // Retrieve the user session using the session ID
            UserSessionManager userSessionManager = new UserSessionManager();
            UserSession userSession = userSessionManager.getSession(sessionId);

            // Get the user object from the user session
            user = userSession.getUser();

            // Display a welcome message with the user's name
            welcomeLabel.setText("Welcome, " + " " + user.getFirstName() + "!");
            username.setText(user.getFirstName() + " " + user.getLastName());
            // Display user details in text fields
            displayUserDetails();
        } else {
            // If session ID is null, display a generic welcome message
            welcomeLabel.setText("Welcome!");
        }
    }
    
    private void displayUserDetails() {
        if (user != null) {
            fieldFirstName.setText(user.getFirstName());
            fieldLastName.setText(user.getLastName());
            fieldPhone.setText(user.getPhoneNo());
            fieldEmail.setText(user.getEmail());
            fieldAddress.setText(user.getAddress());
        }
    }
    @FXML
    private void buttonSignOut() {
        try {
            // Handle logout action here, such as clearing user session, navigating to login page, etc.
            SessionHandler.setCurrentSessionId(null);
            SessionHandler.clearSession();
            NavigationManager.navigateTo("/com/mainview/Login.fxml", "Login");

            System.out.println("User logged out");
        } catch (IOException e) {
            // Handle any IOException that may occur during navigation
            e.printStackTrace();
        }
    }

    @FXML
    private void buttonUpdate() {
        // Retrieve the current session ID
        String sessionId = SessionHandler.getCurrentSessionId();
        String newPassword = fieldPassword.getText();
        String confirmPassword = fieldConfirmPassword.getText();
        
        // Validate that all fields are not empty
        if (fieldFirstName.getText().isEmpty() || fieldLastName.getText().isEmpty() || fieldPhone.getText().isEmpty()
                || fieldEmail.getText().isEmpty() || fieldAddress.getText().isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("All fields are required.");
            return;
        }
        // Check if the new password matches the confirm password
        if (!newPassword.equals(confirmPassword)) {
            showAlert("Passwords do not match!");
            return; // Exit the method if passwords do not match
        }
        
        if (sessionId != null) {
            // Retrieve the user session using the session ID
            UserSession userSession = UserSessionManager.getSession(sessionId);
            
            // Get the user object from the user session
            User user = userSession.getUser();
            
            // Update user details with the new values from text fields
            if (user != null) {
            	
                user.setFirstName(fieldFirstName.getText());
                user.setLastName(fieldLastName.getText());
                user.setPhoneNo(fieldPhone.getText());
                user.setEmail(fieldEmail.getText());
                user.setAddress(fieldAddress.getText());
                user.setPassword(newPassword);
                
                // Save the updated user details to the database or other data source
                boolean success = updateUserDetails(user);
                if (success) {
                	   Alert alert = new Alert(AlertType.WARNING);
                       alert.setTitle("Success");
                       alert.setHeaderText(null);
                       alert.setContentText("User details updated successfully!");
                       alert.showAndWait();                   
                       System.out.println("User details updated successfully!");
                } else {
                    System.out.println("Failed to update user details.");
                }
            }
        } else {
            System.out.println("No active session found.");
        }
    }
    @SuppressWarnings("deprecation")
	private boolean updateUserDetails(User user) {
        // Get the current Hibernate session
        Session session = SessionManager.openSession();
        
        try {
            // Begin a transaction
            session.beginTransaction();
            
            // Update the user in the database
            session.update(user);
            
            // Commit the transaction
            session.getTransaction().commit();
            
            return true; // Update successful
        } catch (Exception e) {
            // Rollback the transaction if an exception occurs
            session.getTransaction().rollback();
            e.printStackTrace();
            return false; // Update failed
        } finally {
            // Close the session
            SessionManager.closeSession();
        }
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
