package com.example.chii.flickr.Option;

import android.database.Observable;

import com.example.chii.flickr.Bean.WallpaperBean;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ContentApi {
    @GET("/mysql.jsp")
    Observable<WallpaperBean> getJson(@Query("source") String str, @Query("start") int i, @Query("size") int i2);
}
