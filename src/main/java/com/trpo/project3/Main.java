package com.trpo.project3;

import com.trpo.project3.analyze.ClassScanner;
import com.trpo.project3.codeGenerator.ObjectCreator;
import com.trpo.project3.examples.Test2;
import com.trpo.project3.generator.Generator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");

//        Generator generator = new Generator();
//        generator.run();

        ClassScanner classScanner = new ClassScanner();
        ObjectCreator objectCreator = new ObjectCreator();
        classScanner.scanPath();
        List<Class> classes = classScanner.getScannedClasses();
        for(int i=0;i<classes.size();i++){
            if(classes.get(i).getSimpleName().contains("Test2")){
                System.out.println(objectCreator.createSimpleObjectCons(classes.get(i)));
            }
        }


        //objectCreator.createSimpleObjectCons()

//        Set<String> hd = new HashSet<>();
//        hd.add("4;");
//        hd.add("5;");
//        System.out.println(hd.stream().map(h -> "1"+h).collect(Collectors.joining()));
    }
}
