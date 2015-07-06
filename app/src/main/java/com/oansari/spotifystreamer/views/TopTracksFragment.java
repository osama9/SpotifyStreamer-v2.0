package com.oansari.spotifystreamer.views;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oansari.spotifystreamer.R;
import com.oansari.spotifystreamer.Spotify;
import com.oansari.spotifystreamer.adapters.TopTracksListAdapter;

import com.oansari.spotifystreamer.helpers.DialogHelper;
import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TopTracksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TopTracksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopTracksFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ARTIST_ID = "artistID";
    private static final String ARG_ARTIST_NAME = "artistName";
    TopTracksListAdapter adapter;
    public static Tracks mTracks;

    Bundle savedState;

    @InjectView(R.id.topTracksListView)
    ListView mTopTracksListView;

    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;

    @InjectView(R.id.notFoundTextView)
    TextView mNotFoundTextView;

    // TODO: Rename and change types of parameters
    private String mArtistId;
    private String mArtistName;
    private OnTopTracksFragmentInteractionListener mListener;

    public Fragment mContext;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment TopTracksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TopTracksFragment newInstance(String artistID, String artistName) {
        TopTracksFragment fragment = new TopTracksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ARTIST_ID, artistID);
        args.putString(ARG_ARTIST_NAME, artistName);
        fragment.setArguments(args);
        return fragment;
    }

    public static TopTracksFragment newInstance() {
        TopTracksFragment fragment = new TopTracksFragment();
        return fragment;
    }

    public TopTracksFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArtistId = getArguments().getString(ARG_ARTIST_ID);
            mArtistName = getArguments().getString(ARG_ARTIST_NAME);
        }

        mContext = this;
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_tracks, container, false);
        ButterKnife.inject(this, view);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setSubtitle(mArtistName);

        if (savedState == null)
            //new FetchSpotifyData().execute();
        FetchSpotifyData();
        else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mTopTracksListView.setSelection(saveState().getInt("POSITION"));
            updateList();
        }

        mTopTracksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Track track = (Track) adapterView.getItemAtPosition(i);
                mListener.OnTopTracksFragmentInteractionListener(mContext, track, i, mTracks);
            }
        });



        return view;
    }

    private void saveStateToArguments() {
        if (getView() != null)
            savedState = saveState();
        if (savedState != null) {
            Bundle b = getArguments();
            b.putBundle("savedState", savedState);
        }
    }

    private Bundle saveState() {
        Bundle state = new Bundle();
        // For Example
        state.putInt("POSITION", mTopTracksListView.getFirstVisiblePosition());
        return state;
    }

    @Override
    public void onStop() {
        getActivity().getActionBar().setSubtitle("");
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveStateToArguments();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTopTracksFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



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
    public interface OnTopTracksFragmentInteractionListener {
        // TODO: Update argument type and name
         void OnTopTracksFragmentInteractionListener(Fragment fragment, Track track, int trackPosition, Tracks tracks);
    }

    private void updateList() {
        adapter = new TopTracksListAdapter(getActivity(), mTracks);
        mTopTracksListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void FetchSpotifyData(){
        Spotify.instance().mSpotifyService.getArtistTopTrack(mArtistId, new Callback<Tracks>() {
            @Override
            public void success(final Tracks tracks, Response response) {
                mTracks = tracks;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        if(tracks.tracks.size() == 0) {
                            mNotFoundTextView.setVisibility(View.VISIBLE);
                            mTopTracksListView.setVisibility(View.INVISIBLE);
                        }else{
                            mNotFoundTextView.setVisibility(View.INVISIBLE);
                            mTopTracksListView.setVisibility(View.VISIBLE);
                        }
                        updateList();
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                DialogHelper.alertUserAboutError(mContext);
            }
        });
    }


    // FOR REFERENCE
//    private class FetchSpotifyData extends AsyncTask {
//
//        @Override
//        protected Object doInBackground(Object... objects) {
//
//            Spotify.instance().mSpotifyService.getArtistTopTrack(mArtistId, new Callback<Tracks>() {
//                @Override
//                public void success(final Tracks tracks, Response response) {
//                    mTracks = tracks;
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mProgressBar.setVisibility(View.INVISIBLE);
//                            if(tracks.tracks.size() == 0) {
//                                mNotFoundTextView.setVisibility(View.VISIBLE);
//                                mTopTracksListView.setVisibility(View.INVISIBLE);
//                            }else{
//                                mNotFoundTextView.setVisibility(View.INVISIBLE);
//                                mTopTracksListView.setVisibility(View.VISIBLE);
//                            }
//                            updateList();
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

}
