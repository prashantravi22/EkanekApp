package com.ekanek.ekanekwallpaper.util;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ekanek.ekanekwallpaper.data.model.Resource;
import com.ekanek.ekanekwallpaper.data.remote.WallpaperApiService;
import com.ekanek.ekanekwallpaper.data.remote.GetRecentResponse;
import com.ekanek.ekanekwallpaper.data.remote.PhotosResponse;

import retrofit2.Call;
import retrofit2.Response;

public class GetRecentPhotosTask implements Runnable {
    private WallpaperApiService mWallpaperApiService;
    protected String mQuery;
    private MutableLiveData<Resource<PhotosResponse>> mLiveData;
    private int mPage;
    private int mPerPage;

    public GetRecentPhotosTask(WallpaperApiService apiService, String query, int page, int perPage) {
        mWallpaperApiService = apiService;
        mQuery = query;
        mLiveData = new MutableLiveData<>();
        mLiveData.setValue(Resource.loading(null));
        mPage = page;
        mPerPage = perPage;
    }

    @Override
    public void run() {
        Call call = null;
        if (mQuery.equals("")) {
            call = mWallpaperApiService.getRecentPhotos(mPage, mPerPage);
        } else {
            call = mWallpaperApiService.searchPhotos(mQuery, mPage, mPerPage);
        }
        try {
            Response<GetRecentResponse> response = call.execute();
            ApiResponse apiResponse = new ApiResponse(response);
            if (apiResponse.isSuccessful()) {
                mLiveData.postValue(Resource.success(apiResponse.body.getPhotos()));
            } else {
                mLiveData.postValue(Resource.error(apiResponse.errorMessage, null));
            }
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse(e);
            mLiveData.postValue(Resource.error(apiResponse.errorMessage, null));
        }
    }

    public LiveData<Resource<PhotosResponse>> asLiveData() {
        return mLiveData;
    }
}
