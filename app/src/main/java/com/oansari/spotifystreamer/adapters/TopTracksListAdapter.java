package com.oansari.spotifystreamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oansari.spotifystreamer.R;
import com.oansari.spotifystreamer.views.TopTracksFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by Osama on 6/8/2015.
 */
public class TopTracksListAdapter extends BaseAdapter {

    private Context mContext;
    private Tracks mTracks;

    public TopTracksListAdapter(Context context, Tracks tracks){
        mContext = context;
        mTracks = tracks;
    }

    @Override
    public int getCount() {
        return mTracks.tracks.size();
    }

    @Override
    public Object getItem(int i) {
        return mTracks.tracks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        ViewHolder oneItemHolder;

        if(view == null){
            if(mTracks.tracks.size() == 1) {
                oneItemHolder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.list_individual_track, null);
                oneItemHolder.trackImage = (ImageView) view.findViewById(R.id.track_image);
                oneItemHolder.trackName = (TextView) view.findViewById(R.id.track_name);
                oneItemHolder.albumName = (TextView) view.findViewById(R.id.album_name);
                holder = oneItemHolder;
                oneItemHolder = null;

            }
            else {
                //This is a new view no need to reuse
                view = LayoutInflater.from(mContext).inflate(R.layout.list_top_tracks, null);
                holder = new ViewHolder();
                holder.trackImage = (ImageView) view.findViewById(R.id.track_image);
                holder.trackName = (TextView) view.findViewById(R.id.track_name);
                holder.albumName = (TextView) view.findViewById(R.id.album_name);
                view.setTag(holder);
            }
        }else {
            holder = (ViewHolder) view.getTag();
        }

        Track track = mTracks.tracks.get(i);
        if (track.album.images.size() > 0)
            Picasso.with(mContext).load(track.album.images.get(0).url).into(holder.trackImage);
        holder.trackName.setText(track.name);
        holder.albumName.setText(track.album.name);

        return view;
    }

    public static class ViewHolder{
        ImageView trackImage;
        TextView trackName;
        TextView albumName;

    }
}
