package com.av.gltrans.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;

import java.io.IOException;

public class BitmapProcessor {

    private int width;
    private int height;

    private int scaledWidth;
    private int scaledHeight;
    private CropType cropType = CropType.FIT_CENTER;

    private Bitmap source;

    public BitmapProcessor(Bitmap bitmap) {
        this.source = bitmap;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        this.scaledWidth = width;
        this.scaledHeight = height;
    }

    public void cropType(CropType cropType) {
        this.cropType = cropType;
    }

    public void crop(int width, int height) {
        this.scaledWidth = width;
        this.scaledHeight = height;
    }

    public Bitmap proceed() {
        return proceedSync();
    }


    public Bitmap proceedSync() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        Bitmap bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        boolean fill = false;
        Rect dstRect = null;
        switch (cropType) {
            case FIT_CENTER:
                dstRect = fitCenterRect();
                fill = false;
                break;
            case FIT_START:
                dstRect = fitStartRect();
                fill = false;
                break;
            case FIT_END:
                dstRect = fitEndRect();
                fill = false;
                break;
            case FILL_CENTER:
                dstRect = fillCenterRect();
                fill = true;
                break;
            case FILL_START:
                dstRect = fillStartRect();
                fill = true;
                break;
            case FILL_END:
                dstRect = fillEndRect();
                fill = true;
                break;

        }

        if (!fill) {
            Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0.5f);
            ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
            bgPaint.setAlpha(150);
            bgPaint.setColorFilter(colorFilter);
            Bitmap bg = FastBlur.blur(source, 14, false);
            canvas.drawBitmap(bg, null, backgroundFillRect(), bgPaint);
        }
        canvas.drawBitmap(source, null, dstRect, null);
        return bitmap;
    }

    private Rect backgroundFillRect() {
        Rect rect = fillCenterRect();
        float scale = 0.1f;
        int d = (int) ((rect.right - rect.left) * scale);
        int h = (int) ((rect.bottom - rect.top) * scale);
        rect.left -= d;
        rect.right += d;
        rect.top -= h;
        rect.bottom += h;

        return rect;
    }

    private Rect fillEndRect() {
        float ratio = width * 1.0f / height;
        int bottom = (int) (scaledWidth * 1.0f / ratio);
        int top = scaledHeight - bottom;
        return new Rect(0, top, scaledWidth, scaledHeight);
    }

    private Rect fillStartRect() {
        float ratio = width * 1.0f / height;
        int bottom = (int) (scaledWidth / ratio);
        int top = 0;
        return new Rect(0, top, scaledWidth, bottom + top);
    }

    private Rect fillCenterRect() {
        float ratio = width * 1.0f / height;
        int bottom = (int) (scaledWidth * .10f / ratio);
        int top = (int) ((scaledHeight - bottom) / 2f);
        return new Rect(0, top, scaledWidth, bottom + top);
    }

    private Rect fitEndRect() {
        float ratio = width * 1.0f / height;
        int right = (int) (scaledHeight * ratio);
        int left = scaledWidth - right;
        return new Rect(left, 0, scaledWidth, scaledHeight);
    }

    private Rect fitCenterRect() {
        float ratio = width * 1.0f / height;
        int right = (int) (scaledHeight * ratio);
        int left = (scaledWidth > right) ? (int) ((scaledWidth - right) / 2f) : (int) ((right - scaledWidth) / 2f);
        return new Rect(left, 0, right + left, scaledHeight);
    }

    private Rect fitStartRect() {
        float ratio = width * 1.0f / height;
        int right = (int) (scaledHeight * ratio);
        return new Rect(0, 0, right, scaledHeight);
    }


    public enum CropType {
        FIT_CENTER,
        FIT_START,
        FIT_END,
        FILL_CENTER,
        FILL_START,
        FILL_END;

        public String key() {
            switch (this) {
                case FIT_CENTER:
                    return "fit-center";
                case FIT_START:
                    return "fit-start";
                case FIT_END:
                    return "fit-end";
                case FILL_CENTER:
                    return "fill-center";
                case FILL_START:
                    return "fill-start";
                case FILL_END:
                    return "fill-end";
            }
            return "";
        }

        public static CropType fromKey(String key) {
            switch (key) {
                case "fit-center":
                    return FIT_CENTER;
                case "fit-start":
                    return FIT_START;
                case "fit-end":
                    return FIT_END;
                case "fill-center":
                    return FILL_CENTER;
                case "fill-start":
                    return FILL_START;
                case "fill-end":
                    return FILL_END;
            }
            return FIT_CENTER;
        }
    }

    public static Bitmap loadSync(String filePath) throws IOException {
        Bitmap b = BitmapFactory.decodeFile(filePath);
        ExifInterface exif = new ExifInterface(filePath);
        int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
        );
        int rotationInDegrees = exifToDegrees(orientation);
        Matrix matrix = new Matrix();
        if (rotationInDegrees != 0)
            matrix.postRotate(rotationInDegrees);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
    }


    private static int exifToDegrees(int orientation) {
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
        }
        return 0;
    }
}
