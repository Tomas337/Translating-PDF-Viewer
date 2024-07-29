package io.github.tomas337.translating_pdf_viewer.utils;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.List;

public class Document {

    public List<Page> pages;
    public HashMap<Integer, TextStyle> intToTextStyleMap;
    public String name;
    public String language;
    public Bitmap thumbnail;

    public Document(
            List<Page> pages,
            HashMap<Integer, TextStyle> intToTextStyleMap,
            String name,
            String language,
            Bitmap thumbnail
    ) {
        this.pages = pages;
        this.intToTextStyleMap = intToTextStyleMap;
        this.name = name;
        this.language = language;
        this.thumbnail = thumbnail;
    }
}
