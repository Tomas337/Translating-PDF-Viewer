package io.github.tomas337.translating_pdf_viewer.ui.main;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.tom_roush.pdfbox.io.MemoryUsageSetting;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.text.TextPosition;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PdfExtractor {

    private final Uri uri;
    private final Context context;

    public PdfExtractor(Context context, Uri uri) {
        this.uri = uri;
        this.context = context;
    }

    public List<List<TextData>> extractText() throws IOException {
        try (
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                PDDocument pdfDocument = PDDocument.load(inputStream,
                        MemoryUsageSetting.setupTempFileOnly())
                ) {
            CustomPDFTextStripper stripper = new CustomPDFTextStripper();
            int numberOfPages = pdfDocument.getNumberOfPages();
            numberOfPages = 3;  // for testing

            List<List<TextData>> pages = new ArrayList<>();

            for (int i = 0; i < numberOfPages; i++) {
                try (PDDocument singlePageDocument = new PDDocument()) {
                    singlePageDocument.addPage(pdfDocument.getPage(i));
                    List<TextData> text = stripper.getTextData(singlePageDocument);
                    pages.add(text);
                }
            }
            return pages;
        }
    }

    private class CustomPDFTextStripper extends PDFTextStripper {

        private List<TextData> textDataList;
        private String prevFont = "";

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

                String baseFont = position.getFont().getName();
                String font = baseFont != null ? baseFont : prevFont;
                prevFont = font;

                float fontSize = position.getFontSize();
                float x = position.getX();
                float y = position.getY();
                textDataList.add(new TextData(x, y, fontSize, font, position.getUnicode()));

                builder.append(position.getUnicode());
            }

            super.writeString(builder.toString());
        }

        public List<TextData> getTextData(PDDocument document) throws IOException {
            textDataList = new ArrayList<>();
            getText(document);
            return textDataList;
        }
    }

    public class TextData {
        float x;
        float y;
        float fontSize;
        String font;
        String text;

        public TextData(float x, float y, float fontSize, String font, String text) {
            this.x = x;
            this.y = y;
            this.fontSize = fontSize;
            this.font = font;
            this.text = text;
        }
    }
}
