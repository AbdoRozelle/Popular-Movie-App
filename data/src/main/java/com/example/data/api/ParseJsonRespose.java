package com.example.data.api;

import android.util.Log;

import com.example.domain.Movie;
import com.example.domain.Review;
import com.example.domain.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Abdo on 9/10/2016.
 */
public class ParseJsonRespose {

    //constant
    final static private String LOG_TAG = ParseJsonRespose.class.getSimpleName();

    //Json Movie parsing
    private static Movie parseMovie(JSONObject jsonObject) {
        try {

            String original_title = jsonObject.getString("original_title");
            String original_language = jsonObject.getString("original_language");
            String poster_path = jsonObject.getString("poster_path");
            String overview = jsonObject.getString("overview");
            String release_date = jsonObject.getString("release_date");
            String id = jsonObject.getString("id");
            String title = jsonObject.getString("title");
            String backdrop_path = jsonObject.getString("backdrop_path");
            String popularity = jsonObject.getString("popularity");
            String vote_count = jsonObject.getString("vote_count");
            boolean video = jsonObject.getBoolean("video");
            String vote_average = jsonObject.getString("vote_average");
            boolean adult = jsonObject.getBoolean("adult");
            Movie movie = new Movie(original_title, original_language, poster_path, overview, release_date, id, title, backdrop_path, popularity, vote_count, video, vote_average, adult);
            return movie;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "error in parseCard() " + e.getMessage());
            return null;
        }
    }

    public static List<Movie> parseMovieResponse(String responseString) {
        List<Movie> movieList;
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            movieList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++)
                movieList.add(parseMovie(jsonArray.getJSONObject(i)));

        } catch (JSONException e) {
            Log.e(LOG_TAG, "error in parseMovieResponse() " + e.getMessage());
            return null;
        }
        return movieList;
    }

    //Json Trailer parsing
    private static Trailer parseTrailer(JSONObject jsonObject) {
        try {

            String id = jsonObject.getString("id");
            String key = jsonObject.getString("key");
            String name = jsonObject.getString("name");
            String site = jsonObject.getString("site");

            Trailer trailer = new Trailer(id, key, name, site);
            return trailer;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "error in parseTrailer() " + e.getMessage());
            return null;
        }
    }

    public static List<Trailer> parseTrailerResponse(String responseString) {
        List<Trailer> trailerList;
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            trailerList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++)
                trailerList.add(parseTrailer(jsonArray.getJSONObject(i)));

        } catch (JSONException e) {
            Log.e(LOG_TAG, "error in parseMovieResponse() " + e.getMessage());
            return null;
        }
        return trailerList;
    }

    //Json reviews parsing
    private static Review parseReview(JSONObject jsonObject) {
        try {

            String author = jsonObject.getString("author");
            String content = jsonObject.getString("content");
            String id = jsonObject.getString("id");

            Review review = new Review(id, author, content);
            return review;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "error in parseReview() " + e.getMessage());
            return null;
        }
    }

    public static List<Review> parseReviewResponse(String responseString) {
        List<Review> reviewList;
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            reviewList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++)
                reviewList.add(parseReview(jsonArray.getJSONObject(i)));

        } catch (JSONException e) {
            Log.e(LOG_TAG, "error in parseMovieResponse() " + e.getMessage());
            return null;
        }
        return reviewList;
    }
}
