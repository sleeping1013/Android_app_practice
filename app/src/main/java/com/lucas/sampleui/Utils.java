package com.lucas.sampleui;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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


}
