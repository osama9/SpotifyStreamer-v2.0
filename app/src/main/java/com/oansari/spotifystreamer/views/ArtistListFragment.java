package com.oansari.spotifystreamer.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oansari.spotifystreamer.R;
import com.oansari.spotifystreamer.Spotify;
import com.oansari.spotifystreamer.adapters.AtristListAdapter;

import java.util.ArrayList;
import java.util.List;

import com.oansari.spotifystreamer.helpers.DialogHelper;
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
    long idle_min = 2000; // 2 seconds after user stops typing
    long last_text_edit = 0;
    Handler h = new Handler();
    boolean already_queried = false;
    List<Artist> mArtists = new ArrayList<>();
    AtristListAdapter adapter;

    SpotifyApi mSpotifyApi;
    SpotifyService mSpotifyService;

    @InjectView(R.id.artistsListView)
    ListView mArtistlistView;
    @InjectView(R.id.filterEditText)
    EditText mFilterEditText;
    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;
    @InjectView(R.id.notFoundTextView)
    TextView mNotFoundTextView;

    boolean stopTextWatcher = false;

    public ArtistListFragment(){ }
    private OnArtistListFragmentInteractionListener mListener;

    public Fragment mContext;




    public static ArtistListFragment newInstance() {
        ArtistListFragment fragment = new ArtistListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mContext = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artist_list, container, false);
        ButterKnife.inject(this, view);
        mArtistlistView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);

        watchSearchEditText(savedInstanceState);

        mArtistlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                stopTextWatcher = true;
                Artist artist = (Artist) adapterView.getItemAtPosition(i);
                mListener.OnArtistListFragmentInteractionListener(artist);

            }
        });

        if(mArtists.size() > 0)
            updateList();

        return view;
    }


    private void watchSearchEditText(final Bundle savedInstanceState) {

        mFilterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String existingKeyword = null;
                String newKeyword= null;

                if(savedInstanceState != null)
                    existingKeyword = savedInstanceState.getString("keyword");
                if(charSequence != null)
                    newKeyword = charSequence.toString();
                if (charSequence.length() > 0 && !newKeyword.equals(existingKeyword)) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mArtistlistView.setVisibility(View.INVISIBLE);
                    stopTextWatcher = false;

                } else {
                    if(charSequence.length() == 0)
                        mArtists.clear();

                    updateList();
                    mNotFoundTextView.setVisibility(View.INVISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mArtistlistView.setVisibility(View.VISIBLE);
                    stopTextWatcher = true;
                }
                already_queried = false;
                //MainActivity.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!stopTextWatcher ){
                    last_text_edit = System.currentTimeMillis();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (System.currentTimeMillis() > (last_text_edit + idle_min - 500)) {
                                // user hasn't changed the EditText for longer than
                                // the min delay (with half second buffer window)
                                if (mFilterEditText.getText().toString().length() > 0) {
                                    mProgressBar.setVisibility(View.VISIBLE);
                                    //new FetchSpotifyData().execute();
                                    FetchSpotifyData();

                                    Log.d("Time Elapsed", "User stopped typing");  // your queries
                                }
                            }
                        }
                    }, idle_min);
                }
                else
                    stopTextWatcher = false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("keyword", mFilterEditText.getText().toString());
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnArtistListFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }


        mSpotifyApi = new SpotifyApi();
        mSpotifyService = mSpotifyApi.getService();

    }

    private void updateList() {
        adapter = new AtristListAdapter(getActivity(), mArtists);
        mArtistlistView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void FetchSpotifyData(){
        Spotify.instance().mSpotifyService.searchArtists(mFilterEditText.getText().toString(), new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                mArtists = artistsPager.artists.items;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateList();
                        mProgressBar.setVisibility(View.INVISIBLE);
                        if (mArtists.size() > 0) {
                            mArtistlistView.setVisibility(View.VISIBLE);
                            mNotFoundTextView.setVisibility(View.INVISIBLE);
                        }
                        else
                            mNotFoundTextView.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                DialogHelper.alertUserAboutError(mContext);
            }
        });
    }

    //FOR REFERENCE

//    private class FetchSpotifyData extends AsyncTask {
//
//        @Override
//        protected Object doInBackground(Object... objects) {
//            Spotify.instance().mSpotifyService.searchArtists(mFilterEditText.getText().toString(), new Callback<ArtistsPager>() {
//                @Override
//                public void success(ArtistsPager artistsPager, Response response) {
//                    mArtists = artistsPager.artists.items;
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            updateList();
//                            mProgressBar.setVisibility(View.INVISIBLE);
//                            if (mArtists.size() > 0) {
//                                mArtistlistView.setVisibility(View.VISIBLE);
//                                mNotFoundTextView.setVisibility(View.INVISIBLE);
//                            }
//                            else
//                                mNotFoundTextView.setVisibility(View.VISIBLE);
//                        }
//                    });
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    DialogHelper.alertUserAboutError(mContext);
//                }
//            });
//            return null;
//        }
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnArtistListFragmentInteractionListener {
        // TODO: Update argument type and name
        void OnArtistListFragmentInteractionListener(Artist artist);
    }


    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        mArtistlistView.setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }
}
