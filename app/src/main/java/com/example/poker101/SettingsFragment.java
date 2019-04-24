package com.example.poker101;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;
import static com.example.poker101.User.MY_PREFS_NAME;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    boolean set_preferences_theme = false;
    boolean set_preferences_card_back = false;

    int cards[] = {R.drawable.card_back_default,R.drawable.card_back1,R.drawable.card_back2};

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinner_theme = getActivity().findViewById(R.id.spinner_theme);

        spinner_theme.setSelection(User.theme_id);

        spinner_theme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (set_preferences_theme == false) {
                    set_preferences_theme = true;
                    return;
                }
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putInt("theme_id", i);
                editor.apply();
                User.theme_id = i;
                Intent intent = getActivity().getIntent();
                User.goBackToSettings = true;
                getActivity().finish();
                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner spinner_card_back = getActivity().findViewById(R.id.spinner_card_back);

        CardAdapter customAdapter=new CardAdapter(getApplicationContext(),cards);
        spinner_card_back.setAdapter(customAdapter);
        spinner_card_back.setSelection(User.card_id);

        spinner_card_back.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (set_preferences_card_back == false) {
                    set_preferences_card_back = true;
                    return;
                }
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putInt("card_id", i);
                editor.apply();
                User.card_id = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

}
