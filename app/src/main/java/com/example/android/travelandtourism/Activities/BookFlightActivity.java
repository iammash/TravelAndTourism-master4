package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Flight;
import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.HotelReservations;
import com.example.android.travelandtourism.Models.HotelRoom;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.Message;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.android.travelandtourism.R.id.spinner;

/**
 * Created by haya on 07/09/2017.
 */

public class BookFlightActivity extends AppCompatActivity {

    String spinnerValue;
    String flightClass;
    int seats=1;
    int economyCost=0;
    int businessCost=0;
    int totalCost=0;
    TextView tv7;
    Realm realm;
    UserModel thisUser;
    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);
    Realm realm1;
    Language lan;
    FlightReservation flightReservation;
    FlightReservation f;
    int resU=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        setContentView(R.layout.activity_book_flight);

        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm.beginTransaction();
        thisUser = realm.where(UserModel.class).findFirst();
        realm.commitTransaction();

        Intent intent1 = getIntent();
        Bundle args = intent1.getBundleExtra("BUNDLE");
        final ArrayList<Flight> object = (ArrayList<Flight>) args.getSerializable("FLIGHT");

        businessCost=object.get(0).getFirstClassTicketPrice();
        economyCost=object.get(0).getEconomyTicketPrice();
        Intent intent = this.getIntent();
        final int flightId = intent.getIntExtra(Intent.EXTRA_TEXT, 0);


        TextView tv = (TextView)findViewById(R.id.tvBookFromCity);
        TextView tv1 = (TextView)findViewById(R.id.tvBookToCity);
        TextView tv2 = (TextView)findViewById(R.id.tvBookFromHour);
        TextView tv3 = (TextView)findViewById(R.id.tvBookFlightDuration);
        TextView tv4 = (TextView)findViewById(R.id.tvBookEconomyPrice);
        TextView tv5 = (TextView)findViewById(R.id.tvBookBusinessPrice);
        TextView tv6 = (TextView)findViewById(R.id.tvBookAirlineNma);
        tv7 = (TextView)findViewById(R.id.tvBookTotalCost);
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        TextView tvAD = (TextView)findViewById(R.id.tvAdultNum);
        Button button = (Button) findViewById(R.id.button_submitFBook);
        Button button1 = (Button)findViewById(R.id.btnCancelFlight);


        if(lan.getLanguage().equals("Arabic"))
        {
            tv.setText("من: "+object.get(0).getSourceCity().getNameAr());
            tv1.setText("الى: "+object.get(0).getDestinationCity().getNameAr());
            tv2.setText("الساعة: "+object.get(0).getTime());
            tv3.setText("مدة الرحلة: "+object.get(0).getFlightDuration());
            tv4.setText(object.get(0).getEconomyTicketPrice().toString()+" $");
            tv5.setText(object.get(0).getFirstClassTicketPrice().toString()+" $");
            tv6.setText("شركة طيران: "+object.get(0).getAirline());
            tvAD.setText("عدد الاشخاص");
            button.setText("تأكيد الحجز");
        }
        else
        {
            tv.setText("from: "+object.get(0).getSourceCity().getNameEn());
            tv1.setText("to: "+object.get(0).getDestinationCity().getNameEn());
            tv2.setText("at: "+object.get(0).getTime());
            tv3.setText("Duration: "+object.get(0).getFlightDuration());
            tv4.setText(object.get(0).getEconomyTicketPrice().toString()+" $");
            tv5.setText(object.get(0).getFirstClassTicketPrice().toString()+" $");
            tv6.setText("Airlines: "+object.get(0).getAirline());

        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerValue =adapterView.getItemAtPosition(i).toString();
                seats =Integer.parseInt(spinnerValue);
                totalPrice();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(thisUser != null)
                {
                    String userId = thisUser.getId();
                    if(flightClass != null)
                    {
                        Call<ResponseValue> call = service.BookFlight(seats,flightClass,userId,flightId);
                        call.enqueue(new Callback<ResponseValue>() {
                            @Override
                            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                                Log.e("Response", "FlightReservations");
                                ResponseValue responseValue = response.body();
                                if(responseValue != null)
                                {
                                    setContentView(R.layout.reserve_flight_confermation);
                                    flightReservation = responseValue.getFlightConfermation();

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
                                    TextView tv14 =(TextView)findViewById(R.id.tvTitle1);
                                    Button button = (Button)findViewById(R.id.btnCancelFlight);


                                    button.setOnClickListener(new View.OnClickListener() {

                                        public void onClick(View v) {
                                            Call<Message> call = service.cancelFlight(flightReservation.getId(), thisUser.getId());
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

                                                                           f = realm.where(FlightReservation.class).equalTo("id",flightReservation.getId()).findFirst();
                                                                           resU = f.getReservationPrice();
                                                                            result.deleteAllFromRealm();
                                                                        }
                                                                    });
                                                                }
                                                                catch (Exception e)
                                                                {
                                                                    Log.e("Realm Error", "error cancel flight" );
                                                                } finally {
                                                                 //   realm.commitTransaction();
                                                                    if(lan.getLanguage().equals("Arabic"))
                                                                    {

                                                                        Toast.makeText(getApplicationContext(), "تم الغاء الحجز بنجاح  ", Toast.LENGTH_LONG).show();

                                                                    }
                                                                    else
                                                                    {
                                                                        Toast.makeText(getApplicationContext(), "Flight Reservation Canceled", Toast.LENGTH_LONG).show();
                                                                    }

                                                                    UpdateCreditCancel();
                                                                 //   UpdateCreditCancel();
                                                                    Intent intent = new Intent(getApplicationContext(), MyFlightReservations.class);
                                                                    startActivity(intent);
                                                                   // finish();
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



                                    if(lan.getLanguage().equals("Arabic"))
                                    {
                                        tv14.setText("تأكيد الحجز");
                                        tv.setText("رحلتك الى: "+flightReservation.getFlight().getDestinationCity().getNameAr());
                                        tv1.setText("الرقم المرجعي للحجز: " + flightReservation.getId());
                                        tv2.setText("من: " + flightReservation.getFlight().getSourceCity().getNameAr());
                                        tv3.setText("التاريخ: " + flightReservation.getFlight().getDisplayDate());
                                        tv4.setText(" " + flightReservation.getFlight().getTime());
                                        tv5.setText("الرقم المرجعي للحجز " + flightReservation.getId().toString());
                                        tv6.setText("مدة الرحلة: " + flightReservation.getFlight().getFlightDuration().toString()+" "+"ساعة");
                                        tv7.setText("درجة الرحلة: " + flightReservation.getFlightClass());
                                        tv8.setText("شرطة طيران: " + flightReservation.getFlight().getAirline());
                                        tv9.setText("رقم الرحلة: " + flightReservation.getFlight().getId().toString());
                                        tv10.setText("اسم المسافر: " + flightReservation.getCustomer().getFirstName() + " " + flightReservation.getCustomer().getLastName());
                                        tv11.setText("عدد المقاعد: " + flightReservation.getSeats().toString());
                                        tv12.setText("تكلفة الحجز: " + flightReservation.getReservationPrice().toString() + " $");
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
                                            tv10.setText("Passenger Name: " + flightReservation.getCustomer().getFirstName() + " " + flightReservation.getCustomer().getLastName());
                                            tv11.setText("Seats Count: " + flightReservation.getSeats().toString());
                                            tv12.setText("Total Reservation Cost: " + flightReservation.getReservationPrice().toString() + " $");
                                            tv13.setText("Reservation Date: " + flightReservation.getDisplayDateTime());

                                        }

                                    Realm realm2 = Realm.getDefaultInstance();
                                    try
                                    {
                                        realm2.beginTransaction();
                                        //for test purpose
                                        String tmp2 = " ";
                                        tmp2 += flightReservation.getId() + "\t" +"from: "+ flightReservation.getFlight().getDestinationCity().getNameEn()
                                                + "to: "+ flightReservation.getFlight().getSourceCity().getNameEn();
                                        Log.e("d",tmp2);
                                        realm2.copyToRealm(flightReservation);

                                    }
                                    catch (Exception e)
                                    {
                                        Log.e("Realm Error", "error" );
                                    } finally {
                                        realm2.commitTransaction();
                                        if(lan.getLanguage().equals("Arabic"))
                                        {
                                            Toast.makeText(getApplicationContext(), "تم الحجز بنجاح", Toast.LENGTH_LONG).show();

                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),"Book Confirmed :)", Toast.LENGTH_LONG).show();
                                        }
                                        UpdateCredit();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Some thing wrong!!", Toast.LENGTH_LONG).show();

                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseValue> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else
                    {
                        if(lan.getLanguage().equals("Arabic"))
                        {
                            Toast.makeText(getApplicationContext(), "الرجاء اختيار درجة الرحلة", Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Please select your flight reserve Class!!", Toast.LENGTH_LONG).show();
                        }
                     /////
                    }
                }
                else
                {
                    if(lan.getLanguage().equals("Arabic"))
                    {
                        Toast.makeText(getApplicationContext(), "يجب تسجيل الدخول لاجراء الحجز.", Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"You must login to do reserve...", Toast.LENGTH_LONG).show();
                    }
                }


            }
        });


    }//onCreate






    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        CheckBox ch1 = (CheckBox)findViewById(R.id.checkbox_economy);
        CheckBox ch2 = (CheckBox)findViewById(R.id.checkbox_business);

        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_economy:
                if (checked)
                {
                    flightClass ="Economy";
                    totalCost =economyCost;
                    ch2.setChecked(false);
                    totalPrice();

                }

                break;

            case R.id.checkbox_business:
                if (checked)
                {
                    flightClass ="FirstClass";
                    totalCost=businessCost;
                    ch1.setChecked(false);
                    totalPrice();
                }
        }
    }

    public void totalPrice()
    {
        int finalCOst = totalCost*seats;

        tv7.setVisibility(View.VISIBLE);
        if(lan.getLanguage().equals("Arabic"))
        {
            tv7.setText("السعر الكامل: " + finalCOst);

        }
        else
            {
                tv7.setText("Total Cost: " + finalCOst);

            }
    }


   public void UpdateCredit() {
       Realm realm3 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        // This query is fast because "character" is an indexed field
       realm3.beginTransaction();

       UserModel user = realm3.where(UserModel.class)
               .equalTo("id", thisUser.getId())
               .findFirst();
        user.setCredit(user.getCredit() - (totalCost*seats));
        realm3.commitTransaction();
    }

    public void UpdateCreditCancel() {
        Realm realm4 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm4.beginTransaction();
        UserModel user = realm4.where(UserModel.class)
                .equalTo("id", thisUser.getId())
                .findFirst();
        user.setCredit(user.getCredit() + resU);
        realm4.commitTransaction();
    }

}
