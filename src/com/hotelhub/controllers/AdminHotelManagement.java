package com.hotelhub.controllers;

import java.io.IOException;

import com.hotelhub.config.NavigationManager;
import com.hotelhub.models.Room;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class AdminHotelManagement {

	@FXML
	private TableView<Room> roomStatusTbl;
	
	
	
	public void initialize() {
		
	}
	
	
	public void setup() {
		
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
}
