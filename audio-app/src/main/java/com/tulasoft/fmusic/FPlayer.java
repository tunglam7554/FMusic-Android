package com.tulasoft.fmusic;

import android.content.Context;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import androidx.annotation.Nullable;
import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class FPlayer {
    public static SimpleExoPlayer player;
    public static void PlayItem(int index){
        player.seekTo(index, 0);
    }

    public static void createPlayerListener(){
        Player.EventListener eventListener = new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
                Log.e("Timeline:",timeline.toString());
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                PlayerLibrary.playingIndex = player.getCurrentWindowIndex();
                MainActivity.onChangeTrack();
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                int latestWindowIndex = player.getCurrentWindowIndex();
                if (latestWindowIndex != PlayerLibrary.playingIndex) {
                    PlayerLibrary.playingIndex = latestWindowIndex;
                    MainActivity.onChangeTrack();
                }
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        };

        player.addListener(eventListener);
    }

    public static void preparePlayingList(final Context context){
        player.release();
        player = null;
        player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
                context, Util.getUserAgent(context, context.getString(R.string.application_name)));
        CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(
                DownloadUtil.getCache(context),
                dataSourceFactory,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
        for (MediaItem item : PlayerLibrary.playingList) {
            MediaSource mediaSource = new ExtractorMediaSource.Factory(cacheDataSourceFactory)
                    .createMediaSource(item.Uri());
            concatenatingMediaSource.addMediaSource(mediaSource);
        }
        player.prepare(concatenatingMediaSource);
        player.seekTo(PlayerLibrary.playingIndex, 0);
        player.setPlayWhenReady(true);
        MainActivity.playerView.setPlayer(FPlayer.player);
        MainActivity.playerView.showController();
        createPlayerListener();
        /////////////////////
        AudioPlayerService.playerNotificationManager.setPlayer(FPlayer.player);
        AudioPlayerService.mediaSession = new MediaSessionCompat(context, C.MEDIA_SESSION_TAG);
        AudioPlayerService.mediaSession.setActive(true);
        AudioPlayerService.playerNotificationManager.setMediaSessionToken(AudioPlayerService.mediaSession.getSessionToken());

        AudioPlayerService.mediaSessionConnector = new MediaSessionConnector(AudioPlayerService.mediaSession);
        AudioPlayerService.mediaSessionConnector.setQueueNavigator(new TimelineQueueNavigator(AudioPlayerService.mediaSession) {
            @Override
            public MediaDescriptionCompat getMediaDescription(Player player, int windowIndex) {
                return PlayerLibrary.getMediaDescription(context, PlayerLibrary.playingList.get(player.getCurrentWindowIndex()));
            }
        });
        AudioPlayerService.mediaSessionConnector.setPlayer(FPlayer.player, null);
    }

}
