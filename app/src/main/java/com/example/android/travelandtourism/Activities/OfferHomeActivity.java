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
 * Created by haya on 15/09/2017.
 */

public class OfferHomeActivity extends AppCompatActivity {

    Realm realm1;
    Language lan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        setContentView(R.layout.activity_offer_home);

        Button button1 = (Button) findViewById(R.id.button_offer1);
        Button button2 = (Button) findViewById(R.id.button_offer2);

        if(lan.getLanguage().equals("Arabic"))
        {
            button1.setText("تصفح جميع العروض السياحية");
            button2.setText("ابحث عن عرض سياحي معين");
        }
        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OffersActivity.class);
                startActivity(intent);
               // finish();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchOffersActivity.class);
                startActivity(intent);
             //   finish();
            }
        });





    }
}
