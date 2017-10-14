package com.example.android.travelandtourism;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelandtourism.Activities.GetStart;
import com.example.android.travelandtourism.Activities.HomeActivity;
import com.example.android.travelandtourism.Activities.test;
import com.example.android.travelandtourism.Interfaces.IApi;
import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.UserModel;
import com.example.android.travelandtourism.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by haya on 19/09/2017.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            //to check not duplicate followup id
            RealmResults<UserModel> result = realm.where(UserModel.class).findAll();

            if (result.size() != 0)
            {
                setContentView(R.layout.activity_home);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {

                setContentView(R.layout.activity_main);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                MainActivity.SomeDialog newFragment = new MainActivity.SomeDialog();
                newFragment.show(ft, "dialog");
              //  finish();

            }
        }
        catch (Exception e)
        {
            Log.e("Realm Error", "error" );
        } finally {
            realm.commitTransaction();
        }


    }


    public static class SomeDialog extends DialogFragment {


        String E = "English";
        String A ="العربية";


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            return new android.support.v7.app.AlertDialog.Builder(getActivity())
                    .setTitle("Your Language")
                    .setMessage("Select Your Language Please..")
                    .setNegativeButton(E, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Realm realmLang1 = Realm.getDefaultInstance();

                            try
                            {
                                realmLang1.beginTransaction();

                                  final RealmResults<Language>  language= realmLang1.where(Language.class).findAll();
                                language.deleteAllFromRealm();

                                Language ll = new Language();
                                ll.setLanguage("English");
                                realmLang1.copyToRealm(ll);
                                //  selected = "English";

                                Toast.makeText(getContext() ,"You select English", Toast.LENGTH_SHORT).show();




                            }
                            catch (Exception e)
                            {
                                Log.e("Realm Error", "error language" );
                            } finally {
                                realmLang1.commitTransaction();
                                Intent intent = new Intent(getContext(), GetStart.class);
                                startActivity(intent);
                            }


                        }
                    })
                    .setPositiveButton(A,  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Realm realmLang2 = Realm.getDefaultInstance();

                            try
                            {
                                realmLang2.beginTransaction();

                                final RealmResults<Language>  language= realmLang2.where(Language.class).findAll();
                                language.deleteAllFromRealm();

                                Language ll = new Language();
                                ll.setLanguage("Arabic");
                                realmLang2.copyToRealm(ll);
                                Toast.makeText(getContext(), "لقد اخترت العربية", Toast.LENGTH_SHORT).show();

                            }
                            catch (Exception e)
                            {
                                Log.e("Realm Error", "error language" );
                            } finally {
                                realmLang2.commitTransaction();
                                Intent intent = new Intent(getContext(), GetStart.class);
                                startActivity(intent);
                            }
                        }
                    })
                    .create();
        }
    }
}
