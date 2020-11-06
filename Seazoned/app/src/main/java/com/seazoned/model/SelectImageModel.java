package com.seazoned.model;

import android.graphics.Bitmap;

/**
 * Created by root on 16/3/18.
 */

public class SelectImageModel {
    private String path;
    private Bitmap imageBitmap;

    public SelectImageModel(String path, Bitmap imageBitmap) {
        this.path = path;
        this.imageBitmap = imageBitmap;
    }

    public String getPath() {
        return path;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }
}
