package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.travelandtourism.MainActivity;
import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.HotelReservations;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by haya on 08/09/2017.
 */

public class MyAccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.row_user);

        UserModel thisUser;
        Realm realm;
        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm.beginTransaction();
        thisUser = realm.where(UserModel.class).findFirst();
        realm.commitTransaction();


        TextView tv = (TextView)findViewById(R.id.tvuserFName);
        tv.setText("Name: "+thisUser.getFirstName()+" "+thisUser.getLastName());
        TextView tv1 = (TextView)findViewById(R.id.tvuserEmail);
        tv1.setText("Email: "+thisUser.getEmail());
        TextView tv2 = (TextView)findViewById(R.id.tvuserGender);
        tv2.setText("Gender: "+thisUser.getGender());

        TextView tv3 = (TextView)findViewById(R.id.tvuserPhone);
        tv3.setText("Phone: "+thisUser.getPhoneNumber());
        TextView tv4 = (TextView)findViewById(R.id.tvuserCountry);
        tv4.setText("From: "+thisUser.getCountry()+", "+thisUser.getCity());

        TextView tv5 = (TextView)findViewById(R.id.tvuserCridet);
        tv5.setText("Cridet: "+thisUser.getCredit());
        TextView tv6 = (TextView)findViewById(R.id.tvuserName);
        tv6.setText("Username: "+thisUser.getUserName());


        /*Button button = (Button) findViewById(R.id.button_logout);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Realm realm3 = Realm.getDefaultInstance();
                try
                {
                    realm3.beginTransaction();
                    //to check not duplicate followup id
                    RealmResults<UserModel> result1 = realm3.where(UserModel.class).findAll();
                    result1.deleteAllFromRealm();
                    RealmResults<City> results2 = realm3.where(City.class).findAll();
                    results2.deleteAllFromRealm();
                    RealmResults<FlightReservation> result3 = realm3.where(FlightReservation.class).findAll();
                    result3.deleteAllFromRealm();
                    RealmResults<HotelReservations> result4 = realm3.where(HotelReservations.class).findAll();
                    result4.deleteAllFromRealm();

                    Intent intent = new Intent(getApplicationContext(), com.example.android.travelandtourism.MainActivity.class);
                    startActivity(intent);
                    //finish();
                }
                catch (Exception e)
                {
                    Log.e("Realm Error", "error" );
                } finally {
                    realm3.commitTransaction();
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Button button1 = (Button) findViewById(R.id.button_sendMessage);
        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SendMessageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button button3 = (Button) findViewById(R.id.button_ChargeCredit);
        button3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChargeCredit.class);
                startActivity(intent);
                finish();
            }
        });


        Button button4 = (Button) findViewById(R.id.button_ChangePassword);
        button4.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button button5 = (Button) findViewById(R.id.button_UpdateInfo1);
        button5.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateUserInfoActivity.class);
                startActivity(intent);
                finish();
            }
        }); */



    }
}
