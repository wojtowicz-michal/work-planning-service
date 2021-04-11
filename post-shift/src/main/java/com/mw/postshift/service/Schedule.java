package com.mw.postshift.service;

import com.mw.commonlib.entity.Employee;
import com.mw.commonlib.entity.Shift;
import com.mw.commonlib.response.Response;
import com.mw.commonlib.service.EmployeeBase;
import com.mw.commonlib.service.ScheduleService;
import com.mw.postshift.format.Data;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mw.commonlib.response.ResponseMessage.createResponseMessage;
import static com.mw.commonlib.util.Constants.WORK_SCHEDULE_TABLE;
import static com.mw.commonlib.util.ShiftUtils.createItem;

public class Schedule extends ScheduleService {
    private final Response response;

    public Schedule(Response response) {
        super(response);
        this.response = response;
    }

    public void saveShift(Data data) {
        EmployeeBase employeeBase = new EmployeeBase();
        List<Long> requestedEmployeeIds = data.getEmployeeIds();
        List<Employee> employees = employeeBase.getRequestedEmployees(requestedEmployeeIds, response);
        if (!employees.isEmpty()) saveShift(data, employees);
    }

    private void saveShift(Data data, List<Employee> employees) {
        List<Shift> schedule = getSchedule();
        List<Shift> shifts = getRequestedShifts(employees, data);
        Collections.sort(shifts);

        StringBuilder savedShifts = new StringBuilder();
        StringBuilder failedShifts = new StringBuilder();
        for (Shift shift : shifts) {
            if (isShiftAlreadyPlanned(shift, schedule)) {
                failedShifts.append(shift).append("\n");
            } else {
                long primaryKey = shift.createPrimaryKey(schedule);
                long shiftId = shift.createId(schedule);
                shift.setPrimaryKey(primaryKey);
                shift.setId(shiftId);
                schedule.add(shift);
                WORK_SCHEDULE_TABLE.putItem(createItem(shift));
                savedShifts.append(shift).append("\n");
            }
        }
        String message = createResponseMessage(savedShifts.toString(), failedShifts.toString());
        response.appendBody(message);
        response.setStatus(200);
    }

    private List<Shift> getRequestedShifts(List<Employee> employees, Data data) {
        LocalTime shiftTimeStart = data.getShiftTimeStart();
        int shiftHourStart = shiftTimeStart.getHourOfDay();
        int shiftMinuteStart = shiftTimeStart.getMinuteOfHour();

        LocalTime shiftTimeEnd = data.getShiftTimeEnd();
        int shiftHourEnd = shiftTimeEnd.getHourOfDay();
        int shiftMinuteEnd = shiftTimeEnd.getMinuteOfHour();

        DateTime shiftPeriodStart = data.getShiftPeriodStart()
                .hourOfDay().setCopy(shiftHourStart)
                .minuteOfHour().setCopy(shiftMinuteStart);

        DateTime shiftPeriodEnd = data.getShiftPeriodEnd()
                .hourOfDay().setCopy(shiftHourEnd)
                .minuteOfHour().setCopy(shiftMinuteEnd);

        List<Shift> requestedShifts = new ArrayList<>();
        DateTime shiftStart = shiftPeriodStart;
        DateTime shiftEnd = shiftPeriodEnd.dayOfMonth().setCopy(shiftStart.getDayOfMonth());

        int daysDifference = shiftPeriodEnd.getDayOfMonth() - shiftPeriodStart.getDayOfMonth();
        if (daysDifference == 0) shiftPeriodEnd = shiftPeriodEnd.plusDays(1);

        if (isShiftEndsNextDay(shiftHourStart, shiftMinuteStart, shiftHourEnd, shiftMinuteEnd)) {
            shiftEnd = shiftEnd.plusDays(1);
        }

        while (shiftStart.isBefore(shiftPeriodEnd)) {
            for (Employee employee : employees) {
                requestedShifts.add(new Shift(shiftStart, shiftEnd, employee));
            }
            shiftStart = shiftStart.plusDays(1);
            shiftEnd = shiftEnd.plusDays(1);
        }
        return requestedShifts;
    }

    private boolean isShiftEndsNextDay(int shiftHourStart, int shiftMinuteStart, int shiftHourEnd, int shiftMinuteEnd) {
        return shiftHourStart > shiftHourEnd || (shiftHourStart == shiftHourEnd && shiftMinuteStart > shiftMinuteEnd);
    }
}
