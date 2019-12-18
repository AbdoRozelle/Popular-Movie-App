package app.com.rozelle.abdo.popmovies.adapter.view_holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.rozelle.abdo.popmovies.R;

/**
 * Created by Abdo on 9/16/2016.
 */
public class TrailerViewHolder extends RecyclerView.ViewHolder {
    public TextView trailerText;
    public ImageView trailerIcon;

    public TrailerViewHolder(View view) {
        super(view);
        trailerText = (TextView) view.findViewById(R.id.trailerText);
        trailerIcon = (ImageView) view.findViewById(R.id.trailerIcon);
    }
}
