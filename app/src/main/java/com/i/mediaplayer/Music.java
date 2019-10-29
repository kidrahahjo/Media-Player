package com.i.mediaplayer;

public class Music {
    String musicName;
    String path;

    public Music(String musicName, String path) {
        this.musicName = musicName;
        this.path = path;
    }

    public String getMusicName() {
        return musicName;
    }

    public String getPath() {
        return path;
    }
}
