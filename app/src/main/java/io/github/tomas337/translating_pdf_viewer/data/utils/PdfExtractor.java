package io.github.tomas337.translating_pdf_viewer.data.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.io.MemoryUsageSetting;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDResources;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.graphics.PDXObject;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.tom_roush.pdfbox.rendering.PDFRenderer;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.text.TextPosition;
import com.tom_roush.pdfbox.text.TextPositionComparator;
import com.tom_roush.pdfbox.util.IterativeMergeSort;
import com.tom_roush.pdfbox.util.Matrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

import io.github.tomas337.translating_pdf_viewer.utils.Document;
import io.github.tomas337.translating_pdf_viewer.utils.Image;
import io.github.tomas337.translating_pdf_viewer.utils.Page;
import io.github.tomas337.translating_pdf_viewer.utils.TextBlock;
import io.github.tomas337.translating_pdf_viewer.utils.TextStyle;

public class PdfExtractor {

    private final Uri uri;
    private final Context context;
    private final Path path;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public PdfExtractor(Context context, Uri uri, Integer fileId) {
        this.uri = uri;
        this.context = context;
        String folderName = String.format(Locale.getDefault(), "fileId-%d", fileId);
        path = Paths.get(context.getFilesDir().getAbsolutePath(), folderName);
    }

    public Document extractAndSaveDocument() throws IOException {
        try (
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                PDDocument pdfDocument = PDDocument.load(inputStream,
                        MemoryUsageSetting.setupTempFileOnly())
                ) {
            Files.createDirectories(path);

            CustomPDFTextStripper stripper = new CustomPDFTextStripper();
            int numberOfPages = pdfDocument.getNumberOfPages();
            List<String> pagePaths = new ArrayList<>();

            for (int i = 0; i < numberOfPages; i++) {
                PDPage page = pdfDocument.getPage(i);
                List<TextBlock> textBlocks = stripper.getPageText(page);
                List<Image> images = getPageImages(page);

                Page pageData = new Page(textBlocks, images);
                String filepath = savePage(pageData, i);
                pagePaths.add(filepath);
            }

            HashMap<Integer, TextStyle> intToTextStyleMap = stripper.getIntToTextStyleMap();
            String title = pdfDocument.getDocumentInformation().getTitle();
            if (title == null) {
                title = "No title";
            }

            PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
            Bitmap thumbnail = pdfRenderer.renderImageWithDPI(0, 300);
            String thumbnailPath = saveThumbnail(thumbnail);

            return new Document(
                    title,
                    numberOfPages,
                    intToTextStyleMap,
                    pagePaths,
                    thumbnailPath
            );
        }
    }

    private String savePage(Page page, int pageIndex) throws IOException {
        String filename = String.format(Locale.getDefault(), "page-%d.json", pageIndex);
        String filepath = path.resolve(filename).toString();
        String pageJson = gson.toJson(page);
        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write(pageJson);
        }
        return filepath;
    }

    private String saveThumbnail(Bitmap thumbnail) throws IOException {
        String filepath = path.resolve("thumbnail.jpeg").toString();
        try (FileOutputStream fos = new FileOutputStream(filepath)) {
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        }
        return filepath;
    }

    public static void deleteDirs(Context context, Integer fileId) throws IOException {
        String folderName = String.format(Locale.getDefault(), "fileId-%d", fileId);
        Path path = Paths.get(context.getFilesDir().getAbsolutePath(), folderName);
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    public void deleteDirs() throws IOException {
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    private static class CustomPDFTextStripper extends PDFTextStripper {

        private List<TextBlock> textBlocks;
        private TextBlock curTextBlock;
        private StringBuilder curText;
        private PDFont prevFont;
        private float prevFontSize;
        private int curMaxStyleIndex;
        private Integer prevEndPadding = null;
        private final HashMap<TextStyle, Integer> textStyleToIntMap = new HashMap<>();
        private final HashMap<Integer, TextStyle> intToTextStyleMap = new HashMap<>();

        public CustomPDFTextStripper() throws IOException {
            super();
        }

        /**
         * Write a Java string to the output stream.
         * Repurposed to also extract information about the text.
         *
         * @param text String representation of one line in the document.
         * @param textPositions Contains the line splitted into characters in TextPosition type.
         *                      The list is going to contain null elements in the case that the
         *                      document doesn't represent separators as ASCII.
         * @throws IOException If there is an error when writing the text or extracting information.
         */
        @Override
        protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
            StringBuilder builder = new StringBuilder();

            // Join parts of a word or separate sentences
            if (curText.length() != 0) {
                int lastIndex = curText.length() - 1;
                char lastChar = curText.charAt(lastIndex);

                if (lastChar == '-') {
                    curText.deleteCharAt(lastIndex);
                } else if (lastChar != ' ') {
                    curText.append(" ");
                }
            }

            if (curTextBlock.getX() == null) {
                assert curTextBlock.getY() == null;
                assert curTextBlock.getEndY() == null;
                assert curTextBlock.getRotation() == null;
                curTextBlock.setX(textPositions.get(0).getX());
                curTextBlock.setY(textPositions.get(0).getY());
                curTextBlock.setRotation(getAngle(textPositions.get(0)));
            }
            float endY = textPositions.get(0).getPageHeight() - textPositions.get(0).getEndY();
            curTextBlock.setEndY(endY);

            for (TextPosition position : textPositions) {

                // Handle non ASCII separator
                if (position == null) {
                    String separator = getWordSeparator();
                    curText.append(separator);
                    builder.append(separator);
                    continue;
                }

                PDFont baseFont = position.getFont();
                PDFont font = baseFont != null ? baseFont : prevFont;
                float fontSize = position.getFontSize();

                // Handle style change
                if (!Objects.deepEquals(font, prevFont) ||
                    fontSize != prevFontSize ||
                    (curTextBlock.isEmpty() && curText.length() == 0)
                ) {
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
            float curPageWidth = textPositions.get(0).getPageWidth();
            float curLineEndX = textPositions.get(textPositions.size() - 1).getEndX();
            int curEndPadding = (int) (curPageWidth - curLineEndX);

            if (prevEndPadding != null && curEndPadding != prevEndPadding) {
                curTextBlock.addText(curText.toString());
                textBlocks.add(curTextBlock);
                curText = new StringBuilder();
                curTextBlock = new TextBlock();
                prevEndPadding = null;
            } else {
                prevEndPadding = curEndPadding;
            }

            super.writeString(builder.toString());
        }

        private static float getAngle(TextPosition text)
        {
            Matrix m = text.getTextMatrix().clone();
            m.concatenate(text.getFont().getFontMatrix());
            return (float) -Math.round(Math.toDegrees(Math.atan2(m.getShearY(), m.getScaleY())));
        }

        /**
         * Handles the case when page ends with a line of maximum length.
          */
        private void onPageEnd() {
            if (curText.length() != 0) {
                curTextBlock.addText(curText.toString());
                textBlocks.add(curTextBlock);
            }
        }

        public List<TextBlock> getPageText(PDPage page) throws IOException {
            try (PDDocument document = new PDDocument()) {
                document.addPage(page);
                textBlocks = new ArrayList<>();
                curTextBlock = new TextBlock();
                curText = new StringBuilder();
                getText(document);
                onPageEnd();
            }
            return textBlocks;
        }

        public HashMap<Integer, TextStyle> getIntToTextStyleMap() {
            return intToTextStyleMap;
        }
    }

    private static List<Image> getPageImages(PDPage page) throws IOException {
        PDResources resources = page.getResources();
        List<Image> images = new ArrayList<>();

        for (COSName xObjectName : resources.getXObjectNames()) {
            PDXObject xObject = resources.getXObject(xObjectName);

            if (xObject instanceof PDImageXObject) {
                Bitmap imageBitmap = ((PDImageXObject) xObject).getImage();

                int height = ((PDImageXObject) xObject).getHeight();
                Image image = new Image(imageBitmap, height);
                images.add(image);
            }
        }
        return images;
    }
}
