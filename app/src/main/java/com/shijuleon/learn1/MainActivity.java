package com.shijuleon.learn1;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import android.content.SharedPreferences;
import java.io.*;
public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "LazyShutdownPrefs";


    public void initializeSSH(String command) {

        try {
            JSch jsch = new JSch();
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String username = settings.getString("username", "shiju");
            String password = settings.getString("password", "password");
            String host = settings.getString("host", "192.168.1.2");
            Session session = jsch.getSession(username, host, 22);
           session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);

            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] tmp = new byte[1024];
            String output = " ";
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    output += new String(tmp, 0, i);
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }
            TextView text  = (TextView) findViewById(R.id.textView);
            text.setText(output);
            channel.disconnect();
            session.disconnect(); /* */
        } catch (Exception e) {
            System.out.println(e);        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        TextView text  = (TextView) findViewById(R.id.textView);
        text.setText("Current host: " + settings.getString("host", "password") + "\n" + "Current user: " + settings.getString("username", "password"));


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


    public void onClickShutdown(View view){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String command = "echo " + settings.getString("password", "password") +  " | sudo -S systemctl poweroff";
        initializeSSH(command);

    }

    public void onClickReboot(View view){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String command = "echo " + settings.getString("password", "password") +  " | sudo -S systemctl reboot";
        initializeSSH(command);

    }

    public void onClickSuspend(View view){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String command = "echo " + settings.getString("password", "password") +  " | sudo -S systemctl suspend";
        initializeSSH(command);

    }
    public void onClickLock(View view){
        String command = "export DISPLAY=\":0.0\"; xscreensaver-command -lock";
        initializeSSH(command);
    }

    public void onClickSettings(View view){
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);

    }
}
