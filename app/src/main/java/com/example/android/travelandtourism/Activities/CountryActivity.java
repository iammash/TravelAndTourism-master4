package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.CityAdapter;
import com.example.android.travelandtourism.Adapters.CountriesAdapter;
import com.example.android.travelandtourism.Adapters.CountryAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Countries;
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
 * Created by haya on 25/08/2017.
 */

public class CountryActivity extends AppCompatActivity {

    ProgressBar progressBar;
    private ListView listView;

    String url = "http://dsh-tourism.somee.com";


    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);
    Realm realm1;
    Language lan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        Intent intent = this.getIntent();
        final int countryId = intent.getIntExtra(Intent.EXTRA_TEXT,0);



        Call<ResponseValue> call = service.getCountry(countryId);
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {

                if(response.isSuccessful())
                {
                    ResponseValue responseValue = response.body();
                    if(responseValue!= null)
                    {
                        Countries cc = responseValue.getCountry();
                        if(cc!= null)
                        {
                            setContentView(R.layout.activity_country_info);
                            listView = (ListView) findViewById(R.id.listCities);
                            progressBar = (ProgressBar) findViewById(R.id.progress2);
                            progressBar.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);

                            TextView tv1 = (TextView) findViewById(R.id.ivname_En1);
                            if(lan.getLanguage().equals("Arabic"))
                            {
                                TextView tv2 = (TextView) findViewById(R.id.ivname_Ar1);
                                tv1.setText(cc.getNameEn());
                                tv2.setText(cc.getNameAr());
                            }
                            else
                            {
                                tv1.setText(cc.getNameEn());
                            }

                            ImageView img = (ImageView)findViewById(R.id.ivflag);
                            Picasso.with(getApplicationContext()).load(url+cc.getFlag().substring(1)).resize(100,100).into(img);

                            ArrayList<City> arrayList = new ArrayList<>();//create a list to store the Strings
                            RealmList<City> cities = responseValue.getCountry().getCities();
                            arrayList.addAll(cities);

                            final CityAdapter adapter = new CityAdapter(getApplicationContext(),R.layout.row_city,arrayList);
                            listView.setAdapter(adapter);


                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    int cityId = adapter.getItem(i).getId();
                                    Intent intent1=new Intent(getApplicationContext(), CityActivity.class)
                                            .putExtra(Intent.EXTRA_TEXT,cityId);
                                    intent1.putExtra("countryId",countryId);
                                    startActivity(intent1);
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"No Country To show..", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(),"Failed Connection", Toast.LENGTH_LONG).show();
            }
        });


    }
}
