package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.CountriesAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Countries;
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
 * Created by haya on 23/08/2017.
 */

public class CountriesActivity extends AppCompatActivity{
    CountriesAdapter adapter;
    ProgressBar progressBar;
    private ListView listView;
    Realm realm1;
    Language lan;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();


        Call<ResponseValue> call = service.getCountries();
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
               if(response.isSuccessful())
               {
                   ResponseValue responseValue = response.body();
                   if(responseValue != null)
                   {
                       List<Countries> cc = responseValue.getContries();

                       if(cc.size() != 0)
                       {
                           setContentView(R.layout.activity_country);
                           listView = (ListView) findViewById(R.id.list);
                           progressBar = (ProgressBar) findViewById(R.id.progress);
                           TextView tvTitle = (TextView)findViewById(R.id.tvTitle5);

                           if(lan.getLanguage().equals("Arabic"))
                           {
                               tvTitle.setText("إختر وجهتك");
                           }


                           progressBar.setVisibility(View.GONE);
                           listView.setVisibility(View.VISIBLE);

                           ArrayList<Countries> arrayList = new ArrayList<>();//create a list to store the objects
                           arrayList.addAll(cc);

                           adapter = new CountriesAdapter(getApplicationContext(),R.layout.row_country,arrayList);
                           listView.setAdapter(adapter);

                           listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                               @Override
                               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                   int countryId = adapter.getItem(position).getId();

                                   Intent intent=new Intent(getApplicationContext(), CountryActivity.class)
                                           .putExtra(Intent.EXTRA_TEXT,countryId);
                                   startActivity(intent);

                               }
                           });
                       }
                       else
                       {
                           Toast.makeText(getApplicationContext(),"No Countries To show..", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(),"Connect Failed", Toast.LENGTH_SHORT).show();

            }
        });
    }
}


