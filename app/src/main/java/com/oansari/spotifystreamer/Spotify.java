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
    SpotifyService mSpotifyService;
    public Spotify(){
        mSpotifyApi = new SpotifyApi();
        //mSpotifyApi.setAccessToken("40b40bec089a4cd6947c626dcd3f5a08");

        mSpotifyService = mSpotifyApi.getService();


    }

    public List<Artist> searchArtist(final String artistName) {
        final List<Artist>[] artistsResult = new ArrayList[1];

        mSpotifyService.searchArtists(artistName, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                mSpotifyService.getArtists(artistName, new Callback<Artists>() {

                    @Override
                    public void success(Artists artists, Response response) {
                        artistsResult[0] = artists.artists;
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //TODO Handle Failure
                    }
                });

            }

            @Override
            public void failure(RetrofitError error) {
                //TODO Handle Failure
            }
        });
        return artistsResult[0];
    }
}
