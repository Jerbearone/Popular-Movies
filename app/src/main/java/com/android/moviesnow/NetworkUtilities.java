package com.android.moviesnow;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtilities {
    //Must delete this KEY --------================---------
    // You can get your own API key by requesting one at https://api.themoviedb.org.
    static final String MYKEY = "Insert your key here";
    static final String THEMOVIEDBBASEURL = "https://api.themoviedb.org/3/discover/movie";
    static final String THEMOVIEIMAGEBASEURL = "http://image.tmdb.org/t/p";
    static final String THEMOVIEIMAGESIZE = "w185"; // This is the image size..

    //These are the Optional paramaters for sorting the information.
    static final String MOSTPOPULAR = "popularity.desc";
    static final String VOTEAVERAGE = "vote_average.desc";
    static final String SORT_PARAM = "sort_by";
    static final String API_KEY_PARAM = "api_key";
    private static final String TAG = "buildUrl was called";

    public static URL buildUrl(String QueryChoice) {
        Uri builtUri = Uri.parse(THEMOVIEDBBASEURL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM,MYKEY)
                .appendQueryParameter(SORT_PARAM, QueryChoice)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildImageUrl(String posterPath) {
        Uri builtImageUri = Uri.parse(THEMOVIEIMAGEBASEURL).buildUpon()
                .appendPath(THEMOVIEIMAGESIZE)
                .appendEncodedPath(posterPath)
                .build();
        URL url = null;
        try {
            url = new URL(builtImageUri.toString());

            } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String getResponseFromUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream input = urlConnection.getInputStream();

            Scanner scanner = new Scanner(input);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }

    }

}
