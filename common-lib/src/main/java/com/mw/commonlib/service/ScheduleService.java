package com.mw.commonlib.service;

import com.mw.commonlib.entity.Shift;
import com.mw.commonlib.response.Response;
import org.joda.time.DateTime;

import java.util.List;

import static com.mw.commonlib.format.DateFormat.getCurrentDateTime;

public class ScheduleService extends ScheduleBase {
    private final Response response;

    public ScheduleService(Response response) {
        this.response = response;
    }

    protected boolean isShiftExpired(Shift shift) {
        return shift.getShiftStart().isBefore(getCurrentDateTime());
    }

    protected boolean isShiftAlreadyPlanned(Shift requestShift, List<Shift> schedule) {
        long requestEmployeeId = requestShift.getEmployee().getId();
        for (Shift shift : schedule) {
            long employeeId = shift.getEmployee().getId();
            if (requestEmployeeId == employeeId && isShiftOverlap(requestShift, shift)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isShiftOverlap(Shift requestShift, Shift shift) {
        DateTime requestShiftStart = requestShift.getShiftStart();
        DateTime requestShiftEnd = requestShift.getShiftEnd();
        DateTime shiftStart = shift.getShiftStart();
        DateTime shiftEnd = shift.getShiftEnd();

        if (requestShiftStart.isEqual(shiftStart) || requestShiftEnd.isEqual(shiftEnd)) {
            return true;
        }

        if (requestShiftStart.isBefore(shiftStart) && (requestShiftEnd.isAfter(shiftStart)
                && requestShiftEnd.isBefore(shiftEnd))) {
            return true;
        }

        if (requestShiftStart.isBefore(shiftStart) && requestShiftEnd.isAfter(shiftEnd)) {
            return true;
        }

        if (requestShiftStart.isAfter(shiftStart) && requestShiftEnd.isBefore(shiftEnd)) {
            return true;
        }

        return requestShiftStart.isBefore(shiftEnd) && requestShiftEnd.isAfter(shiftEnd);
    }
}
