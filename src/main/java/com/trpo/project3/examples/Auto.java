package com.trpo.project3.examples;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Auto {
    ArrayList<Integer> nums;
    private String model;
    private int cost;
    private Integer old;
    private int counter = 0;

    public void setModelAndCost(String model1, int cost1) {
        this.model = model1;
        this.cost = cost1;
    }

    public void setCounter(int num) {
        counter = ++num;
    }

    public Integer getByNumber(int num) {
        return nums.get(num);
    }
}
