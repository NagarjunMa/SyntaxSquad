package com.hotelhub.controllers;

import com.hotelhub.config.NavigationManager;
import com.hotelhub.config.SessionHandler;
import com.hotelhub.config.UserSession;
import com.hotelhub.config.UserSessionManager;
import com.hotelhub.dao.BookingDao;
import com.hotelhub.dao.RoomDao;
import com.hotelhub.hibernate.SessionManager;
import com.hotelhub.models.Room;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;
import com.hotelhub.dao.UserDao;
import com.hotelhub.models.Booking;
import com.hotelhub.models.User;

import java.sql.Date;
import java.time.LocalDate;
import org.hibernate.SessionFactory;

public class BookRoomController {

	 @FXML
	    private TableView<Room> roomTbl;

	    @FXML
	    private ComboBox<String> comboRoomType;

	    @FXML
	    private Button buttonReset;

	    @FXML
	    private Button buttonBook; 
	    
	    @FXML
	    private DatePicker checkInDatePicker;

	    @FXML
	    private DatePicker checkOutDatePicker;

	    private ObservableList<Room> originalRooms;
	
	
	@FXML
	private void initialize() {
	    comboRoomType.getItems().addAll("All", "Deluxe", "Executive");
	    
	    comboRoomType.getSelectionModel().selectFirst();
	    
	    comboRoomType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue != null) {
	            if (newValue.equals("All")) {
	                loadRoomData(); // Reload all rooms
	            } else {
	                filterRoomsByType(newValue);
	            }
	        }
	    });
	    
        buttonBook.setOnAction(event -> {
            bookRoom();
        });
	    
	    setupRoomTable();
	    loadRoomData();
	}

	@FXML
	private void buttonReset() {
	    // Reset combo box selection to "All"
	    comboRoomType.getSelectionModel().selectFirst();
	    
	    // Reset check-in and check-out dates
	    checkInDatePicker.setValue(null);
	    checkOutDatePicker.setValue(null);
	    
	    // Reload all rooms
	    loadRoomData();
	}

	
	private void filterRoomsByType(String roomType) {
	    ObservableList<Room> allRooms = originalRooms != null ? originalRooms : roomTbl.getItems();
	    ObservableList<Room> filteredRooms = FXCollections.observableArrayList();

	    for (Room room : allRooms) {
	        if (roomType.equals("All") || room.getRoomType().equalsIgnoreCase(roomType)) {
	            filteredRooms.add(room);
	        }
	    }
	    roomTbl.setItems(filteredRooms);
	}


		private void resetTable() {
		    if (originalRooms != null) {
		        roomTbl.setItems(originalRooms);
		        originalRooms = null; 
		    }
		}
		
	 @FXML
	 private void buttonBack() throws IOException {
		 NavigationManager.navigateTo("/com/hotelhub/views/UserDashboard.fxml", "User Dashboard");
	  }
	 
	  @SuppressWarnings("unchecked")
	private void setupRoomTable() {
	    	   TableColumn<Room, Integer> roomIdCol = new TableColumn<>("Room ID");
	    	    TableColumn<Room, String> roomTypeCol = new TableColumn<>("Room Type");
	    	    TableColumn<Room, String> hotelNameCol = new TableColumn<>("Hotel Name");
	    	    TableColumn<Room, Double> priceCol = new TableColumn<>("Price");
	    	    TableColumn<Room, Integer> capacityCol = new TableColumn<>("Capacity");
	    	    TableColumn<Room, String> roomStatusCol = new TableColumn<>("Room Status");

	    	    roomIdCol.setCellValueFactory(new PropertyValueFactory<>("roomId"));
	    	    roomTypeCol.setCellValueFactory(new PropertyValueFactory<>("roomType"));
	    	    hotelNameCol.setCellValueFactory(cellData ->
	    	    new SimpleStringProperty(cellData.getValue().getHotel() != null ? cellData.getValue().getHotel().getName() : ""));
	    	    priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
	    	    capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
	    	    roomStatusCol.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));

	    	    roomTbl.getColumns().addAll(roomIdCol, roomTypeCol, hotelNameCol, priceCol, capacityCol, roomStatusCol);

	    }

	  private void loadRoomData() {
		    try {
		        System.out.println("Inside loading more data from the database...");
		        SessionFactory sessionFactory = SessionManager.getSessionFactory();
		        RoomDao roomDao = new RoomDao(sessionFactory);

		        List<Room> rooms = roomDao.findAll();
		        originalRooms = FXCollections.observableArrayList(rooms); // Store the original list
		        ObservableList<Room> observableRooms = FXCollections.observableArrayList(rooms);
		        roomTbl.setItems(observableRooms);
		    } catch (Exception e) {
		        e.printStackTrace();
		        showAlert(AlertType.ERROR, "Error", "Failed to load room data from the database.");
		    }
		}

	    private void showAlert(AlertType alertType, String title, String message) {
	        Alert alert = new Alert(alertType);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }
 
	    private void bookRoom() {
	        Room selectedRoom = roomTbl.getSelectionModel().getSelectedItem();
	        LocalDate checkInDate = checkInDatePicker.getValue();
	        LocalDate checkOutDate = checkOutDatePicker.getValue();
	        LocalDate currentDate = LocalDate.now();

	        if (selectedRoom == null || checkInDate == null || checkOutDate == null) {
	            showAlert(AlertType.WARNING, "Warning", "Please select a room and specify check-in and check-out dates.");
	            return;
	        }

	        if (checkInDate.isBefore(currentDate)) {
	            showAlert(AlertType.WARNING, "Warning", "Check-in date cannot be before today.");
	            return;
	        }

	        if (checkOutDate.isBefore(checkInDate)) {
	            showAlert(AlertType.WARNING, "Warning", "Check-out date cannot be before the check-in date.");
	            return;
	        }

	        if (selectedRoom.getRoomStatus().equalsIgnoreCase("Booked")) {
	            showAlert(AlertType.WARNING, "Warning", "This room is already booked.");
	            return;
	        }

	        try {
	            // Retrieve the current user
	            User currentUser = getCurrentUser();

	            if (currentUser == null) {
	                showAlert(AlertType.ERROR, "Error", "User not found.");
	                return;
	            }

	            SessionFactory sessionFactory = SessionManager.getSessionFactory();
	            BookingDao bookingDao = new BookingDao(sessionFactory);
	            RoomDao roomDao = new RoomDao(sessionFactory);

	            // Create a new Booking instance
	            Booking booking = new Booking();
	            booking.setUser(currentUser);
	            booking.setRoom(selectedRoom);
	            booking.setCheckInDate(Date.valueOf(checkInDate));
	            booking.setCheckOutDate(Date.valueOf(checkOutDate));
	            booking.setStatus("Pending"); 
	            booking.setPriority(1); 


	            bookingDao.update(booking);

	            selectedRoom.setRoomStatus("Booked");
	            roomDao.update(selectedRoom);

	            showAlert(AlertType.INFORMATION, "Success", "Room booked successfully.");

	            // Refresh table data
	            loadRoomData();
	        } catch (Exception e) {
	            e.printStackTrace();
	            showAlert(AlertType.ERROR, "Error", "Failed to book room.");
	        }
	    }



	    private User getCurrentUser() {
	        // Get the current session ID
	        String sessionId = SessionHandler.getCurrentSessionId();

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

}
