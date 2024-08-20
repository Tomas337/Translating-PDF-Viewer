package io.github.tomas337.translating_pdf_viewer.utils;

import java.util.ArrayList;
import java.util.List;

public class Page {

    List<PageContent> orderedData = new ArrayList<>();

    public Page(List<TextBlock> textBlocks, List<Image> images) {
        int j = 0;
        int i = 0;

        // 8th page is the first one with regular text
        // handle empty textBlocks or images
        if (textBlocks.isEmpty() && !images.isEmpty()) {
            orderedData = new ArrayList<>(images);
            return;
        } else if (images.isEmpty()) {
            orderedData = new ArrayList<>(textBlocks);
            return;
        }

        // handle start of the page
        while (i == 0 && j < images.size()) {
            int sumOfHeights = orderedData.stream().mapToInt(o -> ((Image) o).getHeight()).sum();

            // TODO account for top padding
            if (textBlocks.get(i).getY() < (images.get(j).getHeight() + sumOfHeights)) {
                orderedData.add(textBlocks.get(i));
                i++;
            } else {
                orderedData.add(images.get(j));
                j++;
            }
        }

        // handle middle of the page
        while (i < textBlocks.size() && j < images.size()) {
            if ((textBlocks.get(i-1).getEndY() - textBlocks.get(i).getY()) < images.get(j).getHeight()) {
                orderedData.add(textBlocks.get(i));
                i++;
            } else {
                orderedData.add(images.get(j));
                j++;
            }
        }

        // handle end of the page
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
