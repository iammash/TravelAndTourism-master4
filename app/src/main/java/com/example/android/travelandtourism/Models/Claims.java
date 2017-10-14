package com.example.android.travelandtourism.Models;

import io.realm.RealmObject;

/**
 * Created by haya on 04/09/2017.
 */

public class Claims extends RealmObject {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id ;
}
