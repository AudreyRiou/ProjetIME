package com.example.android.imeprojet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by isen on 26/04/2017.
 */

public class MonthActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);
        //setmyToolbar(MainActivity.class);

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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(MonthActivity.this, TwoWeeksActivity.class));
        finish();

    }
}
