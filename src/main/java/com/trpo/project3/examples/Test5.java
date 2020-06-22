package com.trpo.project3.examples;

import lombok.Data;

import java.util.Date;

@Data
public class Test5 {
    String a;
    Test2 test2;
    Date date;

    public Test5(String a, Test2 test2, Date date) {
        this.a = a;
        this.test2 = test2;
        this.date = date;

    }
}
