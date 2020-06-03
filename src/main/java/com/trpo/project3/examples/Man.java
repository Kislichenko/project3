package com.trpo.project3.examples;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Man {
    String name;
    ArrayList<Car> cars;

    public void brokeCarByModel(String model){
        for (int i=0;i<cars.size();i++){
            if(cars.get(i).getModel().equals(model)){
                cars.remove(i);
            }
        }
    }

    //должно ломаться, если машин в массиве нет
    public Car getLastCar(){
        return cars.get(cars.size());
    }

}
