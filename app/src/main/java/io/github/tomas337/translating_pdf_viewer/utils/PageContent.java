package io.github.tomas337.translating_pdf_viewer.utils;

public class PageContent {

    String type;
    Float x;
    Float y;
    Float endY;

    public PageContent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getX() {
        return x;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getY() {
        return y;
    }

    public void setEndY(Float y) {
        endY = y;
    }

    public Float getEndY() {
        return endY;
    }
}
