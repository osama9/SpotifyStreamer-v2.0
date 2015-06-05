package com.oansari.spotifystreamer.views;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oansari.spotifystreamer.R;
import com.oansari.spotifystreamer.Spotify;
import com.oansari.spotifystreamer.adapters.AtristListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class ArtistListFragment extends Fragment {
    long idle_min = 4000; // 4 seconds after user stops typing
    long last_text_edit = 0;
    Handler h = new Handler();
    boolean already_queried = false;
    List<Artist> mArtists = new ArrayList<>();
    AtristListAdapter adapter;

    SpotifyApi mSpotifyApi;
    SpotifyService mSpotifyService;

    @InjectView(R.id.listView)
    ListView mlistView;
    @InjectView(R.id.filterEditText)
    EditText mFilterEditText;
    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;
    @InjectView(R.id.notFoundTextView)
    TextView mNotFoundTextView;



    Context mContext;

    public ArtistListFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artist_list, container, false);
        ButterKnife.inject(this, view);
        mlistView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);


        mFilterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mlistView.setVisibility(View.INVISIBLE);

                } else {
                    mArtists.clear();
                    updateList();
                    mNotFoundTextView.setVisibility(View.INVISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mlistView.setVisibility(View.VISIBLE);
                }
                already_queried = false;
                //MainActivity.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                last_text_edit = System.currentTimeMillis();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (System.currentTimeMillis() > (last_text_edit + idle_min - 500)) {
                            // user hasn't changed the EditText for longer than
                            // the min delay (with half second buffer window)
                            if (mFilterEditText.getText().toString().length() > 0) {
                                mProgressBar.setVisibility(View.VISIBLE);
                                new FetchSpotifyData().execute();

                                Log.d("Time Elapsed", "User stopped typing");  // your queries
                            }
                        }
                    }
                }, idle_min);
            }
        });



        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity;


        final Spotify spotify = new Spotify();

        mSpotifyApi = new SpotifyApi();
        //mSpotifyApi.setAccessToken("40b40bec089a4cd6947c626dcd3f5a08");

        mSpotifyService = mSpotifyApi.getService();



    }

    private void updateList() {
        adapter = new AtristListAdapter(mContext, mArtists);
        mlistView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class FetchSpotifyData extends AsyncTask {

        @Override
        protected Object doInBackground(Object... objects) {
            mSpotifyService.searchArtists(mFilterEditText.getText().toString(), new Callback<ArtistsPager>() {
                @Override
                public void success(ArtistsPager artistsPager, Response response) {
                    mArtists = artistsPager.artists.items;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateList();
                            mProgressBar.setVisibility(View.INVISIBLE);
                            if (mArtists.size() > 0) {
                                mlistView.setVisibility(View.VISIBLE);
                                mNotFoundTextView.setVisibility(View.INVISIBLE);
                            }
                            else
                                mNotFoundTextView.setVisibility(View.VISIBLE);
                        }
                    });
                }

                @Override
                public void failure(RetrofitError error) {
                    //TODO Handle Failure
                }
            });
            return null;
        }
    }
}