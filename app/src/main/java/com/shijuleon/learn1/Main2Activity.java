package com.shijuleon.learn1;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    public static final String PREFS_NAME = "LazyShutdownPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        setContentView(R.layout.activity_main2);
        TextView text  = (TextView) findViewById(R.id.editText);
        text.setText(settings.getString("username", "none"));
        text  = (TextView) findViewById(R.id.editText2);
        text.setText("*******");
        text  = (TextView) findViewById(R.id.editText3);
        text.setText(settings.getString("host", "none"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    public void onClickSetPrefs(View view){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        EditText editText = (EditText)findViewById(R.id.editText);
        String username = editText.getText().toString();

        EditText editText2 = (EditText)findViewById(R.id.editText2);
        String password = editText2.getText().toString();


        EditText editText3 = (EditText)findViewById(R.id.editText3);
        String host = editText3.getText().toString();

        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("host", host);
        editor.commit();
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
