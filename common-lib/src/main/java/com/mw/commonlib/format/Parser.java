package com.mw.commonlib.format;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.mw.commonlib.entity.Shift;
import com.mw.commonlib.response.Response;
import org.joda.time.DateTime;

import java.util.Optional;

import static com.mw.commonlib.format.DateFormat.getCurrentDateTime;
import static com.mw.commonlib.response.ResponseMessage.*;

public class Parser {
    private final Gson gson;

    public Parser() {
        gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeDeserializer())
                .create();
    }

    public Optional<Shift> parseRequestBody(String json, Response response) {
        try {
            Shift shift = gson.fromJson(json, Shift.class);
            if (isInputValid(shift, response)) {
                return Optional.of(shift);
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

    public Optional<Long> parsePathParameter(String pathParameter, Response response) {
        try {
            return Optional.of(Long.parseLong(pathParameter));
        } catch (NumberFormatException e) {
            response.setBody(INVALID_PATH_PARAMETER);
            return Optional.empty();
        }
    }

    private boolean isInputValid(Shift shift, Response response) {
        if (!isAllDataProvided(shift)) {
            response.setBody(INVALID_REQUEST_BODY_SCHEME);
        } else if (isDataInvalid(shift)) {
            response.setBody(INVALID_SHIFT_PERIOD);
        } else {
            return true;
        }
        return false;
    }

    private boolean isAllDataProvided(Shift shift) {
        return shift.getShiftStart() != null && shift.getShiftEnd() != null;
    }

    private boolean isDataInvalid(Shift shift) {
        DateTime shiftStart = shift.getShiftStart();
        DateTime shiftEnd = shift.getShiftEnd();
        return !shiftStart.isBefore(shiftEnd) || shiftStart.isBefore(getCurrentDateTime());
    }
}
