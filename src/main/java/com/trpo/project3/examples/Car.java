package com.trpo.project3.examples;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Car {
    private String model;
    private Cat cat;
    private int cost;
    private int counter=0;
    private int[] abr;
    private Test2 test2;

    public void setModelAndCost(Cat cat, int cost1){
        this.cat = cat;
        this.cost = cost1;
    }

    public void setCounter(int num){
        counter=++num;
    }

    public void setTest2(Test2 test2){
        this.test2 = test2;
    }


}
