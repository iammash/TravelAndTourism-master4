package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.HotelReservations;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.Message;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by haya on 05/09/2017.
 */

public class MyHotelReservationActivity extends AppCompatActivity {

    Intent intent;
    int reservationId;
    Realm realm1;
    Language lan;
    int hotelId;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    Realm realm;

    UserModel userModel;
    int price;
    HotelReservations f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        intent = this.getIntent();
        reservationId = intent.getIntExtra(Intent.EXTRA_TEXT,0);
        final Realm realm2 = Realm.getDefaultInstance();
        realm2.beginTransaction();
        final HotelReservations confirmation = realm2.where(HotelReservations.class).equalTo("id",reservationId).findFirst();
        realm2.commitTransaction();

        final Realm realm3 = Realm.getDefaultInstance();
        realm3.beginTransaction();
        userModel = realm3.where(UserModel.class).findFirst();
        realm3.commitTransaction();

        if(confirmation != null)
        {
            /*For Dania*/
            final String latitude = confirmation.getRoom().getHotel().getGpsX();
            final String Longitude = confirmation.getRoom().getHotel().getGpsY();
            final String hotel_phone= confirmation.getRoom().getHotel().getPhoneNumber();
            hotelId = confirmation.getRoom().getHotel().getId();
            //
            setContentView(R.layout.reserve_hotel_confermation);
            TextView tv = (TextView)findViewById(R.id.tvRHC_HotelName);
            TextView tv1 =(TextView)findViewById(R.id.tvRHC_ReferenceNumber);
            TextView tv2 =(TextView)findViewById(R.id.tvRHC_Type);
            TextView tv3 =(TextView)findViewById(R.id.tvRHC_GuestsCount);
            TextView tv4 =(TextView)findViewById(R.id.tvRHC_checkIn);
            TextView tv5 =(TextView)findViewById(R.id.tvRHC_checkOut);
            TextView tv6 =(TextView)findViewById(R.id.tvRHC_GuestName);
            TextView tv7 =(TextView)findViewById(R.id.tvRHC_ReservationCost);
            TextView tv8 =(TextView)findViewById(R.id.tvRHC_Country);
            TextView tv9 =(TextView)findViewById(R.id.tvRHC_City);

            Button button = (Button)findViewById(R.id.btnCancelHotel);

            if(lan.getLanguage().equals("Arabic"))
            {
                TextView tvTit =(TextView)findViewById(R.id.tvTitle8);
                tvTit.setText("حجزي الفندقي");
                tv.setText("فندق: "+confirmation.getRoom().getHotel().getNameAr());
                tv1.setText("الرقم المرجعي: "+confirmation.getId());
                tv2.setText("نوع الغرفة: "+confirmation.getRoom().getRoom().getType());
                tv3.setText("عدد الاشخاص: "+confirmation.getRoom().getRoom().getCustCount());
                tv4.setText("تاريخ الدخول: "+confirmation.getDisplayCheckInDate());
                tv5.setText("تاريخ المغادرة: "+confirmation.getDisplayCheckOutDate());
                tv6.setText("اسم الزائر: "+confirmation.getGuest().getFirstName() +" "+confirmation.getGuest().getLastName());
                tv7.setText("تكلفة الحجز: "+confirmation.getReservationCost().toString()+"$");
                tv8.setText("الدولة: "+confirmation.getRoom().getHotel().getCity().getCountries().getNameAr());
                tv9.setText("المدينة: "+confirmation.getRoom().getHotel().getCity().getNameAr());


            }
            else
            {

                tv.setText("Hotel: "+confirmation.getRoom().getHotel().getNameEn());
                tv1.setText("Reference Number: "+confirmation.getId());
                tv2.setText("Reserve Type: "+confirmation.getRoom().getRoom().getType());
                tv3.setText("Guest Count: "+confirmation.getRoom().getRoom().getCustCount());
                tv4.setText("Check-In Date: "+confirmation.getDisplayCheckInDate());
                tv5.setText("Check-Out Date: "+confirmation.getDisplayCheckOutDate());
                tv6.setText("Guest Name: "+confirmation.getGuest().getFirstName() +" "+confirmation.getGuest().getLastName());
                tv7.setText("Reservation Cost: "+confirmation.getReservationCost().toString()+"$");
                tv8.setText("Country: "+confirmation.getRoom().getHotel().getCity().getCountries().getNameEn());
                tv9.setText("City: "+confirmation.getRoom().getHotel().getCity().getNameEn());

            }

// Created By Dania ***
            Button button2 = (Button) findViewById(R.id.btnMap);
            if(lan.getLanguage().equals("Arabic"))
            {
                button2.setText("عنوان الفندق");
            }
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT,hotelId);
                    intent.putExtra("Latitude", latitude);
                    intent.putExtra("Longitude", Longitude);
                    intent.putExtra("hotelName",confirmation.getRoom().getHotel().getNameEn());
                    startActivity(intent);
                }
            });
            Button buttonCall = (Button) findViewById(R.id.Call_btn);
            if(lan.getLanguage().equals("Arabic"))
            {
                buttonCall.setText("اتصل بالفندق");
            }
            buttonCall.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent intentCall = new Intent(Intent.ACTION_DIAL);
                    intentCall.setData(Uri.parse("tel:" + hotel_phone));
                    startActivity(intentCall);
                }
            });
            // ***

            button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    int ii = confirmation.getId();
                    String dd = userModel.getId();
                    Call<Message> call = service.CancelHotelReservation(confirmation.getId(), userModel.getId());
                    call.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            if(response.isSuccessful()){
                                Message message = response.body();
                                if(message != null)
                                {
                                    String msg = message.getMessage();
                                    if(msg.equals("Hotel Reservation Canceled.."))
                                    {
                                        try
                                        {
                                            realm = Realm.getDefaultInstance();
                                            realm.executeTransaction(new Realm.Transaction() {
                                                @Override
                                                public void execute(Realm realm) {
                                                    RealmResults<HotelReservations> result = realm.where(HotelReservations.class).equalTo("id",confirmation.getId()).findAll();
                                                    f = realm.where(HotelReservations.class).equalTo("id",confirmation.getId()).findFirst();
                                                    price=f.getReservationCost();
                                                    result.deleteAllFromRealm();
                                                }
                                            });
                                        }
                                        catch (Exception e)
                                        {
                                            Log.e("Realm Error", "error cancel flight" );
                                        } finally
                                        {
                                            UpdateCreditCancel();
                                            if(lan.getLanguage().equals("Arabic"))
                                            {
                                                Toast.makeText(getApplicationContext(),"تم الغاء حجزك الفندقي..", Toast.LENGTH_LONG).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(getApplicationContext(), "Hotel Reservation Canceled..", Toast.LENGTH_LONG).show();
                                            }

                                            Intent intent = new Intent(getApplicationContext(), MyHotelReservations.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Failed...", Toast.LENGTH_LONG).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                            }
                        }


                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Reserve not Available !!", Toast.LENGTH_LONG).show();
        }
    }


    public void UpdateCreditCancel() {
        Realm realm4 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm4.beginTransaction();
        UserModel user = realm4.where(UserModel.class)
                .equalTo("id", userModel.getId())
                .findFirst();
        user.setCredit(user.getCredit() + price);
        realm4.commitTransaction();
    }
}
