package com.trpo.project3.examples;

public class Test3 {
    int a;
    int b;
    Test2 test2;

    public Test3(int a, Test2 test2){
        this.a = a;
        this.test2 = test2;
    }

    public Test3(int a, int b, Test2 test2){
        this.a = a;
        this.b = b;
        this.test2 = test2;
    }
}
