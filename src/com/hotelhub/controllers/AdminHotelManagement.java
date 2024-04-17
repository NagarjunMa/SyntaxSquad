package com.hotelhub.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import com.hotelhub.config.NavigationManager;
import com.hotelhub.dao.RoomDao;
import com.hotelhub.hibernate.SessionManager;
import com.hotelhub.models.Room;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminHotelManagement {

	@FXML
	private TableView<Room> roomStatusTbl;
	
	
	
	public void initialize() {
		setup();
    	System.out.println("inside initialize");
		loadRoomData();

	}
	
	
	public void setup() {
		TableColumn<Room, Integer> roomIdCol = new TableColumn<>("Room ID");
		roomIdCol.setCellValueFactory(new PropertyValueFactory<>("roomId"));
		
		TableColumn<Room, String> roomStatusCol = new TableColumn<>("Room Status");
		roomStatusCol.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));
		
		roomStatusTbl.getColumns().addAll(roomIdCol, roomStatusCol);

	}
	
	@FXML
	public void handleRoomBtn(ActionEvent event) throws IOException {
		try {
			NavigationManager.navigateTo("/com/hotelhub/views/Rooms.fxml", "Add Rooms");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void handleBookingBtn(ActionEvent event) throws IOException {

	}
	
	@FXML
	public void handleHotelDetails(ActionEvent event) throws IOException {
		System.out.print("testing");
		try {
			NavigationManager.navigateTo("/com/hotelhub/views/AdminAddHotel.fxml", "Hotel Management");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@FXML
	public void handleBackBtn(ActionEvent event) throws IOException {
		NavigationManager.goBack();	
	}
	
//    private void loadRoomData() {
//    	System.out.println("inside loading more data from db");
//    	SessionFactory sessionFactory = SessionManager.getSessionFactory();
//    	RoomDao roomDao = new RoomDao(sessionFactory);
//    	
//        List<Room> rooms = roomDao.findAllAndUpdateStatus();
//        System.out.println("testting"+rooms);
//        ObservableList<Room> observableRooms = FXCollections.observableArrayList(rooms);
//        roomStatusTbl.setItems(observableRooms);
//    }
	
	private void loadRoomData() {
	    RoomDao roomDao = new RoomDao(SessionManager.getSessionFactory());
	    Map<Integer, String> statuses = roomDao.findAllRoomsAndUpdateStatus();

	    ObservableList<Room> observableRooms = FXCollections.observableArrayList();
	    statuses.forEach((id, status) -> {
	        Room room = new Room();
	        room.setRoomId(id);
	        room.setRoomStatus(status);
	        observableRooms.add(room);
	    });

	    roomStatusTbl.setItems(observableRooms);
	}

	
}
