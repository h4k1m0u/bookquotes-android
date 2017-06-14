package me.bookquotes.quotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Quote adapter that populates the listview.
 * http://www.journaldev.com/10416/android-listview-with-custom-adapter-example-tutorial
 * https://developer.android.com/training/improving-layouts/smooth-scrolling.html
 * https://www.ricston.com/blog/optimising-listview-viewholder-pattern/
 */

public class QuoteAdapter extends ArrayAdapter<Quote> {
    private static class QuoteViewHolder {
        TextView quoteTextView;
        TextView authorTextView;
    }

    public QuoteAdapter(Context context, List<Quote> quotes) {
        super(context, R.layout.quote_row, quotes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get current quote object
        Quote quote = getItem(position);

        // cache list view rows for a smooth scrolling
        QuoteViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new QuoteViewHolder();

            // inflate layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.quote_row, parent, false);
            viewHolder.quoteTextView = (TextView) convertView.findViewById(R.id.quote_text);
            viewHolder.authorTextView = (TextView) convertView.findViewById(R.id.quote_author);

            // store view in cache
            convertView.setTag(viewHolder);
        } else {
            // get view from cache
            viewHolder = (QuoteViewHolder) convertView.getTag();
        }

        // update view with quote object
        viewHolder.quoteTextView.setText(quote.getText());
        viewHolder.authorTextView.setText(quote.getAuthor());

        return convertView;
    }
}
