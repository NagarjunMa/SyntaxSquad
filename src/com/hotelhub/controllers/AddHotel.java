package com.hotelhub.controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.SessionFactory;

import com.hotelhub.config.HotelContext;
import com.hotelhub.config.NavigationManager;
import com.hotelhub.dao.HotelDao;
import com.hotelhub.hibernate.SessionManager;
import com.hotelhub.models.Amenity;
import com.hotelhub.models.Hotel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class AddHotel {

    private ObservableList<Amenity> amenityObservableList = FXCollections.observableArrayList();

	
	@FXML
	private TextField txtHotelName; 
	@FXML
	private TextField streetTxt; 
	@FXML
	private TextField cityTxt; 
	@FXML
	private TextField stateTxt; 
	@FXML
	private TextField countryTxt; 
	@FXML
	private TextField zipCodeTxt; 
	@FXML
	private TextField totalRoomsTxt; 
	@FXML
	private TextField availableRoomTxt; 
	@FXML
	private TextField amenityTxt; 
	@FXML
	private ListView<Amenity> amenitiesListView; 
	@FXML
	private TextField descriptionTxt;
	
	
	@FXML
	public void handleAddHotelBtn(ActionEvent event) throws IOException {
		
		String hotelName = txtHotelName.getText();
		String street = streetTxt.getText();
		String city = cityTxt.getText();
		String state = stateTxt.getText();
		String country = countryTxt.getText();
		Long zipcode = Long.parseLong(zipCodeTxt.getText());
		int totalRooms = Integer.parseInt(totalRoomsTxt.getText());
		int availableRooms = Integer.parseInt(availableRoomTxt.getText());
		String amenityVal = amenityTxt.getText();
		String description = descriptionTxt.getText();
		
		if(isValidate(hotelName, street, city, state, country, zipcode, totalRooms, availableRooms, amenityVal, description)) {
			Hotel hotel = new Hotel();
			
			hotel.setName(hotelName);
			hotel.setStreet(street);
			hotel.setCity(city);
			hotel.setState(state);
			hotel.setCountry(country);
			hotel.setZipCode(zipcode);
			hotel.setTotalRooms(totalRooms);
			hotel.setAvailableRooms(availableRooms);
			hotel.setDescription(description);
			
			
			Amenity amenity = new Amenity();
			amenity.setAmenityName(amenityVal);
			 if (!amenityObservableList.contains(amenity)) {
	                amenityObservableList.add(amenity);
	                amenityTxt.clear();
	            }
			
	            Set<Amenity> amenities = new HashSet<>(amenityObservableList);
	            hotel.setAmenities(amenities);

	            amenities.forEach(amenityObj -> amenityObj.setHotel(hotel));
	            
	            
	            
	    		SessionFactory sessionFactory = SessionManager.getSessionFactory();
	            HotelDao hotelDao = new HotelDao(sessionFactory);
	            hotelDao.save(hotel);
	            HotelContext.setCurrentHotel(hotel);
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Data added to the database!!");
				alert.setHeaderText(null);
				alert.setContentText("Hotel details are added to the database");
				alert.showAndWait();
	            

		}
		
	}
	
	@FXML
	public void handleUpdateBtn(ActionEvent event) throws IOException {
		
	}
	
	@FXML
	public void handleBackBtn(ActionEvent event) throws IOException {
		NavigationManager.goBack();
	}
	
	
	public boolean isValidate(String hotelName, String street, String city, String state ,String country,Long zipcode,int totalRooms,int availableRooms, String amenity, String description) {
		if (hotelName.isEmpty() || street.isEmpty() || city.isEmpty() || state.isEmpty()
				|| state.isEmpty() || (zipcode==0) || (totalRooms == 0) || amenity.isEmpty()
				|| (availableRooms == 0) || description.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("One or more field is empty");
			alert.setHeaderText(null);
			alert.setContentText("Please try again to the register with correct values!");
			alert.showAndWait();
			return false;
		}
		return true;
	}
	
}
