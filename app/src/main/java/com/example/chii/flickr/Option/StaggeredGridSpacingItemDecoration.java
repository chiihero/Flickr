package com.example.chii.flickr.Option;

import android.graphics.Rect;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.ItemDecoration;
import android.support.p003v7.widget.RecyclerView.State;
import android.view.View;

public class StaggeredGridSpacingItemDecoration extends ItemDecoration {
    private int spacing;
    private int spanCount;

    public StaggeredGridSpacingItemDecoration(int i, int i2) {
        this.spanCount = i;
        this.spacing = i2;
    }

    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
        int i = childAdapterPosition % this.spanCount;
        rect.left = this.spacing - ((this.spacing * i) / this.spanCount);
        rect.right = ((i + 1) * this.spacing) / this.spanCount;
        if (childAdapterPosition < this.spanCount) {
            rect.top = this.spacing;
        }
        rect.bottom = this.spacing;
    }
}
