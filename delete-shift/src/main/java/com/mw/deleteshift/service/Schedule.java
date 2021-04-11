package com.mw.deleteshift.service;

import com.mw.commonlib.entity.Shift;
import com.mw.commonlib.response.Response;
import com.mw.commonlib.service.ScheduleService;

import java.util.List;

import static com.mw.commonlib.response.ResponseMessage.*;
import static com.mw.commonlib.util.Constants.PRIMARY_KEY;
import static com.mw.commonlib.util.Constants.WORK_SCHEDULE_TABLE;

public class Schedule extends ScheduleService {
    private final Response response;

    public Schedule(Response response) {
        super(response);
        this.response = response;
    }

    public void deleteShift(long shiftId) {
        List<Shift> shifts = findShiftsByShiftId(shiftId);
        if (shifts.isEmpty()) {
            response.setBody(INVALID_SHIFT_ID);
        } else {
            deleteShift(shifts);
        }
    }

    private void deleteShift(List<Shift> shifts) {
        if (isShiftExpired(shifts.get(0))) {
            response.setBody(SHIFT_EXPIRED);
        } else {
            shifts.forEach(shift -> WORK_SCHEDULE_TABLE.deleteItem(PRIMARY_KEY, shift.getPrimaryKey()));
            response.setBody(SHIFT_DELETED);
        }
    }
}
