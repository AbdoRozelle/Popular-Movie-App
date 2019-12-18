package app.com.rozelle.abdo.popmovies.presentation.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.com.rozelle.abdo.popmovies.R;
import app.com.rozelle.abdo.popmovies.data.database.DatabaseHelper;
import app.com.rozelle.abdo.popmovies.domain.Movie;
import app.com.rozelle.abdo.popmovies.domain.Review;
import app.com.rozelle.abdo.popmovies.domain.Trailer;
import app.com.rozelle.abdo.popmovies.presentation.adapter.view_holders.HeaderViewHolder;
import app.com.rozelle.abdo.popmovies.presentation.adapter.view_holders.ReviewViewHolder;
import app.com.rozelle.abdo.popmovies.presentation.adapter.view_holders.TrailerViewHolder;
import rx.Observable;
import rx.subjects.PublishSubject;


public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = DetailAdapter.class.getSimpleName();
    private final PublishSubject<String> onClickTrailer = PublishSubject.create();
    private final PublishSubject<Boolean> onClickFavorite = PublishSubject.create();
    private final int REVIEW = 0;
    private final int TRAILER = 1;
    private final int HEADER = 2;
    private List<Object> dataList;
    private Context context;


    public DetailAdapter(Context context) {
        dataList = new ArrayList<>();
        this.context = context;
    }


    public void setReviewData(List<Review> newData) {
        dataList.addAll(newData);
        notifyDataSetChanged();
    }

    public void setTrailerData(List<Trailer> newData) {
        dataList.addAll(1, newData);
        notifyDataSetChanged();
    }

    public void setHeaderData(Movie movie) {
        dataList.clear();
        dataList.add(movie);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) instanceof Review)
            return REVIEW;
        else if (dataList.get(position) instanceof Trailer)
            return TRAILER;
        else if (dataList.get(position) instanceof Movie)
            return HEADER;
        else
            return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TRAILER:
                View itemView = inflater.inflate(R.layout.trailer_view_holder, parent, false);
                viewHolder = new TrailerViewHolder(itemView);
                break;
            case REVIEW:
                View itemView2 = inflater.inflate(R.layout.review_view_holder, parent, false);
                viewHolder = new ReviewViewHolder(itemView2);
                break;

            case HEADER:
                View itemView3 = inflater.inflate(R.layout.header_view_holder, parent, false);
                viewHolder = new HeaderViewHolder(itemView3);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new ReviewViewHolder(v);
                Log.e(TAG, "invalid View Type");
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {

            case REVIEW:
                final Review review = (Review) dataList.get(position);

                ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;
                reviewViewHolder.reviewAuthor.setText(review.getAuthor());
                reviewViewHolder.reviewContent.setText(review.getContent());

                /*reviewViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickTrailer.onNext(review.getId());
                    }
                });*/
                break;
            case TRAILER:
                final Trailer trailer = (Trailer) dataList.get(position);
                TrailerViewHolder trailerViewHolder = (TrailerViewHolder) holder;
                trailerViewHolder.trailerText.setText("Trailer " + position);

                trailerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        onClickTrailer.onNext(trailer.getKey());
                    }
                });
                break;

            case HEADER:
                final Movie movie = (Movie) dataList.get(position);
                final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.release_date.setText(movie.getRelease_date());
                headerViewHolder.vote_average.setText(movie.getVote_average());
                headerViewHolder.overview.setText(movie.getOverview());
                String posterPath = movie.getPoster_path();

                if (posterPath != null && !posterPath.equals("null"))
                    Picasso.get()
                            .load(posterPath)
                            .placeholder(R.drawable.ic_file_download_black_24dp)
                            .error(R.drawable.ic_error_black_24dp)
                            .into(headerViewHolder.imageView);
                else
                headerViewHolder.imageView.setImageResource(R.drawable.image_black_placeholder);

                final DatabaseHelper databaseHelper = new DatabaseHelper(context);
                Movie mMovie = databaseHelper.getMovie(movie.getId());


                if (mMovie != null) {
                    headerViewHolder.imageButton.setTag(1);
                    headerViewHolder.imageButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                } else {
                    headerViewHolder.imageButton.setTag(0);
                    headerViewHolder.imageButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }

                headerViewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getTag().toString().equals("0")) {
                            v.setTag(1);
                            headerViewHolder.imageButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                            databaseHelper.insertMovie(movie);
                        } else {
                            v.setTag(0);
                            headerViewHolder.imageButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            databaseHelper.deleteMovie(movie.getId());
                        }
                    }
                });

                break;
            default:
                Log.e(TAG, "onBindViewHolder: ");
        }


    }

    public Observable<String> getTrailerClicks() {
        return onClickTrailer.asObservable();
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
