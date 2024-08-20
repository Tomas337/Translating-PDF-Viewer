package io.github.tomas337.translating_pdf_viewer.utils;

public class Image extends PageContent {

    String path;
    int x;
    int y;
    int width;
    int height;

    public Image(String path, int x, int y, int width, int height) {
        super("image");
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
