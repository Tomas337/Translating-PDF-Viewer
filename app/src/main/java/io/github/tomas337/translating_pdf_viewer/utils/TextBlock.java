package io.github.tomas337.translating_pdf_viewer.utils;

import java.util.ArrayList;
import java.util.List;

public class TextBlock extends PageContent {

    Float rotation = null;
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

    public Float getRotation() {
        return rotation;
    }

    public void setRotation(Float rotation) {
        this.rotation = rotation;
    }
}
