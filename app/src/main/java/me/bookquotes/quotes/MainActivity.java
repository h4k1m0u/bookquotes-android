package me.bookquotes.quotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Parsing remote json:
 * https://futurestud.io/tutorials/retrofit-getting-started-and-android-client
 * http://square.github.io/retrofit/
 */

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.quotes);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // initialize the quotes json parser with Retrofit
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://bookquotes.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        FeedAPI api = retrofit.create(FeedAPI.class);
        Call<Feed> call = api.getFeed();

        // asynchronously download the remote json feed
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> feed) {
                // fill recyclerview from feed
                QuoteAdapter adapter = new QuoteAdapter(MainActivity.this, feed.body().getResults());
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                String message = t.getMessage();
                Log.d("failure", message);
            }
        });
    }
}
