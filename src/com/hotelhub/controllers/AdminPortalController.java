package com.hotelhub.controllers;

import java.io.IOException;
import java.sql.Date;
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
		
		UserDao userDao = new UserDao(sessionFactory);
		userDao.save(user);
		System.out.println("Successfully added to Database");
		
		
	}
	
    @FXML
    private void handleBackButton(ActionEvent event) {
        // Add your event handling logic here
        System.out.println("Back button clicked!");
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
	
	
	
	
	

}
