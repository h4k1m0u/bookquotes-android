package com.gistutorials.bookquotes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Parsing remote json:
 * https://futurestud.io/tutorials/retrofit-getting-started-and-android-client
 * http://square.github.io/retrofit/
 */

public class QuotesActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.quotes);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // internet connectivity broadcast receiver
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // check if connected to internet
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = (activeNetwork != null && activeNetwork.isConnected());

                // get first page of quotes if connected to internet
                if (isConnected)
                    getInitialData();
                else
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            }
        };
    }

    public void getInitialData() {
        // asynchronously download the first remote json feed
        Retrofit retrofit = Util.getBuilder();
        final FeedAPI api = retrofit.create(FeedAPI.class);
        Call<Feed> call = api.getFeed(1);
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                // fill recyclerview from feed
                Feed feed = response.body();
                QuoteAdapter mQuoteAdapter = new QuoteAdapter(feed, api, mRecyclerView);
                mRecyclerView.setAdapter(mQuoteAdapter);
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                String message = t.getMessage();
                Log.d("failure", message);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register internet connectivity receiver
        registerReceiver(mReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onPause() {
        // unregister internet connectivity receiver
        unregisterReceiver(mReceiver);

        super.onPause();
    }
}
