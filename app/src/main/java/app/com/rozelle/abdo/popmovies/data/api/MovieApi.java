package app.com.rozelle.abdo.popmovies.data.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import app.com.rozelle.abdo.popmovies.BuildConfig;
import app.com.rozelle.abdo.popmovies.domain.Movie;
import app.com.rozelle.abdo.popmovies.domain.Review;
import app.com.rozelle.abdo.popmovies.domain.Trailer;


/**
 * Created by Abdo on 8/9/2016.
 */
public class MovieApi {

    /* constants */
    private final static String LOG_TAG = MovieApi.class.getSimpleName();
    final private static String BASE_API_URI = "api.themoviedb.org";
    final private static String BASE_iMAGE_URI = "http://image.tmdb.org/t/p/w185";
    final private static String MOVIE_API_KEY = "api_key";
    final private static String URI_NUM3 = "3";
    final private static String URI_DISCOVER = "discover";
    final private static String URI_MOVIE = "movie";
    final private static String URI_HTTPS = "https";
    final private static String URI_VIDEOS = "videos";
    final private static String URI_REVIEWS = "reviews";

    //Discover movie in api
    public static Discover discover(Context context) {
        return new Discover(context);
    }

    //Load trailers
    public static MovieReviews getMovieReviews(Context context) {
        return new MovieReviews(context);
    }

    //Load trailers
    public static MovieTrailers getMovieTrailers(Context context) {
        return new MovieTrailers(context);
    }

    public interface MoviesCallback {
        void movieSuccess(List<Movie> movies);

        void movieError(String error);
    }

    public interface TrailerCallback {
        void trailerSuccess(List<Trailer> trailers);

        void trailerError(String error);
    }

    public interface ReviewCallback {
        void reviewSuccess(List<Review> reviews);

        void reviewError(String error);
    }

    public static class Discover {
        final private static String SORT_BY = "sort_by";
        private Context context;
        private Uri.Builder uri;

        public Discover(Context context) {
            this.context = context;
            uri = new Uri.Builder();
        }

        public void load(MoviesCallback callback) {
            uri.scheme(URI_HTTPS)
                    .authority(BASE_API_URI)
                    .appendPath(URI_NUM3)
                    .appendPath(URI_DISCOVER)
                    .appendPath(URI_MOVIE)
                    .appendQueryParameter(MOVIE_API_KEY, BuildConfig.MOVIE_API_KEY);
            Load.loadMovies(context, callback, uri.toString());
        }

        public SortBy sortby(String string) {
            return new SortBy(string);
        }

        public class SortBy {

            public SortBy(String string) {
                uri.appendQueryParameter(SORT_BY, string);
            }

            public void load(MoviesCallback callback) {
                Discover.this.load(callback);
            }
        }
    }

    public static class MovieReviews {
        private Context context;
        private Uri.Builder uri;

        public MovieReviews(Context context) {
            this.context = context;
            uri = new Uri.Builder();
        }

        public void loadReviews(String id, ReviewCallback reviewCallback) {

            uri.scheme(URI_HTTPS)
                    .authority(BASE_API_URI)
                    .appendPath(URI_NUM3)
                    .appendPath(URI_MOVIE)
                    .appendPath(id)
                    .appendPath(URI_REVIEWS)
                    .appendQueryParameter(MOVIE_API_KEY, BuildConfig.MOVIE_API_KEY);
            Load.loadReviews(context, reviewCallback, uri.toString());
        }

    }

    public static class MovieTrailers {
        private Context context;
        private Uri.Builder uri;

        public MovieTrailers(Context context) {
            this.context = context;
            uri = new Uri.Builder();
        }

        public void loadTrail(String id, TrailerCallback trailerCallback) {

            uri.scheme(URI_HTTPS)
                    .authority(BASE_API_URI)
                    .appendPath(URI_NUM3)
                    .appendPath(URI_MOVIE)
                    .appendPath(id)
                    .appendPath(URI_VIDEOS)
                    .appendQueryParameter(MOVIE_API_KEY, BuildConfig.MOVIE_API_KEY);
            Load.loadTrailers(context, trailerCallback, uri.toString());
        }

    }

    //loading data from Api
    private static class Load {
        private static void loadMovies(Context context, final MoviesCallback callback, String uri) {
            // show a progress dialog
            final ProgressDialog progressDialog = ProgressDialog.show(context, "", "Loading Data ...");

            // make a GET requests
            URL url = null;
            try {
                url = new URL(uri);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Ion.with(context)
                    .load("GET", url.toString())
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            // dismiss the dialog

                            // check error
                            if (e != null) {
                                if (e.getMessage() != null)
                                    Log.e(LOG_TAG, e.getMessage());
                                progressDialog.dismiss();
                                callback.movieError("Error Loading Movies");
                                return;
                            }

                            // parse the data
                            if (result != null) {
                                List<Movie> movies = ParseJsonRespose.parseMovieResponse(result);
                                for (Movie movie : movies) {
                                    String posterPath = movie.getPoster_path();
                                    if (!posterPath.equals("null"))
                                        movie.setPoster_path(BASE_iMAGE_URI + movie.getPoster_path());
                                }
                                progressDialog.dismiss();
                                if (movies != null) {
                                    //movieAdapter.setData(movies);
                                    callback.movieSuccess(movies);
                                } else {
                                    Log.e(LOG_TAG, "movies is null");
                                    return;
                                }
                            } else {
                                Log.e(LOG_TAG, "result is null");
                                return;
                            }
                        }

                    });
        }

        private static void loadTrailers(Context context, final TrailerCallback callback, String uri) {
            // show a progress dialog
            final ProgressDialog progressDialog = ProgressDialog.show(context, "", "Loading Trailers ...");

            // make a GET requests
            URL url = null;
            try {
                url = new URL(uri);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Ion.with(context)
                    .load("GET", url.toString())
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            // dismiss the dialog

                            // check error
                            if (e != null) {
                                if (e.getMessage() != null)
                                    Log.e(LOG_TAG, e.getMessage());
                                progressDialog.dismiss();
                                callback.trailerError("Error Loading Trailers");
                                return;
                            }

                            // parse the data
                            if (result != null) {
                                List<Trailer> trailers = ParseJsonRespose.parseTrailerResponse(result);
                                progressDialog.dismiss();
                                if (trailers != null) {
                                    //trailerAdapter.setData(trailers);
                                    callback.trailerSuccess(trailers);
                                } else {
                                    Log.e(LOG_TAG, "movies is null");
                                    return;
                                }
                            } else {
                                Log.e(LOG_TAG, "result is null");
                                return;
                            }
                        }

                    });
        }

        private static void loadReviews(Context context, final ReviewCallback callback, String uri) {
            // show a progress dialog
            final ProgressDialog progressDialog = ProgressDialog.show(context, "", "Loading Reviews ...");

            // make a GET requests
            URL url = null;
            try {
                url = new URL(uri);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Ion.with(context)
                    .load("GET", url.toString())
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            // dismiss the dialog

                            // check error
                            if (e != null) {
                                if (e.getMessage() != null)
                                    Log.e(LOG_TAG, e.getMessage());
                                progressDialog.dismiss();
                                callback.reviewError("Error Loading Reviews");
                                return;
                            }

                            // parse the data
                            if (result != null) {
                                List<Review> reviews = ParseJsonRespose.parseReviewResponse(result);
                                progressDialog.dismiss();
                                if (reviews != null) {
                                    //reviewAdapter.setData(reviews);
                                    callback.reviewSuccess(reviews);
                                } else {
                                    Log.e(LOG_TAG, "movies is null");
                                    return;
                                }
                            } else {
                                Log.e(LOG_TAG, "result is null");
                                return;
                            }
                        }

                    });
        }

    }

}





