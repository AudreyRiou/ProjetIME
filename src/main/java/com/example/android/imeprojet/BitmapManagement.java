package com.example.android.imeprojet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by isen on 05/04/2017.
 */

public class BitmapManagement {

    static public Bitmap decodeFile(File bitmapFile)
    {
        Bitmap bitmap = null;
        final int IMAGE_MAX_SIZE = 500;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        FileInputStream fileInputStream;
        try
        {
            fileInputStream = new FileInputStream(bitmapFile);
            BitmapFactory.decodeStream(fileInputStream, null, bitmapOptions);
            fileInputStream.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        int scale = 1;
        if (bitmapOptions.outHeight > IMAGE_MAX_SIZE || bitmapOptions.outHeight > IMAGE_MAX_SIZE)
        {
            scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE / (double) Math.max(bitmapOptions.outHeight, bitmapOptions.outWidth)) / Math.log(0.5)));
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try
        {
            fileInputStream = new FileInputStream(bitmapFile);
            bitmap = BitmapFactory.decodeStream(fileInputStream, null, o2);
            fileInputStream.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
        return bitmap;

    }

    /*Méthode permettant de convertir un drawable en bitmap*/
    public static Bitmap drawableToBitmap(Drawable drawable)
    {
        Bitmap bitmap = null;
        if(drawable instanceof BitmapDrawable)
        {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        try
        {
            /*On crée un bitmap à partir de la largeur et de la hauteur du draable source*/
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static void saveBitmap(Drawable drawable, File directory, String fileName)
    {
        Bitmap bitmap = BitmapManagement.drawableToBitmap(drawable);
        bitmap = scaleDown(bitmap, 150, true);

        FileOutputStream fileOutputStream = null;
        try
        {
            fileOutputStream = new FileOutputStream(directory+File.separator+fileName);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }

        try
        {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    public static Bitmap getResizedBitmap(Bitmap bitmap, int newHeight, int newWidth)
    {
        //Chaque pixel du bitmap est stocké sur 4 bits
        Bitmap bitmapModified = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        //Mise à l'échelle des dimensions
        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, middleX, middleY);

        //On redessine le pictogramme à l'échelle
        Canvas canvas = new Canvas(bitmapModified);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return bitmapModified;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter)
    {
        float maxImageSizeInitial = maxImageSize;
        Bitmap finalBitmap;

        if(realImage.getWidth()/2>maxImageSize)
        {
            maxImageSize = realImage.getWidth()/2;
            float ratio = Math.min((float) maxImageSize / realImage.getWidth(), (float) maxImageSize / realImage.getHeight());
            int width = Math.round((float) ratio*realImage.getWidth());
            int height = Math.round((float) ratio*realImage.getHeight());
            Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
            finalBitmap = scaleDown(newBitmap, maxImageSizeInitial, true);
            newBitmap.recycle();
        }
        else
        {
            float ratio = Math.min((float) maxImageSize /realImage.getWidth(), (float) maxImageSize / realImage.getHeight());
            int width = Math.round((float) ratio * realImage.getWidth());
            int height = Math.round((float) ratio * realImage.getHeight());
            finalBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        }
        return finalBitmap;
    }

 }

