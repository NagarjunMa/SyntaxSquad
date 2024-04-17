package com.hotelhub.controllers;

import com.hotelhub.models.Room;
import com.hotelhub.config.HotelContext;
import com.hotelhub.config.NavigationManager;
import com.hotelhub.dao.HotelDao;
import com.hotelhub.dao.RoomDao;
import com.hotelhub.hibernate.SessionManager;
import com.hotelhub.models.Hotel;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;

public class RoomManagement {
	
	@FXML
	private TableView<Room> roomTbl;
	
	@FXML
	private TextField roomTypeTxt;
	
	@FXML
	private TextField priceTxt;
	
	@FXML
	private TextField capacityTxt;
	
	@FXML
	private TextField roomStatusTxt;
	
	
	
	
	
	
	@FXML
	public void initialize() {
		setupRoomTable();
    	System.out.println("inside initialize");
		loadRoomData();


	}
	
	private void setupRoomTable() {
		// Room ID
		TableColumn<Room, Integer> roomIdCol = new TableColumn<>("Room ID");
		roomIdCol.setCellValueFactory(new PropertyValueFactory<>("roomId"));
		
		// Room Type
		TableColumn<Room, String> roomTypeCol = new TableColumn<>("Room Type");
		roomTypeCol.setCellValueFactory(new PropertyValueFactory<>("roomType"));
		
		TableColumn<Room, String> hotelNameCol = new TableColumn<>("Hotel");
		hotelNameCol.setCellValueFactory(cellData -> 
		    new SimpleStringProperty(cellData.getValue().getHotel() != null ? cellData.getValue().getHotel().getName() : ""));
		
		// Price
		TableColumn<Room, Double> priceCol = new TableColumn<>("Price");
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		
		// Capacity
		TableColumn<Room, Integer> capacityCol = new TableColumn<>("Capacity");
		capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
		
		// Room Status
		TableColumn<Room, String> roomStatusCol = new TableColumn<>("Room Status");
		roomStatusCol.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));
		
		// Add columns to the table
		roomTbl.getColumns().addAll(roomIdCol, roomTypeCol, hotelNameCol, priceCol, capacityCol, roomStatusCol);
		
		// Optionally, load room data here if you have a method to do so
		// roomTbl.setItems(loadRoomData());
	}
	

	
	
	
	@FXML
	public void handleAddBtn(ActionEvent event) throws IOException {
			String roomType = roomTypeTxt.getText();
			String roomStatus = roomStatusTxt.getText();
			int capacity = Integer.parseInt(capacityTxt.getText());
			double price = Double.parseDouble(priceTxt.getText());
			
			
			if(isValidate(roomType, roomStatus, capacity, price)) {
				
				SessionFactory sessionFactory = SessionManager.getSessionFactory();
				Room room = new Room();
				RoomDao roomDao = new RoomDao(sessionFactory);
				Hotel hotel = HotelContext.getCurrentHotel();
				
				
				
				room.setRoomStatus(roomStatus);
				room.setCapacity(capacity);
				room.setRoomType(roomType);
				room.setPrice(price);
				
				
				if(hotel != null) {
					room.setHotel(hotel);
					
					if(hotel.getRooms() == null) {
						hotel.setRooms(new ArrayList<>());
					}
					
					hotel.getRooms().add(room);
				} else {
					HotelDao hotelDao = new HotelDao(sessionFactory);
					hotel = hotelDao.findByName("sample");
					room.setHotel(hotel);
					if(hotel.getRooms() == null) {
						hotel.setRooms(new ArrayList<>());
					}
					
					hotel.getRooms().add(room);
				}

				roomDao.save(room);
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Data added to the database!!");
				alert.setHeaderText(null);
				alert.setContentText("Rooms details are added to the database");
				alert.showAndWait();
				
				roomStatusTxt.clear();
				roomTypeTxt.clear();
				priceTxt.clear();
				capacityTxt.clear();
				loadRoomData();
				
			}
	}
	
	@FXML
	public void handleUpdateBtn(ActionEvent event) throws IOException {
		
	}
	
	
	@FXML
	public void handleBackBtn(ActionEvent event) throws IOException {
		NavigationManager.goBack();
	}
	
	
    private void loadRoomData() {

    	System.out.println("inside loading more data from db");
    	SessionFactory sessionFactory = SessionManager.getSessionFactory();
    	RoomDao roomDao = new RoomDao(sessionFactory);
    	
        List<Room> rooms = roomDao.findAll();
        System.out.println("testting"+rooms);
        ObservableList<Room> observableRooms = FXCollections.observableArrayList(rooms);
        roomTbl.setItems(observableRooms);
    }
	
	
	
	
	public boolean isValidate(String roomType, String roomStatus, int capacity, double price){
		
		if(roomType.isEmpty() || roomStatus.isEmpty() || capacity == 0 || price == 0.0 ) {
			return false;
		}
		
		return true;
	}
	
	
	
	
	
}
