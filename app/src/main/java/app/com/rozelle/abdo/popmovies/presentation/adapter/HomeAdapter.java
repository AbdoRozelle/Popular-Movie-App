package app.com.rozelle.abdo.popmovies.presentation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.com.rozelle.abdo.popmovies.R;
import app.com.rozelle.abdo.popmovies.domain.Movie;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MovieViewHolder> {

    private final PublishSubject<Movie> onClickMoiveSubject = PublishSubject.create();
    private Context mContext;
    private List<Movie> movieList;


    public HomeAdapter(Context mContext) {
        this.mContext = mContext;
        movieList = new ArrayList<>();
    }

    public void setHomeAdapterData(List<Movie> newData) {
        movieList.clear();
        movieList.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_view_holder, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        final Movie movie = movieList.get(position);
        holder.title.setText(movie.getOriginal_title());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMoiveSubject.onNext(movie);
            }
        });
        // loading album cover using Glide library
        Picasso.with(mContext).load(movie.getPoster_path()).into(holder.poster);
    }

    public Observable<Movie> getPositionClicks() {
        return onClickMoiveSubject.asObservable();
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView poster;

        public MovieViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            poster = (ImageView) view.findViewById(R.id.poster);
        }
    }
}