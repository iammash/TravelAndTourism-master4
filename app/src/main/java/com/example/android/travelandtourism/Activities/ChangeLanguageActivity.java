package com.example.android.travelandtourism.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.android.travelandtourism.Models.Language;
import com.example.android.travelandtourism.Models.UserModel;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by haya on 11/10/2017.
 */

public class ChangeLanguageActivity extends AppCompatActivity {

    Realm realm1;
    Language lan;
    Realm realm;
    UserModel thisUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm1 = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm1.beginTransaction();
        lan = realm1.where(Language.class).findFirst();
        realm1.commitTransaction();

        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"
        realm.beginTransaction();
        thisUser = realm.where(UserModel.class).findFirst();//
        realm.commitTransaction();


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ChangeLanguageActivity.SomeDialog newFragment = new ChangeLanguageActivity.SomeDialog();

        if(lan.getLanguage().equals("Arabic"))
        {
            newFragment.newInstance4(2);
        }
        if(lan.getLanguage().equals("English"))
        {
            newFragment.newInstance4(1);
        }

        if(thisUser!=null)
        {
            newFragment.newInstance(1);// is login
        }
        newFragment.show(ft, "dialog");

    }

    public static class SomeDialog extends DialogFragment {

        String E = "English";
        String A ="العربية";
        String title="";
        String msg="";
        static  int lang;
        static  int log;

        static ChangeLanguageActivity.SomeDialog newInstance4(int lang1) {
            ChangeLanguageActivity.SomeDialog fragment = new ChangeLanguageActivity.SomeDialog();
            lang=lang1;
            return fragment ;
        }
        static ChangeLanguageActivity.SomeDialog newInstance(int log1) {
            ChangeLanguageActivity.SomeDialog fragment = new ChangeLanguageActivity.SomeDialog();
            log=log1;
            return fragment ;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            if(lang==2)
            {
                title ="إختيار اللغة";
                msg ="الرجاء اختيار اللغة ";

            }
            if(lang==1)
            {
                title ="Select Your Language";
                msg ="Select your Language Please.. ";
            }

            return new android.support.v7.app.AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(msg)
                    .setNegativeButton(E, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Realm realmLang1 = Realm.getDefaultInstance();
                            try
                            {
                                realmLang1.beginTransaction();
                                final RealmResults<Language> language= realmLang1.where(Language.class).findAll();
                                language.deleteAllFromRealm();
                                Language ll = new Language();
                                ll.setLanguage("English");
                                realmLang1.copyToRealm(ll);
                                Toast.makeText(getContext() ,"You select English", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e)
                            {
                                Log.e("Realm Error", "error language" );
                            } finally {
                                realmLang1.commitTransaction();
                                if(log==1)
                                {
                                    Intent intent = new Intent(getContext(), HomeActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(getContext(), HomeActivity2.class);
                                    startActivity(intent);
                                }
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
                                if(log==1)
                                {
                                    Intent intent = new Intent(getContext(), HomeActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(getContext(), HomeActivity2.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    })
                    .create();
        }


    }
}
