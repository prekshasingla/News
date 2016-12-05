package com.example.prekshasingla.news;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class SourceFragment extends Fragment {


    private ImageAdapter sourceAdapter;
    private ArrayList<Source> currentSourceList = new ArrayList<>();
    private DBAdapter dba;


    public SourceFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        new NewsSyncTask().execute();
        //Initializes our custom adapter for the Gridview with the current Movie ArrayList data and fetches id's to identify Gridview
        sourceAdapter = new ImageAdapter(getActivity(), currentSourceList);
        View rootView = inflater.inflate(R.layout.fragment_source, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.movieGridView);

        gridView.setAdapter(sourceAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Source currentMovie = sourceAdapter.getItem(position);
                //((Callback) getActivity()).onItemSelected(currentMovie);
            }
        });
        return rootView;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

            new NewsSyncTask().execute();
            return true;

       // return super.onOptionsItemSelected(item);
    }


    public class NewsSyncTask extends AsyncTask<Void, Void, Source[]> {

        private final String LOG_TAG = NewsSyncTask.class.getName();


        private Source[] getSourceDatafromJson(String sourceJsonString)
                throws JSONException {
            final String OWM_RESULTS = "sources";
            final String OWM_SOURCEID = "id";
            final String OWM_SOURCE = "name";
            final String OWM_URLS_TO_LOGOS= "urlsToLogos";
            final String OWM_LOGO_URL = "small";
            final String OWM_CATEGORY = "category";

            JSONObject sourceJson = new JSONObject(sourceJsonString);
            JSONArray sourceArray = sourceJson.getJSONArray(OWM_RESULTS);
            Source[] resultObjects = new Source[sourceArray.length()];



            for (int i = 0; i < sourceArray.length(); i++) {
                JSONObject movieObject = sourceArray.getJSONObject(i);
                JSONObject urlLogos=movieObject.getJSONObject(OWM_URLS_TO_LOGOS);
                resultObjects[i] = new Source(movieObject.getString(OWM_SOURCEID), movieObject.getString(OWM_SOURCE), urlLogos.getString(OWM_LOGO_URL), movieObject.getString(OWM_CATEGORY));
            }
            return resultObjects;
        }



        @Override
        protected  Source[] doInBackground(Void... status) {


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;


            try {

                URL url = new URL("https://newsapi.org/v1/sources?language=en");

                Log.d("URL",""+url);
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                   // return "null";
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    //setLocationStatus(getContext(), LOCATION_STATUS_SERVER_DOWN);
                    Log.e("Error:", "LOCATION_STATUS_SERVER_DOWN");
                    //return "LOCATION_STATUS_SERVER_DOWN";
                }
                forecastJsonStr = buffer.toString();
                Log.d("Data",forecastJsonStr);
                return getSourceDatafromJson(forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                //setLocationStatus(getContext(), LOCATION_STATUS_SERVER_DOWN);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
                // setLocationStatus(getContext(), LOCATION_STATUS_SERVER_INVALID);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }
        protected void onPostExecute(Source[] result) {
            //Log.d("result", result);
            if (result != null) {
                sourceAdapter.clear();
                sourceAdapter.addAll(result);
                sourceAdapter.notifyDataSetChanged();
            }


        }



    }

}
