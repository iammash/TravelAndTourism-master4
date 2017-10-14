package com.example.android.travelandtourism.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.FlightsAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Flight;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.SpinnerModelView;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
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

public class SearchFligtsActivity extends FragmentActivity implements
        DatePickerDialog.OnDateSetListener {
    int test =0;
    EditText et;
    ProgressBar progressBar;
    private ListView listView;
    List<City> citiesList;
    ArrayList<City> citiesListSpinner;
    List<SpinnerModelView> citiesInSpinner;
    int fromCity=0;
    int toCity=0;
    Button btn;
    Button btn2;
    TextView tvFrom;
    TextView tvTo;
    TextView tvTitle;
    TextView tvDate;
    Realm realm1;
    Language lan;
    Button btnDoReserve;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Log.w("DatePicker",day+"/"+month+"/"+ year);
        if (test ==1){et.setText((month+1) +"/"+day+"/"+ year);}

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
                Log.e("Response", "get all the cities");
                ResponseValue responseValue2 = response.body();
                if(responseValue2 != null)
                {
                        citiesList = responseValue2.getCities();
                    setContentView(R.layout.activity_search_flights);

                    et= (EditText)findViewById(R.id.etFromDate);
                    btn = (Button)findViewById(R.id.btnshowDialog);
                    btn2 = (Button)findViewById(R.id.btnshowDialogTo);

                    tvTitle = (TextView)findViewById(R.id.tvTitle);
                    tvFrom =(TextView)findViewById(R.id.tvFrom);
                    tvTo =(TextView)findViewById(R.id.tvTo);
                    tvDate =(TextView)findViewById(R.id.tvDate);
                    btnDoReserve =(Button)findViewById(R.id.btnDoReserve);
                    if(lan.getLanguage().equals("Arabic"))
                    {

                        btn.setText("اختر مدينة الانطلاق");
                        btn2.setText("اختر مدينة الوجهه");
                        tvTitle.setText("ابحث عن رحلتك");
                        tvFrom.setText("من");
                        tvTo.setText("الى");
                        tvDate.setText("التاريخ");
                        btnDoReserve.setText("ابحـث");
                    }

                    et.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DialogFragment newFragment = new SearchFligtsActivity.DatePickerFragment();
                            newFragment.show(getSupportFragmentManager(), "datePicker");
                            test =1;
                        }
                    });
                }
                else
                {
                    ////reservations is null
                    Toast.makeText(getApplicationContext(),"Some thing wrong!!", Toast.LENGTH_LONG).show();

                }
                citiesListSpinner = new ArrayList<>();
                citiesListSpinner.addAll(citiesList);

             }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
            }// My hotel reservations
        });





    }

    public void button_searchFlight(View view)
    {
        if (et.getText().toString().trim().length() != 0 && fromCity !=0 && toCity !=0) {
                Call<ResponseValue> call = service.SearchFlight(fromCity,toCity,et.getText().toString());
                call.enqueue(new Callback<ResponseValue>() {
                    @Override
                    public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                        ResponseValue responseValue = response.body();

                        if (responseValue != null) {
                            List<Flight> flights = responseValue.getFlight();
                            if (flights.size() != 0) {
                            setContentView(R.layout.flights_list);
                            listView = (ListView) findViewById(R.id.listFlights);
                            progressBar = (ProgressBar) findViewById(R.id.progressFlights);
                            progressBar.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);

                            final ArrayList<Flight> arrayList = new ArrayList<>();
                            arrayList.addAll(flights);

                                TextView tv = (TextView) findViewById(R.id.tvFilterFlights);
                                if(lan.getLanguage().equals("Arabic"))
                                {
                                    tv.setText("الرحلات بتاريخ: " + flights.get(0).getDisplayDate());

                                }
                                else
                                {
                                    tv.setText("Flights at: " + flights.get(0).getDisplayDate());
                                }
                                final FlightsAdapter adapter = new FlightsAdapter(getApplicationContext(), R.layout.row_flight, arrayList);
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
                                    Toast.makeText(getApplicationContext(), "لا يوجد رحلات متوفرة في هذه التواريخ, الرجاء تجريب تاريخ آخر..", Toast.LENGTH_LONG).show();

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"No Flight Available in this Date, Please try another date..", Toast.LENGTH_LONG).show();
                                }

                            }
                    }
                    }

                    @Override
                    public void onFailure(Call<ResponseValue> call, Throwable t) {

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

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), (SearchFligtsActivity)getActivity(), year, month, day);
            Calendar today = Calendar.getInstance();
            today.clear(Calendar.HOUR); today.clear(Calendar.MINUTE); today.clear(Calendar.SECOND);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return dialog;
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Log.w("DatePicker",month+"/"+day+"/"+ year);

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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchFligtsActivity.this);
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



/*
*  public void ToCitySpinner(){
        FillSpinner();
        ArrayAdapter citySpinnerAdapter = new ArrayAdapter(this, R.layout.spinner, citiesInSpinner);
        Spinner toCitySpinner = (Spinner) findViewById(R.id.spinner1);
        toCitySpinner.setAdapter(citySpinnerAdapter);
        // to get selected item
        SpinnerModelView spinnerModelView = (SpinnerModelView)((Spinner) findViewById(R.id.spinner1)).getSelectedItem();
      //  toCity = spinnerModelView.getID();

    }

    public void FromCitySpinner(){
        FillSpinner();
        ArrayAdapter citySpinnerAdapter = new ArrayAdapter(this, R.layout.spinner, citiesInSpinner);
        Spinner toCitySpinner = (Spinner) findViewById(R.id.spinner2);
        toCitySpinner.setAdapter(citySpinnerAdapter);
        // to get selected item
        SpinnerModelView spinnerModelView = (SpinnerModelView)((Spinner) findViewById(R.id.spinner2)).getSelectedItem();
    //    fromCity=spinnerModelView.getID();

    }*/