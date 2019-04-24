package com.example.poker101;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.poker101.date.Comanda;
import com.example.poker101.date.Invite;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class Play_Response_Fragment extends Fragment {


    public Play_Response_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play__response_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatTextView friend_name = getActivity().findViewById(R.id.friend_name);
        AppCompatImageView friend_img = getActivity().findViewById(R.id.friend_img);
        final JSONArray list_friend;
        try {
            list_friend = User.user.getJSONObject("friends").getJSONArray("data");
            JSONObject friend = null;
            for (int i = 0; i < list_friend.length(); i++) {
                try {
                    friend = list_friend.getJSONObject(i);
                    if (friend.getString("id").equals(User.currentOpponent)) {
                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (friend != null) {
                friend_name.setText(friend.getString("name"));
                Picasso.get().load("http://graph.facebook.com/" + friend.getString("id") + "/picture?type=large").fit().into(friend_img);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button btn_accept = getActivity().findViewById(R.id.btn_accept);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Comanda comanda = new Comanda("acceptInvite",new Invite(User.currentOpponent,User.user.getString("id")));
                            User.oos.writeObject(comanda);

                        } catch (IOException e) {
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
            }
        });
    }
}
