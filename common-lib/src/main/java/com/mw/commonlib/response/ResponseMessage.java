package com.mw.commonlib.response;

public final class ResponseMessage {
    public static final String EMPLOYEE_SAVED = "EMPLOYEE SAVED";
    public static final String EMPLOYEE_DELETED = "EMPLOYEE DELETED";
    public static final String SHIFT_SAVED = "SHIFT SAVED";
    public static final String SHIFT_UPDATED = "SHIFT UPDATED";
    public static final String SHIFT_DELETED = "SHIFT DELETED";
    public static final String SAVED_SHIFTS = "SAVED SHIFTS";
    public static final String FAILED_SAVES = "FAILED SHIFTS";

    public static final String NULL_REQUEST_BODY = "REQUEST BODY IS NULL";
    public static final String INVALID_REQUEST_BODY = "INVALID REQUEST BODY";

    public static final String INVALID_REQUEST_BODY_SCHEME = "INVALID REQUEST BODY SCHEME";
    public static final String INVALID_EMPLOYEE_ID = "NO EMPLOYEE WITH PROVIDED ID WAS FOUND IN DATABASE";
    public static final String INVALID_SHIFT_ID = "NO SHIFT WITH PROVIDED ID WAS FOUND IN DATABASE";
    public static final String EMPLOYEE_EXISTS = "EMPLOYEE ALREADY EXISTS IN DATABASE";
    public static final String NO_EMPLOYEE_IDS = "IDS NOT PROVIDED OR EMPLOYEES DO NOT EXIST IN DATABASE";
    public static final String INVALID_EMPLOYEE_IDS = "INVALID EMPLOYEE IDS: ";
    public static final String SHIFT_EXISTS = "SHIFT BETWEEN THESE HOURS ALREADY EXISTS";
    public static final String SHIFT_EXPIRED = "IT IS NOT POSSIBLE TO UPDATE PAST SHIFTS";

    public static final String INVALID_SHIFT = "SHIFT DOES NOT EXISTS"
            + "\n"
            + "- make sure shift and employee with provided ID exist"
            + "\n"
            + "- make sure employee with provided ID is assigned to this shift";

    public static final String INVALID_PATH_PARAMETER = "INVALID PATH PARAMETER"
            + "\n"
            + "- id can only contains numbers";

    public static final String INVALID_NAME_FORM = "INVALID REQUEST BODY FORM"
            + "\n"
            + "- make sure your first name and last name start with a capital letter"
            + "\n"
            + "- make sure your first name and last name do not contain illegal characters"
            + "\n"
            + "- make sure your first name and last name contain at least 2 characters";

    public static final String INVALID_DATE_TIME_FORM = "INVALID REQUEST BODY FORM"
            + "\n"
            + "- date format: dd-MM-yyyy"
            + "\n"
            + "- time format: HH:mm";

    public static final String INVALID_SHIFT_PERIOD = "INVALID SHIFT TIME PERIOD"
            + "\n"
            + "- make sure shift start date is before shift end date"
            + "\n"
            + "- make sure you are planning a future shift";

    private ResponseMessage() {
    }

    public static String createResponseMessage(String savedShifts, String failedShifts) {
        String message = "";
        if (savedShifts.length() > 0 && failedShifts.length() > 0) {
            message = SAVED_SHIFTS + "\n" + savedShifts + "\n" + FAILED_SAVES + "\n" + failedShifts;
        } else if (savedShifts.length() > 0) {
            message = SAVED_SHIFTS + "\n" + savedShifts;
        } else if (failedShifts.length() > 0) {
            message = FAILED_SAVES + "\n" + failedShifts;
        }
        return message;
    }
}
