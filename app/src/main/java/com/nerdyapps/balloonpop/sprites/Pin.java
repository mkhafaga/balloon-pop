package com.nerdyapps.balloonpop.sprites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.graphics.drawable.VectorDrawableCompat;

import com.nerdyapps.balloonpop.R;
import com.nerdyapps.balloonpop.utils.PixelHelper;

/**
 * Created by mohamedkhafaga on 2/11/17.
 */

public class Pin {
    private int x;
    private int y;
    private Bitmap bitmap;
    private Paint paint;
    private int index;

    public Pin(Context context, int index) {
        paint = new Paint();
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.hearty);
        bitmap = Bitmap.createScaledBitmap(bitmap, PixelHelper.pixelsToDp(bitmap.getWidth(), context), PixelHelper.pixelsToDp(bitmap.getHeight(), context), true);
        x = 5 + index *bitmap.getWidth();
        y =5;
    }

    public void draw(Canvas canvas) {
        if (canvas != null)
            canvas.drawBitmap(bitmap, x, y, paint);
    }
}
