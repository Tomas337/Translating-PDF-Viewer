package io.github.tomas337.translating_pdf_viewer.data.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.github.tomas337.translating_pdf_viewer.utils.Image;
import io.github.tomas337.translating_pdf_viewer.utils.PageContent;
import io.github.tomas337.translating_pdf_viewer.utils.TextBlock;

public class PageDeserializer implements JsonDeserializer<PageContent> {

    @Override
    public PageContent deserialize(
            JsonElement json,
            Type typeOfT,
            JsonDeserializationContext context
    ) throws JsonParseException {

        JsonObject jObject = (JsonObject) json;
        JsonElement typeObj = jObject.get("type");

        if (typeObj != null) {
            String typeVal = typeObj.getAsString();

            switch (typeVal) {
                case "image":
                    return context.deserialize(json, Image.class);
                case "text-block":
                    return context.deserialize(json, TextBlock.class);
            }
        }

        return null;
    }
}
