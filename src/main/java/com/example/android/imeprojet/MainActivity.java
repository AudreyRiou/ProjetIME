package com.example.android.imeprojet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setmyToolbar(VariablesManagement.class);
        SharedPreferences mSharedPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        setTitle(mSharedPrefs.getString("profil", "Aucun profil n'est sélectionné") + "- Menu principal");
    }

    public void dateClick(View view) {
        Intent dateActivity = new Intent(this, DateActivity.class);

        TextView text = (TextView)view;
        Drawable background = text.getBackground();
        int Color = ((ColorDrawable) background).getColor();

        SharedPreferences mSharedPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        editor.putString("choixJour", text.getText().toString());
        editor.putInt("couleurJour", Color);
        editor.commit();

        Toast.makeText(this, "App fonctionnelle", Toast.LENGTH_SHORT).show();
        startActivity(dateActivity);
    }

    public void twoWeeksClick(View view){
       Toast.makeText(this, "Affichage quinzaine", Toast.LENGTH_SHORT).show();
        Intent twoWeeksActivity = new Intent(this, TwoWeeksActivity.class);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        Drawable background = toolbar.getBackground();
        int Color = ((ColorDrawable) background).getColor();

        SharedPreferences mSharedPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        //editor.putString("choixJour", text.getText().toString());
        editor.putInt("couleurJour", Color);
        editor.commit();



        startActivity(twoWeeksActivity);
    }

    /*Gestion de l'affichage d'un message lorsque l'utilisateur s'apprête à quitter l'application*/
    public void onBackPressed(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Quitter");
        alertDialog.setMessage("Voulez-vous vraiment quitter?");
        /*Gestion de l'appui sur le bouton "Oui"*/
        alertDialog.setPositiveButton("Oui", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int ID)
            {
                moveTaskToBack(true);
            }
        });

        /*Gestion de l'appui sur le bouton "Non"*/
        alertDialog.setNegativeButton("Non", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int ID)
            {
                dialog.dismiss();
            }
        });

        alertDialog.show();


    }

}
