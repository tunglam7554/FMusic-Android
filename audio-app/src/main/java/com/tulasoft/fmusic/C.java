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

public final class C {

  public static final String PLAYBACK_CHANNEL_ID = "playback_channel";
  public static final int PLAYBACK_NOTIFICATION_ID = 1;
  public static final String MEDIA_SESSION_TAG = "audio_demo";
  public static final String DOWNLOAD_CHANNEL_ID = "download_channel";
  public static final int DOWNLOAD_NOTIFICATION_ID = 2;
  private static String key1 = "YOUTUBE_API_KEY_HERE";
  public static final String popularUrl = "https://www.googleapis.com/youtube/v3/videos?part=snippet&key="+key1+"&chart=mostPopular&regionCode=vn&videoCategoryId=10&relevanceLanguage=vi&maxResults=20";
  public static final String searchUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&key="+key1+"&type=video&regionCode=vn&order=relevance&relevanceLanguage=vi&maxResults=20&q=";
}
