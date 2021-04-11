package com.mw.deleteemployee.service;

import com.mw.commonlib.entity.Shift;
import com.mw.commonlib.service.ScheduleBase;

import java.util.List;

import static com.mw.commonlib.util.Constants.PRIMARY_KEY;
import static com.mw.commonlib.util.Constants.WORK_SCHEDULE_TABLE;

public class Schedule {

    public void removeShiftsByEmployeeId(long employeeId) {
        ScheduleBase scheduleBase = new ScheduleBase();
        List<Shift> shifts = scheduleBase.findShiftsByEmployeeId(employeeId);
        shifts.forEach(shift -> WORK_SCHEDULE_TABLE.deleteItem(PRIMARY_KEY, shift.getPrimaryKey()));
    }
}