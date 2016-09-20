package app.com.rozelle.abdo.popmovies.presentation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import app.com.rozelle.abdo.popmovies.R;
import app.com.rozelle.abdo.popmovies.domain.Movie;
import app.com.rozelle.abdo.popmovies.presentation.fragment.DetailsMovieFragment;
import app.com.rozelle.abdo.popmovies.presentation.fragment.GridMovieFragment;
import rx.functions.Action1;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        GridMovieFragment gridMovieFragment = new GridMovieFragment();

        if (getFragmentManager().findFragmentById(R.id.home_Activity) == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.home_Activity, gridMovieFragment)
                    .commit();

        gridMovieFragment.getMovieClick().subscribe(new Action1<Movie>() {
            @Override
            public void call(Movie movie) {
                if (findViewById(R.id.detail_movie) == null) {
                    startActivity(DetailsActivity.getIntent(HomeActivity.this, movie));
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.detail_movie, DetailsMovieFragment.newInstance(movie))
                            .commit();
                }

            }
        });
    }


}
