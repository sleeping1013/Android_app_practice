package com.lucas.sampleui;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by user on 2015/8/31.
 */
public class Utils {

    public static void writeFile(Context context, String fileName, String text) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_APPEND);
            fos.write(text.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
          catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            byte[] buffer = new byte[1024];
            fis.read(buffer);
            fis.close();

            return new String(buffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static Uri getPhotoUri() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if (dir.exists() == false){
            dir.mkdir();
        }

        File file = new File(dir, "simpleui_photo.png");
        return Uri.fromFile(file);

    }

    public static byte[] uriToBytes(Context context, Uri uri) {

        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while( (len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

//   public static byte[] uriToBytes(Context context, Uri uri) {
//
//       try {
//           InputStream is = context.getContentResolver().openInputStream(uri);
//           ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//           byte[] buffer = new byte[1024];
//           int len = 0;
//           while(len = is.read()) {
//               baos.write(buffer, 0, len);
//           }
//
//       } catch (FileNotFoundException e) {
//           e.printStackTrace();
//       }
//
//
//   }


}
