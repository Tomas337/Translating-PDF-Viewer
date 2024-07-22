package io.github.tomas337.translating_pdf_viewer.data.utils;

import java.util.ArrayList;
import java.util.List;

public class TextBlock {

    // TODO remove x if not necessary
    Float x = null;
    Float y = null;
    Float endY = null;
    List<String> texts = new ArrayList<>();
    List<Integer> styles = new ArrayList<>();

    public void addText(String text) {
        texts.add(text);
    }

    public boolean isEmpty() {
        return texts.isEmpty();
    }

    public void addStyle(int style) {
        styles.add(style);
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return this.y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getEndY() {
        return endY;
    }

    public void setEndY(Float y) {
        endY = y;
        }
}
