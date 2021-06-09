package com.ekanek.ekanekwallpaper.data;


import android.app.Application;

import androidx.lifecycle.LiveData;

import com.ekanek.ekanekwallpaper.data.model.Resource;
import com.ekanek.ekanekwallpaper.data.remote.WallpaperApiService;
import com.ekanek.ekanekwallpaper.data.remote.PhotosResponse;
import com.ekanek.ekanekwallpaper.util.GetRecentPhotosTask;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ImagesDataRepo {
    private WallpaperApiService mWallpaperApiService;
    private Application mApplication;
    private Executor mExecutor;

    public ImagesDataRepo(WallpaperApiService wallpaperApiService, Application application) {
        mWallpaperApiService = wallpaperApiService;
        mApplication = application;
        mExecutor = Executors.newFixedThreadPool(2);
    }

    public LiveData<Resource<PhotosResponse>> getPhotos(String searchString) {
        GetRecentPhotosTask task = new GetRecentPhotosTask(mWallpaperApiService, searchString, 0, 15);
        mExecutor.execute(task);
        return task.asLiveData();
    }

    public LiveData<Resource<PhotosResponse>> getNextPage(String searchString, int previousPage) {
        GetRecentPhotosTask task = new GetRecentPhotosTask(mWallpaperApiService, searchString, previousPage + 1, 15);
        mExecutor.execute(task);
        return task.asLiveData();
    }
}
