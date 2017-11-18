package me.bookquotes.quotes;

import com.google.gson.annotations.SerializedName;

/**
 * User response model class that contains the created user.
 */

public class User {
    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}