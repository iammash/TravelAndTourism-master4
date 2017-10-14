package com.example.android.travelandtourism.Models;

/**
 * Created by haya on 02/09/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.io.Serializable;

import io.realm.RealmObject;


public class HotelRate extends RealmObject implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user")
    @Expose
    private UserModel user;
    @SerializedName("hotel")
    @Expose
    private Hotel hotel;
    @SerializedName("rate")
    @Expose
    private String rate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

}
