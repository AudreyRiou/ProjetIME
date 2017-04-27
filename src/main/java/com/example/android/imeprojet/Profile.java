package com.example.android.imeprojet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.example.android.imeprojet.R.layout.profil;

/**
 * Created by isen on 13/04/2017.
 */

public class Profile extends BaseActivity {
    private ArrayList<String> profileNameList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(profil);

        /*      Gestion de la barre d'outils        */
        setmyToolbar(MainActivity.class);
        SharedPreferences mSharedPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        setTitle(mSharedPrefs.getString("profil", "Aucun profil n'est sélectionné") + " - Gérer les profils");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        /*      Mise à jour du profil       */
        updateProfile();

    }

    private void updateProfile() {
        String line;
        String compoLine[];
        profileNameList = new ArrayList<String>();

        try {
            FileInputStream fileInputStream = new FileInputStream(VariablesManagement.dossier_projet + File.separator + "listeProfil.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(" Type de profil : ")) {
                    compoLine = line.split(" Type de profil : ");
                    profileNameList.add(compoLine[0]);
                }
            }
            bufferedReader.close();

        } catch (Exception e) {

        }
    }

    public void AddProfile(View view) {
        AddProfilePopup(Profile.this);
    }

    public void DeleteProfile(View view) {
        DeleteProfilePopup(Profile.this);
    }

    public void ChangeProfile(View view) {
        ChangeProfilePopup(Profile.this);
    }

    public void SaveProfile(String profile) {
        if (profileNameList.contains(profile)) {
            String line;
            StringBuffer stringBuffer = new StringBuffer();
            int numberLinesRead = -1;
            int passage = 0;

            try {
                FileInputStream fileInputStream = new FileInputStream(VariablesManagement.dossier_projet + File.separator + "listeProfil.txt");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                SharedPreferences mSharedPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);

                while ((line = bufferedReader.readLine()) != null) {
                    numberLinesRead++;

                    if (line.startsWith(profile + " Type de profil : ")) {
                        stringBuffer.append(line + "\n");
                        stringBuffer.append(mSharedPrefs.getString("choix_moment", null) + "\n");
                        stringBuffer.append(mSharedPrefs.getString("choix_jour", null) + "\n");
                        stringBuffer.append(mSharedPrefs.getInt("list_timetable_size", 0) + "\n");
                        for (int i = 0; i < mSharedPrefs.getInt("list_timetable_size", 0); i++) {
                            stringBuffer.append(mSharedPrefs.getInt("list_timetable_" + i, 0));
                        }

                        passage = 1;

                    } else if (line.contains("Type de profil : ") && passage == 1) {
                        passage = 0;
                        stringBuffer.append(line + "\n");
                    } else {
                        if (passage == 0) {
                            stringBuffer.append(line + "\n");
                        }
                    }

                }
                bufferedReader.close();
                BufferedWriter out = new BufferedWriter(new FileWriter(VariablesManagement.dossier_projet + File.separator + "listeProfil.txt"));
                out.write(stringBuffer.toString());
                out.close();

            } catch (Exception e) {

            }
        }
    }

    public void AddProfilePopup(final Activity activity) {
        final Dialog inputBox = new Dialog(activity);
        inputBox.setContentView(R.layout.popup_add_profile);
        inputBox.setTitle("Ajouter un nouveau profil");
        inputBox.setCancelable(false);

        final Spinner profileSpinner = (Spinner) inputBox.findViewById(R.id.KindOfProfil);
        String[] myProfile = {"Profil 1 (grande image, petit texte)", "Profil 2 (grand texte, petite image)", "Profil 3 (seulement du texte"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myProfile);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileSpinner.setAdapter(dataAdapter);
        /*  Gestion du clic sur la liste des différents types de profil */
        profileSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences mSharedPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPrefs.edit();
                editor.putInt("type_nouveau_profil", position);
                editor.commit();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //TO-DO auto-generated method sub
            }
        });

        Button bouton_ok = (Button) inputBox.findViewById(R.id.bouton_ok);
        bouton_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText texte = (EditText) inputBox.findViewById(R.id.EditText_boite_saisie_profil);
                String inputBoxText = texte.getText().toString();
                if (inputBox.equals("")) {
                    Toast.makeText(activity.getApplicationContext(), "Ce nom existe déjà, merci de saisir un nouveau nom", Toast.LENGTH_SHORT).show();
                    texte.setText("");
                } else {
                    try {
                        SharedPreferences mSharedPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mSharedPrefs.edit();

                        SaveProfile(mSharedPrefs.getString("profil", "Aucun profil n'est sélectionné"));
                        BufferedWriter file = new BufferedWriter(new FileWriter(VariablesManagement.dossier_projet + File.separator + "listeProfil.txt", true));

                        file.write(inputBoxText + "Type de profil : " + mSharedPrefs.getInt("type_nouveau_profil", 0));
                        /*  Initialisation des lignes utilisées dans le fichier */
                        file.newLine();
                        file.write("null");
                        file.newLine();
                        file.write("null");
                        file.newLine();
                        file.write("0");
                        /*
                        file.newLine();
                        file.write("null");
                        file.newLine();
                        file.write("null");
                        file.newLine();
                        file.write("0");
                        file.newLine();
                        file.write("0");
                        file.newLine();
                        file.write("0");
                        file.newLine();
                        file.write("0");
                        file.newLine();
                        file.write("0");
                        file.newLine();
                        file.write("0");
                        file.newLine();
                        file.write("0");
                        file.newLine();
                        file.write("0");
                        file.newLine();*/

                        file.close();

                        /*  Remplissage du fichier  */
                        editor.putString("profil", inputBoxText);
                        editor.putInt("type_de_profil", mSharedPrefs.getInt("type_nouveau_profil", 0));
                        editor.putString("choix_moment", null);
                        editor.putString("choix_jour", null);
                        editor.putInt("list_timetable_size", 0);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(activity.getApplicationContext(), inputBoxText + "Profil ajouté", Toast.LENGTH_SHORT).show();
                    inputBox.dismiss();
                    finish();
                    startActivity(getIntent());

                }
            }
        });

        Button bouton_annuler = (Button) findViewById(R.id.bouton_annuler);
        bouton_annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputBox.dismiss();
            }
        });

        inputBox.show();

    }

    public void ChangeProfilePopup(final Activity activity) {
        final Dialog changeProfileBox = new Dialog(activity);
        changeProfileBox.setContentView(R.layout.popup_change_profile);
        changeProfileBox.setTitle("Changer de profil");
        changeProfileBox.setCancelable(false);

        final RadioGroup RadioGroupProfile = (RadioGroup) changeProfileBox.findViewById(R.id.radiogroup_changeprofil);

        for (int i = 0; i < profileNameList.size(); i++) {
            RadioButton radioButtonView = new RadioButton(this);
            radioButtonView.setText(profileNameList.get(i));
            RadioGroupProfile.addView(radioButtonView);
        }

        Button bouton_ok = (Button) findViewById(R.id.bouton_ok);
        bouton_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = RadioGroupProfile.getCheckedRadioButtonId();
                if (id != -1) {
                    View radioButton = RadioGroupProfile.findViewById(id);
                    int radioID = RadioGroupProfile.indexOfChild(radioButton);
                    RadioButton button = (RadioButton) RadioGroupProfile.getChildAt(radioID);
                    String selection = (String) button.getText();

                    SharedPreferences mSharedPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPrefs.edit();

                    String line;
                    int numberLinesRead = -1;
                    String compoLine[];
                    int passage = 0;
                    int numberLinesReadPassage = 0;

                    try {
                        SaveProfile(mSharedPrefs.getString("profil", "Aucun profil n'est sélectionné"));
                        FileInputStream fileInputStream = new FileInputStream(VariablesManagement.dossier_projet + File.separator + "listeProfil.txt");
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

                        while ((line = bufferedReader.readLine()) != null) {
                            numberLinesRead++;

                            if (line.contains(selection + "Type de profil : ")) {
                                compoLine = line.split("Type de profil : ");
                                editor.putString("profil", compoLine[0]);
                                editor.putInt("type_de_profil", Integer.parseInt(compoLine[1]));
                                editor.commit();
                                passage = 1;

                            } else if (line.contains("Type de profil : ") && passage == 1) {
                                break;
                            } else {
                                if (passage == 1) {
                                    if (numberLinesReadPassage == 0) {
                                        if (line.equals("null"))
                                            editor.remove("choix_moment");
                                        else
                                            editor.putString("choix_moment", line);
                                        editor.commit();
                                    } else if (numberLinesRead == 1) {
                                        if (line.equals("null"))
                                            editor.remove("choix_jour");
                                        int color = 0;
                                        switch (line) {
                                            case "Lundi":
                                                color = R.color.colorRed;
                                                break;
                                            case "Mardi":
                                                color = R.color.colorGreen;
                                                break;
                                            case "Mercredi":
                                                color = R.color.colorBlue;
                                                break;
                                            case "Jeudi":
                                                color = R.color.colorYellow;
                                                break;
                                            case "Vendredi":
                                                color = R.color.colorBrown;
                                                break;
                                            case "Samedi":
                                                color = R.color.colorOrange;
                                                break;
                                            case "Dimanche":
                                                color = R.color.colorWhite;
                                                break;
                                            default:
                                        }
                                        editor.putInt("couleur_jour", getResources().getColor(color));
                                    }
                                    editor.commit();
                                } else if (numberLinesReadPassage == 2) {
                                    editor.putInt("list_timetable_size", Integer.parseInt(line));
                                    editor.commit();
                                } else {
                                    if (mSharedPrefs.getInt("list_timetable_size", 0) > numberLinesReadPassage - 3) {
                                        editor.putInt("list_timetable_" + (numberLinesReadPassage - 3), Integer.parseInt(line));
                                        editor.commit();
                                    }
                                }

                            }

                            numberLinesReadPassage++;

                        }

                        bufferedReader.close();

                    } catch (Exception e) {

                    }

                    Toast.makeText(activity.getApplicationContext(), selection + "chargé", Toast.LENGTH_SHORT).show();
                    changeProfileBox.dismiss();
                    finish();
                    startActivity(getIntent());

                } else {
                    Toast.makeText(getBaseContext(), "Veuillez sélectionner un profil", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button bouton_annuler = (Button) findViewById(R.id.bouton_annuler);
        bouton_annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfileBox.dismiss();
            }
        });
        changeProfileBox.show();
    }

    public void DeleteProfilePopup(final Activity activity) {
        final Dialog deleteProfileBox = new Dialog(activity);
        deleteProfileBox.setContentView(R.layout.popup_change_profile);
        deleteProfileBox.setTitle("Choisir le profil à supprimer");
        deleteProfileBox.setCancelable(false);

        final RadioGroup RadioGroupProfile = (RadioGroup) deleteProfileBox.findViewById(R.id.radiogroup_changeprofil);

        final SharedPreferences mSharedPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = mSharedPrefs.edit();

        for (int i = 0; i < profileNameList.size(); i++) {
            RadioButton radioButtonView = new RadioButton(this);
            radioButtonView.setText(profileNameList.get(i));
            RadioGroupProfile.addView(radioButtonView);
        }

        Button bouton_ok = (Button) deleteProfileBox.findViewById(R.id.bouton_ok);
        bouton_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = RadioGroupProfile.getCheckedRadioButtonId();
                if (id != -1) {
                    View radioButton = RadioGroupProfile.findViewById(id);
                    int radioID = RadioGroupProfile.indexOfChild(radioButton);
                    RadioButton button = (RadioButton) RadioGroupProfile.getChildAt(radioID);
                    String profile = (String) button.getText();

                    String line;
                    StringBuffer stringBuffer = new StringBuffer();
                    int numberLinesRead = -1;
                    int passage = 0;

                    try {
                        FileInputStream fileInputStream = new FileInputStream(VariablesManagement.dossier_projet + File.separator + "listeProfil.txt");
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

                        while ((line = bufferedReader.readLine()) != null) {
                            numberLinesRead++;

                            if (line.startsWith(profile + " Type de profil : ")) {
                                passage = 1;
                            } else if (line.contains(" Type de profil : ") && passage == 1) {
                                stringBuffer.append(line + "\n");
                                passage = 0;
                            } else {
                                if (passage == 0) {
                                    stringBuffer.append(line + "\n");
                                }
                            }
                        }
                        bufferedReader.close();
                        BufferedWriter out = new BufferedWriter(new FileWriter(VariablesManagement.dossier_projet + File.separator + "listeProfil.txt"));
                        out.write(stringBuffer.toString());
                        out.close();

                        //Suppression des pictogrammes du profil
                        //FilesManagement.DeletePicto(profile));

                        if (mSharedPrefs.getString("profil", "Aucun profil n'est sélectionné").equals(profile)) {
                            editor.putString("profil", "Aucun profil n'est sélectionné");
                            editor.putInt("type_de_profil", 0);
                            editor.commit();
                            Toast.makeText(getBaseContext(), "Profil actuel effacé", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), "Profil " + profile + " effacé", Toast.LENGTH_LONG).show();
                        }


                    } catch (Exception e) {
                    }

                    deleteProfileBox.dismiss();
                    finish();
                    startActivity(getIntent());
                } else {
                    Toast.makeText(getBaseContext(), "Veuillez sélectionner un profil", Toast.LENGTH_LONG).show();
                }

            }
        });

        Button bouton_annuler = (Button) deleteProfileBox.findViewById(R.id.bouton_annuler);
        bouton_annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProfileBox.dismiss();
            }
        });
        deleteProfileBox.show();


    }
}

