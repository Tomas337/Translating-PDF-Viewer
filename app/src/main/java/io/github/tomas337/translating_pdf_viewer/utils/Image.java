package io.github.tomas337.translating_pdf_viewer.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class Image extends PageContent {

    Bitmap image;
//    byte[] image;
    int x;
    int y;
    int width;
    int height;

    public Image(Bitmap image, int x, int y, int width, int height) {
        super("image");
        this.image = image;
//        this.image = bitmapToBuffer(image);
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

//    public byte[] getImage() {
//        return image;
//    }
    public Bitmap getImage() {
        return image;
    }

//    private static byte[] bitmapToBuffer(Bitmap bitmap) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        bitmap.recycle();
//        return byteArray;
//    }
}
