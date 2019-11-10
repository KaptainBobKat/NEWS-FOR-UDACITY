package com.example.goodnews;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Good>>{
    private GoodAdapter mGoodAdapter;
    private TextView NoContent;
    private static String REQUEST_GOOD_NEWS = "http://content.guardianapis.com/search?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView GoodListView = findViewById(R.id.List);
        NoContent = findViewById(R.id.missing_content);
        GoodListView.setEmptyView(NoContent);
        mGoodAdapter = new GoodAdapter(this, new ArrayList<Good>());

        GoodListView.setAdapter(mGoodAdapter);
        GoodListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Good BestNews = mGoodAdapter.getItem(position);
                Uri newsUri = Uri.parse(BestNews.getmUrl());
                Intent GoodIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(GoodIntent);
            }

        });
        ConnectivityManager GoodConnMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = GoodConnMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            android.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, (android.app.LoaderManager.LoaderCallbacks<Object>) this);
        } else {
            View loadingIndicator = findViewById(R.id.Loading_bar);
            loadingIndicator.setVisibility(View.GONE);
            NoContent.setText(R.string.nocontents);
        }
    }


    @Override
    public Loader<List<Good>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String minNews = sharedPreferences.getString(getString(R.string.settings_min_news_key), getString(R.string.settings_min_news_default));
        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
        String section = sharedPreferences.getString(getString(R.string.settings_section_news_key), getString(R.string.settings_section_news_default));

        Uri baseUri = Uri.parse(REQUEST_GOOD_NEWS);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("api-key", "test");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", minNews);
        uriBuilder.appendQueryParameter("order-by", orderBy);

        if (!section.equals(getString(R.string.settings_section_news_default))) {
            uriBuilder.appendQueryParameter("section", section);
        }

        return new GoodNewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Good>> loader, List<Good> data){
        View loadingIndicator = findViewById(R.id.Loading_bar);
        loadingIndicator.setVisibility(View.GONE);
        NoContent.setText(R.string.nonews);
        mGoodAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mGoodAdapter.addAll(data);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onLoaderReset(Loader<List<Good>> loader) {
        mGoodAdapter.clear();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}