package app.com.rozelle.abdo.popmovies.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import app.com.rozelle.abdo.popmovies.R;
import app.com.rozelle.abdo.popmovies.domain.Movie;
import app.com.rozelle.abdo.popmovies.presentation.fragment.DetailsMovieFragment;

public class DetailsActivity extends AppCompatActivity {

    final static private String MOVIE_KEY = "Movie";

    public static Intent getIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(MOVIE_KEY, movie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Movie movie = (Movie) getIntent().getExtras().getSerializable(MOVIE_KEY);

        if (getSupportFragmentManager().findFragmentById(R.id.detail_movie) == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_movie, DetailsMovieFragment.newInstance(movie))
                    .commit();

    }


}
