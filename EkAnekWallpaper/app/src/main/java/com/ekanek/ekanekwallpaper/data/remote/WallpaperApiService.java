package com.ekanek.ekanekwallpaper.data.remote;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WallpaperApiService {
    @GET("?method=flickr.photos.getRecent&api_key=ec0a6588589fceb47b4a1f0f8c5f4728&format=json&nojsoncallback=?")
    public Call<GetRecentResponse> getRecentPhotos(@Query("page") int page, @Query("per_page") int perPage);

    @GET("?method=flickr.photos.search&api_key=ec0a6588589fceb47b4a1f0f8c5f4728&format=json&nojsoncallback=?")
    public Call<GetRecentResponse> searchPhotos(@Query("text") String searchQuery, @Query("page") int page, @Query("per_page") int perPage);
}
