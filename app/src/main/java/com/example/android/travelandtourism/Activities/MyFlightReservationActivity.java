package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Flight;
import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.HotelReservations;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.Message;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by haya on 07/09/2017.
 */

public class MyFlightReservationActivity extends AppCompatActivity {
    Intent intent;
    int referanceId;
    Realm realm;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    Realm realm1;
    Language lan;
    UserModel userModel;
    int resU=0;
    FlightReservation f;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = this.getIntent();
        referanceId = intent.getIntExtra(Intent.EXTRA_TEXT,0);
        final Realm realm2 = Realm.getDefaultInstance();
        realm2.beginTransaction();
        final FlightReservation flightReservation = realm2.where(FlightReservation.class).equalTo("id",referanceId).findFirst();
        realm2.commitTransaction();

        final Realm realm3 = Realm.getDefaultInstance();
        realm3.beginTransaction();
        userModel = realm3.where(UserModel.class).findFirst();
        realm3.commitTransaction();

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();


        if(flightReservation != null)
        {
            setContentView(R.layout.reserve_flight_confermation);

            TextView tv = (TextView)findViewById(R.id.tvRFC_FlightTo);
            TextView tv1 = (TextView)findViewById(R.id.tvRFC_ReferenceNumber);
            TextView tv2 = (TextView)findViewById(R.id.tvRFC_fromCity);
            TextView tv3 = (TextView)findViewById(R.id.tvRFC_Date);
            TextView tv4 = (TextView)findViewById(R.id.tvRFC_Time);
            TextView tv5 = (TextView)findViewById(R.id.tvRFC_ReferenceNumber);
            TextView tv6 = (TextView)findViewById(R.id.tvRFC_Duration);
            TextView tv7 = (TextView)findViewById(R.id.tvRFC_Class);
            TextView tv8 = (TextView)findViewById(R.id.tvRFC_Airline);
            TextView tv9 = (TextView)findViewById(R.id.tvRFC_FlightNumber);
            TextView tv10 = (TextView)findViewById(R.id.tvRFC_PassengerName);
            TextView tv11 = (TextView)findViewById(R.id.tvRFC_passengersCount);
            TextView tv12 = (TextView)findViewById(R.id.tvRFC_Reservation_cost);
            TextView tv13 = (TextView)findViewById(R.id.tvRFC_resDate);
            Button button = (Button)findViewById(R.id.btnCancelFlight);


            if(lan.getLanguage().equals("Arabic"))
            {
                tv.setText("رحلتك الى: "+flightReservation.getFlight().getDestinationCity().getNameAr());
                tv1.setText("الرقم المرجعي للحجز: " + flightReservation.getId());
                tv2.setText("من: " + flightReservation.getFlight().getSourceCity().getNameAr());
                tv3.setText("التاريخ: " + flightReservation.getFlight().getDisplayDate());
                tv4.setText(" " + flightReservation.getFlight().getTime());
                tv5.setText("الرقم المرجعي للحجز: " + flightReservation.getId().toString());
                tv6.setText("مده الرحلة: " + flightReservation.getFlight().getFlightDuration().toString()+" "+"Hour");
                tv7.setText("درجة الرحلة: " + flightReservation.getFlightClass());
                tv8.setText("شكرة طيران: " + flightReservation.getFlight().getAirline());
                tv9.setText("رقم الرحلة: " + flightReservation.getFlight().getId().toString());
                tv10.setText("اسم المسافر: " + userModel.getFirstName() + " " + userModel.getLastName());
                tv11.setText("عدد المقاعد: " + flightReservation.getSeats().toString());
                tv12.setText("الكلفة الاجمالية للحجز: " + flightReservation.getReservationPrice().toString() + " $");
                tv13.setText("تاريخ الحجز: " + flightReservation.getDisplayDateTime());

            }
            else
            {
                tv.setText("Flight To: "+flightReservation.getFlight().getDestinationCity().getNameEn());
                tv1.setText("Reference: " + flightReservation.getId());
                tv2.setText("From: " + flightReservation.getFlight().getSourceCity().getNameEn());
                tv3.setText("at: " + flightReservation.getFlight().getDisplayDate());
                tv4.setText(" " + flightReservation.getFlight().getTime());
                tv5.setText("Reference: " + flightReservation.getId().toString());
                tv6.setText("Flight Duration: " + flightReservation.getFlight().getFlightDuration().toString()+" "+"Hour");
                tv7.setText("Flight Class: " + flightReservation.getFlightClass());
                tv8.setText("Airlines: " + flightReservation.getFlight().getAirline());
                tv9.setText("Flight Number: " + flightReservation.getFlight().getId().toString());
                tv10.setText("Passenger Name: " + userModel.getFirstName() + " " + userModel.getLastName());
                tv11.setText("Seats Count: " + flightReservation.getSeats().toString());
                tv12.setText("Total Reservation Cost: " + flightReservation.getReservationPrice().toString() + " $");
                tv13.setText("Reservation Date: " + flightReservation.getDisplayDateTime());
            }



            button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Call<Message> call = service.cancelFlight(flightReservation.getId(), userModel.getId());
                    call.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {

                            if(response.isSuccessful())
                            {
                                Message message = response.body();
                                if(message != null)
                                {
                                    String msg = message.getMessage();
                                    if(msg.equals("The Flight Reservation Canceled"))
                                    {
                                        try
                                        {
                                            realm = Realm.getDefaultInstance();
                                            realm.executeTransaction(new Realm.Transaction() {
                                                @Override
                                                public void execute(Realm realm) {
                                                    RealmResults<FlightReservation> result = realm.where(FlightReservation.class).equalTo("id",flightReservation.getId()).findAll();
                                                    f =realm.where(FlightReservation.class).equalTo("id",flightReservation.getId()).findFirst();
                                                    resU = f.getReservationPrice();
                                                    result.deleteAllFromRealm();
                                                }
                                            });
                                        }
                                        catch (Exception e)
                                        {
                                            Log.e("Realm Error", "error cancel flight" );
                                        } finally {
                                            if(lan.getLanguage().equals("Arabic"))
                                            {
                                                Toast.makeText(getApplicationContext(),"تم الغاء الحجز", Toast.LENGTH_LONG).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(getApplicationContext(), "Flight Reservation Canceled", Toast.LENGTH_LONG).show();
                                            }
                                            UpdateCreditCancel();
                                            Intent intent = new Intent(getApplicationContext(), MyFlightReservations.class);
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
            if(lan.getLanguage().equals("Arabic"))
            {
                Toast.makeText(getApplicationContext(),"الحجز غير متوفر..", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Reserve not Available !!", Toast.LENGTH_LONG).show();
            }
        }


    }

    public void UpdateCreditCancel() {
        Realm realm4 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm4.beginTransaction();
        UserModel user = realm4.where(UserModel.class)
                .equalTo("id", userModel.getId())
                .findFirst();
        user.setCredit(user.getCredit() + resU);
        realm4.commitTransaction();
    }

}
