package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by haya on 24/09/2017.
 */

public class ChargeCredit extends AppCompatActivity {

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    UserModel thisUser;
    Realm realm;

    EditText ed;
    Realm realm1;
    Language lan;
    int mony;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        setContentView(R.layout.activity_charge_credit);

        EditText ed1 = (EditText)findViewById(R.id.edNameOnCard);
        EditText ed2 = (EditText)findViewById(R.id.edCardNumber);
        EditText ed3 = (EditText)findViewById(R.id.edCvc);
        ed = (EditText)findViewById(R.id.edNewCredit);
        Spinner sp =(Spinner)findViewById(R.id.spinner);
        Spinner sp1 =(Spinner)findViewById(R.id.spinner1);
        Spinner sp2 =(Spinner)findViewById(R.id.spinner2);

        if(lan.getLanguage().equals("Arabic"))
        {
            TextView tv1 = (TextView)findViewById(R.id.tvCCType);
            TextView tv2 = (TextView)findViewById(R.id.tvCCName);
            TextView tv3 = (TextView)findViewById(R.id.tvCCNumber);
            TextView tv4 = (TextView)findViewById(R.id.tvCCEDate);
            TextView tv5 = (TextView)findViewById(R.id.tvCCNewCredit);

            tv1.setText("                                نوع البطاقة");
            tv2.setText("                           اسم صاحب البطاقة");
            tv3.setText("                                رقم البطاقة");
            tv4.setText("تاريخ انتهاء البطاقة");
            tv5.setText("                                          المبلغ");
        }


        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm.beginTransaction();
        thisUser = realm.where(UserModel.class).findFirst();
        realm.commitTransaction();
//Dania
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    public void button_submitCharge(View view)
    {
        if(thisUser.getId() != null)
        {
            if(ed.getText().toString().trim().length() != 0)
            {
                Call<ResponseValue> call = service.ChargeCredit(thisUser.getId(), Integer.valueOf(ed.getText().toString()));
                call.enqueue(new Callback<ResponseValue>() {
                    @Override
                    public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                        if(response.isSuccessful())
                        {
                            ResponseValue responseValue=response.body();
                            if(responseValue != null)
                            {
                                UserModel userModel = responseValue.getChargeCridet();
                                mony = Integer.valueOf(ed.getText().toString());
                                if(lan.getLanguage().equals("Arabic"))
                                {
                                    Toast.makeText(getApplicationContext(),"تم شحن الرصيد بنجاح!!", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"success charge", Toast.LENGTH_LONG).show();
                                }
                                UpdateCredit();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();

                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseValue> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();

                    }
                });
            }
            else
            {
                if(lan.getLanguage().equals("Arabic"))
                {
                    Toast.makeText(getApplicationContext(),"الرجاء ادخال المبلغ المطلوب..", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Enter the value to charge..", Toast.LENGTH_LONG).show();
                }
            }
        }

        else
        {
            if(lan.getLanguage().equals("Arabic"))
            {
                Toast.makeText(getApplicationContext(),"الرجاء تسجيل الدخول للقيام بشحن الرصيد", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please Login to charge", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void UpdateCredit() {
        Realm realm3 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        // This query is fast because "character" is an indexed field
        realm3.beginTransaction();

        UserModel user = realm3.where(UserModel.class)
                .equalTo("id", thisUser.getId())
                .findFirst();
        user.setCredit(user.getCredit() + mony);
        realm3.commitTransaction();
    }


}
