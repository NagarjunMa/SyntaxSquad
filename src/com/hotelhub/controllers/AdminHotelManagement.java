package com.hotelhub.controllers;

import java.io.IOException;

import com.hotelhub.config.NavigationManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AdminHotelManagement {

	
	
	@FXML
	public void handleRoomBtn(ActionEvent event) throws IOException {
		
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
