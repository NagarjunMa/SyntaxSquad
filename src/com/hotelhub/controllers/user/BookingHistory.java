package com.hotelhub.controllers.user;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import com.hotelhub.config.BookingTree;
import com.hotelhub.config.NavigationManager;
import com.hotelhub.config.SessionHandler;
import com.hotelhub.config.UserSession;
import com.hotelhub.config.UserSessionManager;
import com.hotelhub.dao.UserDao;
import com.hotelhub.hibernate.SessionManager;
import com.hotelhub.models.Booking;
import com.hotelhub.models.Room;
import com.hotelhub.models.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class BookingHistory {

	@FXML
	private TableView<Booking> bookingTbl;
	
	

	
	@FXML
	public void handleSortBtn(ActionEvent event) throws IOException {
		
	}
	
	@FXML
	public void handleBackBtn(ActionEvent event) throws IOException {
		NavigationManager.goBack();
	}
	
	
	SessionFactory sessionFactory = SessionManager.getSessionFactory();
    private BookingTree bookingTree;
    private UserDao userDAO;
	
	
	public void initialize() {
		bookingTree = new BookingTree(sessionFactory);
		userDAO = new UserDao(sessionFactory);
		setup();
		loadBookingData();
		
	}
	
	
	
	public void setup() {
	    // Booking ID
	    TableColumn<Booking, Long> bookingIdCol = new TableColumn<>("Booking ID");
	    bookingIdCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));

	    // User
	    TableColumn<Booking, String> userCol = new TableColumn<>("User");
	    userCol.setCellValueFactory(cellData ->
	            new SimpleStringProperty(cellData.getValue().getUser() != null ? cellData.getValue().getUser().getEmail() : ""));

	    // Room
	    TableColumn<Booking, String> roomCol = new TableColumn<>("Room");
	    roomCol.setCellValueFactory(cellData ->
	            new SimpleStringProperty(cellData.getValue().getRoom() != null ? cellData.getValue().getRoom().getRoomType() : ""));

	    // Check-in Date
	    TableColumn<Booking, Date> checkInDateCol = new TableColumn<>("Check-in Date");
	    checkInDateCol.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));

	    // Check-out Date
	    TableColumn<Booking, Date> checkOutDateCol = new TableColumn<>("Check-out Date");
	    checkOutDateCol.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));

	    // Status
	    TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
	    statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

	    // Priority
	    TableColumn<Booking, Integer> priorityCol = new TableColumn<>("Priority");
	    priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));

	    // Adding the columns to the table
	    bookingTbl.getColumns().addAll(bookingIdCol, userCol, roomCol, checkInDateCol, checkOutDateCol, statusCol, priorityCol);
	}
	
	
	private User getCurrentUser() {
        // Get the current session ID
        String sessionId = SessionHandler.getCurrentSessionId();

        // Check if session ID is not null
        if (sessionId != null) {
            // Retrieve the user session using the session ID
            UserSessionManager userSessionManager = new UserSessionManager();
            UserSession userSession = userSessionManager.getSession(sessionId);

            // Get the user object from the user session
            return userSession.getUser();
        } else {
            return null; // Return null if session ID is null
        }
    }
	
	
	
	public void loadBookingData() {

			try {
				
				User user = getCurrentUser();
	            if (user == null) {
	                showAlert(AlertType.ERROR, "Error", "User not found.");
	                return;
	            }
	            
	            List<Booking> bookings = bookingTree.searchBookingsByUser(user);
	            ObservableList<Booking> observableBookings = FXCollections.observableArrayList(bookings);
	            bookingTbl.setItems(observableBookings);
				
			}catch(Exception e) {
				e.printStackTrace();
			}
	}

	
	
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
	
	
			
	
	
	
}
