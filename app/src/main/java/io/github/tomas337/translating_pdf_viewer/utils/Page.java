package io.github.tomas337.translating_pdf_viewer.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Page {

    List<PageContent> orderedData = new ArrayList<>();

    public Page(List<TextBlock> textBlocks, List<Image> images) {
        int j = 0;
        int i = 0;

        // handle empty textBlocks or images
        if (textBlocks.isEmpty() && !images.isEmpty()) {
            orderedData = new ArrayList<>(images);
            return;
        } else if (images.isEmpty() && !textBlocks.isEmpty()) {
            orderedData = new ArrayList<>(textBlocks);
            return;
        } else if (images.isEmpty()) {
            return;
        }

        textBlocks.sort(Comparator.comparing(TextBlock::getY));
        images.sort(Comparator.comparingInt(Image::getY));

        while (i < textBlocks.size() && j < images.size()) {
            if (textBlocks.get(i).getY() < images.get(j).getY()) {
                orderedData.add(textBlocks.get(i));
                i++;
            } else {
                orderedData.add(images.get(j));
                j++;
            }
        }

        while (i != textBlocks.size()) {
            orderedData.add(textBlocks.get(i));
            i++;
        }
        while (j != images.size()) {
            orderedData.add(images.get(j));
            j++;
        }
    }

    public List<PageContent> getOrderedData() {
        return orderedData;
    }
}
