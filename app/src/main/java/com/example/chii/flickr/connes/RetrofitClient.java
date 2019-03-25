package com.example.chii.flickr.connes;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.chii.flickr.Adapter.RecyclerViewAdapter;
import com.example.chii.flickr.Bean.WallpaperBean;
import com.example.chii.flickr.view.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class RetrofitClient {
    private ContentApi service;
    private Subscription mLoadNetScription;
    private String source = "哔哩哔哩";
    private RecyclerViewAdapter mAdapter;
    private List<WallpaperBean.Wallpaper> mData = new ArrayList<>();
    private static final int DEFAULT_TIMEOUT = 5;
    public static String baseUrl = ContentApi.Base_URL;
    private OkHttpClient okHttpClient;
    private ContentApi apiService;
    private RetrofitClient() {

    }
    public static final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            //打印retrofit日志
            Log.i("RetrofitLog", "retrofitBack = " + message);
        }

    });

//    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .connectTimeout(10, TimeUnit.SECONDS)
//            .readTimeout(10, TimeUnit.SECONDS)
//            .writeTimeout(10, TimeUnit.SECONDS)
//            .build();
//
//    private static class ClientHolder {
//        private static Retrofit mRetrofit = new Retrofit.Builder()
//                .baseUrl("http://139.199.11.57:8080/")
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//    }

//    public static Retrofit getInstance() {
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        return ClientHolder.mRetrofit;
//    }
    private RetrofitClient(Context context, String url) {

        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                .addInterceptor(loggingInterceptor)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        apiService = retrofit.create(ContentApi.class);
    }




    private void getDate(int mStart,int mSizes) {
        if (mLoadNetScription != null && mLoadNetScription.isUnsubscribed()) {
            mLoadNetScription.unsubscribe();
        }
        mLoadNetScription = apiService.getJson(source, 1 + mSizes * (mStart - 1), mSizes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<WallpaperBean, Boolean>() {
                    @Override
                    public Boolean call(WallpaperBean WallpaperBean) {
                        return !WallpaperBean.isError();
                    }
                }).flatMap(new FilterMap()).subscribe(new LoadNetSubscriber());
        mStart++;
        mAdapter.notifyDataSetChanged();

    }
    class FilterMap implements Func1<WallpaperBean, Observable<WallpaperBean.Wallpaper>> {
        @Override
        public rx.Observable<WallpaperBean.Wallpaper> call(WallpaperBean WallpaperBean) {
            if (WallpaperBean.getwallpaper().size() >= 10) {
                LoadingMore = true;

            } else {
                LoadingMore = false;
            }

            return rx.Observable.from(WallpaperBean.getwallpaper());
        }
    }

    /**
     * 网络请求
     */
    class LoadNetSubscriber extends Subscriber<WallpaperBean.Wallpaper> {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
//            mAdapter.setLoadingMore(false);
//            mRecyclerView.setRefreshing(false);
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(WallpaperBean.Wallpaper wallpaper) {
            mData.add(wallpaper);
            mAdapter.notifyItemInserted(mData.size());
        }
    }

}


