package me.bookquotes.quotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // fill listview from array
        mListView = (ListView) findViewById(R.id.recipe_list_view);
        String[] listItems = {"word1", "word2", "word3", "word4"};
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        mListView.setAdapter(adapter);
    }
}
