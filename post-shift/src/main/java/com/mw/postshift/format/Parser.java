package com.mw.postshift.format;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.mw.commonlib.response.Response;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.Optional;

import static com.mw.commonlib.format.DateFormat.getCurrentDateTime;
import static com.mw.commonlib.response.ResponseMessage.*;

public class Parser {
    private final Gson gson;

    public Parser() {
        gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeDeserializer())
                .registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer())
                .create();
    }

    public Optional<Data> parseRequestBody(String json, Response response) {
        try {
            Data data = gson.fromJson(json, Data.class);
            if (isInputValid(data, response)) {
                return Optional.of(data);
            }
        } catch (NullPointerException e) {
            response.setBody(NULL_REQUEST_BODY);
        } catch (JsonParseException e) {
            response.setBody(INVALID_REQUEST_BODY);
        } catch (IllegalArgumentException e) {
            response.setBody(INVALID_DATE_TIME_FORM);
        }
        return Optional.empty();
    }

    private boolean isInputValid(Data data, Response response) {
        if (!isAllDataProvided(data)) {
            response.setBody(INVALID_REQUEST_BODY_SCHEME);
        } else if (isDataInvalid(data)) {
            response.setBody(INVALID_SHIFT_PERIOD);
        } else {
            return true;
        }
        return false;
    }

    private boolean isAllDataProvided(Data data) {
        return data.getShiftPeriodStart() != null
                && data.getShiftPeriodEnd() != null
                && data.getShiftTimeStart() != null
                && data.getShiftTimeEnd() != null;
    }

    private boolean isDataInvalid(Data data) {
        DateTime shiftPeriodStart = data.getShiftPeriodStart();
        DateTime shiftPeriodEnd = data.getShiftPeriodEnd();
        return shiftPeriodStart.isAfter(shiftPeriodEnd) || shiftPeriodStart.isBefore(getCurrentDateTime());
    }
}
