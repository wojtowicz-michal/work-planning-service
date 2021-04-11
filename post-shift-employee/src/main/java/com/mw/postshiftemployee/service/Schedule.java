package com.mw.postshiftemployee.service;

import com.mw.commonlib.entity.Employee;
import com.mw.commonlib.entity.Shift;
import com.mw.commonlib.response.Response;
import com.mw.commonlib.service.EmployeeBase;
import com.mw.commonlib.service.ScheduleService;

import java.util.List;
import java.util.Optional;

import static com.mw.commonlib.response.ResponseMessage.*;
import static com.mw.commonlib.util.Constants.WORK_SCHEDULE_TABLE;
import static com.mw.commonlib.util.ShiftUtils.createItem;

public class Schedule extends ScheduleService {
    private final Response response;

    public Schedule(Response response) {
        super(response);
        this.response = response;
    }

    public void saveShift(Shift shift, long employeeId) {
        EmployeeBase employeeBase = new EmployeeBase();
        Optional<Employee> optionalEmployee = employeeBase.findEmployeeById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            shift.setEmployee(employee);
            saveShift(shift);
        } else {
            response.setBody(INVALID_EMPLOYEE_ID);
        }
    }

    private void saveShift(Shift shift) {
        List<Shift> schedule = getSchedule();
        if (isShiftAlreadyPlanned(shift, schedule)) {
            response.setBody(SHIFT_EXISTS);
        } else {
            long primaryKey = shift.createPrimaryKey(schedule);
            long shiftId = shift.createId(schedule);
            shift.setPrimaryKey(primaryKey);
            shift.setId(shiftId);
            WORK_SCHEDULE_TABLE.putItem(createItem(shift));
            response.setBody(SHIFT_SAVED);
            response.appendBody(shift.toString());
        }
    }
}
