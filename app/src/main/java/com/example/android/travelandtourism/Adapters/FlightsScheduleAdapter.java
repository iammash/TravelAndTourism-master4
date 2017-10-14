package com.example.android.travelandtourism.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.travelandtourism.Models.Flight;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;

import java.util.List;

import io.realm.Realm;

/**
 * Created by haya on 07/09/2017.
 */

public class FlightsScheduleAdapter extends ArrayAdapter<Flight> {

    private Context context;
    private List<Flight> flightList;
    Realm realm1;
    Language lan;

    public FlightsScheduleAdapter(Context context, int resource, List<Flight> objects) {
        super(context, resource, objects);
        this.context = context;
        this.flightList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_schedule_flights, parent, false);

        Flight flight = flightList.get(position);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        TextView tv1 = (TextView) view.findViewById(R.id.row_scheduleDate);
        TextView tv2 = (TextView) view.findViewById(R.id.row_scheduleTime);
        TextView tv3 = (TextView) view.findViewById(R.id.row_scheduleAirlineNma);

        if(lan.getLanguage().equals("Arabic"))
        {
            tv1.setText("التاريخ: "+flight.getDisplayDate());
            tv2.setText("الساعة: "+flight.getTime());
            tv3.setText("الخطوط الجوية: "+flight.getAirline());

        }
        else
            {
                tv1.setText("Date: "+flight.getDisplayDate());
                tv2.setText("Time: "+flight.getTime());
                tv3.setText("Airlines: "+flight.getAirline());
            }

        return view;
    }

}
