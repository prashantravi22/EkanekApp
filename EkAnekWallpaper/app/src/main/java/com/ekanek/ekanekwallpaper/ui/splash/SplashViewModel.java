package com.ekanek.ekanekwallpaper.ui.splash;


import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ekanek.ekanekwallpaper.util.SingleLiveEvent;

public class SplashViewModel extends AndroidViewModel {
    protected Handler mHandler = new Handler();
    private SingleLiveEvent<Void> mNavigationCommand = new SingleLiveEvent<>();

    public SplashViewModel(@NonNull Application application) {
        super(application);
    }

    public void splashDisplayed() {
        mHandler.postDelayed(() -> mNavigationCommand.call(), 1500);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mHandler.removeCallbacksAndMessages(null);
    }

    public SingleLiveEvent<Void> getNavigationCommand() {
        return mNavigationCommand;
    }
}

