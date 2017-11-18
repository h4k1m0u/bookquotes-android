package me.bookquotes.quotes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Retrofit API interface (to download quotes only for authenticated users).
 */

public interface FeedAPI {
    @GET("quotes/?format=json")
    Call<Feed> getFeed(@Header("Authorization") String token, @Query("page") int page);
}
