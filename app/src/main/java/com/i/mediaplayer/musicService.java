package com.i.mediaplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class musicService extends Service{

    static ArrayList<Music> music;
    public static int position;
    static int max;
    public MediaPlayer mediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        music = musicInfo.myMusic;
        position = musicInfo.position;
        max = music.size();

        Uri u = Uri.parse(music.get(position).getPath());
        mediaPlayer = MediaPlayer.create(this,u);
        mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        stopSelf();
    }


    public static void pauseOrPlay(Boolean play){

    }

    public void nextOrPrev(Boolean isNext){
        if(isNext){
            position+=1;
            musicInfo.setPosition(position);
        }
        else{
            position-=1;
            musicInfo.setPosition(position);
        }
        Uri u = Uri.parse(music.get(position).getPath());
        mediaPlayer = MediaPlayer.create(this,u);
        mediaPlayer.start();
    }

}
