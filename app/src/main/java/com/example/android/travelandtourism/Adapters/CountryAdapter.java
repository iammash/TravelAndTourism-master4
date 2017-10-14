package com.example.android.travelandtourism.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Countries;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haya on 25/08/2017.
 */

public class CountryAdapter extends ArrayAdapter {

    String url = "http://dsh-tourism.somee.com";
    private Context context;
    private List<Countries> countriesList;
    private ListView listView;

    public CountryAdapter(Context context, int resource, List<Countries> objects) {
        super(context, resource, objects);
        this.context = context;
        this.countriesList = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.country_info,parent,false);

        listView = (ListView) view.findViewById(R.id.listCities);
        listView.setVisibility(View.VISIBLE);

        Countries countries = countriesList.get(position);

        List<City> cc = countries.getCities();
        ArrayList<City> hh = new ArrayList<>();
        hh.addAll(cc);


            CitiesAdapter adapter = new CitiesAdapter(this.context,R.layout.row_city,hh);
            listView.setAdapter(adapter);



/*
* for (String object: list) {
    System.out.println(object);
}
* */


        TextView tv = (TextView) view.findViewById(R.id.ivname_En1);
        TextView tv1 = (TextView) view.findViewById(R.id.ivname_Ar1);
        ImageView img = (ImageView) view.findViewById(R.id.ivflag);

        tv.setText(countries.getNameEn());
        tv1.setText(countries.getNameAr());
        Picasso.with(getContext()).load(url+countries.getFlag().substring(1)).resize(200,200).into(img);

        return view;
    }
}
