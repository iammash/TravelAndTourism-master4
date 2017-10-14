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

import com.example.android.travelandtourism.Adapters.OfferAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Countries;
import com.example.android.travelandtourism.Models.Flight;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.Offer;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by haya on 11/09/2017.
 */

public class OffersActivity extends AppCompatActivity {

    ListView listView;
    ProgressBar progressBar;
    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    Realm realm1;
    Language lan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_offers);
        listView = (ListView) findViewById(R.id.listOffers);
        progressBar =(ProgressBar)findViewById(R.id.progressOffers);

        Call<ResponseValue> call = service.getOffers();
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful())
                {
                    ResponseValue responseValue = response.body();

                    if(responseValue != null)
                    {
                        List<Offer> offers = responseValue.getOffers();
                        if(offers.size()>0)
                        {
                            listView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            final  ArrayList<Offer> arrayList = new ArrayList<>();//create a list to store the objects
                            arrayList.addAll(offers);

                            final OfferAdapter adapter = new OfferAdapter(getApplicationContext(),R.layout.row_offer,arrayList);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    int offerId = adapter.getItem(position).getId();

                                    Intent intent=new Intent(getApplicationContext(), OfferActivity.class);
                                    intent.putExtra(Intent.EXTRA_TEXT, offerId);
                                    startActivity(intent);

                                }
                            });

                        }

                        else
                        {
                            if(lan.getLanguage().equals("Arabic"))
                            {
                                Toast.makeText(getApplicationContext(),"لايوجد عروض متوفره حاليا..", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"no offers to show", Toast.LENGTH_LONG).show();
                            }

                            Intent intent = new Intent(getApplicationContext(), OfferHomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                    else
                    {
                        if(lan.getLanguage().equals("Arabic"))
                        {
                            Toast.makeText(getApplicationContext(),"لايوجد عروض متوفره حاليا..", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"no offers to show", Toast.LENGTH_LONG).show();
                        }                        Intent intent = new Intent(getApplicationContext(), OfferHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), OfferHomeActivity.class);
                    startActivity(intent);
                    finish();
                }



            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Connect Failed", Toast.LENGTH_SHORT).show();

            }
        });







    }
}
