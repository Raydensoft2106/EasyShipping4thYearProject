package com.example.easyshipping.Model;

public class UserLocation {

    public String userEmail;
    public String userLatLng;

    public UserLocation(String userEmail, String userLatLng) {
        this.userEmail = userEmail;
        this.userLatLng = userLatLng;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserLatLng() {
        return userLatLng;
    }

    public void setUserLatLng(String userLatLng) {
        this.userLatLng = userLatLng;
    }
}
