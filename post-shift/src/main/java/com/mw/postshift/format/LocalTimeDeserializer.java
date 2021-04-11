package com.mw.postshift.format;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;

import static com.mw.commonlib.util.Constants.TIME_PATTERN;

public class LocalTimeDeserializer implements JsonDeserializer<LocalTime> {
    private final DateTimeFormatter formatter = DateTimeFormat.forPattern(TIME_PATTERN);

    @Override
    public LocalTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
        return LocalTime.parse(json.getAsString(), formatter);
    }
}