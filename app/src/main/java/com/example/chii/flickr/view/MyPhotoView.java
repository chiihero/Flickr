package com.example.chii.flickr.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.example.chii.flickr.Adapter.ViewpagerAdapter;
import com.example.chii.flickr.R;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;


public class MyPhotoView extends AppCompatActivity {

    private ViewpagerAdapter mAdapter;
    private List<View> viewList = new ArrayList<>();
    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        initView();
    }

    private void initView() {
        PhotoViewPager viewPager = (PhotoViewPager) findViewById(R.id.viewPager);
        intent = getIntent();
        bundle = intent.getExtras();
        String[] datas = (String[]) bundle.get("url");

        for (String data : datas) {
            View view = LayoutInflater.from(this).inflate(R.layout.simple_photoview, null);
            PhotoView photoView = (PhotoView) view.findViewById(R.id.pv_photo);

            Glide.with(MyPhotoView.this)
                    .load(data)
                    .apply(GlideOption.getmOptions())
                    .into(photoView);
            photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    finish();
                }


            });

            viewList.add(view);
        }
        mAdapter = new ViewpagerAdapter(MyPhotoView.this, viewList);
        viewPager.setAdapter(mAdapter);

        int pos = (int) bundle.get("pos");
        viewPager.setCurrentItem(pos);

        int currentItem = viewPager.getCurrentItem();
        Log.i("TGG", currentItem + "");
        viewPager.addOnPageChangeListener(new PhotoViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
