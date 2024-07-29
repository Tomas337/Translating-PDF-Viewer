package io.github.tomas337.translating_pdf_viewer.utils;

import com.tom_roush.pdfbox.pdmodel.font.PDFont;

public class TextStyle {

    float fontSize;
    PDFont font;

    public TextStyle(float fontSize, PDFont font) {
        this.fontSize = fontSize;
        this.font = font;
    }
}
