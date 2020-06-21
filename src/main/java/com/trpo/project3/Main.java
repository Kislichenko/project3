package com.trpo.project3;

import com.trpo.project3.generator.Generator;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        Generator generator = new Generator();
        generator.run();

//        Set<String> hd = new HashSet<>();
//        hd.add("4;");
//        hd.add("5;");
//        System.out.println(hd.stream().map(h -> "1"+h).collect(Collectors.joining()));
    }
}
