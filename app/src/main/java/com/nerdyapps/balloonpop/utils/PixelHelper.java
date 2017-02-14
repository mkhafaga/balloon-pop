package com.nerdyapps.balloonpop.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by mohamedkhafaga on 2/3/17.
 */

public class PixelHelper {
    public static int pixelsToDp(int px, Context context) {
        Resources r = context.getResources();
        DisplayMetrics metrics = r.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return (int) dp;
    }
}
