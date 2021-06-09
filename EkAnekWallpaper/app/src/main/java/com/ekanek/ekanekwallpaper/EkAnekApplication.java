package com.ekanek.ekanekwallpaper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.ekanek.ekanekwallpaper.data.ImagesDataRepo;
import com.ekanek.ekanekwallpaper.data.remote.WallpaperApiService;
import com.ekanek.ekanekwallpaper.util.ViewModelFactory;

import android.app.Application;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EkAnekApplication extends Application {
    ImagesDataRepo mImagesDataRepo;
    WallpaperApiService mWallpaperApiService;
    ViewModelFactory mViewModelFactory;


    @Override
    public void onCreate() {
        super.onCreate();
        // Create global dependencies
        mWallpaperApiService = provideFlickrApiService(new GsonBuilder().create());
        mImagesDataRepo = provideImagesRepository(mWallpaperApiService);
        mViewModelFactory = provideViewModelFactory(mImagesDataRepo);
    }

    private WallpaperApiService provideFlickrApiService(Gson gson) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.flickr.com/services/rest/")
                .addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.client(httpClientBuilder.build())
                .build();
        return retrofit.create(WallpaperApiService.class);
    }

    private ImagesDataRepo provideImagesRepository(WallpaperApiService wallpaperApiService) {
        return new ImagesDataRepo(wallpaperApiService, this);
    }

    private ViewModelFactory provideViewModelFactory(ImagesDataRepo imagesDataRepo) {
        return new ViewModelFactory(imagesDataRepo, this);
    }


    public ImagesDataRepo getImagesRepository() {
        return mImagesDataRepo;
    }

    public WallpaperApiService getFlickrApiService() {
        return mWallpaperApiService;
    }

    public ViewModelFactory getViewModelFactory() {
        return mViewModelFactory;
    }
}
