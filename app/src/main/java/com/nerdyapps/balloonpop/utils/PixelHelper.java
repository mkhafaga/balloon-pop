package com.nerdyapps.balloonpop.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by mohamedkhafaga on 2/3/17.
 */

public class PixelHelper {
    public static int pixelsToDp(int px, Context context) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, px,
                context.getResources().getDisplayMetrics());
    }
}
