package com.example.chii.flickr.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.chii.flickr.Bean.WallpaperBean;
import com.example.chii.flickr.R;
import com.example.chii.flickr.connes.GlideOption;


import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<WallpaperBean.Wallpaper> mData;
    private Context mContext;
    private MyAdapter.OnItemClickListener onItemClickListener;

    public MyAdapter(Context context, List<WallpaperBean.Wallpaper> data) {
        this.mData = data;
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.item_iv);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_item, parent, false);

        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        int height = (int) (((float) screenWidth / 2) / mData.get(position).getWidth() * mData.get(position).getHeight());
        // 绑定数据
        Log.i("RetrofitLog", "message = " + mData.get(position).getLink());
//        holder.mImageView.setData(holder,mData.get(position), mSizeModel.get(position),screenWidth/2);
        if (mData.get(position).isNull()) {
            Log.i("RetrofitLog", "mData is null");

            Glide.with(mContext)
                    .asBitmap()
                    .load(mData.get(position).getLink())
                    .apply(GlideOption.getmOptions())
                    .apply(new RequestOptions().override(screenWidth / 2, screenWidth / 2))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                            int height = (int) (((float) screenWidth / 2) / bitmap.getWidth() * bitmap.getHeight());
                            setCardViewLayoutParams(holder, screenWidth / 2, height);
                            mData.get(position).setSize(screenWidth / 2, height);
                            holder.mImageView.setImageBitmap(bitmap);
                        }
                    });
        } else {
            setCardViewLayoutParams(holder, screenWidth / 2, height);
            Glide.with(mContext)
                    .asBitmap()
                    .load(mData.get(position).getLink())
                    .apply(GlideOption.getmOptions())
                    .apply(new RequestOptions().override(screenWidth / 2, height))
//                    .thumbnail(/*sizeMultiplier=*/ 0.25f)
                    .into(holder.mImageView);

        }
        //③ 对RecyclerView的每一个itemView设置点击事件
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.mImageView, pos);
                }
            }
        });
        holder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.mImageView, pos);
                } //表示此事件已经消费，不会触发单击事件
                return true;
            }
        });
    }
        private void setCardViewLayoutParams (ViewHolder holder,int width, int height){
            ViewGroup.LayoutParams layoutParams = holder.mImageView.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            holder.mImageView.setLayoutParams(layoutParams);
        }

        @Override
        public int getItemCount () {
            return mData.size();
        }

        @Override
        public void onViewRecycled (ViewHolder holder){
            super.onViewRecycled(holder);
        }

        // ① 定义点击回调接口
        public interface OnItemClickListener {
            void onItemClick(View view, int position);
            void onItemLongClick(View view, int position);
        }

        // ② 定义一个设置点击监听器的方法
        public void setOnItemClickListener (MyAdapter.OnItemClickListener listener){
            this.onItemClickListener = listener;
        }

    }

