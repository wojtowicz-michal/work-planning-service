package com.mw.postemployee.service;

import com.mw.commonlib.entity.Employee;
import com.mw.commonlib.response.Response;
import com.mw.commonlib.service.EmployeeBase;

import static com.mw.commonlib.response.ResponseMessage.EMPLOYEE_EXISTS;
import static com.mw.commonlib.response.ResponseMessage.EMPLOYEE_SAVED;
import static com.mw.commonlib.util.Constants.EMPLOYEES_TABLE;
import static com.mw.commonlib.util.EmployeeUtils.createItem;

public class EmployeeService {
    private final Response response;

    public EmployeeService(Response response) {
        this.response = response;
    }

    public void saveEmployee(Employee employee) {
        EmployeeBase employeeBase = new EmployeeBase();
        if (employeeBase.isEmployed(employee)) {
            response.setBody(EMPLOYEE_EXISTS);
        } else {
            employee.setId(employeeBase.findLastEmployeeId() + 1);
            EMPLOYEES_TABLE.putItem(createItem(employee));
            response.setBody(EMPLOYEE_SAVED);
            response.appendBody(employee.toString());
        }
    }
}
