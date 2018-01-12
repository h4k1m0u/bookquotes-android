package com.gistutorials.bookquotes;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Quote adapter that populates the RecyclerView.
 * http://stacktips.com/tutorials/android/android-recyclerview-example
 * http://www.devexchanges.info/2017/02/android-recyclerview-dynamically-load.html
 */

public class QuoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Quote> mQuotes;
    private RecyclerView mRecyclerView;
    private int mNextPage;
    private FeedAPI mAPI;
    private boolean mIsLoading = false;

    private final int VIEW_TYPE_QUOTE = 0;
    private final int VIEW_TYPE_PROGRESSBAR = 1;

    class QuoteViewHolder extends RecyclerView.ViewHolder {
        TextView mQuoteTextView;
        TextView mAuthorTextView;

        public QuoteViewHolder(View itemView) {
            super(itemView);
            mQuoteTextView = (TextView) itemView.findViewById(R.id.quote_text);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.quote_author);
        }
    }

    class ProgressBarHolder extends RecyclerView.ViewHolder {
        ProgressBar mProgressBar;

        public ProgressBarHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
        }
    }

    public QuoteAdapter(Feed feed, FeedAPI api, RecyclerView recyclerView) {
        // custom constructor
        mQuotes = feed.getResults();
        mNextPage = 2;
        mAPI = api;
        mRecyclerView = recyclerView;

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

                // consider the progress bar also
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    mIsLoading = true;

                    // show progress bar before response
                    mQuotes.add(null);
                    QuoteAdapter.this.notifyItemInserted(mQuotes.size() - 1);

                    // asynchronously download the next remote json feed
                    Call<Feed> call = mAPI.getFeed(mNextPage);
                    call.enqueue(new Callback<Feed>() {
                        @Override
                        public void onResponse(Call<Feed> call, Response<Feed> response) {
                            Log.d("Downloading", "Feed downloaded");

                            // remove progress bar after response
                            mQuotes.remove(mQuotes.size() - 1);
                            QuoteAdapter.this.notifyItemRemoved(mQuotes.size());

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
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a quote or a progressbar row
        RecyclerView.ViewHolder viewHolder;

        if (viewType == VIEW_TYPE_QUOTE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_row, parent, false);
            viewHolder = new QuoteViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_row, parent, false);
            viewHolder = new ProgressBarHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // show quote row or progress bar
        if (holder instanceof QuoteViewHolder) {
            // update view with quote object
            Quote quote = mQuotes.get(position);
            QuoteViewHolder quoteViewHolder = (QuoteViewHolder) holder;
            quoteViewHolder.mQuoteTextView.setText(quote.getText());
            quoteViewHolder.mAuthorTextView.setText(quote.getAuthor());
        }
    }

    @Override
    public int getItemCount() {
        return mQuotes.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("Position", Integer.toString(position));
        // display progress bar in end of recycler view (see constructor when null is added & removed)
        return (mQuotes.get(position) == null ? VIEW_TYPE_PROGRESSBAR : VIEW_TYPE_QUOTE);
    }
}
