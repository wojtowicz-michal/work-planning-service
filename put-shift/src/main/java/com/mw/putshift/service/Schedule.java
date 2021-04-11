package com.mw.putshift.service;

import com.mw.commonlib.entity.Shift;
import com.mw.commonlib.response.Response;
import com.mw.commonlib.service.ScheduleService;

import java.util.Collections;
import java.util.List;

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

    public void updateShift(Shift requestShift, long shiftId) {
        List<Shift> shifts = getShiftsToUpdate(shiftId);
        Collections.sort(shifts);
        if (!shifts.isEmpty()) updateShift(requestShift, shifts);
    }

    private void updateShift(Shift requestShift, List<Shift> shifts) {
        List<Shift> schedule = getSchedule();
        StringBuilder savedShifts = new StringBuilder();
        StringBuilder failedShifts = new StringBuilder();
        for (Shift shift : shifts) {
            schedule.remove(shift);
            requestShift.setEmployee(shift.getEmployee());
            if (isShiftAlreadyPlanned(requestShift, schedule)) {
                schedule.add(shift);
                failedShifts.append(requestShift).append("\n");
            } else {
                shift.setShiftStart(requestShift.getShiftStart());
                shift.setShiftEnd(requestShift.getShiftEnd());
                schedule.add(shift);
                WORK_SCHEDULE_TABLE.deleteItem(PRIMARY_KEY, shift.getPrimaryKey());
                WORK_SCHEDULE_TABLE.putItem(createItem(shift));
                savedShifts.append(shift).append("\n");
            }
        }
        String message = createResponseMessage(savedShifts.toString(), failedShifts.toString());
        response.appendBody(message);
        response.setStatus(200);
    }

    private List<Shift> getShiftsToUpdate(long shiftId) {
        List<Shift> shifts = findShiftsByShiftId(shiftId);
        if (shifts.isEmpty()) {
            response.setBody(INVALID_SHIFT_ID);
        } else {
            Shift shiftToUpdate = shifts.get(0);
            if (!isShiftExpired(shiftToUpdate)) {
                return shifts;
            }
            response.setBody(SHIFT_EXPIRED);
        }
        return Collections.emptyList();
    }
}
