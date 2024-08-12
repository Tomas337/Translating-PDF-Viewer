package io.github.tomas337.translating_pdf_viewer.utils;

import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.font.PDFontDescriptor;

public class TextStyle {

    float fontSize;
    int fontWeight;
    boolean isItalic;

    public TextStyle(float fontSize, PDFont font) {
        this.fontSize = fontSize;
        PDFontDescriptor descriptor = font.getFontDescriptor();
        fontWeight = (int) descriptor.getFontWeight();
        isItalic = descriptor.isItalic();
    }

    public float getFontSize() {
        return fontSize;
    }

    public int getFontWeight() {
        return fontWeight;
    }

    public boolean isItalic() {
        return isItalic;
    }
}
