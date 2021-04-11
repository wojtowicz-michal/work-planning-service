package com.mw.commonlib.util;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.mw.commonlib.entity.Employee;
import com.mw.commonlib.entity.Shift;
import com.mw.commonlib.format.DateFormat;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

import static com.mw.commonlib.format.DateFormat.dateTimeToString;
import static com.mw.commonlib.util.Constants.*;

public final class ShiftUtils {

    private ShiftUtils() {
    }

    public static Shift parseShift(Map<String, AttributeValue> map, Employee employee) {
        long primaryKey = Long.parseLong(map.get(Constants.PRIMARY_KEY).getN());
        long id = Long.parseLong(map.get(Constants.SHIFT_ID).getN());
        DateTime shiftStart = DateFormat.stringToDateTime(map.get(Constants.SHIFT_START).getS());
        DateTime shiftEnd = DateFormat.stringToDateTime(map.get(Constants.SHIFT_END).getS());
        return new Shift(primaryKey, id, shiftStart, shiftEnd, employee);
    }

    public static Item createItem(Shift shift) {
        Map<String, Object> map = new HashMap<>();
        map.put(EMPLOYEE_ID, shift.getEmployee().getId());
        map.put(FIRST_NAME, shift.getEmployee().getFirstName());
        map.put(LAST_NAME, shift.getEmployee().getLastName());
        return new Item()
                .withPrimaryKey(PRIMARY_KEY, shift.getPrimaryKey())
                .withLong(SHIFT_ID, shift.getId())
                .withString(SHIFT_START, dateTimeToString(shift.getShiftStart()))
                .withString(SHIFT_END, dateTimeToString(shift.getShiftEnd()))
                .withMap(EMPLOYEE, map);
    }
}
