package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.HotelsAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Hotel;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by haya on 02/09/2017.
 */

public class HotelsActivity extends AppCompatActivity {
    private ListView listView;
    ProgressBar progressBar;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    Realm realm1;
    Language lan;
    int cityId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();


        Intent intent = this.getIntent();
        int countryId = intent.getIntExtra("countryId",0);
        cityId = intent.getIntExtra(Intent.EXTRA_TEXT,0);


        Call<ResponseValue> call = service.getCityHotel(countryId,cityId);
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful())
                {
                    ResponseValue responseHotels = response.body();

                    if(responseHotels != null)
                    {
                        List<Hotel> hh = responseHotels.getHotels();

                        if(hh.size() >0)
                        {
                            setContentView(R.layout.activity_hotels);
                            listView = (ListView) findViewById(R.id.listHotels);
                            progressBar = (ProgressBar) findViewById(R.id.progressHotel);
                            progressBar.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            ArrayList <Hotel> arrayList = new ArrayList<>();
                            arrayList.addAll(hh);

                            final HotelsAdapter adapter = new HotelsAdapter(getApplicationContext(),R.layout.row_hotel,arrayList);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    int hotelId = adapter.getItem(position).getId();

                                    Intent intent=new Intent(getApplicationContext(), HotelActivity.class)
                                            .putExtra(Intent.EXTRA_TEXT,hotelId);
                                    startActivity(intent);
                                }
                            });
                        }
                        else
                            {
                                if(lan.getLanguage().equals("Arabic"))
                                {
                                    Toast.makeText(getApplicationContext(),"لا يوجد فنادق للعرض..", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"no hotels to show", Toast.LENGTH_LONG).show();
                                }
                                Intent intent = new Intent(getApplicationContext(), CityActivity.class)
                                        .putExtra(Intent.EXTRA_TEXT,cityId);
                                startActivity(intent);
                                finish();
                            }

                    }
                    else
                    {
                        if(lan.getLanguage().equals("Arabic"))
                        {
                            Toast.makeText(getApplicationContext(),"لا يوجد فنادق للعرض..", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"no hotels to show", Toast.LENGTH_LONG).show();
                        }                        Intent intent = new Intent(getApplicationContext(), CityActivity.class)
                                .putExtra(Intent.EXTRA_TEXT,cityId);
                        startActivity(intent);
                        finish();
                    }

                }
                else
                    {
                        Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), CityActivity.class)
                                .putExtra(Intent.EXTRA_TEXT,cityId);
                        startActivity(intent);
                        finish();
                    }

            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), CityActivity.class)
                        .putExtra(Intent.EXTRA_TEXT,cityId);
                startActivity(intent);
                finish();

            }
        });


    }
}
