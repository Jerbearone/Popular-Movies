package com.android.moviesnow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import model.Movie;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Linking the image Id's with variables.

        ImageView posters_image = findViewById(R.id.poster_image);
        TextView movies_plot = findViewById(R.id.plot_summary);
        TextView movie_rating = findViewById(R.id.rating);
        TextView movie_title = findViewById(R.id.movie_title);

        Intent intent = getIntent();
        //This will get the entire Movie object for the movie selected on Screen.
        Movie launchedMovie = intent.getParcelableExtra("info");

        //here we will set the image with glide..

        Glide.with(this)
                .asBitmap()
                .load(NetworkUtilities.buildImageUrl(launchedMovie.getPoster_path()))
                .into(posters_image);

        String movie_plot = launchedMovie.getOverview();
        movies_plot.setText(movie_plot);
        movie_rating.setText(launchedMovie.getVote_average());
        movie_title.setText(launchedMovie.getTitle());
    }
}
