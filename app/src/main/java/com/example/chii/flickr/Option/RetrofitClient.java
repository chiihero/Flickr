package com.example.chii.flickr.Option;

import android.util.Log;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import okhttp3.logging.HttpLoggingInterceptor.Logger;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {
    public static final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new C04681());
    private static final OkHttpClient okHttpClient = new Builder().addInterceptor(loggingInterceptor).connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).build();

    private static class ClientHolder {
        private static Retrofit mRetrofit = new Retrofit.Builder().baseUrl("http://139.199.11.57:8080/").client(RetrofitClient.okHttpClient).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();

        private ClientHolder() {
        }
    }

    /* renamed from: com.example.chii.flickr.Option.RetrofitClient$1 */
    static class C04681 implements Logger {
        C04681() {
        }

        public void log(String str) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("retrofitBack = ");
            stringBuilder.append(str);
            Log.i("RetrofitLog", stringBuilder.toString());
        }
    }

    private RetrofitClient() {
    }

    public static Retrofit getInstance() {
        loggingInterceptor.setLevel(Level.BODY);
        return ClientHolder.mRetrofit;
    }
}
