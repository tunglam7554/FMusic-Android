/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tulasoft.fmusic;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaDescription;
import android.os.IBinder;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import androidx.annotation.Nullable;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.tulasoft.fmusic.R;

public class AudioPlayerService extends Service {

//  private SimpleExoPlayer player;
  public static PlayerNotificationManager playerNotificationManager;
  public static MediaSessionCompat mediaSession;
  public static MediaSessionConnector mediaSessionConnector;

  @Override
  public void onCreate() {
    super.onCreate();
    final Context context = this;
    Log.e("create","play");
    FPlayer.player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
    DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
          context, Util.getUserAgent(context, getString(R.string.application_name)));
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
    FPlayer.player.prepare(concatenatingMediaSource);
    FPlayer.player.seekTo(PlayerLibrary.playingIndex, 0);
    FPlayer.player.setPlayWhenReady(true);
    MainActivity.playerView.setPlayer(FPlayer.player);
    MainActivity.playerView.showController();
    FPlayer.createPlayerListener();
    playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            context,
            C.PLAYBACK_CHANNEL_ID,
            R.string.playback_channel_name,
            C.PLAYBACK_NOTIFICATION_ID,
            new MediaDescriptionAdapter() {
              @Override
              public String getCurrentContentTitle(Player player) {
                return PlayerLibrary.playingList.get(player.getCurrentWindowIndex()).Title();
              }

              @Nullable
              @Override
              public PendingIntent createCurrentContentIntent(Player player) {
                return null;
              }

              @Nullable
              @Override
              public String getCurrentContentText(Player player) {
                return PlayerLibrary.playingList.get(player.getCurrentWindowIndex()).ChannelTitle();
              }

              @Nullable
              @Override
              public Bitmap getCurrentLargeIcon(Player player, BitmapCallback callback) {
                return PlayerLibrary.getBitmap(
                        context, PlayerLibrary.playingList.get(player.getCurrentWindowIndex()).MThumbnail());
              }
            }
    );

    playerNotificationManager.setNotificationListener(new NotificationListener() {
      @Override
      public void onNotificationStarted(int notificationId, Notification notification) {
        startForeground(notificationId, notification);
      }

      @Override
      public void onNotificationCancelled(int notificationId) {
        stopSelf();
      }
    });
    playerNotificationManager.setPlayer(FPlayer.player);

    mediaSession = new MediaSessionCompat(context, C.MEDIA_SESSION_TAG);
    mediaSession.setActive(true);
    playerNotificationManager.setMediaSessionToken(mediaSession.getSessionToken());

    mediaSessionConnector = new MediaSessionConnector(mediaSession);
    mediaSessionConnector.setQueueNavigator(new TimelineQueueNavigator(mediaSession) {
      @Override
      public MediaDescriptionCompat getMediaDescription(Player player, int windowIndex) {
        return PlayerLibrary.getMediaDescription(context, PlayerLibrary.playingList.get(player.getCurrentWindowIndex()));
      }
    });
    mediaSessionConnector.setPlayer(FPlayer.player, null);
  }

  @Override
  public void onDestroy() {
    mediaSession.release();
    mediaSessionConnector.setPlayer(null, null);
    playerNotificationManager.setPlayer(null);
    FPlayer.player.release();
    FPlayer.player = null;

    super.onDestroy();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_STICKY;
  }

  @Override
  public void onTaskRemoved(Intent rootIntent) {
    super.onTaskRemoved(rootIntent);
    stopSelf();
  }


}
