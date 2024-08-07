package io.github.tomas337.translating_pdf_viewer.utils;

import java.util.HashMap;
import java.util.List;

public class Document {

    public String name;
    public int pageCount;
    public HashMap<Integer, TextStyle> intToTextStyleMap;
    public List<String> pagePaths;
    public String thumbnailPath;

    public Document(
            String name,
            int pageCount,
            HashMap<Integer, TextStyle> intToTextStyleMap,
            List<String> pagePaths,
            String thumbnailPath
    ) {
        this.name = name;
        this.pageCount = pageCount;
        this.intToTextStyleMap = intToTextStyleMap;
        this.pagePaths = pagePaths;
        this.thumbnailPath = thumbnailPath;
    }
}
