package com.example.chii.flickr.Option;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.internal.view.SupportMenu;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.RequestOptions;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation.CornerType;

public class GlideOption {

    private static class ClientHolder {
        private static RequestOptions mOptions = new RequestOptions().skipMemoryCache(true).placeholder(new ColorDrawable(Color.rgb(255, 240, 245))).error(new ColorDrawable(SupportMenu.CATEGORY_MASK)).fallback(new ColorDrawable(-7829368));
//        private static MultiTransformation<Bitmap> multi = new MultiTransformation(new BlurTransformation(25), new RoundedCornersTransformation(128, 0, CornerType.BOTTOM));
        private ClientHolder() {
        }
    }

    private GlideOption() {
    }

    public static RequestOptions getmOptions() {
        return ClientHolder.mOptions;
    }

//    public static MultiTransformation getmulti() {
//        return ClientHolder.multi;
//    }
}
