package com.mw.putshiftemployee.service;

import com.mw.commonlib.entity.Shift;
import com.mw.commonlib.response.Response;
import com.mw.commonlib.service.ScheduleService;

import java.util.List;
import java.util.Optional;

import static com.mw.commonlib.response.ResponseMessage.*;
import static com.mw.commonlib.util.Constants.PRIMARY_KEY;
import static com.mw.commonlib.util.Constants.WORK_SCHEDULE_TABLE;
import static com.mw.commonlib.util.ShiftUtils.createItem;

public class Schedule extends ScheduleService {
    private final Response response;

    public Schedule(Response response) {
        super(response);
        this.response = response;
    }

    public void updateShift(Shift requestShift, long shiftId, long employeeId) {
        Optional<Shift> optionalShift = getShiftToUpdate(shiftId, employeeId);
        if (optionalShift.isPresent()) {
            Shift shift = optionalShift.get();
            requestShift.setEmployee(shift.getEmployee());
            updateShift(requestShift, shift);
        }
    }

    private void updateShift(Shift requestShift, Shift shift) {
        List<Shift> schedule = getSchedule();
        WORK_SCHEDULE_TABLE.deleteItem(PRIMARY_KEY, shift.getPrimaryKey());
        schedule.remove(shift);
        if (isShiftAlreadyPlanned(requestShift, schedule)) {
            WORK_SCHEDULE_TABLE.putItem(createItem(shift));
            response.setBody(SHIFT_EXISTS);
        } else {
            long primaryKey = shift.getPrimaryKey();
            long shiftId = requestShift.createId(schedule);
            requestShift.setPrimaryKey(primaryKey);
            requestShift.setId(shiftId);
            WORK_SCHEDULE_TABLE.putItem(createItem(requestShift));
            response.setBody(SHIFT_UPDATED);
            response.appendBody(requestShift.toString());
        }
    }

    private Optional<Shift> getShiftToUpdate(long shiftId, long employeeId) {
        Optional<Shift> optionalShift = findShiftByIds(shiftId, employeeId);
        if (optionalShift.isEmpty()) {
            response.setBody(INVALID_SHIFT);
        } else {
            Shift shift = optionalShift.get();
            if (!isShiftExpired(shift)) {
                return Optional.of(shift);
            }
            response.setBody(SHIFT_EXPIRED);
        }
        return Optional.empty();
    }
}
