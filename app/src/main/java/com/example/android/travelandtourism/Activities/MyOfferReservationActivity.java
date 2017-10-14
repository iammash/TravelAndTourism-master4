package com.example.android.travelandtourism.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.MyOfferReservationAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Countries;
import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.HotelRoom;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.Message;
import com.example.android.travelandtourism.Models.OfferConfermation;
import com.example.android.travelandtourism.Models.Reservations;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by haya on 19/09/2017.
 */

public class MyOfferReservationActivity extends AppCompatActivity {
    Intent intent;
    int reservationId;
    ProgressBar progressBar;
    String url = "http://dsh-tourism.somee.com";
    Realm realm;
    OfferConfermation offer;
    UserModel userModel;
    int ofP;
    OfferConfermation f;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = this.getIntent();
        reservationId = intent.getIntExtra(Intent.EXTRA_TEXT,0);

        final Realm realm1 = Realm.getDefaultInstance();
        realm1.beginTransaction();
        userModel = realm1.where(UserModel.class).findFirst();
        realm1.commitTransaction();


        Call<ResponseValue> call = service.getMyOfferReservation(reservationId);
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful())
                {
                    ResponseValue responseValue = response.body();
                    if(responseValue!= null)
                    {
                        offer = responseValue.getOfferReservation();
                        if(offer != null)
                        {
                            setContentView(R.layout.reserve_offer_confirmation);

                            progressBar = (ProgressBar) findViewById(R.id.progress111);
                            progressBar.setVisibility(View.GONE);

                            TextView tv0 = (TextView)findViewById(R.id.ivOfferRES_Reference);
                            tv0.setText("Offer Reservation Number: "+ offer.getId().toString());
                            List<Images> cityImagesList = offer.getOffer().getHotelReservations().get(0).getRoom().getHotel().getCity().getImages();
                            if (cityImagesList.size() != 0) {
                                String img1 = cityImagesList.get(0).getPath();
                                ImageView iv = (ImageView) findViewById(R.id.ivCityImage1);
                                Picasso.with(getApplicationContext()).load(url + img1.substring(1)).fit().centerCrop().into(iv);
                            }


                            TextView tv = (TextView) findViewById(R.id.tvOfferInfoTo);
                            tv.setText(offer.getOffer().getFlightReservations().get(0).getFlight().getDestinationCity().getNameEn());
                            TextView tv1 = (TextView) findViewById(R.id.tvOfferInfoFrom);
                            tv1.setText("from: " + offer.getOffer().getFlightReservations().get(0).getFlight().getSourceCity().getNameEn());

                            TextView tv2 = (TextView) findViewById(R.id.tvOfferInfoDuration);
                            tv2.setText("Duration: " + offer.getOffer().getDuration().toString() + " Day");

                            TextView tv3 = (TextView) findViewById(R.id.tvOfferInfoCustomerCount);
                            tv3.setText("Offered for: " + offer.getOffer().getCustomersCount().toString() + " People");

                            TextView tv4 = (TextView) findViewById(R.id.tvOfferInfoFromDate);
                            tv4.setText("From: " + offer.getOffer().getFlightReservations().get(0).getDisplayDateTime());

                            TextView tv5 = (TextView) findViewById(R.id.tvOfferInfoToDate);
                            tv5.setText("To: " + offer.getOffer().getFlightBackReservations().get(0).getDisplayDateTime());

                            TextView tv6 = (TextView) findViewById(R.id.tvOfferInfoOldPrice);
                            tv6.setText("Old Price: " + offer.getOffer().getPrice());

                            TextView tv7 = (TextView) findViewById(R.id.tvOfferInfoNewPrice);
                            tv7.setText("New Price: " + offer.getOffer().getNewPrice());

                            ////////////// first flight ////////////////////

                            TextView tv00 = (TextView)findViewById(R.id.tvOfferInfoFlight1Reference);
                            tv00.setText("Flight Reservation Number: "+ offer.getOffer().getFlightReservations().get(0).getId().toString());

                            TextView tv01 =(TextView)findViewById(R.id.tvOfferInfoFlight1Num);
                            tv01.setText("Flight Number: "+ offer.getOffer().getFlightReservations().get(0).getFlight().getId().toString());


                            TextView tv8 = (TextView) findViewById(R.id.tvOfferInfoFlight1From);
                            tv8.setText("From: " + offer.getOffer().getFlightReservations().get(0).getFlight().getSourceCity().getNameEn());

                            TextView tv9 = (TextView) findViewById(R.id.tvOfferInfoFlight1To);
                            tv9.setText("To: " + offer.getOffer().getFlightReservations().get(0).getFlight().getDestinationCity().getNameEn());

                            TextView tv10 = (TextView) findViewById(R.id.tvOfferInfoFlight1Date);
                            tv10.setText("at: " + offer.getOffer().getFlightReservations().get(0).getFlight().getDisplayDate());


                            TextView tv12 = (TextView) findViewById(R.id.tvOfferInfoFlight1Time1);
                            tv12.setText("  " + offer.getOffer().getFlightReservations().get(0).getFlight().getTime());

                            TextView tv13 = (TextView) findViewById(R.id.tvOfferInfoFlight1Airline);
                            tv13.setText(offer.getOffer().getFlightReservations().get(0).getFlight().getAirline()+" Airlines");

                            //  TextView tv14 =(TextView)findViewById(R.id.tvOfferInfoFlight1Num);
                            //  tv14.setText("Flight Number: "+offer.getFlightReservations().get(0).getFlight().getId().toString());

                            TextView tv15 = (TextView) findViewById(R.id.tvOfferInfoFlight1Class);
                            tv15.setText("Flight Class: "+offer.getOffer().getFlightReservations().get(0).getFlightClass());


                            TextView tv000 = (TextView)findViewById(R.id.tvOfferInfoFlight1seatCount);
                            tv000.setText("Seat Count: "+ offer.getOffer().getFlightReservations().get(0).getSeats().toString());
                            ////////////////// second flight///////////

                            TextView tv03 = (TextView)findViewById(R.id.tvOfferInfoFlight2Reference);
                            tv03.setText("Flight Reservation Number: "+ offer.getOffer().getFlightBackReservations().get(0).getId().toString());

                            TextView tv04 =(TextView)findViewById(R.id.tvOfferInfoFlight2Num);
                            tv04.setText("Flight Number: "+ offer.getOffer().getFlightBackReservations().get(0).getFlight().getId().toString());


                            TextView tv16 = (TextView) findViewById(R.id.tvOfferInfoFlight2From);
                            tv16.setText("From: " + offer.getOffer().getFlightBackReservations().get(0).getFlight().getSourceCity().getNameEn());

                            TextView tv17 = (TextView) findViewById(R.id.tvOfferInfoFlight2To);
                            tv17.setText("To: " + offer.getOffer().getFlightBackReservations().get(0).getFlight().getDestinationCity().getNameEn());

                            TextView tv18 = (TextView) findViewById(R.id.tvOfferInfoFlight2Date);
                            tv18.setText("at: " + offer.getOffer().getFlightBackReservations().get(0).getFlight().getDisplayDate());

                            TextView tv19 = (TextView) findViewById(R.id.tvOfferInfoFlight2Time1);
                            tv19.setText("   " + offer.getOffer().getFlightReservations().get(0).getFlight().getTime());

                            TextView tv20 = (TextView) findViewById(R.id.tvOfferInfoFlight2Airline);
                            tv20.setText(offer.getOffer().getFlightBackReservations().get(0).getFlight().getAirline()+" Airlines");

                            TextView tv21 = (TextView) findViewById(R.id.tvOfferInfoFlight2Class);
                            tv21.setText("Flight Class: "+offer.getOffer().getFlightBackReservations().get(0).getFlightClass());

                            TextView tv001 = (TextView)findViewById(R.id.tvOfferInfoFlight2seatCount);
                            tv001.setText("Seat Count: "+ offer.getOffer().getFlightBackReservations().get(0).getSeats().toString());

                            /////////////hotel//////////////

                            List<Images> hotelImagesList = offer.getOffer().getHotelReservations().get(0).getRoom().getHotel().getCity().getImages();
                            if (hotelImagesList.size() != 0) {
                                String img2 = hotelImagesList.get(0).getPath();
                                ImageView iv1 = (ImageView) findViewById(R.id.ivOfferHotelImage);
                                Picasso.with(getApplicationContext()).load(url + img2.substring(1)).fit().centerCrop().into(iv1);

                            }

                            TextView tv22 = (TextView) findViewById(R.id.ivOfferInfoHotelName);
                            tv22.setText(offer.getOffer().getHotelReservations().get(0).getRoom().getHotel().getNameEn());

                            RatingBar rb = (RatingBar) findViewById(R.id.offerInfoHotelInfoStars);
                            rb.setRating(offer.getOffer().getHotelReservations().get(0).getRoom().getHotel().getStars());

                            TextView tv05 = (TextView)findViewById(R.id.tvOfferInfoHotelRef);
                            tv05.setText("Hotel Reservation Reference Number: "+offer.getOffer().getHotelReservations().get(0).getId().toString());

                            TextView tv06 =(TextView)findViewById(R.id.tvOfferInfoHotelRoomType);
                            tv06.setText("Room Type: "+offer.getOffer().getHotelReservations().get(0).getRoom().getRoom().getType());

                            Button button = (Button)findViewById(R.id.btnCancelOffer);
                            button.setOnClickListener(new View.OnClickListener() {

                                public void onClick(View v) {

                                    Call<Message> call = service.cancelOffer(offer.getId(), userModel.getId());
                                    call.enqueue(new Callback<Message>() {
                                        @Override
                                        public void onResponse(Call<Message> call, Response<Message> response) {
                                            if(response.isSuccessful())
                                            {
                                                Message message = response.body();
                                                if(message != null)
                                                {
                                                    String msg = message.getMessage();
                                                    if(msg.equals("the reservation canceld.."))
                                                    {
                                                        try
                                                        {
                                                            realm = Realm.getDefaultInstance();
                                                            realm.executeTransaction(new Realm.Transaction() {
                                                                @Override
                                                                public void execute(Realm realm) {
                                                                    RealmResults<OfferConfermation> result = realm.where(OfferConfermation.class).equalTo("id",offer.getId()).findAll();
                                                                  f = realm.where(OfferConfermation.class).equalTo("id",offer.getId()).findFirst();
                                                                    ofP = f.getOffer().getNewPrice();
                                                                    result.deleteAllFromRealm();
                                                                }
                                                            });
                                                        }
                                                        catch (Exception e)
                                                        {
                                                            Log.e("Realm Error", "error cancel flight" );
                                                        } finally {
                                                            UpdateCreditCancel();
                                                            Toast.makeText(getApplicationContext(), "Offer reservation canceled successfully!!.", Toast.LENGTH_LONG).show();
                                                            Intent intent = new Intent(getApplicationContext(), MyOfferReservations.class);
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
                                            Toast.makeText(getApplicationContext(), "Some thing wrong...", Toast.LENGTH_LONG).show();

                                        }
                                    });


                                }
                            });




                            ////////////



                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"You Have no reservation ..", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"no response ..", Toast.LENGTH_LONG).show();

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

    public void UpdateCreditCancel() {
        Realm realm4 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm4.beginTransaction();
        UserModel user = realm4.where(UserModel.class)
                .equalTo("id", userModel.getId())
                .findFirst();
        user.setCredit(user.getCredit() + ofP);
        realm4.commitTransaction();
    }
}
