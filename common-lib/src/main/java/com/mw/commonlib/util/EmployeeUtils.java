package com.mw.commonlib.util;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.mw.commonlib.entity.Employee;

import java.util.Map;

import static com.mw.commonlib.util.Constants.*;

public final class EmployeeUtils {

    private EmployeeUtils() {
    }

    public static Employee parseEmployee(Map<String, AttributeValue> map) {
        long id = Long.parseLong(map.get(EMPLOYEE_ID).getN());
        String firstName = map.get(FIRST_NAME).getS();
        String lastName = map.get(LAST_NAME).getS();
        return new Employee(id, firstName, lastName);
    }

    public static Item createItem(Employee employee) {
        return new Item()
                .withPrimaryKey(EMPLOYEE_ID, employee.getId())
                .withString(FIRST_NAME, employee.getFirstName())
                .withString(LAST_NAME, employee.getLastName());
    }
}
