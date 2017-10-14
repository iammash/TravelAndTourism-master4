package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.travelandtourism.Adapters.MyHotelReservationsAdapter;
import com.example.android.travelandtourism.Models.HotelReservations;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by haya on 05/09/2017.
 */

public class MyHotelReservations extends AppCompatActivity {
    ProgressBar progressBar;
    private ListView listView;

    Realm realm1;
    Language lan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();
        setContentView(R.layout.activity_my_hotel_reservations);

        TextView tvTitle =(TextView)findViewById(R.id.tvTitle7);
        if(lan.getLanguage().equals("Arabic"))
        {
            tvTitle.setText("حجوزاتي الفندقية");
        }
        listView = (ListView) findViewById(R.id.listHotelReservation);
        progressBar = (ProgressBar) findViewById(R.id.progressMyHR);
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);

        Realm.init(getApplicationContext());
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<HotelReservations> reservations = realm.where(HotelReservations.class).findAll();
        realm.commitTransaction();
        final List <HotelReservations> arrayList = new ArrayList<>();
        arrayList.addAll(reservations);

        if(arrayList.size() == 0)
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
        else
        {
           final MyHotelReservationsAdapter adapter =
                    new MyHotelReservationsAdapter(getApplicationContext(),R.layout.row_hotel_reservation,arrayList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int reservationId = adapter.getItem(position).getId();
                    Intent intent=new Intent(getApplicationContext(), MyHotelReservationActivity.class)
                            .putExtra(Intent.EXTRA_TEXT,reservationId);
                    startActivity(intent);

                }
            });


        }

    }
}
