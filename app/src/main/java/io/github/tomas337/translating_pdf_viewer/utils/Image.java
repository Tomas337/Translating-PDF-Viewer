package io.github.tomas337.translating_pdf_viewer.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class Image extends PageContent {

//    Bitmap image;
    String path;
    int x;
    int y;
    int width;
    int height;

    public Image(String path, int x, int y, int width, int height) {
        super("image");
//        this.image = image;
        this.path = path;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getPath() {
        return path;
    }
}
