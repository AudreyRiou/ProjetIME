package com.example.android.imeprojet;

/**
 * Created by isen on 04/04/2017.
 */

import android.graphics.drawable.Drawable;

import java.io.File;
import java.util.ArrayList;

public class VariablesManagement {

    static public File dossier_projet;
    static public File dossier_pictos;
    static public File dossier_pictos_timetable;
    static public File dossier_pictos_meal;
    static public File dossier_pictos_appel;


    static public ArrayList<Drawable> drawable_stockage_timetable=new ArrayList<Drawable>();
    static public ArrayList<String> nom_stockage_timetable=new ArrayList<String>();
    static public ArrayList<Integer> area1_pictos_timetable=new ArrayList<Integer>();


    static public ArrayList<Drawable> drawable_stockage_meal=new ArrayList<Drawable>();
    static public ArrayList<String> nom_stockage_meal=new ArrayList<String>();
    static public ArrayList<Integer> area1_pictos_meal_entree=new ArrayList<Integer>();
    static public ArrayList<Integer> area1_pictos_meal_plat=new ArrayList<Integer>();
    static public ArrayList<Integer> area1_pictos_meal_dessert=new ArrayList<Integer>();

    static public ArrayList<Drawable> drawable_stockage_appel=new ArrayList<Drawable>();
    static public ArrayList<String> nom_stockage_appel=new ArrayList<String>();
    static public ArrayList<Integer> pictos_present_aujourdhui=new ArrayList<Integer>();
    static public ArrayList<Integer> pictos_absent_aujourdhui=new ArrayList<Integer>();
    static public ArrayList<Integer> pictos_present_demain=new ArrayList<Integer>();
    static public ArrayList<Integer> pictos_absent_demain=new ArrayList<Integer>();



}

