package com.ekanek.ekanekwallpaper.util;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ekanek.ekanekwallpaper.data.ImagesDataRepo;
import com.ekanek.ekanekwallpaper.ui.home.MainViewModel;
import com.ekanek.ekanekwallpaper.ui.splash.SplashViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private ImagesDataRepo mImagesDataRepo;
    private Application mApplication;

    public ViewModelFactory(ImagesDataRepo imagesDataRepo, Application application) {
        mImagesDataRepo = imagesDataRepo;
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(mApplication, mImagesDataRepo);
        } else {
            return (T) new SplashViewModel(mApplication);
        }
    }
}