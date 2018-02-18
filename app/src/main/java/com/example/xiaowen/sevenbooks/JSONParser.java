package com.example.xiaowen.sevenbooks;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONParser {

    //先拿到InputStream，这段除了url不一样其他都一样

    static InputStream getInputStream(String url){
        InputStream is = null;
        try {
            HttpURLConnection conn =
                    (HttpURLConnection) new URL(url).openConnection();
            conn.connect();
            conn.setRequestMethod("GET");
            is = conn.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    //用上面Url的InputStream，拿到对应的JSONString

    static String getJSONString(InputStream ips) {

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = null;

        //拿到一个buffer reader，这段都一样可以抄
        try {
            InputStreamReader ipsr = new InputStreamReader(ips, "iso-8859-1");
            reader = new BufferedReader(ipsr, 8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //用拿到的buffer reader，来一行行读，放入sb，这段都一样可以抄

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            ips.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //最后变成一大段String，和我们用网页看到的一样
        return stringBuilder.toString();
    }

    //这个方法，把一段JSONString，通过new JSONObject，变成对应的JSONObject

    static JSONObject getJSONObjectFromUrl(String url){
        JSONObject jObj = null;

        try {
            jObj = new JSONObject(getJSONString(getInputStream(url)));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jObj;
    }

    //这个方法，把一大段JSONString，通过new JSONObject，变成对应的JSONArray

    static JSONArray getJSONArrayFromUrl(String url){
        JSONArray jArray = null;

        try {
            jArray = new JSONArray(getJSONString(getInputStream(url)));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jArray;
    }
}
