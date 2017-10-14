package com.example.android.travelandtourism.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;

import java.util.List;

/**
 * Created by haya on 24/08/2017.
 */

public class UserAdapter extends ArrayAdapter<UserModel> {
    String url = "http://dsh-tourism.somee.com";
    private Context context;
    private List<UserModel> userList;

    public UserAdapter(Context context, int resource, List<UserModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.userList = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_user,parent,false);
        UserModel user = userList.get(position);
        TextView tv = (TextView) view.findViewById(R.id.ivuserid);
        tv.setText(user.getId());
        TextView tv1 =(TextView) view.findViewById(R.id.tvuserFName);
        tv1.setText("First Name: "+user.getFirstName());
        TextView tv3 = (TextView) view.findViewById(R.id.tvuserLName);
        tv3.setText("Last Name: "+user.getLastName());
        TextView tv4 = (TextView) view.findViewById(R.id.tvuserName);
        tv4.setText("Username: "+user.getUserName());
        TextView tv5 = (TextView) view.findViewById(R.id.tvuserEmail);
        tv5.setText("Email: "+user.getEmail());
        TextView tv6 = (TextView) view.findViewById(R.id.tvuserPhone);
        tv6.setText("Phone: "+user.getPhoneNumber());
        TextView tv7 = (TextView) view.findViewById(R.id.tvuserCountry);
        tv7.setText("Country: "+user.getCountry());
        TextView tv8 = (TextView) view.findViewById(R.id.tvuserCity);
        tv8.setText("City: "+user.getCity());
        TextView tv9 = (TextView) view.findViewById(R.id.tvuserGender);
        tv9.setText("Gender: "+user.getGender());
        TextView tv10 = (TextView) view.findViewById(R.id.tvuserCridet);
        tv10.setText("Cridet: "+user.getCredit().toString());

        return view;
    }

}
