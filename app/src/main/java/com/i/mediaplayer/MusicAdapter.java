package com.i.mediaplayer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder>{
    private ArrayList<Music> musicArrayList;
    public Context context;

    public MusicAdapter(Context context, ArrayList<Music> music) {
        this.context = context;
        this.musicArrayList = music;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView musicNameTV;
        public MyViewHolder(View v) {
            super(v);
            final Context context = v.getContext();
            musicNameTV = v.findViewById(R.id.musicNameTV);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(context,musicService.class);
                    int position = getAdapterPosition();
                    musicInfo.setPosition(position);
                    //Show the Music Activity
                    context.startService(in);

                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View musicView = (TextView)inflater.inflate(R.layout.song_list,viewGroup,false);
        MyViewHolder vh = new MyViewHolder(musicView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Music m = musicArrayList.get(i);
        myViewHolder.musicNameTV.setText(m.getMusicName());
    }

    @Override
    public int getItemCount() {
        return musicArrayList.size();
    }




}