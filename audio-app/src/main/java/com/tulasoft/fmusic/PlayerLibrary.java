package com.tulasoft.fmusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import com.google.android.exoplayer2.Player;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class PlayerLibrary {
    public static ListMediaAdapter arrayAdapter;
    public static ArrayList<MediaItem> playingList;
    public static int playingIndex;
    public static void init(){
        playingList = new ArrayList<MediaItem>();
        playingIndex = 0;
        arrayAdapter = new ListMediaAdapter(playingList);
    }

    public static MediaDescriptionCompat getMediaDescription(Context context, MediaItem mediaItem) {
        Bundle extras = new Bundle();
        Bitmap bitmap = getBitmap(context, mediaItem.MThumbnail());
        extras.putParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap);
        extras.putParcelable(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, bitmap);
        return new MediaDescriptionCompat.Builder()
                .setMediaId(mediaItem.VideoID())
                .setIconBitmap(bitmap)
                .setTitle(mediaItem.Title())
                .setDescription(mediaItem.ChannelTitle())
                .setExtras(extras)
                .build();
    }

    public static Bitmap getBitmap(Context context, String mthumbnail) {
        final Bitmap[] output = new Bitmap[1];
        Picasso.get().load(mthumbnail).into(new Target(){
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                output[0] = bitmap;
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
        return output[0];
    };


}
