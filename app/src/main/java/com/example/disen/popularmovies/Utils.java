package com.example.disen.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by disen on 10/18/2017.
 */

public class Utils {

    public static String api_key = BuildConfig.api_key;

    public static ArrayList<Movies> FetchData(String baseUrl){
        URL url = buildUrl(baseUrl);
        String output = null;
        ArrayList<Movies> movies = new ArrayList<>();
        try {
            output = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            movies = ExtractData(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static URL buildUrl(String Url){
        URL url = null;
        try {
            url = new URL(Url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        String output = null;
        InputStream inputStream = null;
        try {
            inputStream = urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            if(scanner.hasNext()){
                output =  scanner.next();
            }
            else {
                return null;
            }
        urlConnection.disconnect();
            return output;
    }

    public static ArrayList<Movies> ExtractData(String output) throws JSONException {
        String title = "";
        String rate = "";
        String release_date = "";
        String overview = "";
        String key = "";
        String image_size = "w500/";
        String image_baseUrl = "http://image.tmdb.org/t/p/";
        String id = "";
        String author = "";
        String content = "";
        StringBuilder image = new StringBuilder();
        ArrayList<Movies> moviesArrayList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(output);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        for (int i = 0; i< jsonArray.length(); i++){
            JSONObject resultObject = jsonArray.getJSONObject(i);
            if(resultObject.has("vote_average")){
                rate = resultObject.getString("vote_average");
            }
            if(resultObject.has("releaseDate")){
                release_date = resultObject.getString("releaseDate");
            }
            if(resultObject.has("id")){
                id = resultObject.getString("id");
            }
            if(resultObject.has("author")){
                author = resultObject.getString("author");
            }
            if(resultObject.has("content")){
                content = resultObject.getString("content");
            }
            else{
                content ="No reviews...";
            }
            if(resultObject.has("key")){
                key = resultObject.getString("key");
            }
            if(resultObject.has("overview")){
                overview = resultObject.getString("overview");
            }
            if(resultObject.has("title")){
                title = resultObject.getString("title");
            }
            if(resultObject.has("poster_path")){
                String poster = resultObject.getString("poster_path");
                image.append(image_baseUrl).append(image_size).append(poster);
            }
            moviesArrayList.add(new Movies(title, String.valueOf(image), rate, release_date, overview,id,key,author,content));
            image.setLength(0);
        }
        return moviesArrayList;
    }
}
