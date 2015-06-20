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

import com.oansari.spotifystreamer.R;

import java.util.zip.Inflater;

/**
 * Created by Osama on 6/19/2015.
 */
public class PlayerDialogFragment extends DialogFragment {

    public static boolean mTwoPane;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        AlertDialog dialog = builder.create();
//        dialog.setContentView(R.layout.fragment_player);
       View  view = getActivity().getLayoutInflater().inflate(R.layout.fragment_player, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        return dialog;
    }

    public static PlayerDialogFragment newInstance (boolean twoPane){
        mTwoPane = twoPane;
        return new PlayerDialogFragment();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mTwoPane){
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        View view = inflater.inflate(R.layout.fragment_player, container, false);

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