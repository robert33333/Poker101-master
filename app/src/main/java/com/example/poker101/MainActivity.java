package com.example.poker101;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.poker101.date.Comanda;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.example.poker101.User.oos;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        User.callbackManager = CallbackManager.Factory.create();

        // If the access token is available already assign it.
        User.accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = User.accessToken != null && !User.accessToken.isExpired();


        if (isLoggedIn) {
            //sarim peste login


            //luam date
            GraphRequest request = GraphRequest.newMeRequest(
                    User.accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            // Application code
                            Intent alarmIntent = new Intent(User.context, AlarmReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(User.context, 0, alarmIntent, 0);

                            if (pendingIntent != null) {
                                User.pendingIntet = pendingIntent;
                            }
                            User.user = object;
                            nextActivity();
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,email,friends,picture");
            request.setParameters(parameters);
            request.executeAsync();

        } else {
            Fragment fragment_facebook_log_in_app_name = new Facebook_Log_In_Fragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_log_in, fragment_facebook_log_in_app_name, "fragment_facebook_log_in_app_name").commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        User.callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void nextActivity() {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (User.socket == null) {
                        User.initialize();
                    }
                    Comanda cmd =
                            new Comanda("login",
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
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        User.context = getApplicationContext();
    }
}
