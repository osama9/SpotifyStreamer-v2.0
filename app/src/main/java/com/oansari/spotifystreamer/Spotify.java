package com.oansari.spotifystreamer;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Artists;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Osama on 6/3/2015.
 */
public class Spotify {
    SpotifyApi mSpotifyApi;
    public SpotifyService mSpotifyService;
    static Spotify mInstance;
    public Spotify(){
        mSpotifyApi = new SpotifyApi();
        mSpotifyService = mSpotifyApi.getService();
        String g  = mSpotifyService.COUNTRY;
    }

    public static Spotify instance(){
        if (mInstance == null){
            mInstance = new Spotify();
        }
        return mInstance;

    }
}
