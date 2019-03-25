package com.example.chii.flickr.Option;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.p003v7.widget.GridLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.ItemDecoration;
import android.support.p003v7.widget.RecyclerView.LayoutManager;
import android.support.p003v7.widget.RecyclerView.LayoutParams;
import android.support.p003v7.widget.RecyclerView.State;
import android.support.p003v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class DividerGridItemDecoration extends ItemDecoration {
    private static final int[] ATTRS = new int[]{16843284};
    private Drawable mDivider;

    public DividerGridItemDecoration(Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(ATTRS);
        this.mDivider = obtainStyledAttributes.getDrawable(0);
        obtainStyledAttributes.recycle();
    }

    public void onDraw(Canvas canvas, RecyclerView recyclerView, State state) {
        drawHorizontal(canvas, recyclerView);
        drawVertical(canvas, recyclerView);
    }

    private int getSpanCount(RecyclerView recyclerView) {
        LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        }
        return layoutManager instanceof StaggeredGridLayoutManager ? ((StaggeredGridLayoutManager) layoutManager).getSpanCount() : -1;
    }

    public void drawHorizontal(Canvas canvas, RecyclerView recyclerView) {
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = recyclerView.getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            int left = childAt.getLeft() - layoutParams.leftMargin;
            int right = (childAt.getRight() + layoutParams.rightMargin) + this.mDivider.getIntrinsicWidth();
            int bottom = childAt.getBottom() + layoutParams.bottomMargin;
            this.mDivider.setBounds(left, bottom, right, this.mDivider.getIntrinsicHeight() + bottom);
            this.mDivider.draw(canvas);
        }
    }

    public void drawVertical(Canvas canvas, RecyclerView recyclerView) {
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = recyclerView.getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            int top = childAt.getTop() - layoutParams.topMargin;
            int bottom = childAt.getBottom() + layoutParams.bottomMargin;
            int right = childAt.getRight() + layoutParams.rightMargin;
            this.mDivider.setBounds(right, top, this.mDivider.getIntrinsicWidth() + right, bottom);
            this.mDivider.draw(canvas);
        }
    }

    private boolean isLastColum(RecyclerView recyclerView, int i, int i2, int i3) {
        LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((i + 1) % i2 == 0) {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            if (((StaggeredGridLayoutManager) layoutManager).getOrientation() == 1) {
                if ((i + 1) % i2 == 0) {
                    return true;
                }
            } else if (i >= i3 - (i3 % i2)) {
                return true;
            }
        }
        return false;
    }

    private boolean isLastRaw(RecyclerView recyclerView, int i, int i2, int i3) {
        LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if (i >= i3 - (i3 % i2)) {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            if (((StaggeredGridLayoutManager) layoutManager).getOrientation() == 1) {
                if (i >= i3 - (i3 % i2)) {
                    return true;
                }
            } else if ((i + 1) % i2 == 0) {
                return true;
            }
        }
        return false;
    }

    public void getItemOffsets(Rect rect, int i, RecyclerView recyclerView) {
        int spanCount = getSpanCount(recyclerView);
        int itemCount = recyclerView.getAdapter().getItemCount();
        if (isLastRaw(recyclerView, i, spanCount, itemCount)) {
            rect.set(0, 10, this.mDivider.getIntrinsicWidth(), 0);
        } else if (isLastColum(recyclerView, i, spanCount, itemCount)) {
            rect.set(10, 10, 0, this.mDivider.getIntrinsicHeight());
        } else {
            rect.set(10, 10, this.mDivider.getIntrinsicWidth(), this.mDivider.getIntrinsicHeight());
        }
    }
}
