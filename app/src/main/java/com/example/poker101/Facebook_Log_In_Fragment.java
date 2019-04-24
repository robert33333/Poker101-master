package com.example.poker101;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.poker101.date.Comanda;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.example.poker101.User.oos;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Facebook_Log_In_Fragment extends Fragment {

    public Facebook_Log_In_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_facebook__log__in_, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoginManager.getInstance().registerCallback(User.callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        User.accessToken = AccessToken.getCurrentAccessToken();

                        GraphRequest request = GraphRequest.newMeRequest(
                                User.accessToken,
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {

                                        Intent alarmIntent = new Intent(User.context, AlarmReceiver.class);
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(User.context, 0, alarmIntent, 0);


                                        if (pendingIntent != null) {
                                            User.pendingIntet = pendingIntent;
                                            AlarmManager manager = (AlarmManager) User.context.getSystemService(Context.ALARM_SERVICE);
                                            int interval = 1000 * 60 * 60 * 24;

                                            /* Set the alarm to start at 10:30 AM */
                                            Calendar calendar = null;
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                calendar = Calendar.getInstance();
                                                calendar.setTimeInMillis(System.currentTimeMillis());
                                                calendar.set(Calendar.HOUR_OF_DAY, 10);
                                                calendar.set(Calendar.MINUTE, 0);

                                                /* Repeating on every 1 day interval */
                                                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                                        interval, pendingIntent);
                                            }
                                        }
                                        // Application code
                                        User.user = object;
                                        nextActivity();
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,link,email,friends,picture");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(getApplicationContext(),R.string.log_in_cancel,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        //getActivity().finish();
                        Toast.makeText(getApplicationContext(),R.string.log_in_error,Toast.LENGTH_LONG).show();
                    }
                });
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

        Activity activity1 = getActivity();
        Intent intent = new Intent(activity1, MenuActivity.class);
        startActivity(intent);
        activity1.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        User.callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }



}
