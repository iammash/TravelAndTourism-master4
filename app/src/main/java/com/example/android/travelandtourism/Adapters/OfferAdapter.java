package com.example.android.travelandtourism.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.Offer;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;

/**
 * Created by haya on 11/09/2017.
 */

public class OfferAdapter extends ArrayAdapter<Offer> {


    String url = "http://dsh-tourism.somee.com";
    private Context context;
    private List<Offer> offerList;

    Realm realm1;
    Language lan;
    public OfferAdapter(Context context, int resource, List<Offer> objects) {
        super(context, resource, objects);
        this.context = context;
        this.offerList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_offer, parent, false);

        Offer offer = offerList.get(position);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();



        TextView tv = (TextView)view.findViewById(R.id.tvOfferDestination);
        tv.setText(offer.getFlightReservations().get(0).getFlight().getDestinationCity().getNameEn());
        TextView tv1 =(TextView)view.findViewById(R.id.tvOfferDuration);
        tv1.setText("Duration: "+offer.getDuration().toString());
        TextView tv2 = (TextView)view.findViewById(R.id.tvOfferCustCount);
        tv2.setText("Offered for: "+ offer.getCustomersCount().toString() +" People");
        TextView tv3 = (TextView)view.findViewById(R.id.tvOldPrice);
        tv3.setText(offer.getPrice().toString());
        tv3.setPaintFlags(tv3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        TextView tv4 = (TextView)view.findViewById(R.id.tvNewPrice);
        tv4.setText(offer.getNewPrice().toString());

        if(lan.getLanguage().equals("Arabic"))
        {
            TextView tv00 = (TextView)view.findViewById(R.id.tvGoTo);
            TextView tv01 = (TextView)view.findViewById(R.id.tvNew);
            TextView tv02 =(TextView)view.findViewById(R.id.tvOld);

            tv00.setText("اذهب الى ");
            tv01.setText("السعر الجديد ");
            tv02.setText("السعر القديم ");

            tv.setText(offer.getFlightReservations().get(0).getFlight().getDestinationCity().getNameAr());
            tv1.setText("المدة: "+offer.getDuration().toString());
            tv2.setText("مقدم لـ : "+ offer.getCustomersCount().toString() +" اشخاص");
            tv3.setText(offer.getPrice().toString());
            tv3.setPaintFlags(tv3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv4.setText(offer.getNewPrice().toString());
        }
        else
        {
            tv.setText(offer.getFlightReservations().get(0).getFlight().getDestinationCity().getNameEn());
            tv1.setText("Duration: "+offer.getDuration().toString());
            tv2.setText("Offered for: "+ offer.getCustomersCount().toString() +" People");
            tv3.setText(offer.getPrice().toString());
            tv3.setPaintFlags(tv3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv4.setText(offer.getNewPrice().toString());
        }



        List<Images> cityImagesList = offer.getHotelReservations().get(0).getRoom().getHotel().getCity().getImages();
        if(cityImagesList.size()!= 0){
            String img1 = cityImagesList.get(0).getPath();
            ImageView iv = (ImageView) view.findViewById(R.id.ivOfferCityImage);
            Picasso.with(getContext()).load(url+img1.substring(1)).fit().centerCrop().into(iv);
        }



        return view;


    }
}

