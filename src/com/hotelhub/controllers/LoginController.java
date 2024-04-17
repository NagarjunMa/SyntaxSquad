package com.hotelhub.controllers;

import java.io.IOException;

import com.hotelhub.config.NavigationManager;
import com.hotelhub.config.SessionHandler;
import com.hotelhub.config.UserSessionManager;
import com.hotelhub.dao.UserDao;
import com.hotelhub.hibernate.SessionManager;
import com.hotelhub.models.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	
	
	@FXML
	private Label errorID;
	

	@FXML
	private Label errorPassword;
	
	@FXML
	private TextField txtUsername;
	
	@FXML
	private TextField txtPassword;
	
	@FXML
	private Button buttonSignIn;

	@FXML
	private Button buttonNavAdminSignIn;
	
    public static String username1;
	
 

    @FXML
    private void handleSignInButton(ActionEvent event) throws IOException {
        String email  = txtUsername.getText();
        String password  = txtPassword.getText();
        
        // Check if the login is for admin
        if(email.equals("admin") && password.equals("Admin@1234")) {
            // Admin login
            NavigationManager.navigateTo("/com/hotelhub/views/AdminPortal.fxml", "Admin Portal");
        } else {
            // Regular user login
            loginUser(email, password);
        }
    }

    private void loginUser(String email, String password) throws IOException {
        // Check if email and password are not empty
        if (!email.isEmpty() && !password.isEmpty()) {
            // Instantiate UserDao to access user data
            UserDao userDao = new UserDao(SessionManager.getSessionFactory());

            // Retrieve user from the database based on email
            User user = userDao.findByEmail(email);

            // Check if user exists and password matches
            if (user != null && user.getPassword().equals(password)) {
                // Login successful, navigate to appropriate page based on user role
            	String sessionId = UserSessionManager.createSession(user);
            	if(sessionId != null) {
            		SessionHandler.setCurrentSessionId(sessionId);
                    NavigationManager.navigateTo("/com/hotelhub/views/UserDashboard.fxml", "User Dashboard");
            	}else {
            		 showAlert(Alert.AlertType.ERROR, "Invalid Credentials", "Invalid username or password");
            	}

            } else {
                // Show an error message for invalid login credentials
                showAlert(Alert.AlertType.ERROR, "Invalid Credentials", "Invalid email or password");
            }
        } else {
            // Show an error message for empty fields
            showAlert(Alert.AlertType.ERROR, "Empty Fields", "Please enter email and password");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    
    @FXML
    private void handleSignUpButton(ActionEvent event)  {
	    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotelhub/views/UserSignUp.fxml"));
        Parent adminLoginParent = loader.load();

        // Get the scene from the current stage
        Scene adminLoginScene = new Scene(adminLoginParent);
        
        // Get the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Set the scene on the stage
        currentStage.setScene(adminLoginScene);
        currentStage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    }
}
