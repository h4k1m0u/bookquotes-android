package me.bookquotes.quotes;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Quote adapter that populates the RecyclerView.
 * http://stacktips.com/tutorials/android/android-recyclerview-example
 */

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder> {
    private List<Quote> mQuotes;
    private RecyclerView mRecyclerView;

    class QuoteViewHolder extends RecyclerView.ViewHolder {
        TextView mQuoteTextView;
        TextView mAuthorTextView;

        public QuoteViewHolder(View itemView) {
            super(itemView);
            mQuoteTextView = (TextView) itemView.findViewById(R.id.quote_text);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.quote_author);
        }
    }

    public QuoteAdapter(List<Quote> quotes) {
        // custom constructor
        mQuotes = quotes;
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
                super.onScrolled(recyclerView, dx, dy);

                // test if bottom position is reached
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();

                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    Log.d("Bottom reached", "bottom");
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
