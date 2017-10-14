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

public class FlightHomeActivity extends AppCompatActivity {

    Realm realm1;
    Language lan;
    Button button1;
    Button button2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        setContentView(R.layout.activity_flight_home);

        button1 = (Button) findViewById(R.id.button_Flight1);
        button2 = (Button) findViewById(R.id.button_Flight2);

        if(lan.getLanguage().equals("Arabic"))
        {
            button1.setText("ابحث عن رحله جوية");
            button2.setText("جدول الرحلات");
        }

        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchFligtsActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FlightsScheduleActivity.class);
                startActivity(intent);
              //  finish();
            }
        });
        /*Button button3 = (Button) findViewById(R.id.button_Flight3);
        button3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyFlightReservations.class);
                startActivity(intent);
                finish();
            }
        });*/

    }
}
