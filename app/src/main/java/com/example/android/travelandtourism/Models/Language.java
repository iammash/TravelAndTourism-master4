package com.example.android.travelandtourism.Models;

import io.realm.RealmObject;

/**
 * Created by haya on 07/10/2017.
 */

public class Language extends RealmObject {
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    private String language;
}
