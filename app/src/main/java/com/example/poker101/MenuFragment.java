package com.example.poker101;


import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.poker101.date.Comanda;
import com.facebook.login.LoginManager;

import org.json.JSONException;

import java.io.IOException;

import static com.example.poker101.User.ois;
import static com.example.poker101.User.oos;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            User.user.get("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //User.context = getContext();

        Button btn_logout = getActivity().findViewById(R.id.btn_logout);

        Button btn_profile = getActivity().findViewById(R.id.btn_profile);

        Button btn_settings = getActivity().findViewById(R.id.btn_settings);

        Button btn_play = getActivity().findViewById(R.id.btn_play);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                AlarmManager manager = (AlarmManager) User.context.getSystemService(Context.ALARM_SERVICE);
                manager.cancel(User.pendingIntet);
                Activity activity1 = getActivity();
                Intent intent = new Intent(activity1,MainActivity.class);
                activity1.startActivity(intent);
                activity1.finish();
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (User.socket == null) {
                                User.initialize();
                            }
                            Comanda cmdBani =
                                    new Comanda("getBani",
                                            User.user.getString("id"));
                            oos.writeObject(cmdBani);
                            User.bani = (String) ois.readObject();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception exp) {
                            exp.printStackTrace();
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
                Fragment fragment_profile = new ProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_menu, fragment_profile, "fragment_profile").addToBackStack(null).commit();
            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment_settings = new SettingsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_menu, fragment_settings, "fragment_settings").addToBackStack(null).commit();
            }
        });


        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment_play = new Play_Friends_Fragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_menu, fragment_play, "fragment_play").addToBackStack(null).commit();
            }
        });

        if (User.goBackToSettings) {
            User.goBackToSettings = false;
            Fragment fragment_settings = new SettingsFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_menu, fragment_settings, "fragment_settings").addToBackStack(null).commit();
        }

    }
}
