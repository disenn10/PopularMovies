package com.example.disen.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by disen on 11/11/2017.
 */

public class MovieContract {

    public static String movieAuthority = "com.example.disen.popularmovies";
    public static Uri BaseContent = Uri.parse("content://"+movieAuthority);
    public static String path = "movies";
    public static Uri Content_Uri = Uri.withAppendedPath(BaseContent, path);


    public static class MovieEntry implements BaseColumns{
        public static String db_name = "Movies";
        public static String ColumnID = BaseColumns._ID;
        public static String ColumnTitle = "title";
        public static String ColumnImage = "image";
        public static String ColumnReleaseDate = "release_date";
        public static String ColumnOverview = "overview";
        public static String ColumnRate = "rate";
    }
}
