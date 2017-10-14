package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;

import io.realm.Realm;

public class MyTripsActivity extends AppCompatActivity {
    Realm realm1;
    Language lan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        setContentView(R.layout.activity_my_trips);

        Button button1 = (Button) findViewById(R.id.button_Hotel2);
        Button button2 = (Button) findViewById(R.id.button_Flight3);
        Button button3 = (Button) findViewById(R.id.button_offer3);

        if(lan.getLanguage().equals("Arabic"))
        {
            button1.setText("حجوزاتي الفندقية");
            button2.setText("حجوزاتي للرحلات الجوية");
            button3.setText("حجوزاتي للعروض السياحية");

        }

        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyHotelReservations.class);
                startActivity(intent);
                finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyFlightReservations.class);
                startActivity(intent);
                finish();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyOfferReservations.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
