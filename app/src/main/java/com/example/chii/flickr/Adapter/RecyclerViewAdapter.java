package com.example.chii.flickr.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.chii.flickr.Bean.WallpaperBean.Wallpaper;
import com.example.chii.flickr.R;
import java.util.List;

public class RecyclerViewAdapter extends Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<Wallpaper> mData;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int i);

        void onItemLongClick(View view, int i);
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            this.mImageView = (ImageView) view.findViewById(R.id.item_iv);
        }
    }

    public RecyclerViewAdapter(Context context, List<Wallpaper> list) {
        this.mData = list;
        this.mContext = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.view_item, viewGroup, false));
    }

    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final int i2 = this.mContext.getResources().getDisplayMetrics().widthPixels;
        int width = (int) (((((float) i2) / 2.0f) / ((float) ((Wallpaper) this.mData.get(i)).getWidth())) * ((float) ((Wallpaper) this.mData.get(i)).getHeight()));
        if (width > 1500) {
            width = 1500;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("message = ");
        stringBuilder.append(((Wallpaper) this.mData.get(i)).getLink());
        Log.i("RetrofitLog", stringBuilder.toString());
        if (((Wallpaper) this.mData.get(i)).isNull()) {
            Log.i("RetrofitLog", "mData is null");
            int i3 = i2 / 2;
            Glide.with(this.mContext).asBitmap().load(((Wallpaper) this.mData.get(i)).getLink()).apply(new RequestOptions().override(i3, i3)).into(new SimpleTarget<Bitmap>() {
                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                    int width = (int) (((((float) i2) / 2.0f) / ((float) bitmap.getWidth())) * ((float) bitmap.getHeight()));
                    RecyclerViewAdapter.this.setCardViewLayoutParams(viewHolder, i2 / 2, width);
                    ((Wallpaper) RecyclerViewAdapter.this.mData.get(i)).setSize(i2 / 2, width);
                    viewHolder.mImageView.setImageBitmap(bitmap);
                }
            });
        } else {
            i2 /= 2;
            setCardViewLayoutParams(viewHolder, i2, width);
            Glide.with(this.mContext).asBitmap().load(((Wallpaper) this.mData.get(i)).getLink()).apply(new RequestOptions().override(i2, width).diskCacheStrategy(DiskCacheStrategy.RESOURCE)).into(viewHolder.mImageView);
        }
        viewHolder.mImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (RecyclerViewAdapter.this.onItemClickListener != null) {
                    RecyclerViewAdapter.this.onItemClickListener.onItemClick(viewHolder.mImageView, viewHolder.getLayoutPosition());
                }
            }
        });
        viewHolder.mImageView.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                if (RecyclerViewAdapter.this.onItemClickListener != null) {
                    RecyclerViewAdapter.this.onItemClickListener.onItemLongClick(viewHolder.mImageView, viewHolder.getLayoutPosition());
                }
                return true;
            }
        });
    }

    private void setCardViewLayoutParams(ViewHolder viewHolder, int i, int i2) {
        LayoutParams layoutParams = viewHolder.mImageView.getLayoutParams();
        layoutParams.width = i;
        layoutParams.height = i2;
        viewHolder.mImageView.setLayoutParams(layoutParams);
    }

    public int getItemCount() {
        return this.mData.size();
    }

    public void onViewRecycled(ViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}