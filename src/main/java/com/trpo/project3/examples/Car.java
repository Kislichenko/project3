package com.trpo.project3.examples;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class Car {
    private String model;
    private Cat cat;
    private int cost;
    //private Integer old;
    private int counter=0;
    //private ArrayList<Integer> nums;
    //private Auto auto;
    private int[] abr;

    public void setModelAndCost(Cat cat, int cost1){
        this.cat = cat;
        this.cost = cost1;
    }

    public void setCounter(int num){
        counter=++num;
    }

//    public Integer getByNumber(int num){
//        return nums.get(num);
//    }

}
