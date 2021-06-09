package com.ekanek.ekanekwallpaper.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.ekanek.ekanekwallpaper.EkAnekApplication;
import com.ekanek.ekanekwallpaper.R;
import com.ekanek.ekanekwallpaper.ui.home.MainActivity;
import com.ekanek.ekanekwallpaper.util.ViewModelFactory;

public class SplashActivity extends AppCompatActivity {
    private SplashViewModel mSplashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ViewModelFactory modelFactory = ((EkAnekApplication)getApplication()).getViewModelFactory();
        mSplashViewModel = ViewModelProviders.of(this, modelFactory).get(SplashViewModel.class);
        mSplashViewModel.splashDisplayed();
        subscribeToViewModel();

    }



    private void subscribeToViewModel() {
        mSplashViewModel.getNavigationCommand().observe(this, (v) -> navigateToHome());
    }

    private void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


}
