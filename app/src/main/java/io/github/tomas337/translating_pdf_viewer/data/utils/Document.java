package io.github.tomas337.translating_pdf_viewer.data.utils;

import com.tom_roush.pdfbox.pdmodel.font.PDFont;

import java.util.HashMap;
import java.util.List;

public class Document {

    public List<Page> pages;
    public HashMap<Integer, TextStyle> intToTextStyleMap;
    public String name;

    public Document(List<Page> pages, HashMap<Integer, TextStyle> intToTextStyleMap, String name) {
        this.pages = pages;
        this.intToTextStyleMap = intToTextStyleMap;
        this.name = name;
    }
}
