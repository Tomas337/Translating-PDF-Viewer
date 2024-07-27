package io.github.tomas337.translating_pdf_viewer.domain.utils;

import android.graphics.Bitmap;

public class Image {

    Bitmap image;
    int height;

    public Image(Bitmap image, int height) {
        this.image = image;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }
}
