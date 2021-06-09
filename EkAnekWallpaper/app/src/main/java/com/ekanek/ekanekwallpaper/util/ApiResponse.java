package com.ekanek.ekanekwallpaper.util;

import androidx.annotation.Nullable;

import com.ekanek.ekanekwallpaper.data.remote.GetRecentResponse;

import java.io.IOException;

import retrofit2.Response;

/**
 * Common class used by API responses (GetRecentPhotos in our case).
 * It represents the result of an api response, httpCode, body and errorMessage received
 */
public class ApiResponse {
    public final int httpCode;
    @Nullable
    public GetRecentResponse body;
    @Nullable
    public String errorMessage;

    public ApiResponse(Throwable error) {
        httpCode = 500;
        body = null;
        errorMessage = error.getMessage();

        if (error instanceof IOException) {
            errorMessage = "Oops! We can't reach";
        }
    }

    public ApiResponse(Response<GetRecentResponse> response) {
        httpCode = response.code();
        if(response.isSuccessful()) {
            body = response.body();
            if (body.getStat().equals("fail")) {
                errorMessage = body.getMessage();
                body = null;
            } else {
                errorMessage = null;
            }
        } else {
            errorMessage = "Oops! We can't reach ";
            body = null;
        }
    }

    public boolean isSuccessful() {
        return httpCode >= 200 && httpCode < 300 && body != null;
    }
}
