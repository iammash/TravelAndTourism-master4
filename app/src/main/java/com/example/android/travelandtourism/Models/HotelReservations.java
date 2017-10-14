package com.example.android.travelandtourism.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by haya on 04/09/2017.
 */

public class HotelReservations extends RealmObject implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("guest")
    @Expose
    private UserModel guest;
    @SerializedName("room")
    @Expose
    private HotelRoom room;
    @SerializedName("check_In_Date")
    @Expose
    private String checkInDate;
    @SerializedName("displayCheck_In_Date")
    @Expose
    private String displayCheckInDate;
    @SerializedName("check_Out_Date")
    @Expose
    private String checkOutDate;
    @SerializedName("displayCheck_Out_Date")
    @Expose
    private String displayCheckOutDate;
    @SerializedName("reservationCost")
    @Expose
    private Integer reservationCost;
    @SerializedName("roomsAvailable")
    @Expose
    private Integer roomsAvailable;
    @SerializedName("isBooked")
    @Expose
    private Boolean isBooked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserModel getGuest() {
        return guest;
    }

    public void setGuest(UserModel guest) {
        this.guest = guest;
    }

    public HotelRoom getRoom() {
        return room;
    }

    public void setRoom(HotelRoom room) {
        this.room = room;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getDisplayCheckInDate() {
        return displayCheckInDate;
    }

    public void setDisplayCheckInDate(String displayCheckInDate) {
        this.displayCheckInDate = displayCheckInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getDisplayCheckOutDate() {
        return displayCheckOutDate;
    }

    public void setDisplayCheckOutDate(String displayCheckOutDate) {
        this.displayCheckOutDate = displayCheckOutDate;
    }

    public Integer getReservationCost() {
        return reservationCost;
    }

    public void setReservationCost(Integer reservationCost) {
        this.reservationCost = reservationCost;
    }

    public Integer getRoomsAvailable() {
        return roomsAvailable;
    }

    public void setRoomsAvailable(Integer roomsAvailable) {
        this.roomsAvailable = roomsAvailable;
    }

    public Boolean getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(Boolean isBooked) {
        this.isBooked = isBooked;
    }

}
