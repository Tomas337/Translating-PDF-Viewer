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

    @Override
    public int hashCode()
    {
        int result = 17;
        result = 31 * result + Float.floatToIntBits(fontSize);
        result = 31 * result + fontWeight;
        result = 31 * result + (isItalic ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        TextStyle other = (TextStyle) o;
        if (this.fontSize != other.fontSize) {
            return false;
        }
        if (this.fontWeight != other.fontWeight) {
            return false;
        }
        if (this.isItalic != other.isItalic) {
            return false;
        }

        return true;
    }
}
