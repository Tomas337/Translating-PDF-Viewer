package io.github.tomas337.translating_pdf_viewer.utils;

import java.util.ArrayList;
import java.util.List;

public class TextBlock extends PageContent {

    // TODO remove x if not necessary
    Float x = null;
    Float y = null;
    Float endY = null;
    List<String> texts = new ArrayList<>();
    List<Integer> styles = new ArrayList<>();

    public TextBlock() {
        super("text-block");
    }

    public void addText(String text) {
        texts.add(text);
    }

    public List<String> getTexts() {
        return texts;
    }

    public boolean isEmpty() {
        return texts.isEmpty();
    }

    public void addStyle(int style) {
        styles.add(style);
    }

    public List<Integer> getStyles() {
        return styles;
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
        this.endY = y;
    }

    public Float getEndY() {
        return endY;
    }

    public void updateEndY(Float y) {
        endY += y;
        }
}
