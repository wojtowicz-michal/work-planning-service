package com.mw.deleteemployee.service;

import com.mw.commonlib.entity.Employee;
import com.mw.commonlib.response.Response;
import com.mw.commonlib.service.EmployeeBase;

import java.util.Optional;

import static com.mw.commonlib.response.ResponseMessage.EMPLOYEE_DELETED;
import static com.mw.commonlib.response.ResponseMessage.INVALID_EMPLOYEE_ID;
import static com.mw.commonlib.util.Constants.EMPLOYEES_TABLE;
import static com.mw.commonlib.util.Constants.EMPLOYEE_ID;

public class EmployeeService {
    private final Response response;

    public EmployeeService(Response response) {
        this.response = response;
    }

    public void deleteEmployee(long employeeId) {
        EmployeeBase employeeBase = new EmployeeBase();
        Optional<Employee> optionalEmployee = employeeBase.findEmployeeById(employeeId);
        if (optionalEmployee.isPresent()) {
            Schedule schedule = new Schedule();
            schedule.removeShiftsByEmployeeId(employeeId);
            EMPLOYEES_TABLE.deleteItem(EMPLOYEE_ID, employeeId);
            response.setBody(EMPLOYEE_DELETED);
        } else {
            response.setBody(INVALID_EMPLOYEE_ID);
        }
    }
}