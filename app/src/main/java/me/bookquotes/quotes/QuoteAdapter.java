package me.bookquotes.quotes;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Quote adapter that populates the RecyclerView.
 * http://stacktips.com/tutorials/android/android-recyclerview-example
 */

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder> {
    private List<Quote> mQuotes;
    private RecyclerView mRecyclerView;
    private int mNextPage;
    private FeedAPI mAPI;
    private boolean mIsLoading = false;

    class QuoteViewHolder extends RecyclerView.ViewHolder {
        TextView mQuoteTextView;
        TextView mAuthorTextView;

        public QuoteViewHolder(View itemView) {
            super(itemView);
            mQuoteTextView = (TextView) itemView.findViewById(R.id.quote_text);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.quote_author);
        }
    }

    public QuoteAdapter(Feed feed, FeedAPI api) {
        // custom constructor
        mQuotes = feed.getResults();
        mNextPage = 2;
        mAPI = api;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        // save reference to recyclerview in adapter: https://stackoverflow.com/a/31250210/2228912
        mRecyclerView = recyclerView;
    }

    @Override
    public QuoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a viewholder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_row, null);
        QuoteViewHolder viewHolder = new QuoteViewHolder(view);

        // attach listener on recyclerview scroll
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // download feed once at a time
                if (mIsLoading)
                    return;

                // test if bottom position is reached
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();

                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    mIsLoading = true;

                    // asynchronously download the next remote json feed
                    Call<Feed> call = mAPI.getFeed(mNextPage);
                    call.enqueue(new Callback<Feed>() {
                        @Override
                        public void onResponse(Call<Feed> call, Response<Feed> response) {
                            Log.d("Downloading", "Feed downloaded");

                            // append feed to recycler view
                            Feed feed = response.body();
                            mQuotes.addAll(feed.getResults());
                            QuoteAdapter.this.notifyDataSetChanged();

                            // increment next page
                            mNextPage++;
                            mIsLoading = false;
                        }

                        @Override
                        public void onFailure(Call<Feed> call, Throwable t) {
                            String message = t.getMessage();
                            Log.d("failure", message);
                        }
                    });
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(QuoteViewHolder holder, int position) {
        // get current quote object
        Quote quote = mQuotes.get(position);

        // update view with quote object
        holder.mQuoteTextView.setText(quote.getText());
        holder.mAuthorTextView.setText(quote.getAuthor());
    }

    @Override
    public int getItemCount() {
        return mQuotes.size();
    }
}
