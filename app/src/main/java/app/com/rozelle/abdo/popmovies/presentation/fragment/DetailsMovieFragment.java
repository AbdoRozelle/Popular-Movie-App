package app.com.rozelle.abdo.popmovies.presentation.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.com.rozelle.abdo.popmovies.R;
import app.com.rozelle.abdo.popmovies.data.api.MovieApi;
import app.com.rozelle.abdo.popmovies.domain.Movie;
import app.com.rozelle.abdo.popmovies.domain.Review;
import app.com.rozelle.abdo.popmovies.domain.Trailer;
import app.com.rozelle.abdo.popmovies.presentation.adapter.DetailAdapter;
import rx.functions.Action1;

/*import app.com.rozelle.abdo.cleanpopmovies.presentation.adapter.DetailAdapter;
import app.com.rozelle.abdo.cleanpopmovies.presentation.adapter.TrailersAdapter;*/

/**
 * Created by Abdo on 8/22/2016.
 */
public class DetailsMovieFragment extends Fragment implements MovieApi.TrailerCallback, MovieApi.ReviewCallback {

    //constants
    private static String MOVIE_KEY = "Movie";
    /*    TrailersAdapter trailersAdapter;
        DetailAdapter reviewAdapter;*/
    DetailAdapter detailAdapter;


    public static DetailsMovieFragment newInstance(Movie movie) {

        Bundle args = new Bundle();
        args.putSerializable(MOVIE_KEY, movie);
        DetailsMovieFragment fragment = new DetailsMovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Movie movie = (Movie) getArguments().getSerializable(MOVIE_KEY);
        detailAdapter = new DetailAdapter(getContext());
        View rootView = inflater.inflate(R.layout.fragment_details_movie, container, false);
        RecyclerView recyclerViewTrailers = (RecyclerView) rootView.findViewById(R.id.recycler_view_trailers_and_reviews);
        recyclerViewTrailers
                .setLayoutManager
                        (new LinearLayoutManager(getContext()));
        recyclerViewTrailers.setAdapter(detailAdapter);
        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(movie.getOriginal_title());
        detailAdapter.setHeaderData(movie);
        MovieApi.getMovieTrailers(getContext()).loadTrail(movie.getId(), this);
        MovieApi.getMovieReviews(getContext()).loadReviews(movie.getId(), this);


        detailAdapter.getTrailerClicks().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + s));
                startActivity(browserIntent);
            }
        });

        return rootView;
    }


    @Override
    public void trailerSuccess(List<Trailer> trailers) {
        detailAdapter.setTrailerData(trailers);
    }

    @Override
    public void trailerError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void reviewSuccess(List<Review> reviews) {
        detailAdapter.setReviewData(reviews);
    }

    @Override
    public void reviewError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

}
