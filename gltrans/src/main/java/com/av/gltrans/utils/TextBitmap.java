package com.av.gltrans.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import androidx.annotation.RequiresApi;

public class TextBitmap {

    public static Bitmap text2Bitmap(Context context, String text) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Bayon-Regular.ttf");
        int margin = 20;
        int w = 720;
        int h = 405;
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        TextPaint textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(30f);
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(typeface);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            drawMultilineText(canvas, text, textPaint, 600, 60f, 250f, 0, text.length());
        }
        return bitmap;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public static void drawMultilineText(Canvas canvas, CharSequence text,
                                  TextPaint textPaint,
                                  int width,
                                  float x,
                                  float y,
                                  int start,
                                  int end
                                  ) {
        StaticLayout staticLayout = new StaticLayout(text, start, end, textPaint, width,
                Layout.Alignment.ALIGN_CENTER,
                1.0f, 0.0f,false);
        canvas.save();
        canvas.translate(x, y);
        staticLayout.draw(canvas);
        canvas.restore();
    }
}
