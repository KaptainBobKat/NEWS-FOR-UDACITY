package com.example.goodnews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private QueryUtils() {
    }

    public static List<Good> fetchGoodData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeGoodHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Good> newsList = extractFeatureFromJson(jsonResponse);

        return newsList;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }
        return url;
    }


    private static String makeGoodHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromGoodStream(inputStream);
            } else {
                Log.d("Error response code: ", String.valueOf(urlConnection.getResponseCode()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromGoodStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Good> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<Good> GoodNewsList = new ArrayList<>();


        try {

            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray resultsArray = response.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject currentResults = resultsArray.getJSONObject(i);

                String GoodHeadline = currentResults.getString("webTitle");
                String GoodCategory = currentResults.getString("sectionName");
                String GoodDate = currentResults.getString("webPublicationDate");
                String GoodUrl = currentResults.getString("webUrl");
                JSONArray tagsWriter = currentResults.getJSONArray("tags");
                String author="";
                if (tagsWriter.length()!= 0) {
                    JSONObject currenttagsauthor = tagsWriter.getJSONObject(0);
                    author = currenttagsauthor.getString("webTitle");
                }/*else{
                    author = "Author Not Available ..";
                }*/

                Good news = new Good(GoodHeadline, GoodCategory, GoodDate, GoodUrl, author);

                GoodNewsList.add(news);
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }

        return GoodNewsList;
    }
}
