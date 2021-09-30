package com.example.barcode_scanner.network;

import com.example.barcode_scanner.data.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static Retrofit retrofit =
            new Retrofit.Builder()
                    .baseUrl(Constants.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create()).build();


    private static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient().newBuilder()
                .connectTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    public static <S> S getService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }


}
