package app.com.rozelle.abdo.popmovies.adapter.view_holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.rozelle.abdo.popmovies.R;

/**
 * Created by Abdo on 9/17/2016.
 */
public class HeaderViewHolder extends RecyclerView.ViewHolder {

    public TextView release_date;
    public TextView vote_average;
    public TextView overview;
    public ImageView imageView;
    public ImageButton imageButton;

    public HeaderViewHolder(View rootView) {
        super(rootView);
        release_date = (TextView) rootView.findViewById(R.id.release_date);
        vote_average = (TextView) rootView.findViewById(R.id.vote_average);
        overview = (TextView) rootView.findViewById(R.id.overview);
        imageView = (ImageView) rootView.findViewById(R.id.poster);
        imageButton = (ImageButton) rootView.findViewById(R.id.btn_favorite);
    }
}
