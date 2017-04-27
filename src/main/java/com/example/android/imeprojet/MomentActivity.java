package com.example.android.imeprojet;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by isen on 04/04/2017.
 */

public class MomentActivity extends BaseActivity {

    /*Déclaration des variables globales*/
    private LinearLayout verticalArea, horizontalArea;
    private ImageView trash;
    public static final int HAUTEUR_PICTO = 150;
    public static final int LARGEUR_PICTO = 150;
    private float y1, y2;
    private TextToSpeech mTtS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment);

        /*      Gestion de l'apparence de la barre d'outils*/
        setmyToolbar(DateActivity.class);
        SharedPreferences mSharedPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        /*      Récupération du jour de la semaine*/
        String activity = mSharedPrefs.getString("choixMoment",null);
        String activityfirstLetterUpper = activity.replaceFirst(".", (activity.charAt(0) + "").toUpperCase());
        /*      Affichage du jour dans la barre d'outils*/
        setTitle(mSharedPrefs.getString("profil", "Aucun profil sélectionné")+" - " + activityfirstLetterUpper);

        verticalArea = (LinearLayout) findViewById(R.id.verticalScrollViewArea);
        horizontalArea = (LinearLayout) findViewById(R.id.horizontalScrollViewArea);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int screenWidth = displayMetrics.widthPixels;
        /* Gestion de la largeur des différentes parties de activity_moment*/
        horizontalArea.setMinimumWidth(screenWidth);
        verticalArea.setMinimumWidth(LARGEUR_PICTO);

        /*Déclaration de nouveaux éléments XML qui seront modifiés lors de l'activité.
        Ici, la vue globale et la TextView contenant le moment de la journée sur lequel l'utilisateur
         a cliqué seront changés
         */

        /*
        final LinearLayout activity_moment = (LinearLayout) findViewById(R.id.activity_moment);
        final TextView moment_text_view = (TextView) findViewById(R.id.moment_text_view);


        if (mSharedPrefs.contains("choixMoment") && mSharedPrefs.getString("choixMoment",null) != null)
        {
            moment_text_view.setText(mSharedPrefs.getString("choixMoment",null));
        };*/
    }

    /*Gestion de l'appui sur le bouton retour*/
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(MomentActivity.this, DateActivity.class));
        finish();

    }

    public void Son(final String texteSon)
    {
        if (mTtS != null)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                ttsGreater21(texteSon);
            }
            else
            {
                ttsUnder20(texteSon);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text)
    {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        mTtS.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text)
    {
        String utteranceId=this.hashCode() + "";
        mTtS.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    private int calculateNewIndex(float y)
    {
        int index = (int) Math.floor((y-HAUTEUR_PICTO) / HAUTEUR_PICTO);
        if (index>verticalArea.getChildCount())
        {
            index = verticalArea.getChildCount();
        }

        if (index < 0) return 0;

        return (index);
    }

    View.OnTouchListener myOnTouchListener = new View.OnTouchListener() {
        public boolean onTouch(final View view, MotionEvent motionEvent)
        {
            switch (motionEvent.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    y1 = motionEvent.getY();
                    if(view.getParent() == verticalArea)
                    {
                        /*ClipData is a complex type that contains one ore more item instances, each of which
                        can hold one or more representations of an item of data
                         */
                        ClipData clipData = ClipData.newPlainText("","");
                        /*DragShadowBuilder : Creates an image that the system displays during the drag and drop operation.*/
                        View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(view);
                        view.startDrag(clipData, dragShadowBuilder, view, 0);
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    y2 = motionEvent.getY();
                    float deltaY = y2 - y1;

                    if(Math.abs(deltaY) > 10 && view.getParent() == horizontalArea) {
                        ClipData clipData = ClipData.newPlainText("", "");
                        View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(view);
                        view.startDrag(clipData, dragShadowBuilder, view, 0);
                    }
                    break;

                default:
                    break;
            }

            return true;
        }
    };

    View.OnDragListener myOnDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            final ScrollView sv = (ScrollView)findViewById(R.id.scrollView);
            SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences mSharedPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);

            switch (event.getAction()) {
                case DragEvent.ACTION_DROP:
                    View view = (View)event.getLocalState();
                    LinearLayout oldParent = (LinearLayout)view.getParent();
                    if(v == trash) v = horizontalArea;
                    LinearLayout newParent = (LinearLayout)v;

                    if(oldParent == newParent && newParent == horizontalArea){
                    }
                    else if(oldParent == verticalArea && newParent == horizontalArea){
                        oldParent.removeView(view);
                    }
                    else {
                        oldParent.removeView(view);

                        if (newParent == verticalArea && oldParent== horizontalArea) {
                            final int index = calculateNewIndex(event.getY());
                            newParent.addView(view, index);

                            if(mSharedPrefs.getInt("type_de_profil", 0) == 0) createProfilePicto1(view.getId(), oldParent);
                            else if(mSharedPrefs.getInt("type_de_profil", 0) == 1) createProfilePicto2(view.getId(), oldParent);
                            else if(mSharedPrefs.getInt("type_de_profil", 0) == 2) createProfilePicto3(view.getId(), oldParent);

                        }
                        else if(newParent == verticalArea){
                            final int index = calculateNewIndex(event.getY());
                            newParent.addView(view, index);
                        }
                        else {
                            newParent.addView(view);
                        }
                    }
                    if(mSharedPreferences.getInt("son_active", 0) == 1) Son(VariablesManagement.nom_stockage_timetable.get(view.getId()));


                    sv.post(new Runnable() {
                        public void run() {
                            sv.scrollTo(0, sv.getBottom());

                        }
                    });

                    break;

                default:
                    break;
            }

            return true;
        }

    };
    /*Création de trois méthodes de création de profil pour trois types d'affichage différents*/

    /* Type de profil numéro 1 : grande image, petit texte*/
    public void createProfilePicto1(int id, LinearLayout Linear)
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 5, 5, 5);
        LinearLayout wholeView;
        wholeView = new LinearLayout(this);
        wholeView.setOrientation(LinearLayout.VERTICAL);
        wholeView.setMinimumWidth(150);
        wholeView.setGravity(Gravity.CENTER);
        wholeView.setLayoutParams(layoutParams);
        wholeView.setOnTouchListener(myOnTouchListener);

        /*Ajout de l'imageView à la vue d'ensemble*/
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(layoutParams);
        wholeView.addView(imageView);

        /*Ajout de la textView à la vue d'ensemble*/
        TextView textView = new TextView(this);
        wholeView.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorWhite));
        LinearLayout.LayoutParams layoutParamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParamsText);
        textView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorBlack));
        textView.setTextSize(20);
        wholeView.addView(textView);

        imageView.setImageDrawable((Drawable) VariablesManagement.drawable_stockage_timetable.get(id));
        textView.setText(VariablesManagement.nom_stockage_timetable.get(id));
        wholeView.setId(id);
        //wholeView.setBackgroundResource(R.drawable.imageview);
        Linear.addView(wholeView);

    }

    /*Type 2 : petite image, grand texte*/
    public void createProfilePicto2(int id, LinearLayout Linear)
    {
        Bitmap bitmapImage;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 5, 5, 5);
        LinearLayout wholeView = new LinearLayout(this);
        wholeView.setOrientation(LinearLayout.VERTICAL);
        wholeView.setMinimumWidth(150);
        wholeView.setGravity(Gravity.CENTER);
        wholeView.setLayoutParams(layoutParams);
        wholeView.setOnTouchListener(myOnTouchListener);

        TextView textView = new TextView(this);
        wholeView.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorWhite));
        LinearLayout.LayoutParams layoutParamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParamsText);
        textView.setTextSize(40);
        textView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorBlack));
        wholeView.addView(textView);

        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParamsImage = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsImage.setMargins(5, 5, 5, 5);
        imageView.setLayoutParams(layoutParamsImage);
        wholeView.addView(imageView);

        bitmapImage = BitmapManagement.drawableToBitmap((Drawable) VariablesManagement.drawable_stockage_timetable.get(id));
        bitmapImage = BitmapManagement.scaleDown(bitmapImage, 50, true);
        imageView.setImageBitmap(bitmapImage);
        textView.setText(VariablesManagement.nom_stockage_timetable.get(id));
        wholeView.setId(id);
        //wholeView.setBackgroundResource(R.drawable.imageview);
        Linear.addView(wholeView);

        bitmapImage = null;

    }

    /*Type 3 : pas d'image, seulement du texte*/
    public void createProfilePicto3(int id, LinearLayout Linear)
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 5, 5, 5);

        TextView textView  = new TextView(this);
        textView.setLayoutParams(layoutParams);
        textView.setOnTouchListener(myOnTouchListener);
        textView.setMinimumWidth(150);
        textView.setMinimumHeight(150);
        textView.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorWhite));
        textView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorBlack));
        textView.setTextSize(40);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(null, Typeface.BOLD);

        textView.setId(id);
        textView.setText(VariablesManagement.nom_stockage_timetable.get(id));
        //textView.setBackgroundResource(R.drawable.imageview);
        Linear.addView(textView);

    }

    public void onResume()
    {
        super.onResume();
        mTtS = new TextToSpeech(this, new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int status)
            {
                    /*S'il n'y a pas d'erreur, on définit la langue en français*/
                if(status != TextToSpeech.ERROR)
                {
                    mTtS.setLanguage(Locale.FRENCH);
                }
            }
        });

        SharedPreferences mSharedPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        FilesManagement.goThroughPictoList(MomentActivity.this, new File(VariablesManagement.dossier_pictos_timetable + File.separator + "liste.txt"), VariablesManagement.drawable_stockage_timetable, VariablesManagement.nom_stockage_timetable, mSharedPrefs.getString("profil", "Aucun profil n'est sélectionné"));

        if (mSharedPrefs.contains("list_timetable_size"))
        {
            VariablesManagement.area1_pictos_timetable.clear();
            for (int i = 0; i < mSharedPrefs.getInt("list_timetable_size", 0); i++)
            {
                VariablesManagement.area1_pictos_timetable.add(mSharedPrefs.getInt("list_timetable_" + i, 0));
            }
        }
        verticalArea.setOnDragListener(myOnDragListener);
        horizontalArea.setOnDragListener(myOnDragListener);

        if (mSharedPrefs.getInt("type_de_profil", 0) == 0)
        {
            for(int i = 0; i < VariablesManagement.drawable_stockage_timetable.size(); i++ )
            {
                createProfilePicto1(i, horizontalArea);
            }

            for(int j = 0; j < VariablesManagement.area1_pictos_timetable.size(); j++)
            {
                createProfilePicto1(VariablesManagement.area1_pictos_timetable.get(j), verticalArea);
            }

        }

        else if (mSharedPrefs.getInt("type_de_profil", 0) == 1)
        {
            for (int i = 0; i < VariablesManagement.drawable_stockage_timetable.size(); i++)
            {
                createProfilePicto2(i, horizontalArea);
            }

            for (int j = 0; j < VariablesManagement.area1_pictos_timetable.size(); j++)
            {
                createProfilePicto2(VariablesManagement.area1_pictos_timetable.get(j), verticalArea);
            }
        }

        else if (mSharedPrefs.getInt("type_de_profil", 0) == 2)
        {
            for (int i = 0; i < VariablesManagement.drawable_stockage_timetable.size(); i++)
            {
                createProfilePicto3(i, horizontalArea);
            }

            for (int j = 0; j < VariablesManagement.area1_pictos_timetable.size(); j++)
            {
                createProfilePicto3(VariablesManagement.area1_pictos_timetable.get(j), verticalArea);
            }
        }

    }

    public void onPause()
    {
        super.onPause();

        if (mTtS != null) {
            mTtS.stop();
            mTtS.shutdown();
        }

        View view = null;
        SharedPreferences mSharedPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        int size = verticalArea.getChildCount();
        editor.putInt("list_timetable_size", size);

        for (int i = 0; i < size; i++) {
            view = verticalArea.getChildAt(i);
            editor.putInt("list_timetable_" + i, view.getId());
        }

        editor.commit();

        verticalArea.removeAllViews();
        horizontalArea.removeAllViews();

    }

}




