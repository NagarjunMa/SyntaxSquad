package com.hotelhub.config;

import com.hotelhub.models.Hotel;

public class HotelContext {
    private static Hotel currentHotel;

    public static Hotel getCurrentHotel() {
        return currentHotel;
    }

    public static void setCurrentHotel(Hotel hotel) {
        currentHotel = hotel;
    }
}