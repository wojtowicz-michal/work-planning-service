package com.mw.commonlib.service;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.mw.commonlib.entity.Employee;
import com.mw.commonlib.entity.Shift;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mw.commonlib.util.Constants.*;
import static com.mw.commonlib.util.EmployeeUtils.parseEmployee;
import static com.mw.commonlib.util.ShiftUtils.parseShift;

@Getter
public class ScheduleBase {
    private final List<Shift> schedule;

    public ScheduleBase() {
        schedule = new ArrayList<>();
        fetchSchedule();
    }

    private void fetchSchedule() {
        ScanRequest scanRequest = new ScanRequest().withTableName(WORK_SCHEDULE);
        ScanResult scanResult = AMAZON_DYNAMO_DB.scan(scanRequest);
        for (Map<String, AttributeValue> shift : scanResult.getItems()) {
            Employee employee = parseEmployee(shift.get(EMPLOYEE).getM());
            schedule.add(parseShift(shift, employee));
        }
    }

    public List<Shift> findShiftsByEmployeeId(long employeeId) {
        return schedule
                .stream()
                .filter(shift -> shift.getEmployee().getId() == employeeId)
                .collect(Collectors.toList());
    }

    protected List<Shift> findShiftsByShiftId(long shiftId) {
        return schedule
                .stream()
                .filter(s -> s.getId() == shiftId)
                .collect(Collectors.toList());
    }

    public Optional<Shift> findShiftByIds(long shiftId, long employeeId) {
        return schedule
                .stream()
                .filter(s -> s.getId() == shiftId && s.getEmployee().getId() == employeeId)
                .findAny();
    }
}
