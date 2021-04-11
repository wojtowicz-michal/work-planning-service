package com.mw.postshift.format;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;

import static com.mw.commonlib.util.Constants.DATE_PATTERN;

public class DateTimeDeserializer implements JsonDeserializer<DateTime> {
    private final DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_PATTERN);

    @Override
    public DateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
        return DateTime.parse(json.getAsString(), formatter);
    }
}