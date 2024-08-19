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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.github.tomas337.translating_pdf_viewer.utils.Image;
import io.github.tomas337.translating_pdf_viewer.utils.Page;
import io.github.tomas337.translating_pdf_viewer.utils.TextBlock;
import io.github.tomas337.translating_pdf_viewer.utils.TextStyle;

/**
 * This class takes a PDPage and extracts the text and images into Page object.
 */
public class Extractor extends PDFTextStripper {

    // variables for text extraction
    private List<TextBlock> textBlocks;
    private TextBlock curTextBlock;
    private StringBuilder curText;
    private PDFont prevFont;
    private float prevFontSize;
    private int curMaxStyleIndex;
    private Integer prevEndPadding = null;
    private final HashMap<TextStyle, Integer> textStyleToIntMap = new HashMap<>();
    private final HashMap<Integer, TextStyle> intToTextStyleMap = new HashMap<>();

    // variables for image extraction
    private final int dpi;
    private List<Image> images;

    /**
     * Default constructor.
     * <p>
     * Based on <a href="https://svn.apache.org/repos/asf/pdfbox/tags/2.0.3/examples/src/main/java/org/apache/pdfbox/examples/util/PrintImageLocations.java">PrintImageLocations.</a>
     *
     * @throws IOException If there is an error loading text stripper properties.
     */
    public Extractor(int dpi) throws IOException {
        super();
        this.dpi = dpi;

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
     * This implementation also extracts information about the text.
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

    private static float getAngle(TextPosition text) {
        Matrix m = text.getTextMatrix().clone();
        m.concatenate(text.getFont().getFontMatrix());
        return (float) -Math.round(Math.toDegrees(Math.atan2(m.getShearY(), m.getScaleY())));
    }

    public HashMap<Integer, TextStyle> getIntToTextStyleMap() {
        return intToTextStyleMap;
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

    private void extractDocument(PDDocument document) throws IOException {
        images = new ArrayList<>();
        textBlocks = new ArrayList<>();
        curTextBlock = new TextBlock();
        curText = new StringBuilder();
        getText(document);
        onPageEnd();
    }

    public Page getPageObject(PDPage page) throws IOException {
        try (PDDocument document = new PDDocument()) {
            document.addPage(page);
            extractDocument(document);
            Log.d("images", images.toString());
            return new Page(textBlocks, images);
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
                int width = imageObject.getWidth();
                int height = imageObject.getHeight();

                Matrix ctmNew = getGraphicsState().getCurrentTransformationMatrix();
                float imageXScale = ctmNew.getScalingFactorX();
                float imageYScale = ctmNew.getScalingFactorY();

                Bitmap imageBitmap = imageObject.getImage();
                int scaledX = Math.round(ctmNew.getTranslateX() * (dpi / 72f));
                int scaledY = Math.round(ctmNew.getTranslateY() * (dpi / 72f));
                int scaledWidth = Math.round(imageXScale * (dpi / 72f));
                int scaledHeight = Math.round(imageYScale * (dpi / 72f));

                Image image = new Image(imageBitmap, scaledX, scaledY, scaledWidth, scaledHeight);
                images.add(image);

                // position in user space units. 1 unit = 1/72 inch at 72 dpi
                Log.i("position in PDF", ctmNew.getTranslateX() + ", " + ctmNew.getTranslateY() + " in user space units");
                // position in px
                Log.i("position in px", scaledX + ", " + scaledY);

                // raw size in pixels
                Log.i("raw image size", width + ", " + height + " in pixels");
                // displayed size in pixels
                 Log.i("displayed size", scaledWidth + ", " + scaledHeight);

            } else if (xObject instanceof PDFormXObject) {
                PDFormXObject form = (PDFormXObject) xObject;
                showForm(form);
            }
        } else {
            super.processOperator(operator, operands);
        }
    }
}
