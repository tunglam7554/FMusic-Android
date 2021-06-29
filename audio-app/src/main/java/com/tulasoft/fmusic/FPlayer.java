package com.tulasoft.fmusic;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;

public class FPlayer {
    public static SimpleExoPlayer player;
    public static void PlayItem(int index){
        player.seekTo(index, 0);
    }
    public static void createPlayerListener(){
        player.addListener(new Player.EventListener() {
            @Override
            public void onPositionDiscontinuity(int reason) {
                int latestWindowIndex = player.getCurrentWindowIndex();
                if (latestWindowIndex != PlayerLibrary.playingIndex) {
                    PlayerLibrary.playingIndex = latestWindowIndex;
                }
            }
        });
    }
}
