package com.example.android.travelandtourism.Activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.travelandtourism.AlertReceiver;
import com.example.android.travelandtourism.MainActivity;
import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.HotelReservations;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by haya on 08/09/2017.
 */

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toggle;
    static int noti_id = 1;

    Realm realm1;
    Language lan;
   // Button button;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();


        setContentView(R.layout.activity_home);

        button1 = (Button) findViewById(R.id.button_Flight);
        button2 = (Button) findViewById(R.id.button_Hotels);
        button3 = (Button) findViewById(R.id.button_offersHome);
        button4 = (Button) findViewById(R.id.button_myaccount);
        button5 = (Button) findViewById(R.id.button_mytrip);

        if(lan.getLanguage().equals("Arabic"))
        {
            button1.setText("الرحلات الجوية");
            button2.setText("الفنادق");
            button3.setText("العروض السياحية");
            button4.setText("حسابي");
            button5.setText("حجوزاتي");
        }

        //for username
        UserModel thisUser;
        Realm realm;
        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm.beginTransaction();
        thisUser = realm.where(UserModel.class).findFirst();
        realm.commitTransaction();

        //Dania
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();


        if(lan.getLanguage().equals("Arabic"))
        {
            MenuItem item1 =menu.findItem(R.id.nav_ChargeCredit);
            MenuItem item2 =menu.findItem(R.id.nav_ChangePassword);
            MenuItem item3 =menu.findItem(R.id.nav_UpdateUserInfo);
            MenuItem item4 =menu.findItem(R.id.nav_SendMessage);
            MenuItem item5 =menu.findItem(R.id.nav_share);
            MenuItem item6 =menu.findItem(R.id.nav_LogOut);
            MenuItem item7 =menu.findItem(R.id.nav_ChangeLanguage);


            item1.setTitle("شحن رصيد");
            item2.setTitle("تغيير كلمة المرور");
            item3.setTitle("تعديل معلومات المستخدم");
            item4.setTitle("تواصل معنا");
            item5.setTitle("مشاركة");
            item6.setTitle("تسجيل خروج");
            item7.setTitle("تغيير اللغة");

        }


        View header = navigationView.getHeaderView(0);

        //to header
        TextView username = (TextView) header.findViewById(R.id.textView_username);
        username.setText(thisUser.getFirstName()+" "+thisUser.getLastName());


        //Dialog for notification ****
        Bundle extras = getIntent().getExtras();
        String Title = "";
        String content_msg = "";
        if (extras != null) {
            Title = extras.getString("title");
            content_msg = extras.getString("msg");
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setMessage(content_msg)
                    .setTitle(Title)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // OK
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create();
            builder.show();
        }
        // ****


        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FlightHomeActivity.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), HotelHomeActivity.class);
                Intent intent = new Intent(getApplicationContext(), CountriesActivity.class);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OfferHomeActivity.class);
                startActivity(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyAccountActivity.class);
                startActivity(intent);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyTripsActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.side_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (toggle.onOptionsItemSelected(item)){
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_AboutUs) {
            AlertDialog.Builder msg  = new AlertDialog.Builder(this);
            if(lan.getLanguage().equals("Arabic"))
            {
                msg.setMessage("منتجنا يتضمن موقع الكتروني وتطبيق أندرويد بحيث يوفر:\n" +
                        "\n" +
                        "-عرض دول ومدن العالم. معلومات, خرائط, صور, والفنادق الرئيسية في كل مدينة.\n" +
                        "-حجز رحلات جوية الى اي مدينة عبر الخدمة.\n" +
                        "-عرض فنادق وحجز غرف في الفنادق\n" +
                        "-حجز حزم سياحية كاملة\n \n" +
                        "المشروع منجز من قبل \n" +
                        "الموقع الالكتروني: محمد شريف قصراوي \n" +
                        "خدمة الويب: هيا عقاد و محمد شريف قصراوي \n" +
                        "تطبيق الأندرويد: هيا عقاد و دانية المكاري");
                msg.setTitle("حول المنتج...");
            }
            else
                {
                    msg.setMessage("Our product includes a website and an Android mobile application that provide:\n" +
                            "\n" +
                            "-Previewing cities of the world. Information, Google map, Images and the major hotels in each city.\n" +
                            "-Booking Flights to any city online.\n" +
                            "-Previewing hotels and booking rooms online.\n" +
                            "-Booking a full tourism package.\n \n" +
                            "Created By: \n" +
                            "Website: Mohammad Sharif Qasrawi \n" +
                            "Web API Service: Haya Akkad and Mohammad Sharif Qasrawi \n" +
                            "Android App: Haya AKKAD and Dania Al-Makari");
                    msg.setTitle("About our Product...");
                }

            msg.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            msg.setCancelable(true);
            msg.create().show();
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_ChargeCredit) {
            // the ChargeCredit action
            Intent intent = new Intent(getApplicationContext(), ChargeCredit.class);
            startActivity(intent);

        } else if (id == R.id.nav_ChangePassword) {
            Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_UpdateUserInfo) {
            Intent intent = new Intent(getApplicationContext(), UpdateUserInfoActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_SendMessage) {
            Intent intent = new Intent(getApplicationContext(), SendMessageActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_ChangeLanguage) {
        Intent intent = new Intent(getApplicationContext(), ChangeLanguageActivity.class);
        startActivity(intent);

    } else if (id == R.id.nav_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "DSH WebSite");
                String sAux = "\nLet me recommend you this WebSite for Travel and Tourism \n\n";
                sAux = sAux + "http://dsh-tourism.somee.com/ \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                //
            }

        } else if (id == R.id.nav_LogOut) {
            Realm realm3 = Realm.getDefaultInstance();
            try
            {
                realm3.beginTransaction();
                //to check not duplicate followup id
                RealmResults<UserModel> result1 = realm3.where(UserModel.class).findAll();
                result1.deleteAllFromRealm();
                RealmResults<City> results2 = realm3.where(City.class).findAll();
                results2.deleteAllFromRealm();
                RealmResults<FlightReservation> result3 = realm3.where(FlightReservation.class).findAll();
                result3.deleteAllFromRealm();
                RealmResults<HotelReservations> result4 = realm3.where(HotelReservations.class).findAll();
                result4.deleteAllFromRealm();

                Intent intent = new Intent(getApplicationContext(), com.example.android.travelandtourism.MainActivity.class);
                startActivity(intent);
                //finish();
            }
            catch (Exception e)
            {
                Log.e("Realm Error", "error" );
            } finally {
                realm3.commitTransaction();
            }

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

