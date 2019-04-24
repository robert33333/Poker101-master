package com.example.poker101;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.poker101.date.Comanda;
import com.example.poker101.date.Invite;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import static com.example.poker101.User.goToWaitScreen;
import static com.example.poker101.User.oos;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewWrapper> {
    JSONArray list_friend;
    Boolean extra = false;

    public FriendListAdapter( JSONArray list_friend) {
        this.list_friend=list_friend;
    }

    public FriendListAdapter( JSONArray list_friend,Boolean extra) {
        this.list_friend=list_friend;
        this.extra = extra;
    }

    @Override
    public ViewWrapper onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View itemLayoutView;
        if (!extra) {
            itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_friend, null);
        }
        else {
            itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_friend_play, null);
        }
        // create ViewHolder
        return new ViewWrapper(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ViewWrapper viewWrapper, int position) {
        try {
            viewWrapper.getFriend_name().setText(list_friend.getJSONObject(viewWrapper.getAdapterPosition()).getString("name"));
            Picasso.get().load("http://graph.facebook.com/" + list_friend.getJSONObject(viewWrapper.getAdapterPosition()).get("id") + "/picture?type=large").fit().into(viewWrapper.getFriend_img());
            if (extra) {
                viewWrapper.getFriend_btn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            //Toast.makeText(viewWrapper.base.getContext(),list_friend.getJSONObject(viewWrapper.getAdapterPosition()).get("id").toString(),Toast.LENGTH_LONG).show();
                            User.currentOpponent = list_friend.getJSONObject(viewWrapper.getAdapterPosition()).get("id").toString();
                            sendInviteToServer();
//                            Intent intent = new Intent(viewWrapper.base.getContext(), PlayActivity.class);
//                            viewWrapper.base.getContext().startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendInviteToServer() {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (User.socket == null) {
                        User.initialize();
                    }
                    Comanda cmd =
                            new Comanda("inviteFriend",
                                    new Invite(User.user.getString("id"),User.currentOpponent));
                    oos.writeObject(cmd);
                    goToWaitScreen(false);

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

    @Override
    public int getItemCount() {
        return list_friend.length();
    }

    class ViewWrapper extends RecyclerView.ViewHolder {
        final View base;
        //TextView tv_name;

        AppCompatTextView friend_name;
        AppCompatImageView friend_img;
        Button friend_btn;

        public ViewWrapper(View itemView) {
            super(itemView);
            base = itemView;
        }

        AppCompatTextView getFriend_name() {
            if (friend_name == null) {
                friend_name = base.findViewById(R.id.friend_name);
            }
            return (friend_name);
        }

        AppCompatImageView getFriend_img() {
            if (friend_img == null) {
                friend_img = base.findViewById(R.id.friend_img);
            }
            return (friend_img);
        }

        Button getFriend_btn() {
            if (friend_btn == null) {
                friend_btn = base.findViewById(R.id.friend_btn);
            }
            return (friend_btn);
        }
    }
}