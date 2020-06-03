package com.trpo.project3.examples;

import lombok.Data;

@Data
public class Car {
    private String model;
    private int cost;
    private Integer old;

    public void setModelAndCost(String model1, int cost1){
        this.model = model1;
        this.cost = cost1;
    }

}
