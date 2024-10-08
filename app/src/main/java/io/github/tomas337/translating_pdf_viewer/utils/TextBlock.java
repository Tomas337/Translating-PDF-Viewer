package io.github.tomas337.translating_pdf_viewer.utils;

import java.util.ArrayList;
import java.util.List;

public class TextBlock extends PageContent {

    Float rotation = null;
    List<String> texts = new ArrayList<>();
    List<Integer> styles = new ArrayList<>();
    String listPrefix = null;
    String textAlign = null;

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

    public String getListPrefix() {
        return listPrefix;
    }

    public void setListPrefix(String prefix) {
        listPrefix = prefix;
    }

    public boolean isInitialized() {
        return y != null && x != null && rotation != null && endY != null;
    }

    public String getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(String textAlign) {
        this.textAlign = textAlign;
    }
}
