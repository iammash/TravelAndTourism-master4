package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.android.travelandtourism.R.id.tvRFN;
import static com.example.android.travelandtourism.R.id.tvRLN;
import static com.example.android.travelandtourism.R.id.tvRCity;
import static com.example.android.travelandtourism.R.id.tvRCountry;
import static com.example.android.travelandtourism.R.id.tvRGender;
import static com.example.android.travelandtourism.R.id.tvRPhone;
import static com.example.android.travelandtourism.R.id.tvREmail;
import static com.example.android.travelandtourism.R.id.tvRUsername;
import static com.example.android.travelandtourism.R.id.tvRPassword;
import static com.example.android.travelandtourism.R.id.tvRConfPassword;



/**
 * Created by haya on 27/08/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    TextView FirstName;
    TextView tv1;
    TextView LastName;
    TextView tv2;
    TextView Gender;
    TextView tv3;
    TextView Email;
    TextView tv4;
    TextView Phone;
    TextView tv5;
    TextView Country;
    TextView tv6;
    TextView City;
    TextView tv7;
    TextView Username;
    TextView tv8;
    TextView Password;
    TextView tv9;
    TextView ConfPassword;
    TextView tv10;
    Button RegistrationBtn;

    Realm realm1;
    Language lan;
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        setContentView(R.layout.activity_register);

        title = (TextView)findViewById(R.id.registration);
        FirstName = (TextView) findViewById(tvRFN);
        tv1 = (TextView) findViewById(R.id.tv1);
        LastName = (TextView) findViewById(tvRLN);
        tv2 = (TextView) findViewById(R.id.tv2);
        Gender = (TextView) findViewById(tvRGender);
        tv3 = (TextView) findViewById(R.id.tv3);
        Email = (TextView) findViewById(tvREmail);
        tv4 = (TextView) findViewById(R.id.tv4);
        Phone =(TextView) findViewById(tvRPhone);
        tv5 = (TextView) findViewById(R.id.tv5);
        Country = (TextView) findViewById(tvRCountry);
        tv6 = (TextView) findViewById(R.id.tv6);
        City = (TextView) findViewById(tvRCity);
        tv7 = (TextView) findViewById(R.id.tv7);
        Username = (TextView) findViewById(tvRUsername);
        tv8 = (TextView) findViewById(R.id.tv8);
        Password = (TextView) findViewById(tvRPassword);
        tv9 = (TextView) findViewById(R.id.tv9);
        ConfPassword = (TextView) findViewById(tvRConfPassword);
        tv10 = (TextView) findViewById(R.id.tv10);
        RegistrationBtn = (Button) findViewById(R.id.btnRegister);

        if(lan.getLanguage().equals("Arabic"))
        {
            title.setText("انشاء حساب");
            tv1.setText("الاسم الأول");
            tv2.setText("الكنية");
            tv3.setText("الجنس");
            tv4.setText("البريد الالكتروني");
            tv5.setText("رقم الهاتف");
            tv6.setText("الدولة");
            tv7.setText("المدينة");
            tv8.setText("اسم المستخدم");
            tv9.setText("كلمة السر");
            tv10.setText("تأكيد كلمة السر");
            RegistrationBtn.setText("تسجيل");

        }

    }


    public void buttonOnClick1(View v)
    {
        setContentView(R.layout.activity_register);

        if (FirstName.getText().toString().trim().length() != 0 && LastName.getText().toString().trim().length() != 0
                && Gender.getText().toString().trim().length() != 0 && Email.getText().toString().trim().length() != 0
                && Phone.getText().toString().trim().length() != 0 && Country.getText().toString().trim().length() != 0
                && City.getText().toString().trim().length() != 0 && Country.getText().toString().trim().length() != 0
                && Username.getText().toString().trim().length() != 0 && Password.getText().toString().trim().length() != 0
                && ConfPassword.getText().toString().trim().length() != 0)
        {

              if (Password.getText().toString().equals( ConfPassword.getText().toString()))
             {
                 Call<ResponseValue> call = service.registration(Username.getText().toString(),Password.getText().toString(),
                         ConfPassword.getText().toString(),FirstName.getText().toString(),LastName.getText().toString()
                         ,Gender.getText().toString(),Email.getText().toString(),Phone.getText().toString(),
                         Country.getText().toString(),City.getText().toString());
                 call.enqueue(new Callback<ResponseValue>() {
                     @Override
                     public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                         if(response.isSuccessful())
                         {
                             if ( response.body().getUserModel().getSucceeded() == true)
                             {
                                 if(lan.getLanguage().equals("Arabic"))
                                 {
                                     Toast.makeText(getApplicationContext(),"تم انشاء حسابك بنجاح.. الرجاء تسجيل الدخول للاستمرار..", Toast.LENGTH_LONG).show();
                                 }
                                 else
                                 {
                                     Toast.makeText(getApplicationContext(),"Registration Done!! Please Login to continue..", Toast.LENGTH_LONG).show();
                                 }

                                 Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                 startActivity(intent);
                                 finish();
                             }
                             else if(response.body().getUserModel().getSucceeded() == false)
                             {
                                 String errorMsg = response.body().getUserModel().getErrors().toString();
                                 Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                             }
                             else
                             {
                                 Toast.makeText(getApplicationContext(),"There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                             }
                         }
                         else{

                             Toast.makeText(getApplicationContext(),"Bad Request", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(),"Password and confirm password dose not match!!", Toast.LENGTH_LONG).show();
                }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please Fill All Field .. ", Toast.LENGTH_LONG).show();
        }

    }
}
