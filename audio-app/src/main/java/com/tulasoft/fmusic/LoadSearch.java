package com.tulasoft.fmusic;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class LoadSearch  extends AsyncTask<String, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(String... query)
    {
        Log.e("Search","do in background");
        String str= C.searchUrl + query[0];
        URLConnection urlConn = null;
        BufferedReader bufferedReader = null;
        try
        {
            URL url = new URL(str);
            urlConn = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(line);
            }

            return new JSONObject(stringBuffer.toString());
        }
        catch(Exception ex)
        {
            Log.e("App", "yourDataTask", ex);
            return null;
        }
        finally
        {
            if(bufferedReader != null)
            {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(JSONObject response)
    {
        if(response != null)
        {
            try {
                Log.e("Search","json");
                PlayerLibrary.searchList.clear();
                JSONArray items = response.getJSONArray("items");
                for(int i = 0; i < items.length(); i++) {
                    JSONObject item = (JSONObject) items.get(i);
                    JSONObject id = item.getJSONObject("id");
                    String videoID = id.getString("videoId");
                    JSONObject snippet = item.getJSONObject("snippet");
                    String title = snippet.getString("title");
                    String channelTitle = snippet.getString("channelTitle");
                    JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                    JSONObject medium = thumbnails.getJSONObject("medium");
                    String mThumbnail = medium.getString("url");
                    JSONObject high = thumbnails.getJSONObject("high");
                    String hThumbnail = high.getString("url");

                    MediaItem mediaItem = new MediaItem(videoID, title, channelTitle, mThumbnail, hThumbnail);
                    PlayerLibrary.searchList.add(mediaItem);
                    PlayerLibrary.searchAdapter.notifyDataSetChanged();
                }
            } catch (JSONException ex) {
                Log.e("App", "Failure", ex);
            }
        }
    }
}
