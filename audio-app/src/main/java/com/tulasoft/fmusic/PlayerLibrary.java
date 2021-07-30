package com.tulasoft.fmusic;

import android.content.Context;
import android.content.Intent;
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
    public static ListMediaAdapter popularAdapter;
    public static ListMediaAdapter searchAdapter;
    public static ListMediaAdapter playingAdapter;
    public static ArrayList<MediaItem> playingList;
    public static ArrayList<MediaItem> popularList;
    public static ArrayList<MediaItem> searchList;
    public static int playingIndex;
    public static String playingListName;
    public static Intent audioPlayerService;
    public static void init(){
        playingList = new ArrayList<MediaItem>();
        searchList = new ArrayList<MediaItem>();
        popularList = new ArrayList<MediaItem>();
        playingIndex = 0;
        playingListName = "None";
        playingAdapter = new ListMediaAdapter(playingList);
        searchAdapter = new ListMediaAdapter(searchList);
        popularAdapter = new ListMediaAdapter(popularList);
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
