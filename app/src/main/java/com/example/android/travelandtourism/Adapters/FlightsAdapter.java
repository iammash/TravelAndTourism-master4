package com.example.android.travelandtourism.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelandtourism.Models.Flight;
import com.example.android.travelandtourism.Models.Hotel;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;

/**
 * Created by haya on 07/09/2017.
 */

public class FlightsAdapter extends ArrayAdapter<Flight> {

    Realm realm1;
    Language lan;
    private Context context;
    private List<Flight> flightList;

    public FlightsAdapter(Context context, int resource, List<Flight> objects) {
        super(context, resource, objects);
        this.context = context;
        this.flightList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_flight,parent,false);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        Flight flight = flightList.get(position);


        if(lan.getLanguage().equals("Arabic"))
        {
            TextView tv1 = (TextView) view.findViewById(R.id.tvRowFromCity);
            tv1.setText("من "+flight.getSourceCity().getNameAr());
            TextView tv2 = (TextView) view.findViewById(R.id.tvRowToCity);
            tv2.setText("الى "+flight.getDestinationCity().getNameAr());
            TextView tv3 = (TextView) view.findViewById(R.id.tvRowFromHour);
            tv3.setText("الساعة: "+flight.getTime());
            TextView tv4 = (TextView) view.findViewById(R.id.tvFlightDuration);
            tv4.setText("مدة الرحلة: "+flight.getFlightDuration());
            TextView tv05 =(TextView)view.findViewById(R.id.tvEco);
            tv05.setText("الدرجة الاقتصادية");
            TextView tv5 = (TextView) view.findViewById(R.id.tvEconomyPrice);
            tv5.setText(flight.getEconomyTicketPrice().toString()+"$");
            TextView tv06 =(TextView)view.findViewById(R.id.tvBus);
            tv06.setText("الدرجة الأولى");
            TextView tv6 = (TextView) view.findViewById(R.id.tvBusinessPrice);
            tv6.setText(flight.getFirstClassTicketPrice().toString()+"$");
            TextView tv7 = (TextView) view.findViewById(R.id.tvAirlineNma);
            tv7.setText("شركة طيران: "+flight.getAirline());
        }
        else
        {
            TextView tv1 = (TextView) view.findViewById(R.id.tvRowFromCity);
            tv1.setText("From "+flight.getSourceCity().getNameEn());
            TextView tv2 = (TextView) view.findViewById(R.id.tvRowToCity);
            tv2.setText("To "+flight.getDestinationCity().getNameEn());
            TextView tv3 = (TextView) view.findViewById(R.id.tvRowFromHour);
            tv3.setText("at: "+flight.getTime());
            TextView tv4 = (TextView) view.findViewById(R.id.tvFlightDuration);
            tv4.setText("Duration: "+flight.getFlightDuration());
            TextView tv5 = (TextView) view.findViewById(R.id.tvEconomyPrice);
            tv5.setText(flight.getEconomyTicketPrice().toString()+"$");
            TextView tv6 = (TextView) view.findViewById(R.id.tvBusinessPrice);
            tv6.setText(flight.getFirstClassTicketPrice().toString()+"$");
            TextView tv7 = (TextView) view.findViewById(R.id.tvAirlineNma);
            tv7.setText("Airlines Name: "+flight.getAirline());
        }



        /*        int showSeat = flight.getSeatsCount();
        if(showSeat <=10)
        {
            TextView tv8 = (TextView) view.findViewById(R.id.tvEconomySeatNum);
            tv8.setVisibility(View.GONE);
            tv8.setText("Only "+flight.getSeatsCount().toString()+"seat");
        }
*/

        return view;
    }
}
