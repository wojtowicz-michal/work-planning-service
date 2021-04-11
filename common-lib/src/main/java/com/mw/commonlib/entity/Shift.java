package com.mw.commonlib.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shift implements Comparable<Shift> {
    private long primaryKey;
    private long id;
    private DateTime shiftStart;
    private DateTime shiftEnd;
    private Employee employee;

    public Shift(DateTime shiftStart, DateTime shiftEnd, Employee employee) {
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
        this.employee = employee;
    }

    public long createPrimaryKey(List<Shift> schedule) {
        return schedule
                .stream()
                .mapToLong(Shift::getPrimaryKey)
                .max()
                .orElse(0) + 1;
    }

    public long createId(List<Shift> schedule) {
        return schedule
                .stream()
                .filter(s -> s.getShiftStart().equals(shiftStart) && s.getShiftEnd().equals(shiftEnd))
                .mapToLong(Shift::getId)
                .findFirst()
                .orElseGet(() -> schedule
                        .stream()
                        .mapToLong(Shift::getId)
                        .max().orElse(0) + 1);
    }

    @Override
    public String toString() {
        return employee
                + "\n"
                + "[" + id + "]"
                + " "
                + shiftStart.toLocalDate()
                + " "
                + shiftStart.toLocalTime()
                + " - "
                + shiftEnd.toLocalDate()
                + " "
                + shiftEnd.toLocalTime()
                + "\n";
    }

    @Override
    public int compareTo(Shift shift) {
        if (shiftStart.isBefore(shift.getShiftStart())) {
            return -1;
        }
        return 1;
    }
}
