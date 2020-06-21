package com.trpo.project3;

import com.trpo.project3.analyze.ClassScanner;
import com.trpo.project3.codeGenerator.ObjectCreator;
import com.trpo.project3.dto.StringObject;
import com.trpo.project3.examples.Test2;
import com.trpo.project3.examples.Test3;
import com.trpo.project3.examples.Test4;
import com.trpo.project3.examples.Test5;

import java.util.*;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");

//        Generator generator = new Generator();
//        generator.run();


        ClassScanner classScanner = new ClassScanner();
        ObjectCreator objectCreator = new ObjectCreator();
        classScanner.scanPath();
        List<Class> classes = classScanner.getScannedClasses();
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).getSimpleName().contains("Test6")) {
                System.out.println(objectCreator.createSimpleObjectCons(classes.get(i)).getStrObject());
            }
        }

    }
}
