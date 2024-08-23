package io.github.tomas337.translating_pdf_viewer.utils;

public class Image extends PageContent {

    String path;
    int width;
    int height;

    public Image(
            String path,
            float x,
            float y,
            float endY,
            int scaledWidth,
            int scaledHeight
    ) {
        super("image");
        super.setX(x);
        super.setY(y);
        super.setEndY(endY);
        this.path = path;
        this.width = scaledWidth;
        this.height = scaledHeight;
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
