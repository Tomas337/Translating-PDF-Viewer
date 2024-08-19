package io.github.tomas337.translating_pdf_viewer.data.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tom_roush.pdfbox.io.MemoryUsageSetting;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.rendering.PDFRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import io.github.tomas337.translating_pdf_viewer.utils.Document;
import io.github.tomas337.translating_pdf_viewer.utils.Page;
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
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             PDDocument pdfDocument = PDDocument.load(inputStream,
                     MemoryUsageSetting.setupTempFileOnly())
        ) {
            Files.createDirectories(path);

            int dpi = context.getResources().getDisplayMetrics().densityDpi;
            Extractor extractor = new Extractor(dpi);
            int numberOfPages = pdfDocument.getNumberOfPages();
            List<String> pagePaths = new ArrayList<>();

            for (int i = 0; i < numberOfPages; i++) {
                PDPage page = pdfDocument.getPage(i);
                Page pageObject = extractor.getPageObject(page);
                String filepath = savePage(pageObject, i);
                pagePaths.add(filepath);
            }

            HashMap<Integer, TextStyle> intToTextStyleMap = extractor.getIntToTextStyleMap();
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
}
