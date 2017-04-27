package com.example.android.imeprojet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by isen on 04/04/2017.
 */

public class SettingsFragment extends PreferenceFragment {
    private CheckBoxPreference checkBoxMdp, checkBoxSon;
    private Preference aPropos, editMdp, resetMdp;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        final SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = mSharedPrefs.edit();

        checkBoxMdp = (CheckBoxPreference) getPreferenceManager().findPreference("checkBoxMotdePasse");
        editMdp = (Preference) getPreferenceManager().findPreference("EditTextMotdePasse");
        resetMdp = (Preference) getPreferenceManager().findPreference("ResetMotdePasse");
        checkBoxSon = (CheckBoxPreference) getPreferenceManager().findPreference("checkBoxSon");
        aPropos = (Preference) getPreferenceManager().findPreference("aPropos");



        checkBoxMdp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                if(newValue.toString().equals("true"))
                {
                    editor.putBoolean("mdpActif", true);
                }
                else
                {
                    editor.putBoolean("mdpActif", false);
                }
                editor.commit();
                return true;
            }
        });


        editMdp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference arg0)
            {
                final Dialog inputBox = new Dialog(getActivity());
                inputBox.setContentView(R.layout.popup_change_mdp);
                inputBox.setTitle("Changement de mot de passe");
                inputBox.setCancelable(false);

                Button buttonOK = (Button) inputBox.findViewById(R.id.bouton_ok);
                buttonOK.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        /*Récupération du mot de passe*/
                        EditText editTextMdp = (EditText) inputBox.findViewById(R.id.EditText_boite_saisie_mdp);
                        /*On récupère la valeur du mot de passe et on la convertit en chaîne de caractère*/
                        String textMdp = editTextMdp.getText().toString();
                        /*Récupération du nouveau mot de passe*/
                        EditText editTextNewMdp = (EditText) inputBox.findViewById(R.id.EditText_boite_saisie_newmdp);
                        String textNewMdp = editTextNewMdp.getText().toString();
                        /*Confirmation du nouveau mot de passe*/
                        EditText editTextConfirmNewMdp = (EditText) inputBox.findViewById(R.id.EditText_boite_confirmation_newmdp);
                        String textConfirmNewMdp = editTextConfirmNewMdp.getText().toString();

                        if (textMdp.equals("") || textNewMdp.equals("") || textConfirmNewMdp.equals(""))
                        {
                            Toast.makeText(getActivity().getApplicationContext(), "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            /*Si le mot de passe saisi est correct*/
                            if (mSharedPrefs.getString("mdp", "0000").equals(textMdp))
                            {
                                /*On vérifie que le nouveau mot de passe correspond bien à la confirmation du mot de passe*/
                                if(textNewMdp.equals(textConfirmNewMdp))
                                {
                                   editor.putString("mdp", textNewMdp);
                                    editor.commit();
                                    inputBox.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(getActivity().getApplicationContext(), "Les mots de passe ne correspondent pas.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity().getApplicationContext(), "L'ancien mot de passe est incorrect.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

                Button boutonAnnuler = (Button) inputBox.findViewById(R.id.bouton_annuler);
                boutonAnnuler.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        inputBox.dismiss();
                    }
                });
                inputBox.show();
                return true;

            }
        });

        resetMdp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            public boolean onPreferenceClick(Preference arg0)
            {
                editor.putString("mdp","0000");
                editor.commit();
                Toast.makeText(getActivity().getApplicationContext(), "Mot de passe par défaut rétabli", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        /*Activation du son ou non en fonction de l'état de la checkbox*/
        checkBoxSon.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                if(newValue.toString().equals("true"))
                {
                    editor.putInt("sonActif",1);
                }
                else
                {
                    editor.putInt("sonActif", 0);
                }

                editor.commit();
                return true;
            }
        });

        /*Gestion de la partie "A propos" de l'application*/
        aPropos.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("A propos");
                alertDialog.setMessage("Application développée par Corentin Bassi et Audrey Riou, en collaboration avec l'IME de Plabennec.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Fermer",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return true;
            }
        });

    }
}
