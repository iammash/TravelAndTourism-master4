package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.Message;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.Models.UserModel1;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

/**
 * Created by haya on 24/09/2017.
 */

public class UpdateUserInfoActivity extends AppCompatActivity {

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    EditText ed;
    EditText ed1;
    EditText ed2;
    EditText ed3;
    EditText ed4;
    EditText ed5;
    EditText ed6;
    TextView ed7;
    Button btn;
    Realm realm;
    Realm realm1;
    Realm realm2;
    Language lan;
    UserModel thisUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm2 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm2.beginTransaction();
        lan = realm2.where(Language.class).findFirst();
        realm2.commitTransaction();

        setContentView(R.layout.activity_update_userinfo);

        btn = (Button)findViewById(R.id.button_UpdateInfo);
        ed = (EditText)findViewById(R.id.evuserFName);
        ed1 = (EditText)findViewById(R.id.evuserLName);
        ed2 = (EditText)findViewById(R.id.evuserEmail);
        ed3 = (EditText)findViewById(R.id.evuserGender);
        ed4 = (EditText)findViewById(R.id.evuserPhone);
        ed5 = (EditText)findViewById(R.id.evuserCountry);
        ed6 = (EditText)findViewById(R.id.evuserCity);
        ed7 = (TextView) findViewById(R.id.evuserName);

        if(lan.getLanguage().equals("Arabic"))
        {
            TextView tv1 = (TextView)findViewById(R.id.tvFN);
            TextView tv2 = (TextView)findViewById(R.id.tvLN);
            TextView tv3 = (TextView)findViewById(R.id.tvEm);
            TextView tv4 = (TextView)findViewById(R.id.tvGen);
            TextView tv5 = (TextView)findViewById(R.id.tvPh);
            TextView tv6 = (TextView)findViewById(R.id.tvCou);
            TextView tv7 = (TextView)findViewById(R.id.tvCit);

            tv1.setText("الاسم الأول");
            tv2.setText("الكنية");
            tv3.setText("البريد الالكتروني");
            tv4.setText("الجنس");
            tv5.setText("رقم الهاتف");
            tv6.setText("الدولة");
            tv7.setText("المدينة");

        }


        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        thisUser = realm1.where(UserModel.class).findFirst();
        realm1.commitTransaction();

        if(thisUser != null)
        {
            ed.setText(thisUser.getFirstName());
            ed1.setText(thisUser.getLastName());
            ed2.setText(thisUser.getEmail());
            ed3.setText(thisUser.getGender());
            ed4.setText(thisUser.getPhoneNumber().toString());
            ed5.setText(thisUser.getCountry());
            ed6.setText(thisUser.getCity());
            ed7.setText(thisUser.getUserName());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void button_UpdateInfo1(View view)
    {


        if(thisUser!= null)
        {
            if(ed.getText().toString().trim().length() != 0 && ed1.getText().toString().trim().length() != 0
                    && ed2.getText().toString().trim().length() != 0 && ed3.getText().toString().trim().length() != 0 &&
                    ed4.getText().toString().trim().length() != 0 && ed5.getText().toString().trim().length() != 0 &&
                    ed6.getText().toString().trim().length() != 0 && ed7.getText().toString().trim().length() != 0)
            {
                Call<Message> call = service.UpdateUserInfo(thisUser.getId(),ed.getText().toString(),
                        ed1.getText().toString(),ed3.getText().toString(),ed2.getText().toString(),
                        ed4.getText().toString(),ed5.getText().toString(),ed6.getText().toString());
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if(response.isSuccessful())
                        {
                            Message responseValue = response.body();
                           // UserModel1 responseValue = response.body();
                            if(responseValue != null)
                            {
                                String msg =responseValue.getMessage();
                               // String msg1 =responseValue.getMessage();


                                if(msg.equals("user info Updated"))
                                {

                                    realm = Realm.getDefaultInstance(); // opens "myrealm.realm"

                                    try {

                                        realm.beginTransaction();
                                        final RealmResults<UserModel> allusers = realm.where(UserModel.class).findAll();
                                        allusers.deleteAllFromRealm();

                                    }
                                    catch (Exception e)
                                    {
                                        Log.e("Realm Error", "error" );
                                    } finally {
                                        realm.commitTransaction();

                                        if(lan.getLanguage().equals("Arabic"))
                                        {
                                            Toast.makeText(getApplicationContext(),"تم تعديل معلوماتك بنجاح!!", Toast.LENGTH_LONG).show();
                                            Toast.makeText(getApplicationContext(),"الرجاء اعادة تسجيل الدخول للاستكمال..", Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),"User Info Updated successfully!!", Toast.LENGTH_LONG).show();
                                            Toast.makeText(getApplicationContext(),"Please Re-Login to continue..", Toast.LENGTH_LONG).show();
                                        }

                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                    }

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Failed update....", Toast.LENGTH_LONG).show();
                                }
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
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();

                    }
                });


            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please Complete All the Failed", Toast.LENGTH_LONG).show();

            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please login to continue", Toast.LENGTH_LONG).show();
        }
    }
}
