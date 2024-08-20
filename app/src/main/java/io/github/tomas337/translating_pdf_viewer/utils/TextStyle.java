package io.github.tomas337.translating_pdf_viewer.utils;

import android.util.Log;

import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.font.PDFontDescriptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextStyle {

    float fontSize;
    int fontWeight;
    boolean isItalic;

    public TextStyle(float fontSize, PDFont font) {
        this.fontSize = fontSize;
        PDFontDescriptor descriptor = font.getFontDescriptor();
        String fontName = font.getName();

        Log.d("font", fontName);
//        Log.d("font size", String.valueOf(fontSize));

        fontWeight = (int) descriptor.getFontWeight();
        if (fontWeight == 0) {
            if (Pattern.matches("(i?).*(Light|Lt|Thin).*", fontName)) {
                fontWeight = 300;
            } else if (Pattern.matches("(i?).*(Medium|Medi).*", fontName)) {
                fontWeight = 500;
            } else if (Pattern.matches("(i?).*(Semibold).*", fontName)) {
                fontWeight = 600;
            } else if (Pattern.matches("(i?).*(Bold|Bd).*", fontName)) {
                fontWeight = 700;
            } else if (Pattern.matches("(i?).*(Black|Heavy|((-|\\s).*H)).*", fontName)) {
                fontWeight = 900;
            } else {
                fontWeight = 400;
            }
        }
        if (Pattern.matches("(i?).*(Italic|Ital|((-|\\s).*It)|Oblique|Obl).*", fontName)) {
            isItalic = true;
        } else {
            isItalic = descriptor.isItalic();
        }
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
