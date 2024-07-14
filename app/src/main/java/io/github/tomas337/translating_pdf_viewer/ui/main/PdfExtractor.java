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
import java.util.List;

public class PdfExtractor {

    private final Uri uri;
    private final Context context;
    private final CustomPDFTextStripper stripper = new CustomPDFTextStripper();

    public PdfExtractor(Context context, Uri uri) throws IOException {
        this.uri = uri;
        this.context = context;
    }

    public void getText() throws IOException {
        try (
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                PDDocument pdfDocument = PDDocument.load(inputStream,
                        MemoryUsageSetting.setupTempFileOnly())
                ) {
            int numberOfPages = pdfDocument.getNumberOfPages();

            for (int i = 0; i < numberOfPages; i++) {
                try (PDDocument singlePageDocument = new PDDocument()) {
                    singlePageDocument.addPage(pdfDocument.getPage(i));
                    String text = stripper.getText(singlePageDocument);
                    Log.d("text", text);
                }
            }
        }
    }

    private static class CustomPDFTextStripper extends PDFTextStripper {
        private String prevBaseFont = "";

        public CustomPDFTextStripper() throws IOException {
            super();
        }

        @Override
        protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
            StringBuilder builder = new StringBuilder();

            for (TextPosition position : textPositions) {
                String baseFont = position.getFont().getName();
                float fontSize = position.getFontSize();
                float x = position.getX();
                float y = position.getY();

                if (baseFont != null && !baseFont.equals(prevBaseFont)) {
                    builder.append('[').append(baseFont).append(']');
                    prevBaseFont = baseFont;
                }
                builder.append(position.getUnicode());
            }

            super.writeString(builder.toString());
        }

//        @Override
//        public String getText(PDDocument doc) throws IOException {
//            StringWriter outputStream = new StringWriter();
//            writeText(doc, outputStream);
//            return outputStream.toString();
//        }
    }
}
