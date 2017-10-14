
package com.example.android.travelandtourism.Models;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



import io.realm.RealmList;
import io.realm.RealmObject;


public class City extends RealmObject implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name_En")
    @Expose
    private String nameEn;
    @SerializedName("name_Ar")
    @Expose
    private String nameAr;
    @SerializedName("description_En")
    @Expose
    private String descriptionEn;
    @SerializedName("description_Ar")
    @Expose
    private String descriptionAr;
    @SerializedName("images")
    @Expose
    private RealmList<Images> images = null;
    @SerializedName("cityLocation")
    @Expose
    private String cityLocation;

    public Countries getCountries() {
        return countries;
    }

    public void setCountries(Countries countries) {
        this.countries = countries;
    }

    @SerializedName("country")
    @Expose
    private Countries countries;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionAr() {
        return descriptionAr;
    }

    public void setDescriptionAr(String descriptionAr) {
        this.descriptionAr = descriptionAr;
    }

    public RealmList<Images> getImages() {
        return images;
    }

    public void setImages(RealmList<Images> images) {
        this.images = images;
    }

    public String getCityLocation() {
        return cityLocation;
    }

    public void setCityLocation(String cityLocation) {
        this.cityLocation = cityLocation;
    }

}