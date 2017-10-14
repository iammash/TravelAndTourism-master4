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

import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.Offer;
import com.example.android.travelandtourism.Models.OfferConfermation;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by haya on 18/09/2017.
 */

public class MyOfferReservationAdapter extends ArrayAdapter<OfferConfermation> {

    String url = "http://dsh-tourism.somee.com";
    private Context context;
    private List<OfferConfermation> offerConfermations;

    Realm realm1;
    Language lan;
    public MyOfferReservationAdapter(Context context, int resource, List<OfferConfermation> objects) {
        super(context, resource, objects);
        this.context = context;
        this.offerConfermations = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_offer_reservation, parent, false);


        OfferConfermation offer = offerConfermations.get(position);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        TextView tv = (TextView)view.findViewById(R.id.tvOfferDestination);
        TextView tv1 =(TextView)view.findViewById(R.id.tvOfferDuration);
        TextView tv2 = (TextView)view.findViewById(R.id.tvOfferCustCount);
        TextView tv3 = (TextView)view.findViewById(R.id.tvResNumber);
        TextView tv4 = (TextView)view.findViewById(R.id.tvNewPrice);

        if(lan.getLanguage().equals("Arabic"))
       {
           tv.setText(offer.getOffer().getFlightReservations().get(0).getFlight().getDestinationCity().getNameAr());
           tv1.setText("لمدة: "+offer.getOffer().getDuration().toString());
           tv2.setText("مقدم لـ: "+ offer.getOffer().getCustomersCount().toString() +" شخص");
           tv3.setText(offer.getId().toString());
           tv4.setText(offer.getOffer().getNewPrice().toString());

       }
       else
        {
            tv.setText(offer.getOffer().getFlightReservations().get(0).getFlight().getDestinationCity().getNameEn());
            tv1.setText("Duration: "+offer.getOffer().getDuration().toString());
            tv2.setText("Offered for: "+ offer.getOffer().getCustomersCount().toString() +" People");
            tv3.setText(offer.getId().toString());
            tv4.setText(offer.getOffer().getNewPrice().toString());
        }

       /**/
        List<Images> cityImagesList = offer.getOffer().getFlightReservations().get(0).getFlight().getDestinationCity().getImages();
        if(cityImagesList.size()!= 0){
            String img1 = cityImagesList.get(0).getPath();
            ImageView iv = (ImageView) view.findViewById(R.id.ivOfferCityImage);
            Picasso.with(getContext()).load(url+img1.substring(1)).fit().centerCrop().into(iv);
        }


        return  view;

    }






    }
