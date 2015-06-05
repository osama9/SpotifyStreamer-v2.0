package com.oansari.spotifystreamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.oansari.spotifystreamer.R;
import com.oansari.spotifystreamer.models.Artist;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Osama on 6/1/2015.
 */
public class AtristListAdapter extends BaseAdapter implements Filterable{

    private boolean changeLayout = false;
    private Context mContext;
    private List<Artist> mArtists;
    private List<Artist> filteredArtistList; //This list to apply filter on it
    private List<Artist> mArtistsHolder; //Keep the orignal list
    private ListFilter mListFilter;
    public AtristListAdapter(Context context, List<Artist> artists){
        mContext = context;
        mArtists = filteredArtistList = mArtistsHolder = artists;
    }

    @Override
    public int getCount() {
        return mArtists.size();
    }

    @Override
    public Object getItem(int i) {
        return mArtists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0; //No need for this method
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        ViewHolder oneItemHolder;

        if(view == null){
            //This is a new view no need to reuse
            view = LayoutInflater.from(mContext).inflate(R.layout.list_artist, null);
            holder = new ViewHolder();
            holder.artistImage = (ImageView) view.findViewById(R.id.artist_image);
            holder.artistName = (TextView) view.findViewById(R.id.artist_name);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
            if(changeLayout) {
                oneItemHolder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.list_individual_artist, null);
                oneItemHolder.artistImage = (ImageView) view.findViewById(R.id.artist_image);
                oneItemHolder.artistName = (TextView) view.findViewById(R.id.artist_name);
                holder = oneItemHolder;
                oneItemHolder = null;

            }
            else
            {
                holder = (ViewHolder) view.getTag();
                if (holder == null)
                    holder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.list_artist, null);
                holder.artistImage = (ImageView) view.findViewById(R.id.artist_image);
                holder.artistName = (TextView) view.findViewById(R.id.artist_name);
            }

        }
        Artist artist = mArtists.get(i);
        holder.artistImage.setImageResource(artist.getImageId());
        holder.artistName.setText(artist.getName());

        return view;

    }

    public static class ViewHolder{
        ImageView artistImage;
        TextView artistName;

    }

    // Filter operation
    @Override
    public Filter getFilter() {
        if (mListFilter == null)
            mListFilter = new ListFilter();
        return mListFilter;
    }
    private class ListFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();

            if (charSequence != null && charSequence.length() > 0){
                List<Artist> filteredListHolder = new ArrayList<Artist>();
                mArtists = mArtistsHolder;
                for(int i =0; i < mArtists.size(); i++){
                    if((mArtists.get(i).getName().toUpperCase())
                        .contains(charSequence.toString().toUpperCase())){

                        Artist artist = new Artist(mArtists.get(i).getImage(),
                                mArtists.get(i).getName());

                        filteredListHolder.add(artist);
                    }
                }

                results.count = filteredListHolder.size();
                results.values = filteredListHolder;
            }else{
                results.count = filteredArtistList.size();
                results.values = filteredArtistList;
            }
            if( results.count ==1 && !changeLayout)
                changeLayout = true;
            else if(results.count > 1)
                changeLayout = false;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mArtists = (List<Artist>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}

