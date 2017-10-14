package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.Message;
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
 * Created by haya on 22/09/2017.
 */

public class SendMessageActivity extends AppCompatActivity {

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    EditText edEmail;
    EditText edSubj;
    EditText edMessage;
    Button btnMsg;

    Realm realm1;
    Language lan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        setContentView(R.layout.activity_send_message);
        edEmail = (EditText)findViewById(R.id.txtEmail);
        edSubj =(EditText)findViewById(R.id.txtEmailSubj);
        edMessage = (EditText)findViewById(R.id.txMessage);

        btnMsg =(Button)findViewById(R.id.btnSend);
        if(lan.getLanguage().equals("Arabic"))
        {
            TextView tv1 =(TextView)findViewById(R.id.tvTitle11);
            TextView tv2 =(TextView)findViewById(R.id.tvEmail);
            TextView tv3 =(TextView)findViewById(R.id.tvSubj);
            TextView tv4 =(TextView)findViewById(R.id.tvMSG);

            tv1.setText("ارسل رسالة لادارة التطبيق");
            tv2.setText("ادخل بريدك الالكتروني");
            tv3.setText("عنوان الرسالة");
            tv4.setText("نص الرسالة");
            btnMsg.setText("ارسل");

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void buttonOnClick(View view)
    {
        if (edEmail.getText().toString().trim().length() != 0 && edSubj.getText().toString().trim().length() != 0
                && edMessage.getText().toString().trim().length() != 0)
        {

            Call<Message> call = service.SendMessage(edEmail.getText().toString(),edSubj.getText().toString(),edMessage.getText().toString());
            call.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    //Message message = response.body();
                  //  String result = message.getMessage();
               //     int  cc = response.code();
                    if(lan.getLanguage().equals("Arabic"))
                    {
                        Toast.makeText(getApplicationContext(),"تم ارسال الرسالة بنجاح", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Message Send Successfully .", Toast.LENGTH_LONG).show();
                    }

                    Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"Error....", Toast.LENGTH_LONG).show();

                }
            });
        }
        else
        {
            if(lan.getLanguage().equals("Arabic"))
            {
                Toast.makeText(getApplicationContext(),"الرجاء اكمال جميع الحقول المطلوبة..", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"please fill all the required fields", Toast.LENGTH_LONG).show();
            }


        }
    }
}
