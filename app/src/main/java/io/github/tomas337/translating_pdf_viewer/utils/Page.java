package io.github.tomas337.translating_pdf_viewer.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Page {

    List<List<PageContent>> orderedData = new ArrayList<>();

    public Page(List<TextBlock> textBlocks, List<Image> images, float margin, float pageWidth) {
        List<PageContent> yOrderedData = new ArrayList<>();
        yOrderedData.addAll(textBlocks);
        yOrderedData.addAll(images);
        yOrderedData.sort(Comparator.comparing(PageContent::getY));

        int i = 0;
        while (i < yOrderedData.size()) {
            PageContent curObject = yOrderedData.get(i);
            List<PageContent> rowContent = new ArrayList<>();

            curObject.setX((curObject.getX() - margin) / (pageWidth - 2*margin));
            rowContent.add(curObject);

            boolean containsText = curObject.type.equals("text-block");

            for (int j = i+1; j < yOrderedData.size(); j++) {
                PageContent nextObject = yOrderedData.get(j);

                if (nextObject.type.equals("text-block") && containsText) {
                    break;
                }
                if ((curObject.endY >= nextObject.y && curObject.y <= nextObject.y) ||
                    (curObject.endY >= nextObject.endY && curObject.y <= nextObject.endY)
                ) {
                    if (nextObject.type.equals("text-block")) {
                        containsText = true;
                    }
                    nextObject.setX((nextObject.getX() - margin) / (pageWidth - 2*margin));
                    rowContent.add(nextObject);
                } else {
                    break;
                }
                i = j;
            }
            i += 1;

            rowContent.sort(Comparator.comparing(PageContent::getX));
            orderedData.add(rowContent);
        }
    }

    public List<List<PageContent>> getOrderedData() {
        return orderedData;
    }
}
