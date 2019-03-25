package com.example.chii.flickr.Behavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.FloatingActionButton.Behavior;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;

public class ScrollAwareFABBehavior extends Behavior {
    public ScrollAwareFABBehavior(Context context, AttributeSet attributeSet) {
    }

    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view, View view2, int i, int i2) {
        return i == 2 || super.onStartNestedScroll(coordinatorLayout, floatingActionButton, view, view2, i, i2);
    }

    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view, int i, int i2, int i3, int i4, int i5) {
        super.onNestedScroll(coordinatorLayout, floatingActionButton, view, i, i2, i3, i4, i5);
        if (i2 > 10 && floatingActionButton.getVisibility() == 0) {
            hide(floatingActionButton);
        } else if (i2 < -10 && floatingActionButton.getVisibility() != 0) {
            show(floatingActionButton);
        }
    }

    private void show(final View view) {
        view.animate().cancel();
        view.setAlpha(0.0f);
        view.setScaleY(0.0f);
        view.setScaleX(0.0f);
        view.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(200).setInterpolator(new LinearOutSlowInInterpolator()).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
                view.setVisibility(0);
            }
        });
    }

    private void hide(final View view) {
        view.animate().cancel();
        view.animate().scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setDuration(200).setInterpolator(new FastOutLinearInInterpolator()).setListener(new AnimatorListenerAdapter() {
            private boolean mCancelled;

            public void onAnimationStart(Animator animator) {
                view.setVisibility(0);
                this.mCancelled = false;
            }

            public void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            public void onAnimationEnd(Animator animator) {
                if (!this.mCancelled) {
                    view.setVisibility(4);
                }
            }
        });
    }
}