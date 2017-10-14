package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.Message;
import com.example.android.travelandtourism.Models.OfferConfermation;
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
 * Created by haya on 13/09/2017.
 */

public class BookOfferActivity extends AppCompatActivity {
    String url = "http://dsh-tourism.somee.com";

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    int offerId;
    Realm realm;
    UserModel thisUser;
    ProgressBar progressBar;
    Realm realm1;
    Language lan;
    OfferConfermation offer;
    int ofP =0;
    OfferConfermation f;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        offerId = intent.getIntExtra(Intent.EXTRA_TEXT, 0);

        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm.beginTransaction();
        thisUser = realm.where(UserModel.class).findFirst();
        realm.commitTransaction();


        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        if(thisUser != null)
        {
            if(offerId != 0)
            {
                Call<ResponseValue> call = service.BookOffer(offerId,thisUser.getId());
                call.enqueue(new Callback<ResponseValue>() {
                    @Override
                    public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {

                        if(response.isSuccessful())
                        {
                            ResponseValue responseValue = response.body();

                            if(responseValue != null)
                            {
                                offer = responseValue.getOfferConfermation();

                                if(offer != null)
                                {

                                    ofP=offer.getOffer().getNewPrice();

                                    setContentView(R.layout.reserve_offer_confirmation);

                                    progressBar = (ProgressBar) findViewById(R.id.progress111);
                                    progressBar.setVisibility(View.GONE);

                                    UpdateCredit();
                                    TextView tv0 = (TextView)findViewById(R.id.ivOfferRES_Reference);
                                    List<Images> cityImagesList = offer.getOffer().getHotelReservations().get(0).getRoom().getHotel().getCity().getImages();
                                    if (cityImagesList.size() != 0) {
                                        String img1 = cityImagesList.get(0).getPath();
                                        ImageView iv = (ImageView) findViewById(R.id.ivCityImage1);
                                        Picasso.with(getApplicationContext()).load(url + img1.substring(1)).fit().centerCrop().into(iv);
                                    }

                                    TextView tv = (TextView) findViewById(R.id.tvOfferInfoTo);
                                    TextView tv1 = (TextView) findViewById(R.id.tvOfferInfoFrom);
                                    TextView tv2 = (TextView) findViewById(R.id.tvOfferInfoDuration);
                                    TextView tv3 = (TextView) findViewById(R.id.tvOfferInfoCustomerCount);
                                    TextView tv4 = (TextView) findViewById(R.id.tvOfferInfoFromDate);
                                    TextView tv5 = (TextView) findViewById(R.id.tvOfferInfoToDate);
                                    TextView tv6 = (TextView) findViewById(R.id.tvOfferInfoOldPrice);
                                    TextView tv7 = (TextView) findViewById(R.id.tvOfferInfoNewPrice);

                                    ////////////// first flight ////////////////////

                                    TextView tv00 = (TextView)findViewById(R.id.tvOfferInfoFlight1Reference);
                                    TextView tv01 =(TextView)findViewById(R.id.tvOfferInfoFlight1Num);
                                    TextView tv8 = (TextView) findViewById(R.id.tvOfferInfoFlight1From);
                                    TextView tv9 = (TextView) findViewById(R.id.tvOfferInfoFlight1To);
                                    TextView tv10 = (TextView) findViewById(R.id.tvOfferInfoFlight1Date);
                                    TextView tv12 = (TextView) findViewById(R.id.tvOfferInfoFlight1Time1);
                                    TextView tv13 = (TextView) findViewById(R.id.tvOfferInfoFlight1Airline);
                                    TextView tv15 = (TextView) findViewById(R.id.tvOfferInfoFlight1Class);
                                    TextView tv000 = (TextView)findViewById(R.id.tvOfferInfoFlight1seatCount);
                                    ////////////////// second flight///////////

                                    TextView tv03 = (TextView)findViewById(R.id.tvOfferInfoFlight2Reference);
                                    TextView tv04 =(TextView)findViewById(R.id.tvOfferInfoFlight2Num);
                                    TextView tv16 = (TextView) findViewById(R.id.tvOfferInfoFlight2From);
                                    TextView tv17 = (TextView) findViewById(R.id.tvOfferInfoFlight2To);
                                    TextView tv18 = (TextView) findViewById(R.id.tvOfferInfoFlight2Date);
                                    TextView tv19 = (TextView) findViewById(R.id.tvOfferInfoFlight2Time1);
                                    TextView tv20 = (TextView) findViewById(R.id.tvOfferInfoFlight2Airline);
                                    TextView tv21 = (TextView) findViewById(R.id.tvOfferInfoFlight2Class);
                                    TextView tv001 = (TextView)findViewById(R.id.tvOfferInfoFlight2seatCount);
                                    /////////////hotel//////////////

                                    List<Images> hotelImagesList = offer.getOffer().getHotelReservations().get(0).getRoom().getHotel().getImages();
                                    if (hotelImagesList.size() != 0) {
                                        String img2 = hotelImagesList.get(0).getPath();
                                        ImageView iv1 = (ImageView) findViewById(R.id.ivOfferHotelImage);
                                        Picasso.with(getApplicationContext()).load(url + img2.substring(1)).fit().centerCrop().into(iv1);

                                    }

                                    TextView tv22 = (TextView) findViewById(R.id.ivOfferInfoHotelName);
                                    RatingBar rb = (RatingBar) findViewById(R.id.offerInfoHotelInfoStars);
                                    rb.setRating(offer.getOffer().getHotelReservations().get(0).getRoom().getHotel().getStars());
                                    TextView tv05 = (TextView)findViewById(R.id.tvOfferInfoHotelRef);
                                    TextView tv06 =(TextView)findViewById(R.id.tvOfferInfoHotelRoomType);

                                    if(lan.getLanguage().equals("Arabic"))
                                    {
                                        TextView tvTo =(TextView)findViewById(R.id.tvTo1);
                                        tvTo.setText("اذهب الى");
                                        TextView tvF =(TextView)findViewById(R.id.tvFlights1);
                                        tvF.setText("الرحلات الجوية");
                                        TextView tvF1 =(TextView)findViewById(R.id.tvFirstFlight1);
                                        tvF1.setText("رحلة الذهاب");
                                        TextView tvF2 =(TextView)findViewById(R.id.tvSecondFlight1);
                                        tvF2.setText("رحلة العودة");
                                        TextView tvH =(TextView)findViewById(R.id.tvHotel1);
                                        tvH.setText("الاقامة في فندق");

                                        tv0.setText("رقم الحجز: "+ offer.getId().toString());
                                        tv.setText(offer.getOffer().getFlightReservations().get(0).getFlight().getDestinationCity().getNameAr());
                                        tv1.setText("من: " + offer.getOffer().getFlightReservations().get(0).getFlight().getSourceCity().getNameAr());
                                        tv2.setText("المدة: " + offer.getOffer().getDuration().toString() + " يوم");
                                        tv3.setText("مقدم لـ: " + offer.getOffer().getCustomersCount().toString() + " شخص");
                                        tv4.setText("من: " + offer.getOffer().getFlightReservations().get(0).getFlight().getDisplayDate());
                                        tv5.setText("الى: " + offer.getOffer().getFlightBackReservations().get(0).getFlight().getDisplayDate());
                                        tv6.setText("السعر القديم: " + offer.getOffer().getPrice());
                                        tv7.setText("السعر الجديد: " + offer.getOffer().getNewPrice());

                                        ////////////// first flight ////////////////////
                                        tv00.setText("رقم المرجعي للحجز: "+ offer.getOffer().getFlightReservations().get(0).getId().toString());
                                        tv01.setText("رقم الرحلة: "+ offer.getOffer().getFlightReservations().get(0).getFlight().getId().toString());
                                        tv8.setText("من: " + offer.getOffer().getFlightReservations().get(0).getFlight().getSourceCity().getNameAr());
                                        tv9.setText("الى: " + offer.getOffer().getFlightReservations().get(0).getFlight().getDestinationCity().getNameAr());
                                        tv10.setText("التاريخ: " + offer.getOffer().getFlightReservations().get(0).getFlight().getDisplayDate());
                                        tv12.setText("  " + offer.getOffer().getFlightReservations().get(0).getFlight().getTime());
                                        tv13.setText("شركة طيران"+offer.getOffer().getFlightReservations().get(0).getFlight().getAirline());
                                        tv15.setText("درجة الرحلة: "+offer.getOffer().getFlightReservations().get(0).getFlightClass());
                                        tv000.setText("عدد المقاعد: "+ offer.getOffer().getFlightReservations().get(0).getSeats().toString());
                                        ////////////////// second flight///////////

                                        tv03.setText("الرقم المرجعي للحجز: "+ offer.getOffer().getFlightBackReservations().get(0).getId().toString());
                                        tv04.setText("رقم الرحلة: "+ offer.getOffer().getFlightBackReservations().get(0).getFlight().getId().toString());
                                        tv16.setText("من: " + offer.getOffer().getFlightBackReservations().get(0).getFlight().getSourceCity().getNameAr());
                                        tv17.setText("الى: " + offer.getOffer().getFlightBackReservations().get(0).getFlight().getDestinationCity().getNameAr());
                                        tv18.setText("التاريخ: " + offer.getOffer().getFlightBackReservations().get(0).getFlight().getDisplayDate());
                                        tv19.setText("   " + offer.getOffer().getFlightReservations().get(0).getFlight().getTime());
                                        tv20.setText("شركة طيران: "+offer.getOffer().getFlightBackReservations().get(0).getFlight().getAirline());
                                        tv21.setText("درجة الرحلة: "+offer.getOffer().getFlightBackReservations().get(0).getFlightClass());
                                        tv001.setText("عدد المقاعد: "+ offer.getOffer().getFlightBackReservations().get(0).getSeats().toString());

                                        /////////////hotel//////////////


                                        tv22.setText(offer.getOffer().getHotelReservations().get(0).getRoom().getHotel().getNameAr());
                                        tv05.setText("رقم حجز الفندق: "+offer.getOffer().getHotelReservations().get(0).getId().toString());
                                        tv06.setText("نوع الغرفة: "+offer.getOffer().getHotelReservations().get(0).getRoom().getRoom().getType());


                                    }
                                    else
                                    {
                                        tv0.setText("Offer Reservation Number: "+ offer.getId().toString());
                                        tv.setText(offer.getOffer().getFlightReservations().get(0).getFlight().getDestinationCity().getNameEn());
                                        tv1.setText("from: " + offer.getOffer().getFlightReservations().get(0).getFlight().getSourceCity().getNameEn());
                                        tv2.setText("Duration: " + offer.getOffer().getDuration().toString() + " Day");
                                        tv3.setText("Offered for: " + offer.getOffer().getCustomersCount().toString() + " People");
                                        tv4.setText("From: " + offer.getOffer().getFlightReservations().get(0).getDisplayDateTime());
                                        tv5.setText("To: " + offer.getOffer().getFlightBackReservations().get(0).getDisplayDateTime());
                                        tv6.setText("Old Price: " + offer.getOffer().getPrice());
                                        tv7.setText("New Price: " + offer.getOffer().getNewPrice());

                                        ////////////// first flight ////////////////////
                                        tv00.setText("Flight Reservation Number: "+ offer.getOffer().getFlightReservations().get(0).getId().toString());
                                        tv01.setText("Flight Number: "+ offer.getOffer().getFlightReservations().get(0).getFlight().getId().toString());
                                        tv8.setText("From: " + offer.getOffer().getFlightReservations().get(0).getFlight().getSourceCity().getNameEn());
                                        tv9.setText("To: " + offer.getOffer().getFlightReservations().get(0).getFlight().getDestinationCity().getNameEn());
                                        tv10.setText("at: " + offer.getOffer().getFlightReservations().get(0).getFlight().getDisplayDate());
                                        tv12.setText("  " + offer.getOffer().getFlightReservations().get(0).getFlight().getTime());
                                        tv13.setText(offer.getOffer().getFlightReservations().get(0).getFlight().getAirline()+" Airlines");
                                        tv15.setText("Flight Class: "+offer.getOffer().getFlightReservations().get(0).getFlightClass());
                                        tv000.setText("Seat Count: "+ offer.getOffer().getFlightReservations().get(0).getSeats().toString());
                                        ////////////////// second flight///////////

                                        tv03.setText("Flight Reservation Number: "+ offer.getOffer().getFlightBackReservations().get(0).getId().toString());
                                        tv04.setText("Flight Number: "+ offer.getOffer().getFlightBackReservations().get(0).getFlight().getId().toString());
                                        tv16.setText("From: " + offer.getOffer().getFlightBackReservations().get(0).getFlight().getSourceCity().getNameEn());
                                        tv17.setText("To: " + offer.getOffer().getFlightBackReservations().get(0).getFlight().getDestinationCity().getNameEn());
                                        tv18.setText("at: " + offer.getOffer().getFlightBackReservations().get(0).getFlight().getDisplayDate());
                                        tv19.setText("   " + offer.getOffer().getFlightReservations().get(0).getFlight().getTime());
                                        tv20.setText(offer.getOffer().getFlightBackReservations().get(0).getFlight().getAirline()+" Airlines");
                                        tv21.setText("Flight Class: "+offer.getOffer().getFlightBackReservations().get(0).getFlightClass());
                                        tv001.setText("Seat Count: "+ offer.getOffer().getFlightBackReservations().get(0).getSeats().toString());

                                        /////////////hotel//////////////


                                        tv22.setText(offer.getOffer().getHotelReservations().get(0).getRoom().getHotel().getNameEn());
                                        tv05.setText("Hotel Reservation Reference Number: "+offer.getOffer().getHotelReservations().get(0).getId().toString());
                                        tv06.setText("Room Type: "+offer.getOffer().getHotelReservations().get(0).getRoom().getRoom().getType());

                                    }


                                    //////////////

                                    Realm realm2 = Realm.getDefaultInstance();
                                    try
                                    {
                                        realm2.beginTransaction();
                                        //for test purpose
                                        String tmp2 = " ";
                                        tmp2 += offer.getId();
                                        Log.e("offer id",tmp2);
                                        realm2.copyToRealm(offer);

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

                                    }

                                    Button button = (Button)findViewById(R.id.btnCancelOffer);
                                    button.setOnClickListener(new View.OnClickListener() {

                                        public void onClick(View v) {

                                            Call<Message> call = service.cancelOffer(offer.getId(), thisUser.getId());
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
                                                                            f=realm.where(OfferConfermation.class).equalTo("id",offer.getId()).findFirst();
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
                                                                    if(lan.getLanguage().equals("Arabic"))
                                                                    {
                                                                        Toast.makeText(getApplicationContext(), "تم الغاء الحجز بنجاح..", Toast.LENGTH_LONG).show();

                                                                    }
                                                                    else
                                                                    {
                                                                        Toast.makeText(getApplicationContext(), "Offer Reservation Canceled", Toast.LENGTH_LONG).show();
                                                                    }
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

                                }
                                else
                                {
                                    // offer confermation == null
                                    Toast.makeText(getApplicationContext(),"ERROR: we cant receive the confirmation ...", Toast.LENGTH_SHORT).show();

                                }
                            }
                            else
                                {
                                    // response value is null
                                    Toast.makeText(getApplicationContext(),"ERROR: no response!!", Toast.LENGTH_SHORT).show();
                                }
                        }
                        else
                            {
                                /// request not success//
                                Toast.makeText(getApplicationContext(),"ERROR: request not success!!", Toast.LENGTH_SHORT).show();

                            }


                    }

                    @Override
                    public void onFailure(Call<ResponseValue> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Connect Failed", Toast.LENGTH_SHORT).show();

                    }
                });


            }
            else
            {
                Toast.makeText(getApplicationContext(),"No offer selected", Toast.LENGTH_SHORT).show();
            }
        }
        else
            {
                if(lan.getLanguage().equals("Arabic"))
                {
                    Toast.makeText(getApplicationContext(), "يجب عليك تسجيل الدخول لإجراء الحجز..", Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"You Must Have Account And login To do reservation", Toast.LENGTH_SHORT).show();
                }

            }





    }

    public void UpdateCredit() {
        Realm realm3 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        // This query is fast because "character" is an indexed field
        realm3.beginTransaction();

        UserModel user = realm3.where(UserModel.class)
                .equalTo("id", thisUser.getId())
                .findFirst();
        user.setCredit(user.getCredit() - ofP);
        realm3.commitTransaction();
    }

    public void UpdateCreditCancel() {
        Realm realm4 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm4.beginTransaction();
        UserModel user = realm4.where(UserModel.class)
                .equalTo("id", thisUser.getId())
                .findFirst();
        user.setCredit(user.getCredit() + ofP);
        realm4.commitTransaction();
    }
}
