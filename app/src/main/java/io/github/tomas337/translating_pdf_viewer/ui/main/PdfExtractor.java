package io.github.tomas337.translating_pdf_viewer.ui.main;

import android.content.Context;
import android.net.Uri;

import com.tom_roush.pdfbox.io.MemoryUsageSetting;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.text.TextPosition;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PdfExtractor {

    private final Uri uri;
    private final Context context;

    public PdfExtractor(Context context, Uri uri) {
        this.uri = uri;
        this.context = context;
    }

    public void extractText() throws IOException {
        try (
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                PDDocument pdfDocument = PDDocument.load(inputStream,
                        MemoryUsageSetting.setupTempFileOnly())
                ) {
            CustomPDFTextStripper stripper = new CustomPDFTextStripper();
            int numberOfPages = pdfDocument.getNumberOfPages();
            numberOfPages = 3;  // for testing

            List<List<TextBlock>> pages = new ArrayList<>();

            for (int i = 0; i < numberOfPages; i++) {
                try (PDDocument singlePageDocument = new PDDocument()) {
                    singlePageDocument.addPage(pdfDocument.getPage(i));
                    List<TextBlock> text = stripper.getTextData(singlePageDocument);
                    pages.add(text);
                }
            }
        }
    }

    private class CustomPDFTextStripper extends PDFTextStripper {

        private List<TextBlock> textBlocks;
        private TextBlock curTextBlock;
        private StringBuilder curText;
        private String prevFont;
        private float prevFontSize;
        private List<TextStyle> textStyles = new ArrayList<>();

        public CustomPDFTextStripper() throws IOException {
            super();
        }

        /**
         * Write a Java string to the output stream.
         * Repurposed to also extract information about the text.
         *
         * @param text String representation of one line in the document.
         * @param textPositions Contains the line splitted into characters in TextPosition type.
         * @throws IOException If there is an error when writing the text or extracting information.
         */
        @Override
        protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
            StringBuilder builder = new StringBuilder();

            for (TextPosition position : textPositions) {
                if (curTextBlock.getX() == null) {
                    assert curTextBlock.getY() == null;
                    curTextBlock.setX(position.getX());
                    curTextBlock.setY(position.getY());
                }
                String baseFont = position.getFont().getName();
                String font = baseFont != null ? baseFont : prevFont;
                float fontSize = position.getFontSize();

                if (!Objects.equals(font, prevFont) || fontSize != prevFontSize) {
                    if (curText.length() != 0) {
                        curTextBlock.addText(curText.toString());

                        // TODO: search if style exists, if not, create it and add it to curTextBlock.styles
                        // TODO: check if curText.toString() is smaller than page width, if so, add curTextBlock to textBlocks
                    }

                    curText = new StringBuilder();
                    prevFont = font;
                    prevFontSize = fontSize;
                }
                String unicode = position.getUnicode();
                curText.append(unicode);
                builder.append(unicode);
            }
            super.writeString(builder.toString());
        }

        public List<TextBlock> getTextData(PDDocument document) throws IOException {
            textBlocks = new ArrayList<>();
            curTextBlock = new TextBlock();
            curText = new StringBuilder();
            getText(document);
            return textBlocks;
        }
    }

    public class TextStyle {
        float fontSize;
        String font;

        public TextStyle(float fontSize, String font) {
            this.fontSize = fontSize;
            this.font = font;
        }
    }

    public class TextBlock {
        Float x = null;
        Float y = null;
        List<String> texts = new ArrayList<>();
        List<Integer> styles = new ArrayList<>();

        public void addText(String text) {
            texts.add(text);
        }

        public void addStyle(int style) {
            styles.add(style);
        }

        public Float getX() {
            return x;
        }

        public void setX(Float x) {
            this.x = x;
        }

        public Float getY() {
            return y;
        }

        public void setY(Float y) {
            this.y = y;
        }
    }
}
