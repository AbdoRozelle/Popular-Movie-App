package app.com.rozelle.abdo.popmovies.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.com.rozelle.abdo.popmovies.R;
import app.com.rozelle.abdo.popmovies.data.api.MovieApi;
import app.com.rozelle.abdo.popmovies.data.database.DatabaseHelper;
import app.com.rozelle.abdo.popmovies.domain.Movie;
import app.com.rozelle.abdo.popmovies.presentation.adapter.HomeAdapter;
import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by Abdo on 8/12/2016.
 */
public class GridMovieFragment extends Fragment implements MovieApi.MoviesCallback {

    final static String LOG_TAG = GridMovieFragment.class.getSimpleName();
    private final PublishSubject<Movie> onClickMoiveSubject = PublishSubject.create();
    HomeAdapter homeAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.menuSort);
        if (item == null) {
            inflater.inflate(R.menu.menu, menu);
            item = menu.findItem(R.id.menuSort);
        }
        Spinner spinner = (Spinner) item.getActionView();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String s = ((TextView) selectedItemView).getText().toString();
                load(s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            Spinner spinner = (Spinner) getActivity().findViewById(R.id.sort_spinner);
            String s = spinner.getSelectedItem().toString();
            load(s);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_grid_movies, container, false);


        // reference the views
        RecyclerView recyclerViewMovies = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        // setup recycler view
        homeAdapter = new HomeAdapter(getContext());

        recyclerViewMovies
                .setLayoutManager
                        (new GridLayoutManager(getContext(), 2));
        recyclerViewMovies.setAdapter(homeAdapter);

        //RecyclerView Listener
        homeAdapter.getPositionClicks().subscribe(new Action1<Movie>() {
            @Override
            public void call(Movie movie) {
                onClickMoiveSubject.onNext(movie);
            }
        });

        // setup swipe to refresh
        final SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_grid);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Spinner spinner = (Spinner) getActivity().findViewById(R.id.sort_spinner);
                        String s = spinner.getSelectedItem().toString();
                        load(s);
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        return rootView;
    }

    @Override
    public void movieSuccess(List<Movie> movies) {
        homeAdapter.setHomeAdapterData(movies);
    }

    @Override
    public void movieError(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    void load(String s) {

        String sortby;
        if (s.compareTo("Most Popular") == 0) {
            sortby = "popularity.desc";
            MovieApi.discover(getContext())
                    .sortby(sortby)
                    .load(this);
        } else if (s.compareTo("Highest Rated") == 0) {
            sortby = "vote_average.desc";
            MovieApi.discover(getContext())
                    .sortby(sortby)
                    .load(this);
        } else {
            DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
            List<Movie> movies = databaseHelper.getAllMovies();
            homeAdapter.setHomeAdapterData(movies);
        }

    }

    public Observable<Movie> getMovieClick() {
        return onClickMoiveSubject.asObservable();
    }
}
