package com.example.android.imeprojet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by isen on 05/04/2017.
 */

public class FilesManagement {

    public static File createPictoFile(String fileName)
    {
        File SDcard = new File(Environment.getExternalStorageDirectory().getPath()+File.separator+fileName);
        String[] SDcardContent = SDcard.list();
        int i = 0;
        if (SDcardContent != null)
        {
            while(i<SDcardContent.length)
            {
                if (SDcardContent[i].equals(fileName))
                {
                    return new File(Environment.getExternalStorageDirectory().getPath()+File.separator+fileName);
                }
                i++;
            }
        }
        File pictoDirectory = new File(Environment.getExternalStorageDirectory().getPath()+File.separator+fileName);
        pictoDirectory.mkdirs();

        return pictoDirectory;
    }


    public static boolean createBasePicto(File targetDirectory, String pictoName, Drawable picto)
    {
        String[] contentPictoDirectory = targetDirectory.list();
        int i = 0;
        if(contentPictoDirectory != null)
        {
            while(i<contentPictoDirectory.length)
            {
                if (contentPictoDirectory[i].equals(pictoName+".png"))
                {
                    return true;
                }
                i++;
            }

        }
        BitmapManagement.saveBitmap(picto, targetDirectory, pictoName + ".png");
        return false;
    }

    public static void goThroughPictoDirectory(Activity activity, File targetDirectory, ArrayList<Drawable> arrayList)
    {
        arrayList.clear();

        if(targetDirectory.exists())
        {
            for(int i = 0; i<targetDirectory.list().length; i++)
            {
                if (targetDirectory.list()[i].endsWith(".png")==true || targetDirectory.list()[i].endsWith(".jpg")==true)
                {
                    File image = new File(targetDirectory+File.separator+targetDirectory.list()[i]);
                    Bitmap bitmap = BitmapManagement.decodeFile(image);
                    arrayList.add(new BitmapDrawable(activity.getBaseContext().getResources(), bitmap));
                }
            }
        }
    }

    public static void goThroughPictoList(Activity activity, File targetFile, ArrayList<Drawable> arrayListBitmap, ArrayList<String> arrayListName, String profile)
    {
        arrayListBitmap.clear();
        arrayListName.clear();
        if(targetFile.exists())
        {
            try
            {
                InputStream inputStream = new FileInputStream(targetFile);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                String[] compoLine;
                String bitmapName;
                String[] name;
                while ((line=bufferedReader.readLine()) != null)
                {
                    compoLine = line.split(" Nom_associé:");
                    bitmapName = compoLine[0];
                    name = compoLine[1].split("Access :");
                    if((bitmapName.endsWith(".png")==true || bitmapName.endsWith(".jpg")==true) && (name[1].equals(profile) || name[1].equals("Tout le monde")))
                    {
                        File image = new File(targetFile.getParentFile()+File.separator+bitmapName);
                        Bitmap bitmap = BitmapManagement.decodeFile(image);
                        arrayListBitmap.add(new BitmapDrawable(activity.getBaseContext().getResources(),bitmap));
                        arrayListName.add(name[0]);
                    }
                }
                bufferedReader.close();
            }
            catch(Exception e)
            {
                System.out.println(e.toString());
            }
        }
    }

    public static void deletePictoProfile(String profile)
    {
        File tab[] = {VariablesManagement.dossier_pictos_appel, VariablesManagement.dossier_pictos_meal, VariablesManagement.dossier_pictos_timetable};
        File targetFile;
        for(int i =  0; i < tab.length; i++)
        {
            targetFile = new File(tab[i] + File.separator + "liste.txt");
            if (targetFile.exists())
            {
               String line;
               StringBuffer stringBuffer = new StringBuffer();
               int nbLinesRead = -1;
               String compoLine[];
               String bitmapName;
               String name[];
                try
                {
                    FileInputStream fileInputStream = new FileInputStream(targetFile);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                    {
                        while((line = reader.readLine()) != null)
                        {
                            nbLinesRead ++;
                            compoLine = line.split("Nom_associé:");
                            bitmapName = compoLine[0];
                            name = compoLine[1].split("Access");
                            if(name[1].equals(profile))
                            {
                                File image = new File(targetFile.getParentFile() + File.separator + bitmapName);
                                image.delete();
                            }
                            else
                            {
                                stringBuffer.append(line + "\n");
                            }
                        }
                    }
                        reader.close();
                        BufferedWriter out = new BufferedWriter(new FileWriter(targetFile));
                        out.write(stringBuffer.toString());
                        out.close();
                }
                    catch (Exception e)
                    {
                        System.out.println(e.toString());
                    }
            }
        }

    }
    public static boolean DeletePicto(String fileName, int lineNumber)
    {
        String line;
        StringBuffer stringBuffer = new StringBuffer();
        int nbLinesRead = -1;
        String compoLine[];
        try
        {
            FileInputStream fileInputStream = new FileInputStream(fileName+File.separator+"liste.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));

            while ((line = reader.readLine()) != null)
            {
                nbLinesRead++;
                if (nbLinesRead == lineNumber)
                {
                    compoLine = line.split("Nom_associé:");
                    File temp = new File(fileName+File.separator+compoLine[0]);
                    temp.delete();
                }
                else
                {
                    stringBuffer.append(line + "\n");
                }
            }
            reader.close();
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName+File.separator+"liste.txt"));
            out.write(stringBuffer.toString());
            out.close();

        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }

    public static void ResizeFilePhoto(File image)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(image.getPath());
        try {
            ExifInterface exif = new ExifInterface(image.getPath());
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotate = 0;

            switch(exifOrientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            if(rotate != 0)
            {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                Matrix matrix = new Matrix();
                matrix.preRotate(rotate);

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            }
        }
        catch(IOException e)
        {

        }

        Bitmap outBitmap = BitmapManagement.getResizedBitmap(bitmap, 150, 150);
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(image);
            outBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            bitmap.recycle();
            outBitmap.recycle();
        }
        catch(Exception e) {

        }
    }

    public static void ResizeFileGallery(File image)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(image.getPath());
        Bitmap outBitmap = BitmapManagement.scaleDown(bitmap, 150, true);

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(image);
            outBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            bitmap.recycle();
            outBitmap.recycle();
        }
        catch (Exception e)
        {

        }
    }


}
