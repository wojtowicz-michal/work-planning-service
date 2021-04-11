package com.mw.deleteshiftemployee.service;

import com.mw.commonlib.entity.Shift;
import com.mw.commonlib.response.Response;
import com.mw.commonlib.service.ScheduleService;

import java.util.Optional;

import static com.mw.commonlib.response.ResponseMessage.*;
import static com.mw.commonlib.util.Constants.PRIMARY_KEY;
import static com.mw.commonlib.util.Constants.WORK_SCHEDULE_TABLE;

public class Schedule extends ScheduleService {
    private final Response response;

    public Schedule(Response response) {
        super(response);
        this.response = response;
    }

    public void deleteShift(long shiftId, long employeeId) {
        Optional<Shift> optionalShift = findShiftByIds(shiftId, employeeId);
        optionalShift.ifPresentOrElse(this::deleteShift, () -> response.setBody(INVALID_SHIFT));
    }

    private void deleteShift(Shift shift) {
        if (isShiftExpired(shift)) {
            response.setBody(SHIFT_EXPIRED);
        } else {
            WORK_SCHEDULE_TABLE.deleteItem(PRIMARY_KEY, shift.getPrimaryKey());
            response.setBody(SHIFT_DELETED);
        }
    }
}
