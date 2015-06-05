package com.oansari.spotifystreamer.models;

import com.oansari.spotifystreamer.R;

/**
 * Created by Osama on 6/1/2015.
 */
public class Artist {
    private String mName;
    private String mImage;

    public Artist(String image, String name){
        mName = name;
        mImage = image;
    }
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    //TODO temporary
    public int getImageId(){
        return R.mipmap.ic_launcher;
    }
}
