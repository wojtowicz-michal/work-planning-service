package com.mw.commonlib.util;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;

public final class Constants {
    public static final String PRIMARY_KEY = "primary_key";
    public static final String SHIFT_ID = "shift_id";
    public static final String SHIFT_START = "shift_start";
    public static final String SHIFT_END = "shift_end";
    public static final String EMPLOYEE = "employee";
    public static final String EMPLOYEE_ID = "employee_id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";

    public static final String NAME_REGEX = "[\\p{Lu}\\p{M}][\\p{L}\\p{M},.'-]+(?: [\\p{L}\\p{M},.'-]+)*";

    public static final int TIME_ZONE_HOURS = 2;
    public static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm";
    public static final String DATE_PATTERN = "dd-MM-yyyy";
    public static final String TIME_PATTERN = "HH:mm";

    public static final String EMPLOYEES = "EMPLOYEES";
    public static final String WORK_SCHEDULE = "WORK_SCHEDULE";

    public static final AmazonDynamoDB AMAZON_DYNAMO_DB = AmazonDynamoDBClientBuilder.defaultClient();
    public static final DynamoDB DYNAMO_DB = new DynamoDB(AMAZON_DYNAMO_DB);
    public static final Table EMPLOYEES_TABLE = DYNAMO_DB.getTable(EMPLOYEES);
    public static final Table WORK_SCHEDULE_TABLE = DYNAMO_DB.getTable(WORK_SCHEDULE);

    private Constants() {
    }
}
