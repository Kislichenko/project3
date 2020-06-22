package com.trpo.project3;

import com.trpo.project3.analyze.ClassScanner;
import com.trpo.project3.codeGenerator.ObjectCreator;
import com.trpo.project3.examples.Test2;
import com.trpo.project3.examples.Test8Impl;
import com.trpo.project3.examples.Test9;
import com.trpo.project3.generator.Generator;

import java.util.List;

public class Main {


    public static void main(String[] args) {
        System.out.println("Hello World!");
        Test9 test9 = new Test9(new Test8Impl("hello", new Test2("ttt", new int[]{1, 2})), "ff");
        test9.getHello();

        Generator generator = new Generator();
        generator.run();


        String str = String.valueOf(1);
        ClassScanner classScanner = new ClassScanner();
        ObjectCreator objectCreator = new ObjectCreator();
        classScanner.scanPath();
        List<Class> classes = classScanner.getScannedClasses();
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).getSimpleName().contains("Test7")) {
                System.out.println(objectCreator.createObjectConsByClass(classes.get(i)).getStrObject());
            }
        }
//
//
//        Parser parser = new Parser(new HtmlTreeBuilder());
//        System.out.println(parser.isTrackErrors());

//        ClassInformer classInformer = new ClassInformer();
//        classInformer.getInfoClass("Test9");

    }
}
