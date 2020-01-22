package com.itonemm.clientapponline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MediaFireConnect {

    public static String getVideoFileLink(String VideoLink) throws JSONException, IOException {
        String query= URLEncoder.encode(VideoLink,"utf-8");
        //https://media-fire-api.herokuapp.com/?url=//
        String url="https://channelbox.apkmm.net/api/v1/mediafire?url="+query;
        URL websiteurl=new URL(url);
        HttpURLConnection myConnection=(HttpURLConnection)websiteurl.openConnection();
        myConnection.setRequestMethod("GET");
        myConnection.setRequestProperty("Content-type","application/json");
        myConnection.setRequestProperty("Accept","application/json;charset=utf-8");
        myConnection.setDoInput(true);
        InputStream stream=myConnection.getInputStream();

        BufferedReader reader=new BufferedReader(new InputStreamReader(stream));
        String line="";
        StringBuffer buffer=new StringBuffer();
        while ((line=reader.readLine())!=null)
        {
            buffer.append(line);
        }


        JSONObject object=new JSONObject(buffer.toString());
        JSONObject data=object.getJSONObject("data");
        String directlink=data.getString("file");
        return directlink;
    }

}
