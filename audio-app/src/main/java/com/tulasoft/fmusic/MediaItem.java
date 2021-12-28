package com.tulasoft.fmusic;

import android.net.Uri;

public class MediaItem {
    private String videoID;
    private String title;
    private String channelTitle;
    private String mThumbnail;
    private String hThumbnail;
    private Uri uri;

    public MediaItem(String videoID, String title, String channelTitle, String mThumbnail, String hThumbnail){
        this.videoID = videoID;
        this.title = title;
        this.channelTitle = channelTitle;
        this.mThumbnail = mThumbnail;
        this.hThumbnail = hThumbnail;
        this.uri = Uri.parse("https://tulamusic-servi.glitch.me/download?format=audio&id=" + this.videoID);
    }

    public String VideoID(){
        return this.videoID;
    }

    public String Title(){
        return this.title;
    }

    public String ChannelTitle(){
        return this.channelTitle;
    }

    public String MThumbnail(){
        return this.mThumbnail;
    }

    public String HThumbnail(){
        return this.hThumbnail;
    }

    public Uri Uri(){
        return this.uri;
    }
}
