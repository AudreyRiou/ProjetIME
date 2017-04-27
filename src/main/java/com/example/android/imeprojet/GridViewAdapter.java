package com.example.android.imeprojet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by isen on 12/04/2017.
 */

public class GridViewAdapter extends BaseActivity {
    private ArrayList<Integer> IDList;
    private ArrayList<String> nameList;
    private ArrayList<Drawable> picturesList;
    private Activity activity;
    private int type;

    public GridViewAdapter(Activity activity, ArrayList<Integer> IDList, ArrayList<String> nameList, ArrayList<Drawable> picturesList, int type)
    {
        super();
        this.IDList = IDList;
        this.nameList = nameList;
        this.picturesList = picturesList;
        this.type = type;
    }

    public int getCount()
    {
        return IDList.size();
    }

    public Integer getItem(int position)
    {
        return IDList.get(position);
    }

    public String getItemName(int position)
    {
        return nameList.get(position);
    }

    public long getItemID(int position)
    {
        return 0;
    }

    public static class ViewHolder
    {
        public ImageView imgViewPicture;
        public TextView textViewTitle;
    }

    public void removeItem(int position)
    {
        IDList.remove(position);
        nameList.remove(position);
        picturesList.remove(position);
        notify();
    }

    public View getView(int position, View convertView, ViewGroup parentView)
     {
         ViewHolder view;
         LayoutInflater inflater = activity.getLayoutInflater();

         if(convertView == null)
         {
             view = new ViewHolder();
             convertView = inflater.inflate(R.layout.gridview_row, null);

             view.textViewTitle = (TextView) convertView.findViewById(R.id.textViewGrid);
             view.imgViewPicture  = (ImageView) convertView.findViewById(R.id.imageViewGrid);

             convertView.setTag(view);
         }
         else
         {
             view = (ViewHolder) convertView.getTag();
         }

         Bitmap bitmapImage;
         if (picturesList.get(position) != null && type == 0)
         {
             view.textViewTitle.setText(nameList.get(position));
             view.imgViewPicture.setImageDrawable(picturesList.get(position));
             view.imgViewPicture.setMinimumHeight(150);
             view.imgViewPicture.setMinimumWidth(150);
         }
         else if (picturesList.get(position) != null && type ==1)
         {
             view.textViewTitle.setMinimumHeight(100);
             view.textViewTitle.setMinimumWidth(100);
             view.textViewTitle.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorWhite));
             view.textViewTitle.setTextColor(ContextCompat.getColor(activity, R.color.colorBlack));
             view.textViewTitle.setTextSize(35);
             view.textViewTitle.setGravity(Gravity.CENTER);
             view.textViewTitle.setTypeface(null, Typeface.BOLD);
             view.textViewTitle.setText(nameList.get(position));

             bitmapImage = BitmapManagement.drawableToBitmap(picturesList.get(position));
             bitmapImage = BitmapManagement.scaleDown(bitmapImage, 50, true);
             view.imgViewPicture.setImageBitmap(bitmapImage);
             view.imgViewPicture.setMinimumHeight(50);
             view.imgViewPicture.setMinimumWidth(50);
             bitmapImage = null;
         }
         else
         {
             view.textViewTitle.setMinimumHeight(150);
             view.textViewTitle.setMinimumWidth(150);
             view.textViewTitle.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorWhite));
             view.textViewTitle.setTextColor(ContextCompat.getColor(activity, R.color.colorBlack));
             view.textViewTitle.setTextSize(35);
             view.textViewTitle.setGravity(Gravity.CENTER);
             view.textViewTitle.setTypeface(null, Typeface.BOLD);
             view.textViewTitle.setText(nameList.get(position));

             view.imgViewPicture.setMaxHeight(0);
             view.imgViewPicture.setMaxWidth(0);
             view.imgViewPicture.setImageDrawable(picturesList.get(position));
         }

         return convertView;
     }

}
