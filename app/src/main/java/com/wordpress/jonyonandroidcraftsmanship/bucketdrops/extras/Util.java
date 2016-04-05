package com.wordpress.jonyonandroidcraftsmanship.bucketdrops.extras;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import java.util.List;

public class Util {
    public static void showViews(List<View> views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hideViews(List<View> views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    public static void setBackground(View view, Drawable drawable) {
        if (moreThanICS()) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }

    }

    private static boolean moreThanICS() {
        return Build.VERSION.SDK_INT>15;
    }
}
