package com.i.mediaplayer;

import java.util.ArrayList;

public class musicInfo {
    public static ArrayList<Music> myMusic;
    public static int position;
    public static int TOTAL;
    public static String maxTime;
    public static String currentTime;
    public static boolean isPlaying = false;

    public static void setMusicInfo(ArrayList<Music> myMusic){
        musicInfo.myMusic = myMusic;
        TOTAL = myMusic.size();
    }

    public static void setPosition(int position){
        musicInfo.position = position;
    }

    public static void setMaxTime(String max) {
        musicInfo.maxTime = max;
    }

    public static void setCurrentTime(String currentTime){
        musicInfo.currentTime = currentTime;
    }

}
