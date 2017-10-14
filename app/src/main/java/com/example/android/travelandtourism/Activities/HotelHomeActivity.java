package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.android.travelandtourism.R;

/**
 * Created by haya on 08/09/2017.
 */

public class HotelHomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_home);

        Button button1 = (Button) findViewById(R.id.button_Hotel1);
        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CountriesActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button button2 = (Button) findViewById(R.id.button_Hotel2);
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyHotelReservations.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
