package com.theacecoder.task2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {
    private ArrayList<Chat> arrayChats;
    private Context context;
    private LayoutInflater inflater;

    public ChatAdapter(Context context, ArrayList<Chat> arrayChats) {
        this.context = context;
        this.arrayChats = arrayChats;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayChats.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayChats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Holder holder;
        if (v == null) {
            v = inflater.inflate(R.layout.chat_message, null);
            holder = new Holder();
            holder.author = (TextView) v.findViewById(R.id.author);
            holder.message = (TextView) v.findViewById(R.id.message);
            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }

        holder.author.setTextColor(Color.RED);
        holder.message.setTextColor(Color.BLUE);
        holder.author.setText(arrayChats.get(position).getAuthor());
        holder.message.setText(String.valueOf(arrayChats.get(position).getMessage()));
        return v;
    }

    class Holder {
        TextView author,message;
    }
}