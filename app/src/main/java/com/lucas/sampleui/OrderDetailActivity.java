package com.lucas.sampleui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;


public class OrderDetailActivity extends ActionBarActivity {

    private TextView textView;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        textView = (TextView) findViewById(R.id.textView);
        webView = (WebView) findViewById(R.id.webView);

        Intent intent = getIntent();
        String note = intent.getStringExtra("note");
        String storeInfo = intent.getStringExtra("store_info");
        String menu = intent.getStringExtra("menu");



        textView.setText(note + "," + storeInfo + ",..." + menu);

        loadGeoPoint(storeInfo);
        loadWebView();
    }

    private void loadGeoPoint(String storeInfo){
        String geoQueryUrl = Utils.getGeoQueryUrl(storeInfo);
        Utils.NetworkTask task = new Utils.NetworkTask();
        task.setCallback(new Utils.NetworkTask.Callback() {
            @Override
            public void done(byte[] fetchResult) {
                String jsonString = new String(fetchResult);
                textView.setText(jsonString);
            }
        });
        task.execute(geoQueryUrl);
    }





    private final static String STATIC_MAP_URL = "https://maps.googleapis.com/maps/api/staticmap?";

    private void loadWebView() {

        String url = "https://maps.googleapis.com/maps/api/staticmap?center=25.020384,%20121.544608&zoom=15&size=600x300";
        webView.loadUrl(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
