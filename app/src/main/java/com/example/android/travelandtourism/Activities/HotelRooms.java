package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.travelandtourism.Adapters.HotelRoomsAdapter;
import com.example.android.travelandtourism.Models.HotelRoom;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by haya on 03/09/2017.
 */

public class HotelRooms extends AppCompatActivity {

    ListView listView;
    ProgressBar progressBar;
    Realm realm1;
    Language lan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        setContentView(R.layout.activity_hotel_rooms);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");


        ArrayList<HotelRoom> object = (ArrayList<HotelRoom>) args.getSerializable("RoomLIST");

        //List<Bar> bars = getArguments().getParcelable("bars");
        //ArrayList<HotelRoom> object = args.getParcelable("RoomLIST");

        Intent intent1 = getIntent();
        String hotelName =intent1.getStringExtra("hotelName");
        Intent intent11 = getIntent();
        String hotelNameAr =intent11.getStringExtra("hotelNameAr");

        listView = (ListView) findViewById(R.id.listHotelRooms);
        progressBar = (ProgressBar) findViewById(R.id.progressHoteRooms);
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);

        TextView tv = (TextView)findViewById(R.id.tvHR_HotelNme);
        TextView tv2 = (TextView)findViewById(R.id.tvHR_HotelNmeAr);

        if(lan.getLanguage().equals("Arabic"))
        {
            tv.setText(hotelName+ ", ");
            tv2.setText( ", غرف فندق:  "+hotelNameAr);

        }
        else
            {
                tv.setText(hotelName+" Rooms:");
            }


       final HotelRoomsAdapter adapter = new HotelRoomsAdapter(getApplicationContext(),R.layout.row_room,object);
        listView.setAdapter(adapter);


       // Button button = (Button) findViewById(R.id.btnReserveRoom);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int roomId = adapter.getItem(position).getId();
                int nightPrice = adapter.getItem(position).getNightPrice();

                Intent intent=new Intent(getApplicationContext(), BookRoomActivity.class)
                        .putExtra(Intent.EXTRA_TEXT,roomId);
                intent.putExtra("NightPrice",nightPrice);
                startActivity(intent);

            }
        });


    }
}
