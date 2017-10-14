package com.example.android.travelandtourism.Models;

import io.realm.RealmObject;

/**
 * Created by haya on 04/09/2017.
 */

public class Logins extends RealmObject {
    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    private Integer Id;

}
