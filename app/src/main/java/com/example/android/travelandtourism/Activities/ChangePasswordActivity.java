package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.Message;
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
import retrofit2.http.Field;

/**
 * Created by haya on 24/09/2017.
 */

public class ChangePasswordActivity extends AppCompatActivity {

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    EditText ed;
    EditText ed1;
    EditText ed2;

    TextView tv1;
    TextView tv2;
    TextView tv3;

    Realm realm;
    UserModel thisUser;
    Realm realm1;
    Language lan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        setContentView(R.layout.activity_change_password);

        ed =(EditText)findViewById(R.id.edCurrentPW);
        ed1=(EditText)findViewById(R.id.edNewPW);
        ed2=(EditText)findViewById(R.id.edConfirmPW);

        if(lan.getLanguage().equals("Arabic"))
        {
            tv1=(TextView)findViewById(R.id.tvCPCurrent);
            tv2=(TextView)findViewById(R.id.tvCPNew);
            tv3=(TextView)findViewById(R.id.tvCPConf);

            tv1.setText("       كلمة المرور الحالية");
            tv2.setText("       كلمة المرور الجديدة");
            tv3.setText("         تأكيد كلمة المرور");

        }

        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm.beginTransaction();
        thisUser = realm.where(UserModel.class).findFirst();
        realm.commitTransaction();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void ChangePW(View view)
    {
        if(ed.getText().toString().trim().length() != 0 && ed2.getText().toString().trim().length() != 0
                && ed2.getText().toString().trim().length() != 0)
        {
            if(ed1.getText().toString().equals(ed2.getText().toString()))
            {


                Call<Message> call = service.ChangePassword(thisUser.getId(),ed.getText().toString(),ed1.getText().toString());
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if(response.isSuccessful())
                        {
                            Message message = response.body();
                            if(message!=null)
                            {
                                String message1 = message.getMessage();

                                if(message1.equals("PasswordChanged"))
                                {
                                    if(lan.getLanguage().equals("Arabic"))
                                    {
                                        Toast.makeText(getApplicationContext(),"تم تغيير كلمة المرور بنجاح", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Password changed successfully!!", Toast.LENGTH_LONG).show();
                                    }

                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(intent);
                                }
                                else
                                    {
                                        if(lan.getLanguage().equals("Arabic"))
                                        {
                                            Toast.makeText(getApplicationContext(),"لم يتم تغيير كلمة السر, الرجاء ادخال معلومات ملائمة", Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),"Password not changed.. try again with true values", Toast.LENGTH_LONG).show();
                                        }
                                    }
                            }
                            else
                                {
                                    Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                                }

                        }
                        else
                        {
                            if(lan.getLanguage().equals("Arabic"))
                            {
                                Toast.makeText(getApplicationContext(),"لم يتم تغيير كلمة السر, الرجاء ادخال معلومات ملائمة", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Password not changed.. try again with true values", Toast.LENGTH_LONG).show();
                            }                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();

                    }
                });
            }
            else {
                if(lan.getLanguage().equals("Arabic"))
                {
                    Toast.makeText(getApplicationContext(),"كلمة السر غير مطابقة للتأكيد", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"New Password and Confirm Password not Matched !!", Toast.LENGTH_LONG).show();
                }
            }
        }
        else
            {
                if(lan.getLanguage().equals("Arabic"))
                {
                    Toast.makeText(getApplicationContext(),"الرجاء اكمال جميع البيانات", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Complete All the Fields", Toast.LENGTH_LONG).show();
                }
            }
    }
}
