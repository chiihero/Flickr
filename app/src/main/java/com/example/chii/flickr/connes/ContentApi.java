package com.example.chii.flickr.connes;

import android.database.Observable;

import com.example.chii.flickr.Bean.WallpaperBean;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ContentApi {
    String Base_URL = "http://139.199.11.57:8080/";
    @GET("/mysql.jsp")
    Observable<WallpaperBean> getJson(@Query("source") String str, @Query("start") int i, @Query("size") int i2);
}
