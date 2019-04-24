package com.example.poker101;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CardAdapter extends BaseAdapter {
    Context context;
    int cards[];
    LayoutInflater inflter;

    public CardAdapter(Context applicationContext, int[] cards) {
        this.context = applicationContext;
        this.cards = cards;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return cards.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        ImageView icon = view.findViewById(R.id.imageView);
        icon.setImageResource(cards[i]);
        return view;
    }
}
