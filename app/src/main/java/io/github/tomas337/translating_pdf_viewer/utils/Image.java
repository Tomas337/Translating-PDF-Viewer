package io.github.tomas337.translating_pdf_viewer.utils;

import android.graphics.Bitmap;

public class Image extends PageContent {

    Bitmap image;
    int height;

    public Image(Bitmap image, int height) {
        super("image");
        this.image = image;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public Bitmap getImage() {
        return image;
    }
}
