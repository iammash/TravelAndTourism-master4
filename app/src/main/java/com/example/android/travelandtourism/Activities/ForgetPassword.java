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
 * Created by haya on 24/09/2017.
 */

public class ForgetPassword extends AppCompatActivity {

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    Realm realm1;
    Language lan;

    EditText ed;
    TextView textView;
    Button button_resetPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        setContentView(R.layout.activity_forget_password);
        ed= (EditText)findViewById(R.id.edEmailAdd);


        if(lan.getLanguage().equals("Arabic"))
        {
            textView =(TextView)findViewById(R.id.tvMessage);
            textView.setText("ادخل بريدك الالكتروني المسجل به للتحقق وارسال كلمة المرور الجديدة عليه");
            button_resetPassword =(Button)findViewById(R.id.button_resetPassword);
            button_resetPassword.setText("ارسل");
        }

    }

    public void button_resetPassword(View view)
    {
       if(ed.getText().toString().trim().length() != 0)
       {
           Call<Message> call = service.forgetPassword(ed.getText().toString());
           call.enqueue(new Callback<Message>() {
               @Override
               public void onResponse(Call<Message> call, Response<Message> response) {
                   if(response.isSuccessful())
                   {
                       setContentView(R.layout.activity_forget_password);
                       if(lan.getLanguage().equals("Arabic"))
                       {
                           Toast.makeText(getApplicationContext(),"تم اعادة تعيين كلمة مرور لحسابك.. الرجاء الاطلاع على بريدك الالكتروني للحصول على الكلمة الجديدة.", Toast.LENGTH_LONG).show();
                       }
                       else
                       {
                           Toast.makeText(getApplicationContext(),"Password Reset and send to your email, Please check Your email..", Toast.LENGTH_SHORT).show();
                       }

                       Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                       startActivity(intent);
                   }
                   else
                   {
                       Toast.makeText(getApplicationContext(),"some information wrong!!", Toast.LENGTH_SHORT).show();

                   }
               }

               @Override
               public void onFailure(Call<Message> call, Throwable t) {

               }
           });
       }
       else
           {
               Toast.makeText(getApplicationContext(),"Please enter valid Email!!", Toast.LENGTH_SHORT).show();
           }


    }
}
