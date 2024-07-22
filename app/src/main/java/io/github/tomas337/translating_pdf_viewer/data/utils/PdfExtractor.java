package io.github.tomas337.translating_pdf_viewer.data.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.io.MemoryUsageSetting;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDResources;
import com.tom_roush.pdfbox.pdmodel.graphics.PDXObject;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.text.TextPosition;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PdfExtractor {

    private final Uri uri;
    private final Context context;

    public PdfExtractor(Context context, Uri uri) {
        this.uri = uri;
        this.context = context;
    }

    public Document extractDocument() throws IOException {
        try (
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                PDDocument pdfDocument = PDDocument.load(inputStream,
                        MemoryUsageSetting.setupTempFileOnly())
                ) {
            CustomPDFTextStripper stripper = new CustomPDFTextStripper();
            int numberOfPages = pdfDocument.getNumberOfPages();
            numberOfPages = 3;  // for testing

            List<Page> pages = new ArrayList<>();

            //for (PDPage page : pdfDocument.getPages()) {
            for (int i = 0; i < numberOfPages; i++) {
                PDPage page = pdfDocument.getPage(i);
                List<TextBlock> textBlocks = stripper.getPageText(page);
                List<Image> images = getPageImages(page);

                Page pageData = new Page(textBlocks, images);
                pages.add(pageData);
            }

            //TODO implement initialization
            return new Document();
        }
    }

    private class CustomPDFTextStripper extends PDFTextStripper {

        private List<TextBlock> textBlocks;
        private TextBlock curTextBlock;
        private StringBuilder curText;
        private String prevFont;
        private float prevFontSize;
        private float maxPageWidth;
        private int curMaxStyleIndex;
        private HashMap<TextStyle, Integer> textStyleToInt = new HashMap<>();
        private HashMap<Integer, TextStyle> intToTextStyle = new HashMap<>();

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
            float pageWidth = textPositions.size();

            // Handle maxPageWidth update
            if (pageWidth > maxPageWidth) {
                maxPageWidth = pageWidth;
                if (!curTextBlock.isEmpty()) {
                    textBlocks.add(curTextBlock);
                    curTextBlock = new TextBlock();
                }
            }

            for (TextPosition position : textPositions) {
                if (curTextBlock.getX() == null) {
                    assert curTextBlock.getY() == null;
                    assert curTextBlock.getEndY() == null;
                    curTextBlock.setX(position.getX());
                    curTextBlock.setY(position.getY());
                    curTextBlock.setEndY(position.getEndY());
                }

                String baseFont = position.getFont().getName();
                String font = baseFont != null ? baseFont : prevFont;
                float fontSize = position.getFontSize();

                // Handle style change
                if (!Objects.equals(font, prevFont) || fontSize != prevFontSize) {
                    if (curText.length() != 0) {
                        curTextBlock.addText(curText.toString());
                        curText = new StringBuilder();

                        TextStyle style = new TextStyle(fontSize, font);

                        if (textStyleToInt.containsKey(style)) {
                            curTextBlock.addStyle(textStyleToInt.get(style));
                        } else {
                            textStyleToInt.put(style, curMaxStyleIndex);
                            intToTextStyle.put(curMaxStyleIndex, style);
                            curTextBlock.addStyle(curMaxStyleIndex);
                            curMaxStyleIndex++;
                        }
                    }
                    prevFont = font;
                    prevFontSize = fontSize;
                }

                String unicode = position.getUnicode();
                curText.append(unicode);
                builder.append(unicode);
            }

            // Handle end of block
            if (pageWidth < maxPageWidth) {
                curTextBlock.addText(curText.toString());
                curText = new StringBuilder();
                textBlocks.add(curTextBlock);
                curTextBlock = new TextBlock();
            }

            super.writeString(builder.toString());
        }

        public List<TextBlock> getPageText(PDPage page) throws IOException {
            try (PDDocument document = new PDDocument()) {
                document.addPage(page);
                textBlocks = new ArrayList<>();
                curTextBlock = new TextBlock();
                curText = new StringBuilder();
                getText(document);
            }
            return textBlocks;
        }
    }

    private List<Image> getPageImages(PDPage page) throws IOException {
        PDResources resources = page.getResources();
        List<Image> images = new ArrayList<>();

        for (COSName xObjectName : resources.getXObjectNames()) {
            PDXObject xObject = resources.getXObject(xObjectName);

            if (xObject instanceof PDImageXObject) {
                Bitmap imageBitMap = ((PDImageXObject) xObject).getImage();
                int height = ((PDImageXObject) xObject).getHeight();
                Image image = new Image(imageBitMap, height);
                images.add(image);
            }
        }
        return images;
    }

    private void getFonts(PDDocument document) {


    }
}