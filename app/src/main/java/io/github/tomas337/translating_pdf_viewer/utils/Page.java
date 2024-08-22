package io.github.tomas337.translating_pdf_viewer.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Page {

    List<PageContent> orderedData = new ArrayList<>();

    public Page(List<TextBlock> textBlocks, List<Image> images) {
        orderedData.addAll(textBlocks);
        orderedData.addAll(images);
        orderedData.sort(Comparator.comparing(PageContent::getY));
    }

    public List<PageContent> getOrderedData() {
        return orderedData;
    }
}
