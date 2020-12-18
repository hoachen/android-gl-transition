package com.av.gltrans.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapDiskCache {

    private Context mContext;

    public BitmapDiskCache(Context context) {
        mContext = context;
    }

    public void set(String key, Bitmap bitmap) {
        File dir = new File(mContext.getCacheDir(), "bmp-cache");
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, String.format("bitmap-cache-%s.jpg", key));
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean contains(String key) {
        File dir = new File(mContext.getCacheDir(), "bmp-cache");
        File file = new File(dir, String.format("bitmap-cache-%s.jpg", key));
        return file.exists();
    }

    public Bitmap get(String key) {
        File dir = new File(mContext.getCacheDir(), "bmp-cache");
        if (contains(key)) {
            File file = new File(dir, String.format("bitmap-cache-%s.jpg", key));
            return BitmapFactory.decodeFile(file.getPath());
        }
        return null;
    }


    public void clear() {
        File dir = new File(mContext.getCacheDir(), "bmp-cache");
        if (dir.exists())
            deleteFolder(dir);
    }

    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { // some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }
}
