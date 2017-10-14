package com.example.android.travelandtourism.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.MyOfferReservationAdapter;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Countries;
import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.OfferConfermation;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
 * Created by haya on 19/09/2017.
 */

public class MyOfferReservations extends AppCompatActivity {
    ProgressBar progressBar;
    private ListView listView;

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    Realm realm2;
    Language lan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Realm realm1 = Realm.getDefaultInstance();
        realm1.beginTransaction();
        final UserModel userModel = realm1.where(UserModel.class).findFirst();
        realm1.commitTransaction();

        realm2 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm2.beginTransaction();
        lan = realm2.where(Language.class).findFirst();
        realm2.commitTransaction();

        Call <ResponseValue> call = service.getMyOfferReservations(userModel.getId());
        call.enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful())
                {
                    ResponseValue responseValue = response.body();
                    if(responseValue != null)
                    {
                        final List<OfferConfermation> offerConfermations = responseValue.getOfferConfermationList();
                        if(offerConfermations.size() != 0)
                        {
                            setContentView(R.layout.activity_my_offer_reservations);
                            listView = (ListView)findViewById(R.id.listOffersReservation);
                            progressBar =(ProgressBar)findViewById(R.id.progressMyOR);
                            progressBar.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            if(lan.getLanguage().equals("Arabic"))
                            {
                                TextView tvTit = (TextView)findViewById(R.id.tvTitle10);
                                tvTit.setText("حجوزات العروض السياحية");
                            }

                            final ArrayList<OfferConfermation> arrayList = new ArrayList<>();//create a list to store the objects
                            arrayList.addAll(offerConfermations);

                            final MyOfferReservationAdapter adapter =
                                    new MyOfferReservationAdapter(getApplicationContext(), R.layout.row_offer_reservation,arrayList);

                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    int reservationId = adapter.getItem(position).getId();
                                    RealmList<OfferConfermation> offerRealm= new RealmList<>();
                                    offerRealm.addAll(offerConfermations);

                            final ArrayList<OfferConfermation> of = new ArrayList<>();
                                    int i=0;
                                    for(OfferConfermation c : offerRealm)
                                    {
                                        if(offerConfermations.get(i).getId() == reservationId)
                                        {
                                            of.add(c);
                                        }
                                        i++;
                                    }
                                    Intent intent=new Intent(getApplicationContext(), MyOfferReservationActivity.class)
                                            .putExtra(Intent.EXTRA_TEXT,reservationId);
                                    startActivity(intent);
                                    /*  Bundle args = new Bundle();
                                    args.putSerializable("OFFER",of);
                                    intent.putExtra("BUNDLE",args);*/

                                }
                            });

                        }
                        else
                        {
                            if(lan.getLanguage().equals("Arabic"))
                            {
                                Toast.makeText(getApplicationContext(),"لا يوجد لديك اية حجوزات حالباً..", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"You Have not any reservations!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"no response ..", Toast.LENGTH_LONG).show();
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
