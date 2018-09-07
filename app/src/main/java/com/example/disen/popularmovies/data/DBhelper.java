package com.example.disen.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by disen on 11/11/2017.
 */

public class DBhelper extends SQLiteOpenHelper {
    static String dbName = MovieContract.MovieEntry.db_name;
    static int db_version = 4;
    public DBhelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, dbName, factory, db_version);
    }

    public static String CreateEntries = "CREATE TABLE " + MovieContract.MovieEntry.db_name+"("
            +MovieContract.MovieEntry.ColumnID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +MovieContract.MovieEntry.ColumnTitle+ " TEXT, "
            +MovieContract.MovieEntry.ColumnImage+ " TEXT, "
            +MovieContract.MovieEntry.ColumnRate+ " TEXT, "
            +MovieContract.MovieEntry.ColumnOverview+ " TEXT, "
            +MovieContract.MovieEntry.ColumnReleaseDate+ " TEXT)";
    public static String deleteTable = "DROP TABLE IF EXISTS "+ dbName;
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CreateEntries);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(deleteTable);
        onCreate(sqLiteDatabase);
    }
}
