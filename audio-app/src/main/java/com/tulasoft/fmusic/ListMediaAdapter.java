package com.tulasoft.fmusic;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.tulasoft.fmusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListMediaAdapter extends BaseAdapter {
    final ArrayList<MediaItem> arrayVideo;

    public ListMediaAdapter(ArrayList<MediaItem> arrayVideo){
        this.arrayVideo = arrayVideo;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayVideo.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arrayVideo.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View viewVideo;
        if(convertView == null)
            viewVideo = View.inflate(parent.getContext(), R.layout.mediaitem, null);
        else
            viewVideo = convertView;

        MediaItem videoItem = (MediaItem) getItem(position);
        ((TextView) viewVideo.findViewById(R.id.mi_title)).setText(videoItem.Title());
        ((TextView) viewVideo.findViewById(R.id.mi_channeltitle)).setText(videoItem.ChannelTitle());
        Picasso.get()
                .load(videoItem.MThumbnail()).fit().centerCrop()
                .into((ImageView) viewVideo.findViewById(R.id.mi_mthumbnail));
        return viewVideo;
    }
}
