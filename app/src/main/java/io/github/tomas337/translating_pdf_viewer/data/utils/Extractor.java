/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.tomas337.translating_pdf_viewer.data.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.tom_roush.pdfbox.contentstream.operator.DrawObject;
import com.tom_roush.pdfbox.contentstream.operator.Operator;
import com.tom_roush.pdfbox.contentstream.operator.state.Concatenate;
import com.tom_roush.pdfbox.contentstream.operator.state.Restore;
import com.tom_roush.pdfbox.contentstream.operator.state.Save;
import com.tom_roush.pdfbox.contentstream.operator.state.SetGraphicsStateParameters;
import com.tom_roush.pdfbox.contentstream.operator.state.SetMatrix;
import com.tom_roush.pdfbox.cos.COSBase;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.graphics.PDXObject;
import com.tom_roush.pdfbox.pdmodel.graphics.form.PDFormXObject;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.text.TextPosition;
import com.tom_roush.pdfbox.util.Matrix;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.tomas337.translating_pdf_viewer.utils.Image;
import io.github.tomas337.translating_pdf_viewer.utils.Page;
import io.github.tomas337.translating_pdf_viewer.utils.TextBlock;
import io.github.tomas337.translating_pdf_viewer.utils.TextStyle;

/**
 * This class takes a PDPage and extracts the text and images into Page object.
 */
public class Extractor extends PDFTextStripper {

    private Float margin = null;

    // Variables for text extraction.
    private final float MAX_LINE_SPACE = 3f;
    private List<TextBlock> textBlocks;
    private TextBlock curTextBlock;
    private StringBuilder curText;
    private PDFont prevFont;
    private float prevFontSize;
    private int curMaxStyleIndex;
    private Integer prevEndPadding = null;
    private final HashMap<TextStyle, Integer> textStyleToIntMap = new HashMap<>();
    private final HashMap<Integer, TextStyle> intToTextStyleMap = new HashMap<>();

    // Variables for image extraction.
    private final int dpi;
    private final Path path;
    private List<Image> images;

    /**
     * Default constructor.
     * <p>
     * Based on <a href="https://svn.apache.org/repos/asf/pdfbox/tags/2.0.3/examples/src/main/java/org/apache/pdfbox/examples/util/PrintImageLocations.java">PrintImageLocations.</a>
     *
     * @throws IOException If there is an error loading text stripper properties.
     */
    public Extractor(int dpi, Path path) throws IOException {
        super();
        this.dpi = dpi;
        this.path = path;

        // Initialize operators for image extraction
        addOperator(new Concatenate());
        addOperator(new DrawObject());
        addOperator(new SetGraphicsStateParameters());
        addOperator(new Save());
        addOperator(new Restore());
        addOperator(new SetMatrix());
    }

    /**
     * Write a Java string to the output stream.
     * This implementation also extracts information about the text divided into text blocks
     * containing texts, styles and information about the positioning.
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
        TextPosition firstPosition = textPositions.get(0);

        if (margin == null || firstPosition.getX() < margin) {
            margin = firstPosition.getX();
        }

        int curPageWidth = (int) firstPosition.getPageWidth();
        int curLineEndX = Math.round(textPositions.get(textPositions.size() - 1).getEndX());
        int curEndPadding = curPageWidth - curLineEndX;

//        Log.d("line", text);

        float lineSpacePx = curTextBlock.isInitialized()
                ? firstPosition.getY() - curTextBlock.getEndY()
                : 0;
        float lineSpace = lineSpacePx / firstPosition.getHeight();

        // Block ends when the current line isn't at the start of a block
        // and is longer than the previous one
        // or the line spacing is larger than MAX_LINE_SPACE.
        if (prevEndPadding != null &&
            (curEndPadding < prevEndPadding ||
             lineSpace > MAX_LINE_SPACE)
        ) {
            onEndOfBlock();
        }

        // Handle bullet prefix.
        int firstIndex = 0;
        Pattern bulletPattern = Pattern.compile("^([•◦▪]|([0-9]{1,2}[).])|([a-zA-Z][).]))(?= )");
        Matcher bulletMatcher = bulletPattern.matcher(text);
        if (bulletMatcher.find()) {
            if (!curTextBlock.isEmpty()) {
                onEndOfBlock();
            }
            String bulletString = bulletMatcher.group();
            firstIndex = bulletString.length() + 1;
            firstPosition = textPositions.get(firstIndex);
            assert firstPosition != null;
            curTextBlock.setListPrefix(bulletString + "\t\t");
            curTextBlock.setX(firstPosition.getX());
        }

        // Join parts of a word or separate sentences.
        if (curText.length() != 0) {
            int lastIndex = curText.length() - 1;
            char lastChar = curText.charAt(lastIndex);

            // hyphen-minus and hyphen character.
            if (lastChar == '-' || lastChar == '‐') {
                curText.deleteCharAt(lastIndex);
            } else if (lastChar != ' ') {
                curText.append(" ");
            }
        }

        for (int i = firstIndex; i < textPositions.size(); i++) {
            TextPosition position = textPositions.get(i);

            // Handle non ASCII separator.
            if (position == null) {
                String separator = getWordSeparator();
                curText.append(separator);
                builder.append(separator);
                continue;
            }

            PDFont baseFont = position.getFont();
            PDFont font = baseFont != null ? baseFont : prevFont;
            float fontSize = position.getFontSize();

            // Handle style change.
            if (!Objects.deepEquals(font, prevFont) ||
                fontSize != prevFontSize ||
                (curTextBlock.isEmpty() && curText.length() == 0)
            ) {
                if (curText.length() != 0) {
                    curTextBlock.addText(curText.toString());
                    curText = new StringBuilder();
                }

                // Block ends when the font size changes between lines.
                if (fontSize != prevFontSize &&
                    Objects.equals(position, firstPosition) &&
                    !curTextBlock.isEmpty()
                ) {
                    assert curTextBlock.isInitialized();
                    textBlocks.add(curTextBlock);
                    curTextBlock = new TextBlock();
                    prevEndPadding = null;
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

            curTextBlock.setEndY(position.getY());
        }

        if (curTextBlock.getY() == null &&
            curTextBlock.getRotation() == null
        ) {
            curTextBlock.setY(firstPosition.getY());
            curTextBlock.setRotation(getAngle(firstPosition));
        }

        // Set x to the x of the most left line to handle block starting with an indent or bullet.
        if (curTextBlock.getX() == null || firstPosition.getX() < curTextBlock.getX()) {
            curTextBlock.setX(firstPosition.getX());
        }

        // Block ends when the current line is shorter than the previous one
        // and isn't at the start of a block.
        if (prevEndPadding != null && curEndPadding > prevEndPadding) {
            onEndOfBlock();
        } else {
            prevEndPadding = curEndPadding;
        }

        super.writeString(builder.toString());
    }

    private static float getAngle(TextPosition text) {
        Matrix m = text.getTextMatrix().clone();
        m.concatenate(text.getFont().getFontMatrix());
        return (float) -Math.round(Math.toDegrees(Math.atan2(m.getShearY(), m.getScaleY())));
    }

    public HashMap<Integer, TextStyle> getIntToTextStyleMap() {
        return intToTextStyleMap;
    }

    private void onEndOfBlock() {
        assert curTextBlock.isInitialized();
        curTextBlock.addText(curText.toString());
        textBlocks.add(curTextBlock);
        curText = new StringBuilder();
        curTextBlock = new TextBlock();
        prevEndPadding = null;
    }

    /**
     * Handles the case when page ends with a line of maximum length.
     */
    private void onPageEnd() {
        if (curText.length() != 0) {
            curTextBlock.addText(curText.toString());
            textBlocks.add(curTextBlock);
        }
        if (margin == null) {
            margin = 0f;
        }
    }

    private void extractDocument(PDDocument document) throws IOException {
        images = new ArrayList<>();
        textBlocks = new ArrayList<>();
        curTextBlock = new TextBlock();
        curText = new StringBuilder();
        prevEndPadding = null;
        margin = null;
        getText(document);
        onPageEnd();
    }

    public Page getPageObject(PDPage page) throws IOException {
        try (PDDocument document = new PDDocument()) {
            document.addPage(page);
            extractDocument(document);
            float pageWidth = page.getMediaBox().getWidth();
            return new Page(textBlocks, images, margin, pageWidth);
        }
    }

    /**
     * This is used to extract images.
     * <p>
     * Based on <a href="https://svn.apache.org/repos/asf/pdfbox/tags/2.0.3/examples/src/main/java/org/apache/pdfbox/examples/util/PrintImageLocations.java">PrintImageLocations.</a>
     *
     * @param operator The operation to perform.
     * @param operands The list of arguments.
     *
     * @throws IOException If there is an error processing the operation.
     */
    @Override
    protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
        String operation = operator.getName();

        if ("Do".equals(operation)) {
            COSName objectName = (COSName) operands.get(0);
            PDXObject xObject = getResources().getXObject(objectName);

            if (xObject instanceof PDImageXObject) {
                PDImageXObject imageObject = (PDImageXObject) xObject;

                Bitmap imageBitmap = imageObject.getImage();
                String filepath = saveImage(imageBitmap, objectName.getName());

                Matrix ctmNew = getGraphicsState().getCurrentTransformationMatrix();
                float imageXScale = ctmNew.getScalingFactorX();
                float imageYScale = ctmNew.getScalingFactorY();
                float pageHeight = getCurrentPage().getMediaBox().getHeight();

                float x = Math.round(ctmNew.getTranslateX());
                float y = Math.round(pageHeight - ctmNew.getTranslateY() - imageYScale);
                float endY = Math.round(pageHeight - ctmNew.getTranslateY());
                int scaledWidth = Math.round(imageXScale * (dpi / 72f));
                int scaledHeight = Math.round(imageYScale * (dpi / 72f));

                Image image = new Image(
                        filepath,
                        x,
                        y,
                        endY,
                        scaledWidth,
                        scaledHeight
                );
                images.add(image);

                if (margin == null || ctmNew.getTranslateX() < margin) {
                    margin = ctmNew.getTranslateX();
                }
            }
        } else {
            super.processOperator(operator, operands);
        }
    }

    private String saveImage(Bitmap image, String name) throws IOException {
        Path filepath = path.resolve(name + ".png");
        String filepathString = filepath.toString();

        if (!Files.exists(filepath)) {
            try (FileOutputStream fos = new FileOutputStream(filepathString)) {
                image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            }
        }
        return filepathString;
    }
}