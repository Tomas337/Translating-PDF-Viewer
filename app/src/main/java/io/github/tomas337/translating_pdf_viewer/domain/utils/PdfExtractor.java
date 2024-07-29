package io.github.tomas337.translating_pdf_viewer.domain.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.io.MemoryUsageSetting;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDResources;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
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

import io.github.tomas337.translating_pdf_viewer.data.utils.Document;
import io.github.tomas337.translating_pdf_viewer.data.utils.Image;
import io.github.tomas337.translating_pdf_viewer.data.utils.Page;
import io.github.tomas337.translating_pdf_viewer.data.utils.TextBlock;
import io.github.tomas337.translating_pdf_viewer.data.utils.TextStyle;

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
            List<Page> pages = new ArrayList<>();

            // for debugging
            numberOfPages = 3;

            //for (PDPage page : pdfDocument.getPages()) {
            for (int i = 0; i < numberOfPages; i++) {
                PDPage page = pdfDocument.getPage(i);
                List<TextBlock> textBlocks = stripper.getPageText(page);
                List<Image> images = getPageImages(page);

                Page pageData = new Page(textBlocks, images);
                pages.add(pageData);
            }

            HashMap<Integer, TextStyle> intToTextStyleMap = stripper.getIntToTextStyleMap();
            String title = pdfDocument.getDocumentInformation().getTitle();
            String language = pdfDocument.getDocumentCatalog().getLanguage();

            return new Document(pages, intToTextStyleMap, title, language);
        }
    }

    private class CustomPDFTextStripper extends PDFTextStripper {

        private List<TextBlock> textBlocks;
        private TextBlock curTextBlock;
        private StringBuilder curText;
        private PDFont prevFont;
        private float prevFontSize;
        private float maxLineWidth;
        private int curMaxStyleIndex;
        private HashMap<TextStyle, Integer> textStyleToIntMap = new HashMap<>();
        private HashMap<Integer, TextStyle> intToTextStyleMap = new HashMap<>();

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
            int lineWidth = textPositions.size();

            // Handle maxLineWidth update
            if (lineWidth > maxLineWidth) {
                maxLineWidth = lineWidth;
                if (!curTextBlock.isEmpty()) {
                    textBlocks.add(curTextBlock);
                    curTextBlock = new TextBlock();
                }
            }

            if (curTextBlock.getX() == null) {
                assert curTextBlock.getY() == null;
                assert curTextBlock.getEndY() == null;
                curTextBlock.setX(textPositions.get(0).getX());
                curTextBlock.setY(textPositions.get(0).getY());
            }
            curTextBlock.updateEndY(textPositions.get(0).getHeight());

            for (TextPosition position : textPositions) {
                PDFont baseFont = position.getFont();
                PDFont font = baseFont != null ? baseFont : prevFont;
                float fontSize = position.getFontSize();

                // Handle style change
                if (!Objects.equals(font, prevFont) || fontSize != prevFontSize) {
                    if (curText.length() != 0) {
                        curTextBlock.addText(curText.toString());
                        curText = new StringBuilder();
                    }

                    TextStyle style = new TextStyle(fontSize, font);

                    if (textStyleToIntMap.containsKey(style)) {
                        curTextBlock.addStyle(textStyleToIntMap.get(style));
                    } else {
                        textStyleToIntMap.put(style, curMaxStyleIndex);
                        intToTextStyleMap.put(curMaxStyleIndex, style);
                        curTextBlock.addStyle(curMaxStyleIndex);
                        curMaxStyleIndex++;
                    }
                    prevFont = font;
                    prevFontSize = fontSize;
                }

                String unicode = position.getUnicode();
                curText.append(unicode);
                builder.append(unicode);
            }

            // Handle end of block
            if (lineWidth < maxLineWidth) {
                curTextBlock.addText(curText.toString());
                textBlocks.add(curTextBlock);
                curText = new StringBuilder();
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

        public HashMap<TextStyle, Integer> getTextStyleToIntMap() {
            return textStyleToIntMap;
        }

        public HashMap<Integer, TextStyle> getIntToTextStyleMap() {
            return intToTextStyleMap;
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
}
