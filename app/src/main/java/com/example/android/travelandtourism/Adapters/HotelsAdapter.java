package com.example.android.travelandtourism.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.travelandtourism.Models.Hotel;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;

/**
 * Created by haya on 02/09/2017.
 */

public class HotelsAdapter extends ArrayAdapter<Hotel> {
    String url = "http://dsh-tourism.somee.com";
    private Context context;
    private List<Hotel> hotelList;
    Realm realm1;
    Language lan;

    public HotelsAdapter(Context context, int resource, List<Hotel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.hotelList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_hotel,parent,false);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        Hotel hotel = hotelList.get(position);

        TextView tv1 = (TextView) view.findViewById(R.id.ivname_EnHotel);
        tv1.setText(hotel.getNameEn());


        if(lan.getLanguage().equals("Arabic"))
        {
            tv1.setText(hotel.getNameEn());
            TextView tv2 = (TextView) view.findViewById(R.id.ivname_ArHotel);
            tv2.setText(hotel.getNameAr());
        }


        RatingBar rb = (RatingBar) view.findViewById(R.id.HotelStars);
        rb.setRating(hotel.getStars());

        List<Images> hotelImagesList = hotel.getImages();
        if(hotelImagesList.size()!= 0){
            String img1 = hotelImagesList.get(0).getPath();
            ImageView iv = (ImageView) view.findViewById(R.id.ivHotelImage);
            Picasso.with(getContext()).load(url+img1.substring(1)).fit().centerCrop().into(iv);
        }
        return view;
    }

    }

