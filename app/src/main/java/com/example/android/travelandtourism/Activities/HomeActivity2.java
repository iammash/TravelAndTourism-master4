package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;

import io.realm.Realm;

/**
 * Created by haya on 08/09/2017.
 */

public class HomeActivity2 extends AppCompatActivity {
    Realm realm1;
    Language lan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        setContentView(R.layout.activity_home2);


        Button button1 = (Button) findViewById(R.id.button_Flight);
        Button button2 = (Button) findViewById(R.id.button_Hotels);
        Button button3 = (Button) findViewById(R.id.button_offersHome);

        if(lan.getLanguage().equals("Arabic"))
        {
            button1.setText("الرحلات الجوية");
            button2.setText("المدن و الفنادق");
            button3.setText("العروض السياحية");
        }

        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FlightHomeActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), HotelHomeActivity.class);
                Intent intent = new Intent(getApplicationContext(), CountriesActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OfferHomeActivity.class);
                startActivity(intent);
                //finish();
            }
        });


    }
}
