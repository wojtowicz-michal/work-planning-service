package com.mw.commonlib.service;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.mw.commonlib.entity.Employee;
import com.mw.commonlib.response.Response;

import java.util.*;
import java.util.stream.Collectors;

import static com.mw.commonlib.response.ResponseMessage.INVALID_EMPLOYEE_IDS;
import static com.mw.commonlib.response.ResponseMessage.NO_EMPLOYEE_IDS;
import static com.mw.commonlib.util.Constants.AMAZON_DYNAMO_DB;
import static com.mw.commonlib.util.Constants.EMPLOYEES;
import static com.mw.commonlib.util.EmployeeUtils.parseEmployee;

public class EmployeeBase {
    private final List<Employee> employees;

    public EmployeeBase() {
        employees = new ArrayList<>();
        fetchEmployees();
    }

    private void fetchEmployees() {
        ScanRequest scanRequest = new ScanRequest().withTableName(EMPLOYEES);
        ScanResult scanResult = AMAZON_DYNAMO_DB.scan(scanRequest);
        for (Map<String, AttributeValue> employee : scanResult.getItems()) {
            employees.add(parseEmployee(employee));
        }
    }

    public Optional<Employee> findEmployeeById(long employeeId) {
        return employees
                .stream()
                .filter(e -> e.getId() == employeeId)
                .findAny();
    }

    public long findLastEmployeeId() {
        return employees
                .stream()
                .mapToLong(Employee::getId)
                .max()
                .orElse(0);
    }

    public boolean isEmployed(Employee employee) {
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();
        return employees
                .stream()
                .anyMatch(e -> firstName.equals(e.getFirstName()) && lastName.equals(e.getLastName()));
    }

    public List<Employee> getRequestedEmployees(List<Long> requestedEmployeeIds, Response response) {
        List<Employee> requestedEmployees = findEmployeesById(requestedEmployeeIds);
        if (requestedEmployeeIds.isEmpty() || requestedEmployees.isEmpty()) {
            response.setBody(NO_EMPLOYEE_IDS);
            return Collections.emptyList();
        }
        List<Long> requestedInvalidEmployeeIds = getInvalidEmployeeIds(requestedEmployeeIds);
        if (!requestedInvalidEmployeeIds.isEmpty()) {
            response.setBody(INVALID_EMPLOYEE_IDS + requestedInvalidEmployeeIds + "\n");
        }
        return requestedEmployees;
    }

    private List<Long> getInvalidEmployeeIds(List<Long> employeeIds) {
        List<Long> invalidEmployeeIds = new ArrayList<>();
        for (Long id : employeeIds) {
            if (!isEmployed(id)) {
                invalidEmployeeIds.add(id);
            }
        }
        return invalidEmployeeIds;
    }

    private boolean isEmployed(long id) {
        return employees
                .stream()
                .anyMatch(e -> e.getId() == id);
    }

    private List<Employee> findEmployeesById(List<Long> employeeIds) {
        return employees
                .stream()
                .filter(e -> employeeIds.stream().anyMatch(id -> id == e.getId()))
                .collect(Collectors.toList());
    }
}