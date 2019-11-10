package com.example.goodnews;


import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class GoodNewsLoader extends AsyncTaskLoader<List<Good>> {

    String mURL ;

    public GoodNewsLoader(Context context,String url) {
        super(context);
        this.mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Good> loadInBackground() {
        if (mURL == null) {
            return null;
        }

        List<Good> newsList = QueryUtils.fetchGoodData(mURL);
        return newsList;
    }
}