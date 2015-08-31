package com.lucas.sampleui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Vector;


public class MainActivity extends ActionBarActivity {

    private EditText inputText;
    private CheckBox hide;
    private ListView history;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = sp.edit();

        inputText = (EditText) findViewById(R.id.editText);
        //inputText.setText("Hello World");
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

        loadHistory();
    }

    private void loadHistory() {
       String result = Utils.readFile(this, "history.txt");
       String[] data = result.split("\n"); //由空格隔開形成array

        ArrayAdapter<String> adaptor = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, data);

       history.setAdapter(adaptor); // 把寫下來的值丟到ListView中

   }



    public void submit (View view) {                              // Submit在這!!
        String text = inputText.getText().toString();
        if (hide.isChecked()) {
            text = "*********";
        }
        inputText.setText("");
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();

        Utils.writeFile(this, "history.txt", text + "\n"); // 在sumbit時寫入
        loadHistory();

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
