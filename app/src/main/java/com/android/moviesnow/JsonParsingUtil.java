package com.android.moviesnow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.Movie;

public class JsonParsingUtil {
    private static Movie parsedFullJson;
    //example json..:
    /*{"vote_count":5378,"id":299534,"video":false,"vote_average":8.5,"title":"Avengers: Endgame","popularity":319.667,
    "poster_path":"\/or06FN3Dka5tukK1e9sl16pB3iy.jpg","original_language":"en",
    "original_title":"Avengers: Endgame","genre_ids":[12,878,28],
    "backdrop_path":"\/7RyHsO4yDXtBv1zUU3mTpHeQ0d5.jpg","adult":false,
    "overview":"After the devastating events of Avengers: Infinity War,
     the universe is in ruins due to the efforts of the Mad Titan, Thanos. With the help of remaining allies,
      the Avengers must assemble once more in order to undo Thanos' actions and restore order
     to the universe once and for all, no matter what consequences may be in store.","release_date":"2019-04-24"}/*
      */

    public static Movie parseMovieJson (JSONObject json) throws JSONException {
        JSONObject someMovie = json;
            String vote_count = someMovie.getString("vote_count");
            String id = someMovie.getString("id");
            String vote_average = someMovie.getString("vote_average");
            String title = someMovie.getString("title");
            String poster_path = someMovie.getString("poster_path");
            boolean adult = someMovie.getBoolean("adult");
            JSONArray genre_ids = someMovie.getJSONArray("genre_ids");
            List<String> parsed_genre_ids = new ArrayList<String>();
            for (int x = 0; x < genre_ids.length(); x++) {
                parsed_genre_ids.add(genre_ids.getString(x));
            }


            String overview = someMovie.getString("overview");
            Movie oneOfTheMovies = new Movie(vote_count, id, vote_average, title, poster_path, adult,
                    parsed_genre_ids, overview);
            parsedFullJson = oneOfTheMovies;

        return parsedFullJson;

    }

}
