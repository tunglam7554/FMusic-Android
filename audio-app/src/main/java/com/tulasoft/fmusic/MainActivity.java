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

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;
import com.tulasoft.fmusic.fragments.HomeFragment;
import com.tulasoft.fmusic.fragments.MenuFragment;
import com.tulasoft.fmusic.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {
  private Fragment homeFragment;
  private Fragment searchFragment;
  private static TextView title;
  private static TextView channelTitle;
  private static TextView np_title;
  private static TextView np_channelTitle;
  private static ImageView np_thumbnail;
  public static PlayerView playerView;
  public static DownloadManager downloadManager;
  private FrameLayout layoutBottomSheet;
  private BottomSheetBehavior sheetBehavior;
  private LinearLayout np_topbar;
  private LinearLayout np_main;
  private ImageButton np_download;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);
    PlayerLibrary.init();
    final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    navigation.setOnNavigationItemReselectedListener(mOnNavigationItemReselectedListener);
    homeFragment = new HomeFragment();
    loadFragment(homeFragment);

    title = findViewById(R.id.title);
    channelTitle = findViewById(R.id.channelTitle);
    playerView = findViewById(R.id.player_view);


    np_title = findViewById(R.id.np_title);
    np_channelTitle = findViewById(R.id.np_channelTitle);
    np_thumbnail = findViewById(R.id.np_thumbnail);
    np_topbar = findViewById(R.id.np_topbar);
    np_main = findViewById(R.id.np_main);
    np_download = findViewById(R.id.np_download);

    layoutBottomSheet = findViewById(R.id.np_screen);
    sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

    np_download.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = PlayerLibrary.playingList.get(PlayerLibrary.playingIndex).Uri();
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        long reference = downloadManager.enqueue(request);
      }
    });


    sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
      @Override
      public void onStateChanged(@NonNull View bottomSheet, int newState) {
        switch (newState) {
          case BottomSheetBehavior.STATE_HIDDEN:
          case BottomSheetBehavior.STATE_EXPANDED:
            navigation.setVisibility(View.GONE);
            np_topbar.setVisibility(View.GONE);
            np_main.setVisibility(View.VISIBLE);
            break;
          case BottomSheetBehavior.STATE_COLLAPSED:
            navigation.setVisibility(View.VISIBLE);
            np_topbar.setVisibility(View.VISIBLE);
            np_main.setVisibility(View.INVISIBLE);
            break;
          case BottomSheetBehavior.STATE_DRAGGING:
            np_main.setVisibility(View.VISIBLE);
            navigation.setVisibility(View.GONE);
            break;
          case BottomSheetBehavior.STATE_SETTLING:
            break;
        }
      }


      @Override
      public void onSlide(@NonNull View bottomSheet, float slideOffset) {

      }
    });
  }

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
          = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
        case R.id.navigation_home:
          if(homeFragment == null)
            homeFragment = new HomeFragment();
          loadFragment(homeFragment);
          return true;
        case R.id.navigation_search:
          if(searchFragment == null)
            searchFragment = new SearchFragment();
          loadFragment(searchFragment);
          return true;
        case R.id.navigation_menu:
          loadFragment(new MenuFragment());
          return true;
      }

      return false;
    }
  };

  private BottomNavigationView.OnNavigationItemReselectedListener mOnNavigationItemReselectedListener
          = new BottomNavigationView.OnNavigationItemReselectedListener() {

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
        case R.id.navigation_home:
          loadFragment(homeFragment);
          if(PlayerLibrary.popularList.size() == 0){
            new LoadPopular().execute();
          }
          break;
        case R.id.navigation_search:
          loadFragment(searchFragment);
          break;
        case R.id.navigation_menu:
          loadFragment(new MenuFragment());
          break;
      }
    }
  };

  private void loadFragment(Fragment fragment) {
    // load fragment
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.frame, fragment);
    transaction.addToBackStack(null);
    transaction.commit();

  }

  public static void onChangeTrack(){
    title.setText(PlayerLibrary.playingList.get(PlayerLibrary.playingIndex).Title());
    channelTitle.setText(PlayerLibrary.playingList.get(PlayerLibrary.playingIndex).ChannelTitle());
    np_title.setText(PlayerLibrary.playingList.get(PlayerLibrary.playingIndex).Title());
    np_channelTitle.setText(PlayerLibrary.playingList.get(PlayerLibrary.playingIndex).ChannelTitle());
    Picasso.get()
            .load(PlayerLibrary.playingList.get(PlayerLibrary.playingIndex).HThumbnail()).fit().centerCrop()
            .into(np_thumbnail);
  }

}


