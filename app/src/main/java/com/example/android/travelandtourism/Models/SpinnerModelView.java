package com.example.android.travelandtourism.Models;

/**
 * Created by haya on 08/09/2017.
 */

public class SpinnerModelView {
    public int ID;
    public String name;



    public SpinnerModelView(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;            // What to display in the Spinner list.
    }


}
