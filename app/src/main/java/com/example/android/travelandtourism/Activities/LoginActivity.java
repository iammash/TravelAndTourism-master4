package com.example.android.travelandtourism.Activities;

/**
 * Created by haya on 23/08/2017.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Adapters.UserAdapter;
import com.example.android.travelandtourism.AlertReceiver;
import com.example.android.travelandtourism.Interfaces.IApi;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.android.travelandtourism.R.id.txtPassword;
import static com.example.android.travelandtourism.R.id.txtUsername;

import com.example.android.travelandtourism.MainActivity;
import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.HotelReservations;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class LoginActivity extends AppCompatActivity {
    RetrofitBuilder rB = new RetrofitBuilder();
    IApi service =rB.retrofit.create(IApi.class);

    UserModel user;
    TextView Usernametxt;
    TextView Passwordtxt;
    Button Loginbtn;
    private ListView listView;
    Realm realm;

    Realm realm1;
    Language lan;
    TextView first;
    TextView sec;
    TextView thir;
    TextView u;
    TextView p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        setContentView(R.layout.activity_login);
        Usernametxt = (TextView) findViewById(txtUsername);
        Passwordtxt = (TextView) findViewById(txtPassword);
        Loginbtn = (Button) findViewById(R.id.btnLogin);
        first =(TextView)findViewById(R.id.login_title);
        sec=(TextView)findViewById(R.id.textView2);
        thir=(TextView)findViewById(R.id.textView3);
        u=(TextView)findViewById(R.id.usernameLan);
        p=(TextView)findViewById(R.id.passwordLan);


        if(lan.getLanguage().equals("Arabic"))
        {
            Loginbtn.setText("تسجيل دخول");
            first.setText("سجل دخولك");
            sec.setText("إلى");
            thir.setText("حسابك");
            u.setText("اسم المستخدم:");
            p.setText("كلمة المرور:");

        }

    }

    public void buttonOnClick(View v) {

        setContentView(R.layout.activity_user);
        listView = (ListView) findViewById(R.id.listUser);
        listView.setVisibility(View.VISIBLE);

        //for Notification
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 23);
        calendar.set(Calendar.SECOND, 0);

        final Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY, 15);
        calendar2.set(Calendar.MINUTE, 24);
        calendar2.set(Calendar.SECOND, 0);
        //**


        if (Passwordtxt.getText().toString().trim().length() != 0 && Usernametxt.getText().toString().trim().length() != 0)
        {
            Call<ResponseValue> call = service.authenticate(Usernametxt.getText().toString(), Passwordtxt.getText().toString());
            call.enqueue(new Callback<ResponseValue>() {
                @Override
                public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                    final ResponseValue responseValue = response.body();
                    realm = Realm.getDefaultInstance(); // opens "myrealm.realm"

                    try
                    {
                        Realm.init(LoginActivity.this);
                        // final Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        final RealmResults<UserModel> allusers = realm.where(UserModel.class).findAll();
                        allusers.deleteAllFromRealm();
                        if (response.isSuccessful())
                        {

                            user = responseValue.getUserModel();
                            String userId = user.getId().toString();
                            if((userId.length() >0) && (!userId.isEmpty()))
                            {
                                realm.copyToRealm(user);
                                RealmResults<UserModel> result = realm.where(UserModel.class)
                                        .findAll();

                              //  realm.commitTransaction();
                                String tmp = " ";
                                for (UserModel u : result) {
                                    tmp += u.getUserName()  + "\t" + u.getId();

                                }

                                Log.e("Add to realm", tmp);

                          /*  ArrayList<UserModel> arrayList = new ArrayList<>();
                            arrayList.add(user);
                            UserAdapter adapter = new UserAdapter(getApplicationContext(),R.layout.row_user,arrayList);
                            listView.setAdapter(adapter);*/


                                if(lan.getLanguage().equals("Arabic"))
                                {
                                    Toast.makeText(LoginActivity.this,"تم تسجيل الدخول بنجاح", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this,"Login successfully", Toast.LENGTH_LONG).show();
                                }


                                /////////// Flight Reservations ////////////
                                Call<ResponseValue> call3 = service.getMyFlightReservations(userId);
                                call3.enqueue(new Callback<ResponseValue>() {
                                    @Override
                                    public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                                        Log.e("Response", "Flight Reservations");
                                        ResponseValue responseValue3 = response.body();
                                        if(responseValue3 != null)
                                        {
                                            Realm realm3 = Realm.getDefaultInstance();
                                            try
                                            {
                                                List<FlightReservation> flightReservationsList = responseValue3.getFlightReservationList();
                                                realm3.beginTransaction();
                                                //to check not duplicate followup id
                                                RealmResults<FlightReservation> result = realm3.where(FlightReservation.class).findAll();
                                                result.deleteAllFromRealm();

                                                //for test purpose
                                                String tmp2 = " ";

                                                if(flightReservationsList.size() == 0)
                                                {
                                                    if(lan.getLanguage().equals("Arabic"))
                                                    {
                                                        Toast.makeText(LoginActivity.this,"حان وقت السفر !!", Toast.LENGTH_LONG).show();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getApplicationContext(),"Lets Travel :D !!", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                                //copy followup to realm
                                                for (FlightReservation a : flightReservationsList) {
                                                    tmp2 += a.getId() + "\t" + a.getDateTime();


                                                    ArrayList<FlightReservation> testing = new ArrayList<>();
                                                    testing.addAll(flightReservationsList);
                                                    realm3.copyToRealm(a);
                                                    //     realm.commitTransaction();
                                                    // Toast.makeText(getApplicationContext(),tmp2, Toast.LENGTH_LONG).show();
                                                }
                                                //Set Notification for Flight
                                                if (flightReservationsList.size() != 0) {
                                                    final FlightReservation flightReservation = flightReservationsList.get(0);

                                                    Intent Alertintent1 = new Intent(getApplicationContext(), AlertReceiver.class);
                                                    AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
                                                    Alertintent1.putExtra("title", "موعد رحلتك القادمة مع " + flightReservation.getFlight().getAirline());
                                                    Alertintent1.putExtra("msg", flightReservation.getDisplayDateTime());
                                                    Alertintent1.putExtra("code", "0");

                                                    alarmManager1.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), PendingIntent.getBroadcast(LoginActivity.this, 0, Alertintent1, PendingIntent.FLAG_UPDATE_CURRENT));
                                                }
                                                //**
                                            }
                                            catch (Exception e)
                                            {
                                                Log.e("Realm Error", "error flight" );
                                            } finally {
                                                realm3.commitTransaction();


                                            }


                                        }
                                        else
                                        {
                                            ////reservations is null
                                            Toast.makeText(getApplicationContext(),"Some thing wrong!!", Toast.LENGTH_LONG).show();

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseValue> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                                    }
                                });
                                ////////////end flight reservations/////////


                                //////////////hotel///////////////////

                                Call<ResponseValue> call1 = service.MyHotelReservations(userId);
                                call1.enqueue(new Callback<ResponseValue>() {
                                    @Override
                                    public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                                        Log.e("Response", "hotelReservations");
                                        ResponseValue responseValue1 = response.body();
                                        if(responseValue1 != null)
                                        {
                                            Realm realm2 = Realm.getDefaultInstance();
                                            try
                                            {
                                                List<HotelReservations> hotelReservationsList = responseValue1.getHotelReservations();
                                                realm2.beginTransaction();
                                                //to check not duplicate followup id
                                                RealmResults<HotelReservations> result = realm2.where(HotelReservations.class).findAll();
                                                result.deleteAllFromRealm();

                                                //for test purpose
                                                String tmp2 = " ";

                                                if(hotelReservationsList.size() == 0)
                                                {
                                                    Toast.makeText(getApplicationContext(),"Lets Travel :D !!", Toast.LENGTH_LONG).show();
                                                }
                                                //copy followup to realm
                                                for (HotelReservations a : hotelReservationsList) {
                                                    tmp2 += a.getId() + "\t" + a.getRoom().getHotel().getCity().getNameEn() + "\t"
                                                            + a.getRoom().getHotel().getNameEn();


                                                    ArrayList<HotelReservations> testing = new ArrayList<>();
                                                    testing.addAll(hotelReservationsList);
                                                    realm2.copyToRealm(a);
                                                    //     realm.commitTransaction();
                                                    // Toast.makeText(getApplicationContext(),tmp2, Toast.LENGTH_LONG).show();
                                                }
                                                //SET notification for Hotel
                                                if (hotelReservationsList.size() != 0) {
                                                    final HotelReservations hotelReservation = hotelReservationsList.get(0);

                                                    Intent Alertintent = new Intent(getApplicationContext(), AlertReceiver.class);
                                                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                                    Alertintent.putExtra("title", "موعد حجزك القادم في " + hotelReservation.getRoom().getHotel().getNameAr());
                                                    Alertintent.putExtra("msg", hotelReservation.getDisplayCheckInDate());
                                                    Alertintent.putExtra("code", "1");

                                                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), PendingIntent.getBroadcast(LoginActivity.this, 1, Alertintent, PendingIntent.FLAG_UPDATE_CURRENT));
                                                }
                                                //**

                                            }
                                            catch (Exception e)
                                            {
                                                Log.e("Realm Error", "error" );
                                            } finally {
                                                realm2.commitTransaction();
                                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }


                                        }
                                        else
                                        {
                                            ////reservations is null
                                            Toast.makeText(getApplicationContext(),"Some thing wrong!!", Toast.LENGTH_LONG).show();

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseValue> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(),"Server down There is an Wrong, Please Try Again", Toast.LENGTH_LONG).show();
                                    }// My hotel reservations
                                });
                                //////////////////


                                ////////////////

                            }
                            else
                            {
                                if(lan.getLanguage().equals("Arabic"))
                                {
                                    Toast.makeText(LoginActivity.this,"اسم المستخدم أو كلمة المرور خاطئة", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this,"Password or Username is False!! try again..", Toast.LENGTH_LONG).show();
                                }
                              //  realm.commitTransaction();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else
                        {
                            if(lan.getLanguage().equals("Arabic"))
                            {
                                Toast.makeText(LoginActivity.this,"اسم المستخدم أو كلمة المرور خاطئة", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,"Password or Username is False!! try again..", Toast.LENGTH_LONG).show();
                            }                         //   realm.commitTransaction();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    catch (Exception e)
                    {
                        Log.e("Realm Error", "error" );
                    } finally {
                        realm.commitTransaction();
                    }


                    ///////////////////////////////////////////
                    //////////////////////////
                    //////////////////////////
                }

                @Override
                public void onFailure(Call<ResponseValue> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"No Connection", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(user != null)
         realm.close();


    }
    public void buttonOnClick_forgetPW(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ForgetPassword.class);
        startActivity(intent);
        finish();

    }
}