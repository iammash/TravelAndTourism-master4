package com.example.android.travelandtourism.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.ImagesAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Hotel;
import com.example.android.travelandtourism.Models.HotelRate;
import com.example.android.travelandtourism.Models.HotelRoom;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;


import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by haya on 02/09/2017.
 */

public class HotelActivity extends AppCompatActivity {
    private ListView listView;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    Realm realm;
    Realm realm1;
    Language lan;
    RatingBar rating;
    UserModel thisUser;
    int rateValue;
    String rate;
    int hotelId;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm.beginTransaction();
        thisUser = realm.where(UserModel.class).findFirst();
        realm.commitTransaction();

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        Intent intent = this.getIntent();
        hotelId = intent.getIntExtra(Intent.EXTRA_TEXT,0);


        Call<ResponseValue> call = service.getHotel(hotelId);
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {

                if(response.isSuccessful())
                {
                    ResponseValue responseValue = response.body();
                    if(responseValue!= null)
                    {
                        Hotel hotel = responseValue.getHotel();
                        if(hotel != null)
                        {
                            setContentView(R.layout.activity_hotel_info);
                            Button btn1 = (Button) findViewById(R.id.btnAddHotelRate);
                            if(lan.getLanguage().equals("Arabic"))
                            {
                                btn1.setText("أضف تقييمك للفندق");
                            }
                            btn1.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    ShowDialog();
                                }
                            });

                            listView = (ListView) findViewById(R.id.listHotelImages);
                            listView.setVisibility(View.VISIBLE);

                            /// for Dania:
                            final String latitude = hotel.getGpsX();
                            final String Longitude = hotel.getGpsY();
                            final String hotel_phone= hotel.getPhoneNumber();
                            //

                            TextView tv = (TextView)findViewById(R.id.tvHotelInfoNameEn);
                            RatingBar rb = (RatingBar)findViewById(R.id.HotelInfoStars);
                            rb.setRating(hotel.getStars());
                            TextView tv2 = (TextView)findViewById(R.id.tvHotelInfoPhone);
                            TextView tv3 = (TextView)findViewById(R.id.tvHotelInfoEmail);
                            TextView tv4 = (TextView)findViewById(R.id.tvHotelInfoWebSite);
                            TextView tv5 = (TextView)findViewById(R.id.tvHotelInfoCityName);
                            TextView tv6 = (TextView)findViewById(R.id.tvHotelInfoDesEng);
                            TextView tv8 =(TextView)findViewById(R.id.tvHotelStar);
                            TextView tv9 =(TextView)findViewById(R.id.tvHotelRat);

                            if(lan.getLanguage().equals("Arabic"))
                            {
                                tv.setText("الاسم الانجليزي للفندق: "+ hotel.getNameEn());
                                TextView tv1 = (TextView)findViewById(R.id.tvHotelInfoNameAr);
                                tv1.setText(hotel.getNameAr());
                                tv8.setText("عدد النجوم: ");
                                tv9.setText("تقييم الفندق: ");
                                tv2.setText("الهاتف: " +hotel.getPhoneNumber());
                                tv3.setText("البريد الالكتروني: "+hotel.getEmail());
                                tv4.setText("الموقع الالكتروني: " +hotel.getWebsite());
                                tv5.setText("المدينة: "+hotel.getCity().getNameAr());
                                TextView tv7 = (TextView)findViewById(R.id.tvHotelInfoDesAr);
                                tv7.setText("عن الفندق: " +hotel.getDetailsAr());

                            }
                            else
                            {
                                tv.setText("Hotel Eng Name: "+ hotel.getNameEn());

                                tv2.setText("Phone: " +hotel.getPhoneNumber());
                                tv3.setText("E-mail: "+hotel.getEmail());
                                tv4.setText("Website: " +hotel.getWebsite());
                                tv5.setText("Hotel City"+hotel.getCity().getNameEn());
                                tv6.setText("Details in english: "+hotel.getDetailsEn());

                            }


                            RatingBar rb1 = (RatingBar)findViewById(R.id.HotelInfoRate);
                            List<HotelRate> ratList = responseValue.getHotel().getHotelRates();
                            if(ratList.size()!=0)
                            {
                                List<HotelRate> notBadList = new ArrayList<>();

                                for (HotelRate item : ratList) {
                                    if (item.getRate().equals("Not Bad")) {
                                        notBadList.add(item);
                                    }
                                }
                                List<HotelRate> badList = new ArrayList<>();

                                for (HotelRate item : ratList) {
                                    if (item.getRate().equals("Bad")) {
                                        badList.add(item);
                                    }
                                }
                                List<HotelRate> excellentList = new ArrayList<>();

                                for (HotelRate item : ratList) {
                                    if (item.getRate().equals("Excellent")) {
                                        excellentList.add(item);
                                    }
                                }
                                List<HotelRate> goodList = new ArrayList<>();

                                for (HotelRate item : ratList) {
                                    if (item.getRate().equals("Good")) {
                                        goodList.add(item);
                                    }
                                }
                                float sumOfList =badList.size()+notBadList.size()+goodList.size()+excellentList.size();
                                float avg = (((4*excellentList.size())+(3*goodList.size())+(2*notBadList.size())+(2*badList.size()))/sumOfList);

                                rb1.setRating(avg);
                            }


                            ArrayList<Images> arrayList = new ArrayList<>();
                            RealmList<Images> images = hotel.getImages();
                            arrayList.addAll(images);
                            final ImagesAdapter adapter = new ImagesAdapter(getApplicationContext(),arrayList);
                            listView.setAdapter(adapter);

                            listView.setOnTouchListener(new View.OnTouchListener() {
                                // Setting on Touch Listener for handling the touch inside ScrollView
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    // Disallow the touch request for parent scroll on touch of child view
                                    v.getParent().requestDisallowInterceptTouchEvent(true);
                                    return false;
                                }
                            });

                            final String hotelName =hotel.getNameEn();
                            final String hotelNameAr = hotel.getNameAr();
                            final RealmList<HotelRoom> hotelRooms = hotel.getHotelRooms();


                            final ArrayList<HotelRoom> testarr =new ArrayList<>();
                            testarr.addAll(hotelRooms);


                            Button button = (Button) findViewById(R.id.btnHotelsRoom);
                            if(lan.getLanguage().equals("Arabic"))
                            {
                                button.setText("الغرف المتوفرة");
                            }
                            button.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {

                                    Intent intent = new Intent(getApplicationContext(), HotelRooms.class);
                                    Bundle args = new Bundle();
                                    intent.putExtra("hotelName",hotelName);
                                    intent.putExtra("hotelNameAr",hotelNameAr);
                                    //  args.putParcelable("RoomLIST",  Parcels.wrap(hotelRooms));
                                    args.putSerializable("RoomLIST",testarr);
                                    intent.putExtra("BUNDLE",args);
                                    startActivity(intent);
                                    // startActivity(intent1);

                                }
                            });
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
                                    intent.putExtra("hotelName",hotelName);
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
                            Toast.makeText(getApplicationContext(),"No Hotel To show..", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"No Response....", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Connect Failed", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void ShowDialog()
    {

        if(thisUser != null)
        {
            final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.custom_rate, null);
            popDialog.setView(convertView);

            rating = (RatingBar) convertView.findViewById(R.id.RateHotel);
            rating.setMax(4);
            rating.setRating(1);
            popDialog.setIcon(android.R.drawable.btn_star_big_on);
            popDialog.setTitle("Add Rating: ");
            // popDialog.setView(rating);

            // Button OK
            popDialog.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            rateValue= rating.getProgress();

                            if(rateValue==1)
                                rate="Bad";
                            if(rateValue==2)
                                rate="Not Bad";
                            if(rateValue==3)
                                rate="Good";
                            if(rateValue==4)
                                rate="Excellent";



                            Call<ResponseValue> call = service.RateHotel(thisUser.getId(),hotelId,rate);
                            call.enqueue(new Callback<ResponseValue>() {
                                @Override
                                public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {

                                    if(response.isSuccessful())
                                    {
                                        ResponseValue responseValue = response.body();
                                        if(responseValue != null)
                                        {
                                            HotelRate hotelRate = responseValue.getYourRate();
                                            Toast.makeText(getApplicationContext(),"Thank you for rating :)", Toast.LENGTH_LONG).show();

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

                                    Toast.makeText(getApplicationContext(),"3Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();

                                }
                            });

                            dialog.dismiss();
                        }

                    })

                    // Button Cancel
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            popDialog.create();
            popDialog.show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"You Must login to do rate..", Toast.LENGTH_LONG).show();
        }

    }

}
