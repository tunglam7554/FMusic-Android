package com.tulasoft.fmusic.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import com.google.android.exoplayer2.offline.ProgressiveDownloadAction;
import com.google.android.exoplayer2.util.Util;
import com.tulasoft.fmusic.*;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private SearchView searchView;
    private boolean searchSubmit;
    public SearchFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        searchView = (SearchView) view.findViewById(R.id.search);
        ListView listViewSearch = (ListView)view.findViewById(R.id.list_view_search);
        listViewSearch.setAdapter(PlayerLibrary.searchAdapter);
        listViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenItem(position);
            }
        });
        searchSubmit = true;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("Search","text submit");
                searchSubmit = false;
                new LoadSearch().execute(query);
                PlayerLibrary.playingListName = "None";
                searchView.clearFocus();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void OpenItem(int position){
        PlayerLibrary.playingIndex = position;

        ////////new code
        ProgressiveDownloadAction action = new ProgressiveDownloadAction(
            PlayerLibrary.searchList.get(position).Uri(), false, null, null);
        AudioDownloadService.startWithAction(
            getContext(),
            AudioDownloadService.class,
            action,
            false);


        if(PlayerLibrary.audioPlayerService == null){
            PlayerLibrary.playingList = PlayerLibrary.searchList;
            PlayerLibrary.audioPlayerService = new Intent(getActivity(), AudioPlayerService.class);
            Util.startForegroundService(getActivity(), PlayerLibrary.audioPlayerService);
            Log.e("log","create service from search");
        }else if(!PlayerLibrary.playingListName.equals("Search")){
            PlayerLibrary.playingList = PlayerLibrary.searchList;
            FPlayer.preparePlayingList(getContext());
            FPlayer.PlayItem(position);
            Log.e("log","play from search");
        }
        else{
            FPlayer.PlayItem(position);
            Log.e("log","continue play from search");
        }
        MainActivity.onChangeTrack();
        PlayerLibrary.playingListName = "Search";
    }


}
