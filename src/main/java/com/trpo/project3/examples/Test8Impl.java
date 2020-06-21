package com.trpo.project3.examples;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Test8Impl implements Test8 {
    String a;
    Test2 test2;

    @Override
    public String getHello() {
        return "hello";
    }
}
