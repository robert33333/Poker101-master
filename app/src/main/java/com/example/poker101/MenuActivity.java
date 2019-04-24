package com.example.poker101;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.poker101.date.Comanda;

import org.json.JSONException;

import java.io.IOException;

import static com.example.poker101.User.MY_PREFS_NAME;
import static com.example.poker101.User.oos;

public class MenuActivity extends AppCompatActivity {
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //getUserPreferences
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        User.theme_id = prefs.getInt("theme_id",0);
        User.card_id = prefs.getInt("card_id",0);

        super.onCreate(savedInstanceState);

        switch (User.theme_id) {
            case 0:
                setTheme(R.style.Default);
                break;
            case 1:
                setTheme(R.style.Dark);
                break;
            case 2:
                setTheme(R.style.Light);
                break;
            default: setTheme(R.style.Default);
        }

        setContentView(R.layout.activity_menu);

        if (getIntent().getBooleanExtra("fromDecline", false)) {
            Toast.makeText(this, "The opponent has closed the game.", Toast.LENGTH_LONG).show();
        }
        Fragment fragment_menu = new MenuFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_menu, fragment_menu, "fragment_menu").commit();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        User.callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        User.getMessage = true;
        User.context = getApplicationContext();
        sendAvailabilityInfoToServer("userOnline");
    }

    @Override
    protected void onPause() {
        User.getMessage = false;
        sendAvailabilityInfoToServer("userOffline");
        super.onPause();
    }

    private void sendAvailabilityInfoToServer(final String option) {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (User.socket == null) {
                        User.initialize();
                    }
                    Comanda cmd =
                            new Comanda(option,
                                    User.user.get("id"));
                    oos.writeObject(cmd);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        Thread myThread = new Thread(myRunnable);
        myThread.start();
        try {
            myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
