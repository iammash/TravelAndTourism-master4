package com.example.android.travelandtourism.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.HotelReservations;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;

/**
 * Created by haya on 07/09/2017.
 */

public class MyFlightReservationAdapter extends ArrayAdapter<FlightReservation> {

    String url = "http://dsh-tourism.somee.com";
    private Context context;
    private List<FlightReservation> flightReservationList;
    Realm realm1;
    Language lan;

    public MyFlightReservationAdapter(Context context, int resource, List<FlightReservation> objects) {
        super(context, resource, objects);
        this.context = context;
        this.flightReservationList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_flight_reservation, parent, false);

        FlightReservation res = flightReservationList.get(position);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        TextView tv = (TextView)view.findViewById(R.id.rowFly_From);
        TextView tv2 = (TextView)view.findViewById(R.id.rowFly_To);
        TextView tv3 = (TextView)view.findViewById(R.id.rowFly_Date);

        if(lan.getLanguage().equals("Arabic"))
        {
            tv.setText(res.getFlight().getSourceCity().getNameAr() + " الى --> ");
            tv2.setText(res.getFlight().getDestinationCity().getNameAr());
            tv3.setText(res.getFlight().getDisplayDate());
        }
        else
            {
                tv.setText(res.getFlight().getSourceCity().getNameEn() + " To --> ");
                tv2.setText(res.getFlight().getDestinationCity().getNameEn());
                tv3.setText(res.getFlight().getDisplayDate());
            }




        List<Images> CityImagesList = res.getFlight().getDestinationCity().getImages();
        if(CityImagesList.size()!= 0){
            String img1 = CityImagesList.get(0).getPath();
            ImageView iv = (ImageView) view.findViewById(R.id.ivMyResFlightCityImage);
            Picasso.with(getContext()).load(url+img1.substring(1)).resize(300,300).into(iv);
        }

        return  view;
    }



}
