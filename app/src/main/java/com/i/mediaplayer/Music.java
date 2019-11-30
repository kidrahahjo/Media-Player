package com.i.mediaplayer;

public class Music {
    String musicName;
    String path;
    String art;

    public Music(String musicName, String path, String art) {
        this.musicName = musicName;
        this.path = path;
        this.art = art;
    }

    public String getMusicName() {
        return musicName;
    }

    public String getPath() {
        return path;
    }

    public String getArt() {
        return art;
    }
}
