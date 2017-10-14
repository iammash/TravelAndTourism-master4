package com.example.android.travelandtourism.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;

/**
 * Created by haya on 25/08/2017.
 */

public class CityAdapter extends ArrayAdapter<City> {
    String url = "http://dsh-tourism.somee.com";
    private Context context;
    private List<City> cityList;
    Realm realm1;
    Language lan;

    public CityAdapter(Context context, int resource, List<City> objects) {
        super(context, resource, objects);
        this.context = context;
        this.cityList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_city,parent,false);

        City city = cityList.get(position);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        TextView tv1 = (TextView) view.findViewById(R.id.ivname_EnCity);

        if(lan.getLanguage().equals("Arabic"))
        {
            tv1.setText(city.getNameEn());
            TextView tv2 = (TextView) view.findViewById(R.id.ivname_ArCity);
            tv2.setText(city.getNameAr());
        }
        else
        {
            tv1.setText(city.getNameEn());

        }



        List<Images> cityImagesList = city.getImages();
        if(cityImagesList.size()!= 0){
            String img1 = cityImagesList.get(0).getPath();
            ImageView iv = (ImageView) view.findViewById(R.id.ivCityImage);
            Picasso.with(getContext()).load(url+img1.substring(1)).fit().centerCrop().into(iv);
        }

        return view;
    }
}
