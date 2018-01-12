package com.gistutorials.bookquotes;

import com.google.gson.annotations.SerializedName;

/**
 * Retrofit model class.
 */

public class Quote {
    @SerializedName("text")
    private String text;

    @SerializedName("author")
    private String author;

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }
}
