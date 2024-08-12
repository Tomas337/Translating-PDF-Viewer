package io.github.tomas337.translating_pdf_viewer.utils;

import java.util.HashMap;
import java.util.List;

public class Document {

    String name;
    int pageCount;
    HashMap<Integer, TextStyle> intToTextStyleMap;
    List<String> pagePaths;
    String pathOfThumbnail;

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
        this.pathOfThumbnail = thumbnailPath;
    }

    public String getName() {
        return name;
    }

    public int getPageCount() {
        return pageCount;
    }

    public HashMap<Integer, TextStyle> getIntToTextStyleMap() {
        return intToTextStyleMap;
    }

    public List<String> getPagePaths() {
        return pagePaths;
    }

    public String getPathOfThumbnail() {
        return pathOfThumbnail;
    }
}
