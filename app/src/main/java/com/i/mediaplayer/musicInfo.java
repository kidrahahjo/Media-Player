package com.i.mediaplayer;

import java.util.ArrayList;

public class musicInfo {
    public static ArrayList<Music> myMusic;
    public static int position;
    public static int max;

    public static void setMusicInfo(ArrayList<Music> myMusic){
        musicInfo.myMusic = myMusic;
        max = myMusic.size();
    }

    public static void setPosition(int position){
        musicInfo.position = position;
    }
}
