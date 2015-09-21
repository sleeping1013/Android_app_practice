package com.lucas.sampleui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_DRINK_MENU = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;

    private EditText inputText;
    private CheckBox hide;
    private ListView history;
    private Spinner storeInfo;
    private ImageView imageView;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private String drinkMenuResult;







     private List<ParseObject> orderResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "kbdBJ7bunictVpfjTwNNejQayzhKTav0zeZmziei", "BNZ65eOBQiIbrbcWbBSl8akqgv5wSLEJShRORwr9");

        sp = getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = sp.edit();

        inputText = (EditText) findViewById(R.id.editText);
        inputText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                editor.putString("input", inputText.getText().toString());
                editor.commit();

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    submit(v);
                    return true;
                }

                return false;
            }
        });

        inputText.setText(sp.getString("input", ""));

        hide = (CheckBox) findViewById(R.id.checkBox); // 在這裡才能開始拿到實體
        hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("hide",isChecked);
                editor.commit();
            }
        });

        hide.setChecked(sp.getBoolean("hide", false)); //把checkbox的值寫到SharedPreference裡面

        history = (ListView) findViewById(R.id.history);
        history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToOrderDetail(position);
            }
        });

        storeInfo = (Spinner) findViewById(R.id.spinner);
        imageView = (ImageView) findViewById(R.id.imageView);

        loadHistory();
        loadStoreInfo();
    }

    private void loadStoreInfo() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("StoreInfo"); //  !!! Parse.com上面要有一個class!!!

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
                String[] data  = new String[list.size()];

                for (int i =0; i < list.size(); i++) {
                    ParseObject object = list.get(i);
                    data[i] = object.getString("name") + " " + object.getString("address");
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_item, data);
                storeInfo.setAdapter(adapter);
            }
        });




    }



    private void loadHistory() {
        //
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Order");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e == null){
                    orderResult = list;

                    List<Map<String, String>> data = new ArrayList<>();
                    for(int i =0; i < list.size(); i++) {
                        ParseObject object = list.get(i); //
                        String note = object.getString("note");
                        String storeInfo = object.getString("store_info");
                        JSONArray menu = object.getJSONArray("menu");

                        Map<String, String> item = new HashMap<>();
                        item.put("note",note);
                        item.put("store_info",storeInfo);
                        item.put("sum","5");

                        data.add(item);

                    }
                    String[] from = new String[]{"note","store_info", "sum"};
                    int[] to = new int[] {R.id.note, R.id.store_info, R.id.sum};
                    SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, data, R.layout.listview_item, from, to);
                    history.setAdapter(adapter); // 把寫下來的值丟到ListView中

                }
            }
        });

   }



    private void saveOrder(SaveCallback saveCallback){
        ParseObject object = new ParseObject("Order"); //要上傳的表單名為Order
        object.put("note", inputText.getText().toString()); // 和JSONObject語法一樣
        object.put("store_info", (String) storeInfo.getSelectedItem());  // 和JSONObject語法一樣

        if (drinkMenuResult != null) {
            try {
                object.put("menu", new JSONArray(drinkMenuResult));// 和JSONarray語法類似
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        object.saveInBackground(saveCallback);
    }



    public void submit (View view) {                              // Submit在這!!
        String text = inputText.getText().toString();
        if (hide.isChecked()) {
            text = "*********";
        }

        Toast.makeText(this,text,Toast.LENGTH_LONG).show();

        saveOrder(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                loadHistory();
            }
        });

        inputText.setText("");
        drinkMenuResult = null;
    }


    private void goToOrderDetail(int position) {

        ParseObject order = orderResult.get(position);

        Intent intent = new Intent();
        intent.setClass(this, OrderDetailActivity.class);
        intent.putExtra("note", order.getString("note"));
        intent.putExtra("store_info", order.getString("store_info"));
        intent.putExtra("menu", order.getJSONArray("menu").toString());
        startActivity(intent);
    }


    public void goToDrinkMenu (View view) {
        String storeInfoString = (String) storeInfo.getSelectedItem(); //轉成String

        Intent intent = new Intent();
        intent.setClass(this, DrinkMenuActivity.class);
        intent.putExtra("store_info", storeInfoString);//把值put出去
        //startActivity(intent);
        startActivityForResult(intent, REQUEST_DRINK_MENU);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DRINK_MENU) {
            if (resultCode == RESULT_OK) {
                drinkMenuResult = data.getStringExtra("result");
                Log.d("debug", drinkMenuResult);
            }
        } else if (requestCode ==REQUEST_TAKE_PHOTO) {

            if (resultCode == RESULT_OK){
                Bitmap bm = data.getParcelableExtra("data");
                imageView.setImageBitmap(bm);
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_take_photo) {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
