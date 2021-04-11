package com.mw.getshift.service;

import com.mw.commonlib.entity.Shift;
import com.mw.commonlib.response.Response;
import com.mw.commonlib.service.ScheduleService;

import java.util.Comparator;
import java.util.List;

import static com.mw.commonlib.response.ResponseMessage.INVALID_SHIFT_ID;

public class  Schedule extends ScheduleService {
    private final Response response;

    public Schedule(Response response) {
        super(response);
        this.response = response;
    }

    public void fetchShift(long shiftId) {
        List<Shift> shifts = findShiftsByShiftId(shiftId);
        if (shifts.isEmpty()) {
            response.setBody(INVALID_SHIFT_ID);
        } else {
            shifts.sort(Comparator.comparingLong(Shift::getId));
            response.setBody(getShifts(shifts));
            response.setStatus(200);
        }
    }

    private String getShifts(List<Shift> shifts) {
        StringBuilder shiftData = new StringBuilder();
        shifts.forEach(s -> shiftData.append(s).append("\n"));
        return shiftData.toString();
    }
}
