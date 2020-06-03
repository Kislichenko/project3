package com.trpo.project3.examples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Car1Test {

    Car1 car1 = new Car1();


    @Test
    void getCounter() {
        car1.setCounter(6);
        assertEquals(7, car1.getCounter());
    }

    //в этом тесте нужно обновлять состояние перед взятием
    @Test
    void getFirstNumOfNums() {
        //car1.getByNumber(1);
    }

}
