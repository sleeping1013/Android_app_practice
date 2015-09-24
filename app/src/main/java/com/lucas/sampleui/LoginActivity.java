package com.lucas.sampleui;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.FacebookSdk;

/**
 * Created by user on 2015/9/24.
 */
public class LoginActivity extends Activity{

    @Override
        protected void onCreate(Bundle savedInstanceState) {
                FacebookSdk.sdkInitialize(getApplicationContext());
                super.onCreate(savedInstanceState);
               setContentView(R.layout.activity_login);
            }
}
