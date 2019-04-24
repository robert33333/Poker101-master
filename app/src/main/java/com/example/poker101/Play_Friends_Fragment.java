package com.example.poker101;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import com.example.poker101.date.Comanda;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.poker101.User.oos;


/**
 * A simple {@link Fragment} subclass.
 */
public class Play_Friends_Fragment extends Fragment {


    public Play_Friends_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play__friends_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        try {

            final JSONArray list_friend = User.user.getJSONObject("friends").getJSONArray("data");
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (User.socket == null) {
                            User.initialize();
                        }
                        List<String> list = new ArrayList<String>();
                        for(int i = 0; i < list_friend.length(); i++){
                            list.add(list_friend.getJSONObject(i).getString("id"));
                        }
                        Comanda cmd =
                                new Comanda("getFriendsOnline",
                                        list);
                        oos.writeObject(cmd);
                        List<String> list_friend2 = (List<String>) User.readMessage();
                        RecyclerView rv_test = view.findViewById(R.id.rv_friend_list);
                        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
                        rv_test.setLayoutManager(mLinearLayoutManager);
                        rv_test.setHasFixedSize(true);
                        rv_test.setItemAnimator(new DefaultItemAnimator());

                        //Set Adapter
                        JSONArray list_friend3 = new JSONArray();
                        for (int i = 0; i < list_friend.length(); i++) {
                            if (list_friend2.contains(list_friend.getJSONObject(i).getString("id"))) {
                                list_friend3.put(list_friend.getJSONObject(i));
                            }
                        }
                        FriendListAdapter mAdapter = new FriendListAdapter(list_friend3,true);
                        rv_test.setAdapter(mAdapter);

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





        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
