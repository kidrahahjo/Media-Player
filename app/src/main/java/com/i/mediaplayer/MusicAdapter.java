package com.i.mediaplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder>{
    private String[] music;
    private Context context;

    public MusicAdapter(Context context, String[] music) {
        this.context = context;
        this.music = music;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView musicNameTV;
        public MyViewHolder(TextView v) {
            super(v);
            musicNameTV = v;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView musicView = (TextView)inflater.inflate(R.layout.song_list,viewGroup,false);
        MyViewHolder vh = new MyViewHolder(musicView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.musicNameTV.setText(music[i]);
    }

    @Override
    public int getItemCount() {
        return music.length;
    }


}