package com.example.android.travelandtourism.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.OfferAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.Offer;
import com.example.android.travelandtourism.Models.SpinnerModelView;
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
 * Created by haya on 13/09/2017.
 */

public class SearchOffersActivity extends AppCompatActivity {

    ProgressBar progressBar;
    private ListView listView;
    List<City> citiesList;
    ArrayList<City> citiesListSpinner;
    List<SpinnerModelView> citiesInSpinner;
    int fromCity=0;
    int toCity=0;
    Button btn;
    Button btn2;
    Button btnSearchOffer;

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

        Call<ResponseValue> call2 = service.getCities();
        call2.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful())
                {
                    Log.e("Response", "get all the cities");
                    ResponseValue responseValue2 = response.body();
                    if(responseValue2 != null)
                    {
                        citiesList = responseValue2.getCities();
                        if (citiesList.size() > 0)
                        {
                            setContentView(R.layout.activity_search_offer);


                            btn = (Button)findViewById(R.id.btnshowDialog);
                            btn2 = (Button)findViewById(R.id.btnshowDialogTo);
                            btnSearchOffer=(Button)findViewById(R.id.btnSearchOffer);
                            if(lan.getLanguage().equals("Arabic"))
                            {
                                TextView tv1= (TextView)findViewById(R.id.tvFrom1);
                                TextView tv2= (TextView)findViewById(R.id.tvTo1);
                                TextView tvTit =(TextView)findViewById(R.id.tvTitle9);
                                tv1.setText("من");
                                tv2.setText("الى");
                                tvTit.setText("ابحث عن عرض سياحي يناسبك");
                                btn.setText("اختر مدينة الذهاب");
                                btn2.setText("اختر المدينة الوجهة");
                                btnSearchOffer.setText("ابدأ البحث");

                            }

                        }
                        else
                        {
                            if(lan.getLanguage().equals("Arabic"))
                            {
                                Toast.makeText(getApplicationContext(), "لا يوجد عروض سياحية حاليا..", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"no offers to show", Toast.LENGTH_LONG).show();
                            }
                            Intent intent = new Intent(getApplicationContext(), OfferHomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        citiesListSpinner = new ArrayList<>();
                        citiesListSpinner.addAll(citiesList);
                        }
                    else
                    {
                        if(lan.getLanguage().equals("Arabic"))
                        {
                            Toast.makeText(getApplicationContext(), "لا يوجد عروض سياحية حاليا..", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), OfferHomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"No connection", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void button_searchOffer(View view)
    {
        if (fromCity !=0 && toCity !=0)
        {

            Call<ResponseValue> call = service.getOffersOptions(fromCity,toCity);
            call.enqueue(new Callback<ResponseValue>() {
                @Override
                public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                    ResponseValue responseValue = response.body();
                    if (responseValue != null)
                    {
                        List<Offer> offers = responseValue.getOffersOptions();

                        if (offers.size() != 0)
                        {
                            setContentView(R.layout.activity_offers);
                            listView = (ListView) findViewById(R.id.listOffers);
                            progressBar =(ProgressBar)findViewById(R.id.progressOffers);

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
                            Toast.makeText(getApplicationContext(),"No Offers Available from ot to this cities, Please try another options..", Toast.LENGTH_LONG).show();

                        }
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
            if(lan.getLanguage().equals("Arabic"))
            {
                Toast.makeText(getApplicationContext(), "الرجاء اكمال جميع المعلومات المطلوبة..", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please Fill All the Fields..", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void FillDialog()
    {
        citiesInSpinner = new ArrayList<>();
        String spinnerName;
        for(City cc: citiesListSpinner)
        {
            int spinnerId = cc.getId();
            if(lan.getLanguage().equals("Arabic"))
            {
                spinnerName= cc.getNameAr();
            }
            else
            {
                spinnerName= cc.getNameEn();
            }
            citiesInSpinner.add(new SpinnerModelView(spinnerId,spinnerName));
        }
    }


    public void SelectCity(final View view1)
    {
        FillDialog();

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchOffersActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Select City");
        ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,citiesInSpinner);

        lv.setAdapter(adapter);
        final AlertDialog dialog = alertDialog.show();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {

                SpinnerModelView city = citiesInSpinner.get(position);
                switch (view1.getId()) {
                    case R.id.btnshowDialog:
                        fromCity = city.getID();
                        String name = city.getName();
                        btn.setText(name);
                        btn.setBackgroundColor(Color.GREEN);
                        break;
                    case R.id.btnshowDialogTo:
                        toCity = city.getID();
                        String name1 = city.getName();
                        btn2.setText(name1);
                        btn2.setBackgroundColor(Color.GREEN);
                        break;
                }

                dialog.dismiss();

            }
        });
    }


}
