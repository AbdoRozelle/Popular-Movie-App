package app.com.rozelle.abdo.popmovies.adapter.view_holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import app.com.rozelle.abdo.popmovies.R;

/**
 * Created by Abdo on 9/16/2016.
 */
public class ReviewViewHolder extends RecyclerView.ViewHolder {
    public TextView reviewAuthor;
    public TextView reviewContent;

    public ReviewViewHolder(View view) {
        super(view);
        reviewAuthor = (TextView) view.findViewById(R.id.reviewAuthor);
        reviewContent = (TextView) view.findViewById(R.id.reviewContent);
    }
}

