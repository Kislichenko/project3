package com.trpo.project3;

import com.trpo.project3.analyze.ClassScanner;
import com.trpo.project3.codeGenerator.ObjectCreator;
import com.trpo.project3.codeGenerator.Utils;
import com.trpo.project3.examples.Test2;
import com.trpo.project3.examples.Test8Impl;
import com.trpo.project3.examples.Test9;
import com.trpo.project3.generator.Generator;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        System.out.println("Hello World!");
        Test9 test9 = new Test9(new Test8Impl("hello", new Test2("ttt", new int[]{1, 2})), "ff");
        test9.getHello();

        Class aClass = Parser.class.getConstructors()[0].getParameters()[0].getType();
        System.out.println(aClass.getProtectionDomain().getCodeSource().getLocation().getPath());

        Utils utils = new Utils();

        Generator generator = new Generator();
        try {
            System.out.println(
                    utils.findClassesInJar(aClass,
                            aClass.getProtectionDomain()
                                    .getCodeSource()
                                    .getLocation()
                                    .getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        generator.run();


        String str= String.valueOf(1);
        ClassScanner classScanner = new ClassScanner();
        ObjectCreator objectCreator = new ObjectCreator();
        classScanner.scanPath();
        List<Class> classes = classScanner.getScannedClasses();
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).getSimpleName().contains("Test11")) {
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
