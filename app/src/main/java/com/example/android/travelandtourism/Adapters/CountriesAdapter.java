package com.example.android.travelandtourism.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.travelandtourism.Models.Countries;

import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;

/**
 * Created by haya on 23/08/2017.
 */

public class CountriesAdapter extends ArrayAdapter<Countries> {
    String url = "http://dsh-tourism.somee.com";
    private Context context;
    Realm realm1;
    Language lan;
    private List<Countries> countriesList;
    public CountriesAdapter(Context context, int resource, List<Countries> objects) {
        super(context, resource, objects);
        this.context = context;
        this.countriesList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_country,parent,false);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        Countries countries = countriesList.get(position);

        TextView tv = (TextView) view.findViewById(R.id.ivname_En);

        if(lan.getLanguage().equals("Arabic"))
        {
            tv.setText(countries.getNameAr());

        }
        else
            {
                tv.setText(countries.getNameEn());

            }


        ImageView img = (ImageView) view.findViewById(R.id.ivflag);
        Picasso.with(getContext()).load(url+countries.getFlag().substring(1)).resize(200,200).into(img);
        return view;
    }
}
