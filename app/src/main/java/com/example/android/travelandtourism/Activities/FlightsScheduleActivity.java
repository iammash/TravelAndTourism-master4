package com.example.android.travelandtourism.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.example.android.travelandtourism.Adapters.FlightsScheduleAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Flight;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.SpinnerModelView;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by haya on 05/09/2017.
 */

public class FlightsScheduleActivity extends AppCompatActivity {

    ProgressBar progressBar;
    private ListView listView;
    Button bt;
    Button bt1;
    List<City> citiesList;
    ArrayList<City> citiesListSpinner;
    List<SpinnerModelView> citiesInSpinner;
    int fromCity;
    int toCity;
    String City1;
    String City2;
    ArrayList<Flight> arrayList;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    Realm realm1;
    Language lan;

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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(FlightsScheduleActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Select City");
        ListView lv = (ListView) convertView.findViewById(R.id.listView1);
       // EditText ed = (EditText)convertView.findViewById(R.id.country_picker_search);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,citiesInSpinner);

        lv.setAdapter(adapter);
        final AlertDialog dialog = alertDialog.show();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {

                SpinnerModelView city = citiesInSpinner.get(position);
                switch (view1.getId()) {
                    case R.id.btnshowDialogFrom11:
                        fromCity = city.getID();
                        String name = city.getName();
                        bt.setText(name);
                        bt.setBackgroundColor(Color.GREEN);
                        City1=name;
                        break;
                    case R.id.btnshowDialogTo11:
                        toCity = city.getID();
                        String name1 = city.getName();
                        bt1.setText(name1);
                        bt1.setBackgroundColor(Color.GREEN);
                        City2=name1;
                        break;
                }

                dialog.dismiss();

            }
        });
    }

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
                  if(responseValue2 != null) {
                      citiesList = responseValue2.getCities();
                      if(citiesList.size()!=0)
                      {
                          setContentView(R.layout.activity_flight_schedule);

                          bt = (Button)findViewById(R.id.btnshowDialogFrom11);
                          bt1 =(Button)findViewById(R.id.btnshowDialogTo11);
                          TextView tv =(TextView)findViewById(R.id.tvTitle3);
                          TextView tv1 =(TextView)findViewById(R.id.tv1);
                          TextView tv2 =(TextView)findViewById(R.id.text1);
                          if(lan.getLanguage().equals("Arabic"))
                          {
                              bt.setText("اختر مدينة الذهاب");
                              bt1.setText("اختر مدينة الوجهه");
                              tv.setText("احصل على جدول الرحلات");
                              tv1.setText("من");
                              tv2.setText("الى");
                          }

                      }
                      else
                      {
                          ////reservations is null
                          Toast.makeText(getApplicationContext(),"No Cities to show", Toast.LENGTH_LONG).show();

                      }
                      citiesListSpinner = new ArrayList<>();
                      citiesListSpinner.addAll(citiesList);
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
            }// My hotel reservations
        });




    }

    public void searchFlightsSchedule(View view) {

    if(fromCity != 0 && toCity != 0)
    {
        Call<ResponseValue> call = service.getFlightSchedule(fromCity, toCity);
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful())
                {
                    ResponseValue responseValue = response.body();

                    if (responseValue != null) {

                        List<Flight> flight = responseValue.getFlightSchedule();

                        if (flight.size() != 0) {

                            setContentView(R.layout.shcedule_flights_list);
                            listView = (ListView) findViewById(R.id.listScheduleFlights);
                            progressBar = (ProgressBar) findViewById(R.id.progressScheduleFlights);
                            progressBar.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);

                            TextView tv111 = (TextView)findViewById(R.id.tvTitle4);
                            TextView tv = (TextView) findViewById(R.id.schedule_from);

                            TextView tv1 = (TextView) findViewById(R.id.schedule_to);

                            if(lan.getLanguage().equals("Arabic"))
                            {
                                tv111.setText("جدول الرحلات");
                                tv.setText("من: " + City1);
                                tv1.setText("الى: " + City2);

                            }
                            else
                            {
                                tv.setText("From: " + City1);
                                tv1.setText("To: " + City2);
                            }

                            arrayList = new ArrayList<>();
                            arrayList.addAll(flight);


                            final  FlightsScheduleAdapter adapter = new FlightsScheduleAdapter(getApplicationContext(), R.layout.row_schedule_flights, arrayList);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    int flightId = adapter.getItem(position).getId();
                                    ArrayList<Flight> obj = new ArrayList<>();
                                    for (Flight flight : arrayList) {
                                        if (flight.getId() == flightId) {
                                            obj.add(flight);
                                        }
                                    }
                                    Intent intent = new Intent(getApplicationContext(), BookFlightActivity.class);
                                    Bundle args = new Bundle();
                                    intent.putExtra(Intent.EXTRA_TEXT, flightId);
                                    args.putSerializable("FLIGHT", obj);
                                    intent.putExtra("BUNDLE", args);
                                    startActivity(intent);
                                }
                            });


                        }
                        else
                        {
                            if(lan.getLanguage().equals("Arabic"))
                            {
                                Toast.makeText(getApplicationContext(),"لا يوجد رحلات للعرض..", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"No Flights To Show..", Toast.LENGTH_LONG).show();
                            }
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
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"no connection..", Toast.LENGTH_LONG).show();

            }
        });
    }
    else
    {
        Toast.makeText(getApplicationContext(),"Please File All the Failed..", Toast.LENGTH_LONG).show();

    }

    }


}
