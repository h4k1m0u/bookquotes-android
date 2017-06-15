package me.bookquotes.quotes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

    class QuoteViewHolder extends RecyclerView.ViewHolder {
        TextView mQuoteTextView;
        TextView mAuthorTextView;

        public QuoteViewHolder(View itemView) {
            super(itemView);
            mQuoteTextView = (TextView) itemView.findViewById(R.id.quote_text);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.quote_author);
        }
    }

    public QuoteAdapter(Context context, List<Quote> quotes) {
        mQuotes = quotes;
    }

    @Override
    public QuoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a viewholder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_row, null);
        QuoteViewHolder viewHolder = new QuoteViewHolder(view);

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
