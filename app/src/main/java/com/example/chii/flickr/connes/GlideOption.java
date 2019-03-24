package com.example.chii.flickr.connes;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.bumptech.glide.request.RequestOptions;

public class GlideOption {
    private GlideOption() {
    }

    private static class ClientHolder {
        private static RequestOptions mOptions = new RequestOptions()
                .skipMemoryCache(true)
                .placeholder(new ColorDrawable(Color.rgb(255, 240, 245)))//Placeholder 请求图片加载中
                .error(new ColorDrawable(Color.RED))//Error 请求图片加载错误
                .fallback(new ColorDrawable(Color.GRAY))//Fallback 请求url/model为空
                .dontAnimate();
    }

    public static RequestOptions getmOptions() {
        return ClientHolder.mOptions;
    }
}
