package com.example.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.domain.Movie;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Abdo on 8/30/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "Movie.data";
    public static final int DATABASE_VERSION = 1;
    /* constants */
    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createSql =
                "CREATE TABLE " + MovieContract.FavoriteMovies.TABLE_NAME + "(\n" +
                        MovieContract.FavoriteMovies._ID + " TEXT PRIMARY KEY\n" +
                        ", " + MovieContract.FavoriteMovies.ORIGINAL_TITLE + " TEXT \n" +
                        ", " + MovieContract.FavoriteMovies.RELEASE_DATE + " TEXT \n" +
                        ", " + MovieContract.FavoriteMovies.VOTE_AVERAGE + " TEXT\n" +
                        ", " + MovieContract.FavoriteMovies.OVERVIEW + " TEXT\n" +
                        ", UNIQUE (" + MovieContract.FavoriteMovies._ID + ") ON CONFLICT REPLACE)";

        sqLiteDatabase.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String deleteSql = "DROP TABLE IF EXISTS " + MovieContract.FavoriteMovies.TABLE_NAME;
        sqLiteDatabase.execSQL(deleteSql);
        onCreate(sqLiteDatabase);
    }


    /**
     * adds a card to the database
     * the key is the card's title
     * replaces in case of conflict
     */
    public void insertMovie(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.FavoriteMovies._ID, movie.getId());
        contentValues.put(MovieContract.FavoriteMovies.ORIGINAL_TITLE, movie.getOriginal_title());
        contentValues.put(MovieContract.FavoriteMovies.RELEASE_DATE, movie.getRelease_date());
        contentValues.put(MovieContract.FavoriteMovies.VOTE_AVERAGE, movie.getVote_average());
        contentValues.put(MovieContract.FavoriteMovies.OVERVIEW, movie.getOverview());

        SQLiteDatabase database = getWritableDatabase();
        database.insert(MovieContract.FavoriteMovies.TABLE_NAME, null, contentValues);
    }


    /**
     * returns all the cards in the database
     */
    public Movie getMovie(String Id) {
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(MovieContract.FavoriteMovies.TABLE_NAME,
                null,
                MovieContract.FavoriteMovies._ID + "=?",
                new String[]{Id},
                null,
                null,
                null);

        // parse data from the cursor
        Movie movie = null;
        if (cursor.moveToFirst())
            do {
                movie = new Movie();

                movie.setId(cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovies._ID)));
                movie.setOriginal_title(cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovies.ORIGINAL_TITLE)));
                movie.setRelease_date(cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovies.RELEASE_DATE)));
                movie.setVote_average(cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovies.VOTE_AVERAGE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovies.OVERVIEW)));

            } while (cursor.moveToNext());

        cursor.close();
        return movie;
    }

    public List<Movie> getAllMovies() {
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(MovieContract.FavoriteMovies.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        // parse data from the cursor
        Movie movie = null;
        List<Movie> movies = new ArrayList<>();
        if (cursor.moveToFirst())
            do {
                movie = new Movie();

                movie.setId(cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovies._ID)));
                movie.setOriginal_title(cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovies.ORIGINAL_TITLE)));
                movie.setRelease_date(cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovies.RELEASE_DATE)));
                movie.setVote_average(cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovies.VOTE_AVERAGE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovies.OVERVIEW)));

                movies.add(movie);
            } while (cursor.moveToNext());
        cursor.close();
        return movies;
    }

    public boolean deleteMovie(String id) {
        SQLiteDatabase database = getWritableDatabase();
        return database.delete(MovieContract.FavoriteMovies.TABLE_NAME, MovieContract.FavoriteMovies._ID + "=" + id, null) > 0;
    }


}
