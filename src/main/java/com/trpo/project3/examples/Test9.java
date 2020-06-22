package com.trpo.project3.examples;

public class Test9 {
    String b;
    Test8 s;

    public Test9(Test8 test8, String b) {
        this.b = b;
        this.s = test8;
    }

    public void getHello() {
        System.out.println(s.getHello());
    }
}
