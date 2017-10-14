package com.example.android.travelandtourism.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.HotelRoom;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;

import java.util.List;

import io.realm.Realm;

/**
 * Created by haya on 03/09/2017.
 */

public class HotelRoomsAdapter extends ArrayAdapter<HotelRoom> {
    private Context context;
    private List<HotelRoom> hotelRoomList;

    Realm realm1;
    Language lan;
    public HotelRoomsAdapter(Context context, int resource, List<HotelRoom> objects) {
        super(context, resource, objects);
        this.context = context;
        this.hotelRoomList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_room, parent, false);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();


        HotelRoom HR = hotelRoomList.get(position);

        TextView tv =(TextView)view.findViewById(R.id.tvHR_name);
        TextView tv1 =(TextView)view.findViewById(R.id.tvHRType);
        TextView tv2 =(TextView)view.findViewById(R.id.tvHRCostumerCount);

        TextView tv5 = (TextView)view.findViewById(R.id.tvHRPrice);
        TextView tv6 =(TextView)view.findViewById(R.id.tvReserv);

        if(lan.getLanguage().equals("Arabic"))
        {
            tv.setText("اسم الغرفة: "+HR.getRoom().getName().toString());
            tv1.setText("نوع الغرفة: "+HR.getRoom().getType().toString());
            tv2.setText("عدد الأشخاص: "+HR.getRoom().getCustCount().toString());
            TextView tv4 =(TextView)view.findViewById(R.id.tvHRDetailsAr);
            tv4.setText("تفاصيل الغرفة: "+HR.getRoom().getDetailsAr());
            tv5.setText("سعر اليلة: "+HR.getNightPrice().toString() +"$");
            tv6.setText("احجز الغرفة");
        }
        else
        {
            tv.setText("Room Name: "+HR.getRoom().getName().toString());
            tv1.setText("Room Type: "+HR.getRoom().getType().toString());
            tv2.setText("Customer Number: "+HR.getRoom().getCustCount().toString());
            TextView tv3 =(TextView)view.findViewById(R.id.tvHRDetailsEng);
            tv3.setText("Details in English: "+HR.getRoom().getDetailsEn());
            tv5.setText("Night Price: "+HR.getNightPrice().toString() +"$");
        }


        return  view;
    }
}
