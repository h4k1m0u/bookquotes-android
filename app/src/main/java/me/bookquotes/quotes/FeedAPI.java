package me.bookquotes.quotes;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Retrofit API interface (to download quotes).
 */

public interface FeedAPI {
    @GET("api/quotes/?format=json")
    Call<Feed> getFeed();
}
