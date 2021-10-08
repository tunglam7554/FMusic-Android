package com.tulasoft.fmusic;

import static com.tulasoft.fmusic.C.DOWNLOAD_CHANNEL_ID;
import static com.tulasoft.fmusic.C.DOWNLOAD_NOTIFICATION_ID;

import android.app.Notification;

import androidx.annotation.Nullable;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadManager.TaskState;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.scheduler.Scheduler;
import com.google.android.exoplayer2.ui.DownloadNotificationUtil;


public class AudioDownloadService extends DownloadService {

  public AudioDownloadService() {
    super(
        DOWNLOAD_NOTIFICATION_ID,
        DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
        DOWNLOAD_CHANNEL_ID,
        R.string.download_channel_name);
  }

  @Override
  protected DownloadManager getDownloadManager() {
    return DownloadUtil.getDownloadManager(this);
  }

  @Nullable
  @Override
  protected Scheduler getScheduler() {
    return null;
  }

  @Override
  protected Notification getForegroundNotification(TaskState[] taskStates) {
    return DownloadNotificationUtil.buildProgressNotification(
            this,
            R.drawable.exo_icon_play,
            DOWNLOAD_CHANNEL_ID,
            null,
            null,
            taskStates);
  }

}