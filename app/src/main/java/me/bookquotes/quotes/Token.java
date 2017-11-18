package me.bookquotes.quotes;

import com.google.gson.annotations.SerializedName;

/**
 * Token response model class that contains a token.
 */

public class Token {
    @SerializedName("auth_token")
    private String token;

    public String getToken() {
        return token;
    }
}