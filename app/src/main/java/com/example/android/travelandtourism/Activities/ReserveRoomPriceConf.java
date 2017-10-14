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

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by haya on 10/10/2017.
 */

public class ReserveRoomPriceConf extends AppCompatActivity {

    Realm realm;
    Realm realm5;
    String userId;
    UserModel thisUser;
    Intent intent;
    int roomId;
    Realm realm1;
    Language lan;
    HotelReservations confirmation;
    HotelReservations f;
    int hotelId;
    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    int price;
    Intent intent3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();
        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm.beginTransaction();
        thisUser = realm.where(UserModel.class).findFirst();
        userId = thisUser.getId();
        realm.commitTransaction();
        intent = this.getIntent();
        roomId = intent.getIntExtra(Intent.EXTRA_TEXT,0);

        intent3 = this.getIntent();
        price=intent3.getIntExtra("price",0);

        Intent intent3 = getIntent();
        Bundle extras = intent3.getExtras();
        String chIn1 = extras.getString("checkIn");
        String chOut1 = extras.getString("checkOut");

        Call<ResponseValue> call = service.bookHotel(roomId,userId,chIn1,chOut1);
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful())
                {
                    ResponseValue responseValue = response.body();
                    if(responseValue != null)
                    {
                        confirmation = responseValue.getConfermation();
                        if(confirmation != null)
                        {
                            setContentView(R.layout.reserve_hotel_confermation);

                           UpdateCredit();

                            TextView tv = (TextView) findViewById(R.id.tvRHC_HotelName);
                            TextView tv1 = (TextView) findViewById(R.id.tvRHC_ReferenceNumber);
                            TextView tv2 = (TextView) findViewById(R.id.tvRHC_Type);
                            TextView tv3 = (TextView) findViewById(R.id.tvRHC_GuestsCount);
                            TextView tv4 = (TextView) findViewById(R.id.tvRHC_checkIn);
                            TextView tv5 = (TextView) findViewById(R.id.tvRHC_checkOut);
                            TextView tv6 = (TextView) findViewById(R.id.tvRHC_GuestName);
                            TextView tv7 = (TextView) findViewById(R.id.tvRHC_ReservationCost);
                            TextView tv8 = (TextView) findViewById(R.id.tvRHC_Country);
                            TextView tv9 = (TextView) findViewById(R.id.tvRHC_City);


                            if(lan.getLanguage().equals("Arabic"))
                            {
                                tv.setText("فندق: " + confirmation.getRoom().getHotel().getNameAr()+", "
                                        +confirmation.getRoom().getHotel().getNameEn());
                                tv1.setText("رقم الحجز: " + confirmation.getId());
                                tv2.setText("نوع الغرفة: " + confirmation.getRoom().getRoom().getType());
                                tv3.setText("عدد الاشخاص: " + confirmation.getRoom().getRoom().getCustCount());
                                tv4.setText("تاريخ الدخول: " + confirmation.getDisplayCheckInDate());
                                tv5.setText("تاريخ المغادرة: " + confirmation.getDisplayCheckOutDate());
                                tv6.setText("صاحب الحجز: " + confirmation.getGuest().getFirstName() + " " + confirmation.getGuest().getLastName());
                                tv7.setText("سعر الحجز: " + confirmation.getReservationCost().toString() + "$");
                                tv8.setText("البلد: " + confirmation.getRoom().getHotel().getCity().getCountries().getNameEn());
                                tv9.setText("المدينة: " + confirmation.getRoom().getHotel().getCity().getNameEn());
                                Toast.makeText(getApplicationContext(), " تم حجز الغرفة بنجاح", Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                tv.setText("Hotel: " + confirmation.getRoom().getHotel().getNameEn());
                                tv1.setText("Reference Number: " + confirmation.getId());
                                tv2.setText("Reserve Type: " + confirmation.getRoom().getRoom().getType());
                                tv3.setText("Guest Count: " + confirmation.getRoom().getRoom().getCustCount());
                                tv4.setText("Check-In Date: " + confirmation.getDisplayCheckInDate());
                                tv5.setText("Check-Out Date: " + confirmation.getDisplayCheckOutDate());
                                tv6.setText("Guest Name: " + confirmation.getGuest().getFirstName() + " " + confirmation.getGuest().getLastName());
                                tv7.setText("Reservation Cost: " + confirmation.getReservationCost().toString() + "$");
                                tv8.setText("Country: " + confirmation.getRoom().getHotel().getCity().getCountries().getNameEn());
                                tv9.setText("City: " + confirmation.getRoom().getHotel().getCity().getNameEn());
                                Toast.makeText(getApplicationContext(), " Reserve Confirm.", Toast.LENGTH_LONG).show();

                            }



                            /////////////////////Refreash Reservation List//////////////////


                            Call<ResponseValue> call1 = service.MyHotelReservations(userId);
                            call1.enqueue(new Callback<ResponseValue>() {
                                @Override
                                public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                                    Log.e("Response", "hotelReservations");
                                    ResponseValue responseValue1 = response.body();
                                    if(responseValue1 != null)
                                    {
                                        Realm realm2 = Realm.getDefaultInstance();
                                        try
                                        {
                                            List<HotelReservations> hotelReservationsList = responseValue1.getHotelReservations();
                                            realm2.beginTransaction();
                                            //to check not duplicate followup id
                                            RealmResults<HotelReservations> result = realm2.where(HotelReservations.class).findAll();
                                            result.deleteAllFromRealm();

                                            //for test purpose
                                            String tmp2 = " ";

                                            if(hotelReservationsList.size() == 0)
                                            {
                                                if(lan.getLanguage().equals("Arabic"))
                                                {
                                                    Toast.makeText(getApplicationContext(), "حان وقت السفر :D", Toast.LENGTH_LONG).show();

                                                }
                                                else
                                                {
                                                    Toast.makeText(getApplicationContext(),"Lets Travel :D !!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                            //copy followup to realm
                                            for (HotelReservations a : hotelReservationsList) {
                                                tmp2 += a.getId() + "\t" + a.getRoom().getHotel().getCity().getNameEn() + "\t"
                                                        + a.getRoom().getHotel().getNameEn();


                                                ArrayList<HotelReservations> testing = new ArrayList<>();
                                                testing.addAll(hotelReservationsList);
                                                realm2.copyToRealm(a);
                                                //     realm.commitTransaction();
                                                // Toast.makeText(getApplicationContext(),tmp2, Toast.LENGTH_LONG).show();


                                            }

                                        }
                                        catch (Exception e)
                                        {
                                            Log.e("Realm Error", "error" );
                                        } finally {
                                            realm2.commitTransaction();
                                        }

                                        Button button = (Button)findViewById(R.id.btnCancelHotel);

                                        button.setOnClickListener(new View.OnClickListener() {

                                            public void onClick(View v) {

                                                Call<Message> call = service.CancelHotelReservation(confirmation.getId(), thisUser.getId());
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
                                                                            Toast.makeText(getApplicationContext(), "تم الغاء الحجز..", Toast.LENGTH_LONG).show();

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

                                           /*For Dania*/
                                        final String latitude = confirmation.getRoom().getHotel().getGpsX();
                                        final String Longitude = confirmation.getRoom().getHotel().getGpsY();
                                        final String hotel_phone= confirmation.getRoom().getHotel().getPhoneNumber();
                                        hotelId = confirmation.getRoom().getHotel().getId();
                                        //
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



                                    }
                                    else
                                    {
                                        ////reservations is null
                                        Toast.makeText(getApplicationContext(),"Some thing wrong!!", Toast.LENGTH_LONG).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseValue> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                                }// My hotel reservations
                            });

                            ////////////////////////////////////

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
                else
                {
                    Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                }




            }
            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void UpdateCredit() {
        Realm realm3 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        // This query is fast because "character" is an indexed field
        realm3.beginTransaction();

        UserModel user = realm3.where(UserModel.class)
                .equalTo("id", thisUser.getId())
                .findFirst();
        user.setCredit(user.getCredit() - price);
        realm3.commitTransaction();
    }

    public void UpdateCreditCancel() {
        Realm realm4 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm4.beginTransaction();
        UserModel user = realm4.where(UserModel.class)
                .equalTo("id", thisUser.getId())
                .findFirst();
        user.setCredit(user.getCredit() + price);
        realm4.commitTransaction();
    }
}
