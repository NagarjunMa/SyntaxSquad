package com.hotelhub.controllers;

import java.io.IOException;

import com.hotelhub.config.NavigationManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AdminBooking {

	@FXML
	public void handleSortBtn(ActionEvent event) throws IOException {
		
	}
	
	@FXML
	public void handleBackBtn(ActionEvent event) throws IOException {
		NavigationManager.goBack();
	}
	
	
}
