package com.mw.postemployee.format;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.mw.commonlib.entity.Employee;
import com.mw.commonlib.response.Response;

import java.util.Optional;

import static com.mw.commonlib.response.ResponseMessage.*;
import static com.mw.commonlib.util.Constants.NAME_REGEX;

public class Parser {
    private final Gson gson;

    public Parser() {
        gson = new GsonBuilder()
                .create();
    }

    public Optional<Employee> parseRequestBody(String json, Response response) {
        try {
            Employee employee = gson.fromJson(json, Employee.class);
            if (isInputValid(employee, response)) {
                return Optional.of(employee);
            }
        } catch (NullPointerException e) {
            response.setBody(NULL_REQUEST_BODY);
        } catch (JsonParseException e) {
            response.setBody(INVALID_REQUEST_BODY);
        }
        return Optional.empty();
    }

    private boolean isInputValid(Employee employee, Response response) {
        if (!isAllDataProvided(employee)) {
            response.setBody(INVALID_REQUEST_BODY_SCHEME);
        } else if (!isDataValid(employee)) {
            response.setBody(INVALID_NAME_FORM);
        } else {
            return true;
        }
        return false;
    }

    private boolean isAllDataProvided(Employee employee) {
        return employee.getFirstName() != null && employee.getLastName() != null;
    }

    private boolean isDataValid(Employee employee) {
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();
        return firstName.matches(NAME_REGEX) && lastName.matches(NAME_REGEX);
    }
}
