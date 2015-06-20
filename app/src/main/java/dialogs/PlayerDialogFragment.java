package dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oansari.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Osama on 6/19/2015.
 */
public class PlayerDialogFragment extends DialogFragment {

    @InjectView(R.id.artistNameTextView)
    TextView mArtistNameTextView;
    @InjectView(R.id.trackNameTextView)
    TextView mTrackNameTextView;
    @InjectView(R.id.AlbumArtImageView)
    ImageView mAlbumArtImageView;
    @InjectView(R.id.albumNameTextView)
    TextView mAlbumNameTextView;
    @InjectView(R.id.elapsedTimeTextView)
    TextView mElapsedTimeTextView;
    private static Track mTrack;

    public static boolean mTwoPane;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        AlertDialog dialog = builder.create();
//        dialog.setContentView(R.layout.fragment_player);
       View  view = getActivity().getLayoutInflater().inflate(R.layout.fragment_player, null);
        ButterKnife.inject(this, view);
        feedInfo();
        builder.setView(view);
        Dialog dialog = builder.create();
        addEvents();
        return dialog;
    }

    private void feedInfo() {
        mArtistNameTextView .setText(mTrack.artists.get(0).name);
        mTrackNameTextView.setText(mTrack.name);
        mAlbumNameTextView.setText(mTrack.album.name);
        mElapsedTimeTextView.setText( mTrack.preview_url.length() +"");
        Picasso.with(getActivity()).load(mTrack.album.images.get(0).url).into(mAlbumArtImageView);

    }

    private void addEvents() {

    }

    public static PlayerDialogFragment newInstance (boolean twoPane, Track track){
        mTwoPane = twoPane;
        mTrack = track;
        return new PlayerDialogFragment();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mTwoPane){
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.inject(this, view);
        feedInfo();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }

        int dialogWidth = 800;
        int dialogHeight = 1100;

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }
}