package com.av.gltrans.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.media.ExifInterface;

import java.io.IOException;

public class BitmapProcessor {


    private static final int MAX_IMAGE_WIDTH = 720;
    private static final int MAX_IMAGE_HEIGHT = 1280;

    public static Bitmap proceed(Bitmap srcBitmap, CropType cropType, int scaledWidth, int scaledHeight) {
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        Bitmap desBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(desBitmap);
        boolean fill = false;
        Rect dstRect = null;
        switch (cropType) {
            case FIT_CENTER:
                dstRect = fitCenterRect(srcWidth, srcHeight, scaledWidth, scaledHeight);
                fill = false;
                break;
            case FIT_START:
                dstRect = fitStartRect(srcWidth, srcHeight, scaledWidth, scaledHeight);
                fill = false;
                break;
            case FIT_END:
                dstRect = fitEndRect(srcWidth, srcHeight, scaledWidth, scaledHeight);
                fill = false;
                break;
            case FILL_CENTER:
                dstRect = fillCenterRect(srcWidth, srcHeight, scaledWidth, scaledHeight);
                fill = true;
                break;
            case FILL_START:
                dstRect = fillStartRect(srcWidth, srcHeight, scaledWidth, scaledHeight);
                fill = true;
                break;
            case FILL_END:
                dstRect = fillEndRect(srcWidth, srcHeight, scaledWidth, scaledHeight);
                fill = true;
                break;
            case ASPECT_FIT:
                fill = true;
                dstRect = aspectFitRect(srcWidth, srcHeight, scaledWidth, scaledHeight);
                break;
        }
        // 背景不用高斯模糊填充, 填充黑色
        if (!fill) {
            Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0.5f);
            ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
            bgPaint.setAlpha(150);
            bgPaint.setColorFilter(colorFilter);
            Bitmap bg = FastBlur.blur(srcBitmap, 14, false);
            canvas.drawBitmap(bg, null, backgroundFillRect(srcWidth, srcHeight,
                    scaledWidth, scaledHeight), bgPaint);
        }
        // 设置背景为黑色
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(Color.BLACK);
//        canvas.drawRect(new Rect(0, 0, scaledWidth, scaledHeight), paint);
        PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
        canvas.setDrawFilter(pfd);
        canvas.drawBitmap(srcBitmap, null, dstRect, null);
        return desBitmap;
    }

    private static Rect backgroundFillRect(int srcWidth, int srcHeight, int scaledWidth, int scaledHeight) {
        Rect rect = fillCenterRect(srcWidth, srcHeight, scaledWidth, scaledHeight);
        float scale = 0.1f;
        int d = (int) ((rect.right - rect.left) * scale);
        int h = (int) ((rect.bottom - rect.top) * scale);
        rect.left -= d;
        rect.right += d;
        rect.top -= h;
        rect.bottom += h;

        return rect;
    }

    private static Rect fillEndRect(int width, int height, int scaledWidth, int scaledHeight) {
        float ratio = width * 1.0f / height;
        int bottom = (int) (scaledWidth * 1.0f / ratio);
        int top = scaledHeight - bottom;
        return new Rect(0, top, scaledWidth, scaledHeight);
    }

    private static Rect fillStartRect(int width, int height, int scaledWidth, int scaledHeight) {
        float ratio = width * 1.0f / height;
        int bottom = (int) (scaledWidth / ratio);
        int top = 0;
        return new Rect(0, top, scaledWidth, bottom + top);
    }

    private static Rect fillCenterRect(int width, int height, int scaledWidth, int scaledHeight) {
        float ratio = width * 1.0f / height;
        int bottom = (int) (scaledWidth * 1.0f / ratio);
        int top = (int) ((scaledHeight - bottom) / 2f);
        return new Rect(0, top, scaledWidth, bottom + top);
    }

    private static Rect fitEndRect(int width, int height, int scaledWidth, int scaledHeight) {
        float ratio = width * 1.0f / height;
        int right = (int) (scaledHeight * ratio);
        int left = scaledWidth - right;
        return new Rect(left, 0, scaledWidth, scaledHeight);
    }

    private static Rect fitCenterRect(int width, int height, int scaledWidth, int scaledHeight) {
        float ratio = width * 1.0f / height;
        int right = (int) (scaledHeight * ratio);
        int left = (scaledWidth > right) ? (int) ((scaledWidth - right) / 2f) : (int) ((right - scaledWidth) / 2f);
        return new Rect(left, 0, right + left, scaledHeight);
    }

    private static Rect fitStartRect(int width, int height, int scaledWidth, int scaledHeight) {
        float ratio = width * 1.0f / height;
        int right = (int) (scaledHeight * ratio);
        return new Rect(0, 0, right, scaledHeight);
    }


    private static Rect aspectFitRect(int width, int height, int scaledWidth, int scaledHeight) {
        float ratio = width * 1.0f / height;
        float scaledRatio = scaledWidth * 1.0f / scaledHeight;
        float plus = ratio / scaledRatio - 1.0f;
        if (Math.abs(plus) <= 0.01f) {
            return new Rect(0, 0, scaledWidth, scaledHeight);
        }
        if (plus > 0.0f) {
            // 宽铺满,重新计算高
            int w = scaledWidth;
            int h = (int) (scaledWidth / ratio);
            int top = (scaledHeight - h ) / 2;
            return new Rect(0,  top, w, top + h );
        } else {
            // 高铺满，重新计算宽
            int h = scaledHeight;
            int w = (int) (h * ratio);
            int left = (scaledWidth - w )/ 2;
            return new Rect(left, 0, left + w, h);
        }
    }

    public enum CropType {
        FIT_CENTER,
        FIT_START,
        FIT_END,
        FILL_CENTER,
        FILL_START,
        FILL_END,
        ASPECT_FIT;

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
                case ASPECT_FIT:
                    return "aspect_fit";
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
                case "aspect_fit":
                    return ASPECT_FIT;
            }
            return FIT_CENTER;
        }
    }

    public static Bitmap loadBitmap(String filePath) throws IOException {
//        Bitmap b = BitmapFactory.decodeFile(filePath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int width = options.outWidth;
        int height = options.outHeight;
        int sampleSize = 1;
        if (width > MAX_IMAGE_WIDTH || height > MAX_IMAGE_HEIGHT) {
            final int heightRatio = Math.round((float) height / (float) MAX_IMAGE_HEIGHT);
            final int widthRatio = Math.round((float) width / (float) MAX_IMAGE_WIDTH);
            sampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        Bitmap b = BitmapFactory.decodeFile(filePath, options);
        ExifInterface exif = new ExifInterface(filePath);
        int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
        );
        int rotationInDegrees = exifToDegrees(orientation);
        Matrix matrix = new Matrix();
        if (rotationInDegrees != 0) {
            matrix.postScale(-1, 1);
            matrix.postRotate(rotationInDegrees+180);
        } else {
            matrix.postScale(-1, 1);
            matrix.postRotate(180);
        }

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
