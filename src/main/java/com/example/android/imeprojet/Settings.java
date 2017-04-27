package com.example.android.imeprojet;

import android.os.Bundle;

/**
 * Created by isen on 04/04/2017.
 */

public class Settings extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getFragmentManager().findFragmentById(android.R.id.content)==null)
        {
            getFragmentManager().beginTransaction().add(android.R.id.content, new SettingsFragment()).commit();
        }

    }
}
