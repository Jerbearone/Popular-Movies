package com.android.moviesnow;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import model.Movie;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<Movie> mMovieInfo = new ArrayList<>();
    RecyclerViewAdapter mainAdapter;
    JSONObject mJsonToParse;
    URL url = NetworkUtilities.buildUrl(NetworkUtilities.MOSTPOPULAR);
    JSONArray movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Main onCreate started.");
        //creating Spinner
        Spinner spinner = findViewById(R.id.spinner_main);
        ArrayAdapter<CharSequence> SpinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.sorting_selector, android.R.layout.simple_spinner_item);
        //here we are fetching the data from the url to get our JSON object.
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(SpinnerAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void initializeImages() {
        initializeRecyclerView();

    }

    //Override for spinner to use functionality.

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String spinnerChoice = parent.getItemAtPosition(position).toString();
        Log.d(TAG, "onItemSelected: " + spinnerChoice);
        if (position == 0) {
            mImageUrls.clear();
            mNames.clear();
            mMovieInfo.clear();
            url = NetworkUtilities.buildUrl(NetworkUtilities.MOSTPOPULAR);
            //retrive Json Data and start recyclerView afterwards.
            retreiveJsonData(url);
        }
        else if (position == 1) {
            mImageUrls.clear();
            mNames.clear();
            mMovieInfo.clear();
            url = NetworkUtilities.buildUrl(NetworkUtilities.VOTEAVERAGE);
            //retrive Json Data and start recyclerView afterwards.
            retreiveJsonData(url);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class MoviesDbQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String moviesDbJson = null;
            try {
                moviesDbJson = NetworkUtilities.getResponseFromUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return moviesDbJson;
        }

        // Override onPostExecute to prepare mJsonToParse for parsing the JSON information.
        @Override
        protected void onPostExecute(String moviesDbJson) {
            if (moviesDbJson != null && !moviesDbJson.equals("")) {
                String mJsonStringToParse = moviesDbJson;
                try {
                    mJsonToParse = new JSONObject(mJsonStringToParse);
                    Log.d("JSON FROM ONPOSTEXECUTE", mJsonToParse.getString("page"));

                    //Parsing Json here.

                    try {
                        Log.d("someTag", "Right before Json");
                        movieList = mJsonToParse.getJSONArray("results");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //itterating through each movie to add info to mNames and mImageUrls

                    for (int x = 0; x < movieList.length(); x ++) {
                        try {

                            Movie parsedMovie = (JsonParsingUtil.parseMovieJson(movieList.getJSONObject(x)));
                            mImageUrls.add(NetworkUtilities.buildImageUrl(parsedMovie.getPoster_path()).toString());
                            mNames.add(parsedMovie.getTitle());
                            mMovieInfo.add(parsedMovie);
                            Log.d("HERE IS A TITLE", parsedMovie.getTitle());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //here we are initializing images and then recyclerView.
                    initializeImages();
                    //and it ends here..


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onPostExecute called json is " + moviesDbJson);
            }
        }
    }

    private void retreiveJsonData(URL ourUrl) {
        new MoviesDbQueryTask().execute(ourUrl);

    }

    private void initializeRecyclerView(){
        Log.d(TAG, "initializeRecyclerView: initializing recyclerView");
        RecyclerView recyclerView = findViewById(R.id.movie_recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mImageUrls,mNames, mMovieInfo, this);
        recyclerView.setAdapter(adapter);
        this.mainAdapter=adapter;
        mainAdapter.notifyDataSetChanged();
        //here we are using the grid layout for recyclerView.
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }
}
