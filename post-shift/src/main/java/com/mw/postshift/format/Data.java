package com.mw.postshift.format;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.List;

@Getter
@Setter
public class Data {
    private DateTime shiftPeriodStart;
    private DateTime shiftPeriodEnd;
    private LocalTime shiftTimeStart;
    private LocalTime shiftTimeEnd;
    private List<Long> employeeIds;
}
