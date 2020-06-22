package com.trpo.project3;

import com.trpo.project3.analyze.ClassInformer;
import com.trpo.project3.analyze.ClassScanner;
import com.trpo.project3.codeGenerator.ObjectCreator;
import com.trpo.project3.examples.Test2;
import com.trpo.project3.examples.Test8Impl;
import com.trpo.project3.examples.Test9;
import com.trpo.project3.generator.Generator;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class Main {



    public static void main(String[] args) {
        System.out.println("Hello World!");
        Test9 test9 = new Test9(new Test8Impl("hello", new Test2("ttt", new int[]{1,2})), "ff");
        test9.getHello();

        Class aClass = Parser.class.getConstructors()[0].getParameters()[0].getType();

//        Reflections reflections = new Reflections(
//                ClasspathHelper.forPackage("your.root.package"), new SubTypesScanner());
//        Set<Class<? extends aClass>> implementingTypes =
//                reflections.getSubTypesOf(aClass);
//
//        System.out.println(implementingTypes.size());


        System.out.println(aClass.getProtectionDomain().getCodeSource().getLocation().getPath());


        Generator generator = new Generator();
        try {
            System.out.println(
                    generator.findClassesInJar(aClass,
                            aClass.getProtectionDomain()
                                    .getCodeSource()
                                    .getLocation()
                                    .getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        generator.run();


//        String str= String.valueOf(1);
//        ClassScanner classScanner = new ClassScanner();
//        ObjectCreator objectCreator = new ObjectCreator();
//        classScanner.scanPath();
//        List<Class> classes = classScanner.getScannedClasses();
//        for (int i = 0; i < classes.size(); i++) {
//            if (classes.get(i).getSimpleName().contains("Test11")) {
//                System.out.println(objectCreator.createObjectConsByClass(classes.get(i)).getStrObject());
//            }
//        }
//
//
//        Parser parser = new Parser(new HtmlTreeBuilder());
//        System.out.println(parser.isTrackErrors());

//        ClassInformer classInformer = new ClassInformer();
//        classInformer.getInfoClass("Test9");

    }
}
