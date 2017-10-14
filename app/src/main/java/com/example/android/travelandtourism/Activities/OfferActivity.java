package com.example.android.travelandtourism.Activities;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.example.android.travelandtourism.Models.Offer;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by haya on 11/09/2017.
 */

public class OfferActivity  extends AppCompatActivity {
    String url = "http://dsh-tourism.somee.com";
    ProgressBar progressBar;
    Button reserbBtn;
    int offerId;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    Realm realm1;
    Language lan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        Intent intent = this.getIntent();
        offerId = intent.getIntExtra(Intent.EXTRA_TEXT, 0);


        Call<ResponseValue> call = service.getOfferById(offerId);
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful())
                {
                    ResponseValue responseValue = response.body();
                    Offer offer = responseValue.getOffer();

                    if(offer != null)
                    {

                        setContentView(R.layout.reserve_offer);
                        progressBar = (ProgressBar) findViewById(R.id.progress111);
                        progressBar.setVisibility(View.GONE);

                        reserbBtn = (Button) findViewById(R.id.btnReserveOffer);

                        TextView tv = (TextView) findViewById(R.id.tvOfferInfoTo);
                        TextView tv1 = (TextView) findViewById(R.id.tvOfferInfoFrom);
                        TextView tv2 = (TextView) findViewById(R.id.tvOfferInfoDuration);
                        TextView tv3 = (TextView) findViewById(R.id.tvOfferInfoCustomerCount);
                        TextView tv4 = (TextView) findViewById(R.id.tvOfferInfoFromDate);
                        TextView tv5 = (TextView) findViewById(R.id.tvOfferInfoToDate);
                        TextView tv6 = (TextView) findViewById(R.id.tvOfferInfoOldPrice);
                        TextView tv7 = (TextView) findViewById(R.id.tvOfferInfoNewPrice);
                        ////////////// first flight ////////////////////

                        TextView tv8 = (TextView) findViewById(R.id.tvOfferInfoFlight1From);
                        TextView tv9 = (TextView) findViewById(R.id.tvOfferInfoFlight1To);
                        TextView tv10 = (TextView) findViewById(R.id.tvOfferInfoFlight1Date);
                        TextView tv12 = (TextView) findViewById(R.id.tvOfferInfoFlight1Time1);
                        TextView tv13 = (TextView) findViewById(R.id.tvOfferInfoFlight1Airline);
                        TextView tv15 = (TextView) findViewById(R.id.tvOfferInfoFlight1Class);

                        ////////////////// second flight///////////

                        TextView tv16 = (TextView) findViewById(R.id.tvOfferInfoFlight2From);
                        TextView tv17 = (TextView) findViewById(R.id.tvOfferInfoFlight2To);
                        TextView tv18 = (TextView) findViewById(R.id.tvOfferInfoFlight2Date);
                        //TextView tv11 = (TextView)findViewById(R.id.tvOfferInfoFlight1Reference);
                        // tv11.setText("at: "+offer.getFlightReservations().get(0));
                        TextView tv19 = (TextView) findViewById(R.id.tvOfferInfoFlight2Time1);
                        TextView tv20 = (TextView) findViewById(R.id.tvOfferInfoFlight2Airline);

                        //  TextView tv14 =(TextView)findViewById(R.id.tvOfferInfoFlight1Num);
                        //  tv14.setText("Flight Number: "+offer.getFlightReservations().get(0).getFlight().getId().toString());

                        TextView tv21 = (TextView) findViewById(R.id.tvOfferInfoFlight2Class);

                        /////////////hotel//////////////

                        List<Images> hotelImagesList = offer.getHotelReservations().get(0).getRoom().getHotel().getImages();
                        if (hotelImagesList.size() != 0) {
                            String img2 = hotelImagesList.get(0).getPath();
                            ImageView iv1 = (ImageView) findViewById(R.id.ivOfferHotelImage);
                            Picasso.with(getApplicationContext()).load(url + img2.substring(1)).fit().centerCrop().into(iv1);

                        }

                        TextView tv22 = (TextView) findViewById(R.id.ivOfferInfoHotelName);
                        RatingBar rb = (RatingBar) findViewById(R.id.offerInfoHotelInfoStars);
                        rb.setRating(offer.getHotelReservations().get(0).getRoom().getHotel().getStars());
                        TextView tv06 =(TextView)findViewById(R.id.tvOfferInfoHotelRoomType);


                        if(lan.getLanguage().equals("Arabic"))
                        {
                            reserbBtn.setText("احجـز العرض");
                            TextView tv01 =(TextView)findViewById(R.id.tvTo1);
                            tv01.setText("اذهب الى");
                            TextView tv02 =(TextView)findViewById(R.id.tvFlights);
                            tv02.setText("الرحل الجوية");
                            TextView tv03 =(TextView)findViewById(R.id.tvFirstFlight);
                            tv03.setText("رحلة الذهاب");
                            TextView tv04 =(TextView)findViewById(R.id.tvSecondFlight);
                            tv04.setText("رحلة العودة");
                            TextView tv05 =(TextView)findViewById(R.id.tvHotel);
                            tv05.setText("الاقامة في فندق");


                            tv.setText(offer.getFlightReservations().get(0).getFlight().getDestinationCity().getNameAr());
                            tv1.setText("من: " + offer.getFlightReservations().get(0).getFlight().getSourceCity().getNameAr());
                            tv2.setText("المدة: " + offer.getDuration().toString() + " يوم");
                            tv3.setText("مقدم لـ: " + offer.getCustomersCount().toString() + " شخص");
                            tv4.setText("من: " + offer.getFlightReservations().get(0).getFlight().getDisplayDate());
                            tv5.setText("الى: " + offer.getFlightBackReservations().get(0).getFlight().getDisplayDate());
                            tv6.setText("السعر القديم: " + offer.getPrice());
                            tv7.setText("السعر الجديد: " + offer.getNewPrice());

                            ////////////// first flight ////////////////////
                            tv8.setText("من: " + offer.getFlightReservations().get(0).getFlight().getSourceCity().getNameAr());
                            tv9.setText("الى: " + offer.getFlightReservations().get(0).getFlight().getDestinationCity().getNameAr());
                            tv10.setText("التاريخ: " + offer.getFlightReservations().get(0).getFlight().getDisplayDate());
                            tv12.setText("  " + offer.getFlightReservations().get(0).getFlight().getTime());
                            tv13.setText("الخطوط الجوية: "+offer.getFlightReservations().get(0).getFlight().getAirline());
                            tv15.setText("درجة الرحلة: "+offer.getFlightReservations().get(0).getFlightClass());


                            ////////////////// second flight///////////

                            tv16.setText("من: " + offer.getFlightBackReservations().get(0).getFlight().getSourceCity().getNameAr());
                            tv17.setText("الى: " + offer.getFlightBackReservations().get(0).getFlight().getDestinationCity().getNameAr());
                            tv18.setText("التاريخ: " + offer.getFlightBackReservations().get(0).getFlight().getDisplayDate());

                            //TextView tv11 = (TextView)findViewById(R.id.tvOfferInfoFlight1Reference);
                            // tv11.setText("at: "+offer.getFlightReservations().get(0));
                            tv19.setText("   " + offer.getFlightReservations().get(0).getFlight().getTime());
                            tv20.setText("شركة طيران: "+offer.getFlightBackReservations().get(0).getFlight().getAirline());

                            //  TextView tv14 =(TextView)findViewById(R.id.tvOfferInfoFlight1Num);
                            //  tv14.setText("Flight Number: "+offer.getFlightReservations().get(0).getFlight().getId().toString());
                            tv21.setText("درجة الرحلة: "+offer.getFlightBackReservations().get(0).getFlightClass());

                            /////////////hotel//////////////


                            tv22.setText(offer.getHotelReservations().get(0).getRoom().getHotel().getNameEn()+"\n"+
                                    offer.getHotelReservations().get(0).getRoom().getHotel().getNameAr());
                            tv06.setText("نوع الغرفة: "+offer.getHotelReservations().get(0).getRoom().getRoom().getType());

                        }
                        else
                        {

                            tv.setText(offer.getFlightReservations().get(0).getFlight().getDestinationCity().getNameEn());
                            tv1.setText("from: " + offer.getFlightReservations().get(0).getFlight().getSourceCity().getNameEn());
                            tv2.setText("Duration: " + offer.getDuration().toString() + " Day");
                            tv3.setText("Offered for: " + offer.getCustomersCount().toString() + " People");
                            tv4.setText("From: " + offer.getFlightReservations().get(0).getFlight().getDisplayDate());
                            tv5.setText("To: " + offer.getFlightBackReservations().get(0).getFlight().getDisplayDate());
                            tv6.setText("Old Price: " + offer.getPrice());
                            tv7.setText("New Price: " + offer.getNewPrice());

                            ////////////// first flight ////////////////////
                            tv8.setText("From: " + offer.getFlightReservations().get(0).getFlight().getSourceCity().getNameEn());
                            tv9.setText("To: " + offer.getFlightReservations().get(0).getFlight().getDestinationCity().getNameEn());
                            tv10.setText("at: " + offer.getFlightReservations().get(0).getFlight().getDisplayDate());
                            tv12.setText("  " + offer.getFlightReservations().get(0).getFlight().getTime());
                            tv13.setText(offer.getFlightReservations().get(0).getFlight().getAirline()+" Airlines");
                            tv15.setText("Flight Class: "+offer.getFlightReservations().get(0).getFlightClass());


                            ////////////////// second flight///////////

                            tv16.setText("From: " + offer.getFlightBackReservations().get(0).getFlight().getSourceCity().getNameEn());
                            tv17.setText("To: " + offer.getFlightBackReservations().get(0).getFlight().getDestinationCity().getNameEn());
                            tv18.setText("at: " + offer.getFlightBackReservations().get(0).getFlight().getDisplayDate());

                            //TextView tv11 = (TextView)findViewById(R.id.tvOfferInfoFlight1Reference);
                            // tv11.setText("at: "+offer.getFlightReservations().get(0));
                            tv19.setText("   " + offer.getFlightReservations().get(0).getFlight().getTime());
                            tv20.setText(offer.getFlightBackReservations().get(0).getFlight().getAirline()+" Airlines");

                            //  TextView tv14 =(TextView)findViewById(R.id.tvOfferInfoFlight1Num);
                            //  tv14.setText("Flight Number: "+offer.getFlightReservations().get(0).getFlight().getId().toString());
                            tv21.setText("Flight Class: "+offer.getFlightBackReservations().get(0).getFlightClass());

                            /////////////hotel//////////////


                            tv22.setText(offer.getHotelReservations().get(0).getRoom().getHotel().getNameEn());
                            tv06.setText("Room Type: "+offer.getHotelReservations().get(0).getRoom().getRoom().getType());
                        }

                        List<Images> cityImagesList = offer.getHotelReservations().get(0).getRoom().getHotel().getCity().getImages();
                        if (cityImagesList.size() != 0) {
                            String img1 = cityImagesList.get(0).getPath();
                            ImageView iv = (ImageView) findViewById(R.id.ivCityImage1);
                            Picasso.with(getApplicationContext()).load(url + img1.substring(1)).fit().centerCrop().into(iv);
                        }



                    }
                    else
                        {
                            if(lan.getLanguage().equals("Arabic"))
                            {
                                Toast.makeText(getApplicationContext(),"العرض غير متوفر حاليا..", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"no offer to show", Toast.LENGTH_LONG).show();
                            }
                        }

                }
                else
                    {
                        Toast.makeText(getApplicationContext(), "Request Error..", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Connect Failed", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void ReserveOffer(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Create and show the dialog.

        SomeDialog newFragment = new SomeDialog(); // add offerId
        Intent intent = this.getIntent();
        offerId = intent.getIntExtra(Intent.EXTRA_TEXT, 0);
        newFragment.newInstance(offerId);
        newFragment.show(ft, "dialog");

    }


    public static class SomeDialog extends DialogFragment {

        static int Id;
        static SomeDialog newInstance(int offerId) {
            SomeDialog fragment = new SomeDialog();
            Id=offerId;
            return fragment ;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

           final int offer=Id;
            return new AlertDialog.Builder(getActivity())
                    .setTitle("Reserve Offer")
                    .setMessage("Sure you wanna reserve this Offer?")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing (will close dialog)
                        }
                    })
                    .setPositiveButton(android.R.string.yes,  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(getContext(), BookOfferActivity.class)
                                    .putExtra(Intent.EXTRA_TEXT,offer);
                            startActivity(intent);
                        }
                    })
                    .create();
        }


    }

}

