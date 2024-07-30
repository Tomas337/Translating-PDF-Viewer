package io.github.tomas337.translating_pdf_viewer.utils;

import java.util.HashMap;
import java.util.List;

public class Document {

    public String name;
    public String language;
    public int maxPage;
    public HashMap<Integer, TextStyle> intToTextStyleMap;
    public List<String> pagePaths;
    public String thumbnailPath;

    public Document(
            String name,
            String language,
            int maxPage,
            HashMap<Integer, TextStyle> intToTextStyleMap,
            List<String> pagePaths,
            String thumbnailPath
    ) {
        this.name = name;
        this.language = language;
        this.maxPage = maxPage;
        this.intToTextStyleMap = intToTextStyleMap;
        this.pagePaths = pagePaths;
        this.thumbnailPath = thumbnailPath;
    }
}
