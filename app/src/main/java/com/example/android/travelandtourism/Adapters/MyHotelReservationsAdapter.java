package com.example.android.travelandtourism.Adapters;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.HotelReservations;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;

/**
 * Created by haya on 05/09/2017.
 */

public class MyHotelReservationsAdapter extends ArrayAdapter<HotelReservations> {

    String url = "http://dsh-tourism.somee.com";
    private Context context;
    private List<HotelReservations> hotelReservationsList;
    Realm realm1;
    Language lan;

    public MyHotelReservationsAdapter(Context context, int resource, List<HotelReservations> objects) {
        super(context, resource, objects);
        this.context = context;
        this.hotelReservationsList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_hotel_reservation, parent, false);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        HotelReservations res = hotelReservationsList.get(position);

        TextView tv = (TextView)view.findViewById(R.id.ivMyResHotelName);
        TextView tv2 = (TextView)view.findViewById(R.id.ivMyResHotelCity);
        TextView tv3 = (TextView)view.findViewById(R.id.ivMyResHotelCheck_in);
        tv3.setText(res.getDisplayCheckInDate());

        if(lan.getLanguage().equals("Arabic"))
        {
            tv.setText(res.getRoom().getHotel().getNameEn()+"/ \n "+res.getRoom().getHotel().getNameAr());
            tv2.setText(res.getRoom().getHotel().getCity().getNameEn()+"/ \n "+res.getRoom().getHotel().getCity().getNameAr());
        }
        else
        {
            tv.setText(res.getRoom().getHotel().getNameEn());
            tv2.setText(res.getRoom().getHotel().getCity().getNameEn());
        }

        List<Images> HotelImagesList = res.getRoom().getHotel().getImages();
        if(HotelImagesList.size()!= 0){
            String img1 = HotelImagesList.get(0).getPath();
            ImageView iv = (ImageView) view.findViewById(R.id.ivMyResHotelImage);
            Picasso.with(getContext()).load(url+img1.substring(1)).fit().centerCrop().into(iv);
        }


        return view;
    }
}
