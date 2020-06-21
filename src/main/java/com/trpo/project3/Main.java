package com.trpo.project3;

import com.trpo.project3.analyze.ClassScanner;
import com.trpo.project3.codeGenerator.ObjectCreator;
import com.trpo.project3.generator.Generator;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        Generator generator = new Generator();
        generator.run();


//        String str= String.valueOf(1);
//        ClassScanner classScanner = new ClassScanner();
//        ObjectCreator objectCreator = new ObjectCreator();
//        classScanner.scanPath();
//        List<Class> classes = classScanner.getScannedClasses();
//        for (int i = 0; i < classes.size(); i++) {
//            if (classes.get(i).getSimpleName().contains("Test7")) {
//                System.out.println(objectCreator.createObjectConsByClass(classes.get(i)).getStrObject());
//            }
//        }

    }
}
