package com.example.android.imeprojet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by isen on 04/04/2017.
 */

public class DateActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        setmyToolbar(MainActivity.class);
        SharedPreferences mSharedPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);

        /*On passe la première lettre du jour en majuscules*/
        char days[];
        String day = mSharedPrefs.getString("choixJour", null);
        String dayfirstLetterUpper = day.replaceFirst(".", (day.charAt(0) + "").toUpperCase());



        setTitle(mSharedPrefs.getString("profil", "Aucun profil sélectionné")+" - " + dayfirstLetterUpper);


        /*Déclaration de nouveaux éléments XML qui seront modifiés lors de l'activité.
        Ici, la vue globale et la TextView contenant le jour seront changés
         */
        /*
        final LinearLayout activity_date = (LinearLayout) findViewById(R.id.activity_date);
        final TextView jour_text_view = (TextView) findViewById(R.id.jour_text_view);

        /*Si on trouve un choixJour dans les Shared Preferences, on va modifier le texte et la couleur
        de l'arrière-plan pour la zone de texte de activity_date.xml
         */
        /*
        if (mSharedPrefs.contains("choixJour") && mSharedPrefs.getString("choixJour",null) != null)
        {
            jour_text_view.setText(mSharedPrefs.getString("choixJour",null));
            jour_text_view.setBackgroundColor(mSharedPrefs.getInt("couleurJour",0));
        };*/

    }

    public void click_on_day_time(View view) {
        Toast.makeText(this, "App fonctionnelle", Toast.LENGTH_SHORT).show();
        Intent momentActivity = new Intent(this, MomentActivity.class);

        TextView text = (TextView)view;
        Drawable background = text.getBackground();

        SharedPreferences mSharedPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        editor.putString("choixMoment", text.getText().toString());
        editor.commit();

        startActivity(momentActivity);

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(DateActivity.this, MainActivity.class));
        finish();

    }

}
