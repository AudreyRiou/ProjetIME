package com.example.android.imeprojet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Method;

import static com.example.android.imeprojet.R.id.bouton_annuler;

/**
 * Created by isen on 04/04/2017.
 */

public class BaseActivity extends AppCompatActivity {

    public void setmyToolbar(final Class destinationClass) {
        SharedPreferences mSharedPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(mSharedPrefs.getInt("couleurJour", 0));
        Drawable fleche = ResourcesCompat.getDrawable(getResources(), R.drawable.abc_ic_ab_back_mtrl_am_alpha, null);

        if (mSharedPrefs.getInt("couleurJour", 0) == ContextCompat.getColor(getBaseContext(), R.color.colorWhite)
                || mSharedPrefs.getInt("couleurJour", 0) == ContextCompat.getColor(getBaseContext(), R.color.colorYellow)
                || mSharedPrefs.getInt("couleurJour", 0) == 0) {
            toolbar.setTitleTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorBlack));

            fleche.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(fleche);
        } else {
            fleche.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(fleche);
        }
        setTitle(mSharedPrefs.getString("profil", "Aucun profil n'est sélectionné "));

        if (destinationClass != VariablesManagement.class) {
            toolbar.setNavigationIcon(fleche);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent returnToMain = new Intent(view.getContext(), destinationClass);
                    view.getContext().startActivity(returnToMain);
                }
            });
        }


        Initialisation();
    }

    public void Initialisation() {
        VariablesManagement.dossier_projet = FilesManagement.createPictoFile("Classeur Autisme");
        VariablesManagement.dossier_pictos = FilesManagement.createPictoFile(VariablesManagement.dossier_projet.getName() + File.separator + "Pictogrammes");
        VariablesManagement.dossier_pictos_timetable = FilesManagement.createPictoFile(VariablesManagement.dossier_projet.getName() + File.separator + VariablesManagement.dossier_pictos.getName() + File.separator + "Emploi du temps");
        VariablesManagement.dossier_pictos_meal = FilesManagement.createPictoFile(VariablesManagement.dossier_projet.getName() + File.separator + VariablesManagement.dossier_pictos.getName() + File.separator + "Repas");
        VariablesManagement.dossier_pictos_appel = FilesManagement.createPictoFile(VariablesManagement.dossier_projet.getName() + File.separator + VariablesManagement.dossier_pictos.getName() + File.separator + "Appel");
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_common, menu);

        //Code pour afficher les icones car ils sont désactivés depuis android 3.0+
        if(menu.getClass().getSimpleName().equals("MenuBuilder")){
            try{
                Method m = menu.getClass().getDeclaredMethod(
                        "setOptionalIconsVisible", Boolean.TYPE);
                m.setAccessible(true);
                m.invoke(menu, true);
            }
            catch(NoSuchMethodException e){
                Log.e("MyActivity", "onMenuOpened", e);
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        switch (item.getItemId())
        {
            case R.id.menu_settings:
                if(mSharedPrefs.getBoolean("mdpActif", false) == true)
                {
                    Intent activity = new Intent(BaseActivity.this, DateActivity.class);
                    startActivity(activity);
                    //checkPassword(Settings.class);
                }
                else
                {
                    Intent intent = new Intent(BaseActivity.this, Settings.class);
                    startActivity(intent);
                }

                return true;
            /*
            case R.id.menu_add_picture:
                if (mSharedPrefs.getBoolean("mdpActif", false) == true)
                {
                    checkPassword(ChoixAjoutPicto.class);
                }
                else
                {
                    Intent intentAjoutPicto = new Intent(BaseActivity.this, ChoixAjoutPicto.class);
                    startActivity(intentAjoutPicto);
                }
                return true;
            /*
            case R.id.menu_reset:
                if (mSharedPrefs.getBoolean("mdp_actif", false) == true) {
                    CheckPassword(ResetActivity.class);
                }
                else {
                    Intent intent2 = new Intent(BaseActivity.this, ResetActivity.class);
                    startActivity(intent2);
                }
                return true;*/
            /*
            case R.id.menu_profils:
                if (mSharedPrefs.getBoolean("mdp_actif", false) == true) {
                    CheckPassword(Profil.class);
                }
                else {
                    Intent intent3 = new Intent(BaseActivity.this, Profil.class);
                    startActivity(intent3);
                }
                return true;*/

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void checkPassword(final Class destinationClass)
    {
        final SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final Dialog inputBox = new Dialog(this);
        inputBox.setContentView(R.layout.popup_mdp);
        inputBox.setTitle("Veuillez entrer votre mot de passe");
        inputBox.setCancelable(false);

        Button boutonOK = (Button) inputBox.findViewById(R.id.bouton_ok);
        boutonOK.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                EditText editTextMdp = (EditText) inputBox.findViewById(R.id.EditText_boite_saisie_mdp);
                String texteMdp = editTextMdp.getText().toString();

                if (texteMdp.equals(""))
                {
                    Toast.makeText(getBaseContext().getApplicationContext(), "Veuillez saisir un mot de passe.", Toast.LENGTH_LONG);
                }
                else
                {
                    if (mSharedPrefs.getString("mdp", "0000").equals(texteMdp))
                    {
                        Intent intentDestination = new Intent(BaseActivity.this, destinationClass);
                        startActivity(intentDestination);
                        inputBox.dismiss();
                    }
                    else
                    {
                        Toast.makeText(getBaseContext().getApplicationContext(), "Mot de passe invalide", Toast.LENGTH_LONG);
                        inputBox.dismiss();
                    }
                }
            }
        });

        Button cancelButton = (Button) inputBox.findViewById(bouton_annuler);
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                inputBox.dismiss();
            }
        });
        inputBox.show();
}




}
