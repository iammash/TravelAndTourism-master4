package com.example.android.travelandtourism.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Images;
import com.example.android.travelandtourism.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by haya on 29/08/2017.
 */

public class ImagesAdapter extends BaseAdapter {

    String url = "http://dsh-tourism.somee.com";
    private Context context;
    private List<Images> imagesList;

    public ImagesAdapter(Context context,  List<Images> objects) {
      //  super(context, resource, objects);
        this.context = context;
        this.imagesList = objects;
    }


    @Override
    public int getCount() {

        return imagesList.size();
    }

    @Override
    public Object getItem(int position) {

        return imagesList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_image,parent,false);

        Images image = imagesList.get(position);
        ImageView iv = (ImageView)view.findViewById(R.id.ivImage);
        String imgPath = image.getPath().substring(1);
        Picasso.with(this.context).load(url+imgPath).resize(1000,1000).into(iv);


        return view;

    }


}
