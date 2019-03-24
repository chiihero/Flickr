package com.example.chii.flickr.connes;



import com.example.chii.flickr.Bean.WallpaperBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chii on 2018/4/11.
 */

public interface HttpService {
    @GET("/mysql.jsp")
    Observable<WallpaperBean> getJson(@Query("start") int starts,
                                      @Query("size") int sizes);
}
