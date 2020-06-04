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
    //private int[] abr;

   // public void setModelAndCost(String model1, int cost1){
        //this.model = model1;
        //this.cost = cost1;
        //byte[] arg0 = new byte[]{0b10};
    //}

    /*public void setCounter(int num){
        counter=++num;
    }*/

    /*public Integer getByNumber(int num){
        return nums.get(num);
    }*/

}
