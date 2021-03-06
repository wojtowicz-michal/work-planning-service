package com.mw.commonlib.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private long id;
    private String firstName;
    private String lastName;

    @Override
    public String toString() {
        return "[" + id + "]"
                + " "
                + firstName
                + " "
                + lastName;
    }
}