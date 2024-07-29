package io.github.tomas337.translating_pdf_viewer.data.utils;

import java.util.HashMap;
import java.util.List;

public class Document {

    public List<Page> pages;
    public HashMap<Integer, TextStyle> intToTextStyleMap;
    public String name;
    public String language;

    public Document(
            List<Page> pages,
            HashMap<Integer, TextStyle> intToTextStyleMap,
            String name,
            String language
    ) {
        this.pages = pages;
        this.intToTextStyleMap = intToTextStyleMap;
        this.name = name;
        this.language = language;
    }
}
