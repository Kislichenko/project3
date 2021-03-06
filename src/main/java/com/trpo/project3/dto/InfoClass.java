package com.trpo.project3.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class InfoClass {
    private String name;
    private String classPackage;
    private ArrayList<InfoMethod> methods;
    private ArrayList<InfoField> fields;
    private ArrayList<InfoConstructor> constructors;
    private Class aClass;

}
