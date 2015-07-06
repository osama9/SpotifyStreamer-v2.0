package com.oansari.spotifystreamer.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import javax.xml.transform.sax.TemplatesHandler;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by Osama on 7/4/2015.
 */
public class ParcelableTrack implements Parcelable {

    private static final String TRACK_NAME = "com.oansari.spotifystreamer.trackName";
    private static final String PREVIEW_URL = "com.oansari.spotifystreamer.previewkUrl";
    private static final String ALBUM_NAME = "com.oansari.spotifystreamer.albumName";
    private static final String ALBUM_IMAGE = "com.oansari.spotifystreamer.albumImageUrl";

    private String trackName;
    private String previewUrl;
    private String albumName;
    private String albumImageUrl;

    public ParcelableTrack(Track track){
        trackName = track.name;
        previewUrl = track.preview_url;
        albumName = track.album.name;
        albumImageUrl = track.album.images.get(0).url.toString();
    }

    private ParcelableTrack(Parcel in){
        trackName = in.readString();
        previewUrl = in.readString();
        albumName = in.readString();
        albumImageUrl = in.readString();
    }

    public static ParcelableTrack from(Track track){
        if(track == null){
            return null;
        }

        return new ParcelableTrack(track);
    }

    public static final Creator<ParcelableTrack> CREATOR = new Creator<ParcelableTrack>() {
        @Override
        public ParcelableTrack createFromParcel(Parcel parcel) {
            return new ParcelableTrack(parcel);
        }

        @Override
        public ParcelableTrack[] newArray(int i) {
            return new ParcelableTrack[i];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

//        Bundle bundle = new Bundle();
//        bundle.putString(TRACK_NAME, trackName);
//        bundle.putString(PREVIEW_URL, previewUrl);
//        bundle.putString(ALBUM_NAME, albumName);
//        bundle.putString(ALBUM_IMAGE, albumImageUrl);
//        parcel.writeBundle(bundle);
        parcel.writeString(trackName);
        parcel.writeString(previewUrl);
        parcel.writeString(albumName);
        parcel.writeString(albumImageUrl);

    }

    public static ArrayList<ParcelableTrack> trackArrayList(Tracks tracks){
        ArrayList<ParcelableTrack> newTracksList = new ArrayList<ParcelableTrack>();
        ParcelableTrack parcelableTrack;
        for (Track track : tracks.tracks){
            if(track != null){
                parcelableTrack = new ParcelableTrack(track);
                newTracksList.add(parcelableTrack);
            }
        }

        return newTracksList;
    }
}
