package com.oansari.spotifystreamer;

import android.util.Log;

import java.util.concurrent.Callable;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
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
        mSpotifyService.getAlbum("2dIGnmEIy1WZIcZCFSj6i8", new Callback<Album>() {
            @Override
            public void success(Album album, Response response) {
                Log.d("Album success", album.name);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Album failure", error.toString());
            }
        });
        mSpotifyService.searchArtists("coldplay", new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                mSpotifyService.getArtistAlbums(artistsPager.artists.items.get(0).id, new Callback<Pager<Album>>() {
                    @Override
                    public void success(Pager<Album> albumPager, Response response) {
                        for(int i =0; i< albumPager.items.size(); i++)
                            Log.d("Albums Data: ", albumPager.items.get(i).name );
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
//        mSpotifyService.getArtists("0OdUWJ0sBjDrqHygGUXeCF", new Callback<Artists>() {
//            @Override
//            public void success(Artists artists, Response response) {
//                Log.d("Artist success", artists.artists.get(0).name);
//
//                for (int i = 0; i < artists.artists.size(); i++)
//                    Log.d("Artist success", artists.artists.get(i).name);
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.d("Artist failure", error.toString());
//            }
//        });
    }
}
