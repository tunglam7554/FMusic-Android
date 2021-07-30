package com.tulasoft.fmusic.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.google.android.exoplayer2.util.Util;
import com.tulasoft.fmusic.*;

public class HomeFragment extends Fragment {
    public HomeFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final ListView listView = (ListView)view.findViewById(R.id.list_view);
        listView.setAdapter(PlayerLibrary.popularAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenItem(position);
            }
        });
        if(PlayerLibrary.popularList.size() == 0){
            new LoadPopular().execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    private void OpenItem(int position){
        PlayerLibrary.playingIndex = position;
        if(PlayerLibrary.audioPlayerService == null){
            PlayerLibrary.playingList = PlayerLibrary.popularList;
            PlayerLibrary.audioPlayerService = new Intent(getActivity(), AudioPlayerService.class);
            Util.startForegroundService(getActivity(), PlayerLibrary.audioPlayerService);
            Log.e("log","create service from home");
        }else if(!PlayerLibrary.playingListName.equals("Popular")){
            PlayerLibrary.playingList = PlayerLibrary.popularList;
            FPlayer.preparePlayingList(getContext());
            FPlayer.PlayItem(position);
            Log.e("log","play from home");
        }
        else{
            FPlayer.PlayItem(position);
            Log.e("log","continue play from home");
        }
        PlayerLibrary.playingListName = "Popular";

    }

}
