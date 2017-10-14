package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.ImagesAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Countries;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by haya on 29/08/2017.
 */

public class CityActivity extends AppCompatActivity {
    private ListView listView;
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
        int cityId = intent.getIntExtra(Intent.EXTRA_TEXT,0);
        final int countryId = intent.getIntExtra("countryId",0);

        Call<ResponseValue> call = service.getCity(cityId);
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, final Response<ResponseValue> response) {
               if(response.isSuccessful())
               {
                  ResponseValue responseValue = response.body();
                   if(responseValue!=null)
                   {
                       City cc = responseValue.getCity();
                       if(cc != null)
                      {
                          setContentView(R.layout.activity_city_info);
                          listView = (ListView) findViewById(R.id.listCityImages);
                          listView.setVisibility(View.VISIBLE);
                          TextView tv1 = (TextView) findViewById(R.id.tvCityInfoNameEn);
                          if(lan.getLanguage().equals("Arabic"))
                          {
                              tv1.setText(cc.getNameEn());
                              TextView tv2 = (TextView) findViewById(R.id.tvCityInfoNameAr);
                              tv2.setText(cc.getNameAr());

                              TextView tv4 = (TextView) findViewById(R.id.tvCityInfoDesAr);
                              tv4.setText(cc.getDescriptionAr());
                          }
                          else
                          {
                              TextView tv3 = (TextView) findViewById(R.id.tvCityInfoDesEng);
                              tv1.setText(cc.getNameEn());
                              tv3.setText(cc.getDescriptionEn());
                          }


                          ArrayList<Images> arrayList = new ArrayList<>();
                          RealmList<Images> images = responseValue.getCity().getImages();
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

                          final int cityId = response.body().getCity().getId();

                          Button button = (Button) findViewById(R.id.btnCityHotels);
                          if(lan.getLanguage().equals("Arabic"))
                          {
                              button.setText("ابحث عن فندق");
                          }
                          button.setOnClickListener(new View.OnClickListener() {

                              public void onClick(View v) {
                                  Intent intent = new Intent(getApplicationContext(), HotelsActivity.class)
                                          .putExtra(Intent.EXTRA_TEXT,cityId);
                                  intent.putExtra("countryId",countryId);
                                  startActivity(intent);
                              //    finish();

                              }
                          });

                /*
                *
                 *  final  ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollParent);

                listView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                        int action = event.getActionMasked();
                        switch (action) {
                            case MotionEvent.ACTION_UP:
                                mScrollView.requestDisallowInterceptTouchEvent(false);
                                break;
                        }
                        return false;
                    }
                });
                */

                          // final ImagesAdapter adapter = new ImagesAdapter(getApplicationContext(),R.layout.row_image,arrayList);


                      }
                      else
                          {
                              Toast.makeText(getApplicationContext(),"No City To show..", Toast.LENGTH_LONG).show();
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

                Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();

            }
        });


    }
}
