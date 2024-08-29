package io.github.tomas337.translating_pdf_viewer.utils;

import java.util.Arrays;
import java.util.List;

public class Rectangle extends PageContent {

    float[] color;
    List<Line> lines;

    public Rectangle() {
        super("rect");
    }

    public void addLine(Line line) {
        lines.add(line);
    }

}
