package com.example.android.travelandtourism.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by haya on 07/10/2017.
 */

public class GetStart extends AppCompatActivity {

    Button button;
    Button button2;
    Button button3;
    Language lan;
    Realm realm1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        //thisUser = realm.where(UserModel.class).findFirst();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            //to check not duplicate followup id
            RealmResults<UserModel> result = realm.where(UserModel.class).findAll();

            if (result.size() != 0)
            {
                setContentView(R.layout.activity_home);

                Realm realmLang = Realm.getDefaultInstance();

                try
                {
                    realmLang.beginTransaction();

                    final RealmResults<Language>  language= realmLang.where(Language.class).findAll();
                    if(language.size()== 0)
                    {
                        Language ll = new Language();
                        ll.setLanguage("English");
                        realmLang.copyToRealm(ll);
                    }

                    /*
                    * language.deleteAllFromRealm();
                    Language ll = new Language();
                    ll.setLanguage("English");
                    realmLang.copyToRealm(ll);*/


                }
                catch (Exception e)
                {
                    Log.e("Realm Error", "error language" );
                } finally {
                    realmLang.commitTransaction();
                }


                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();


            }
            else
            {
                setContentView(R.layout.activity_main);


                //    showD();


              /*
              *   realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
                realm1.beginTransaction();
                //thisUser = realm.where(UserModel.class).findFirst();
                lan = realm1.where(Language.class).findFirst();
                realm1.commitTransaction();*/




               // String ssss = lan.getLanguage();


                ///     setContentView(R.layout.activity_main);
                button = (Button) findViewById(R.id.start_btn);
                button2 = (Button) findViewById(R.id.login_btn);
                button3 = (Button) findViewById(R.id.signup_btn);

                if(lan.getLanguage().equals("Arabic"))
                {

                    button.setText("ابدأ");
                    button2.setText("تسجيل دخول");
                    button3.setText("انشاء حساب");

                }
                else
                {
                    button.setText("Start");
                    button2.setText("Login");
                    button3.setText("SignUp");
                }
                //
                button.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity2.class);
                        startActivity(intent);
                        finish();
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                button3.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });






            }
        }
        catch (Exception e)
        {
            Log.e("Realm Error", "error" );
        } finally {
            realm.commitTransaction();
        }






    }






}
