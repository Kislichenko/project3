package com.trpo.project3.codeGenerator;


import com.google.googlejavaformat.java.FormatterException;
import com.trpo.project3.dto.InfoClass;
import com.trpo.project3.dto.InfoField;
import com.trpo.project3.dto.InfoMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static jdk.nashorn.internal.ir.LiteralNode.newInstance;

public class CodeGenerator {
    private final String endOfLine = "; ";
    private final String openBracket = "{";
    private final String endBracket = "}";
    private final String testAnnotation = "@Test ";
    private final String headers = "import org.junit.jupiter.api.Test; \nimport static org.junit.jupiter.api.Assertions.*;\n";

    private Map<String, String> classTests = new HashMap<>();
    Set<String> neededPackages = new HashSet<>();
    ConsCodeGenerator consCodeGenerator = new ConsCodeGenerator();
    MethodCodeGenerator methodCodeGenerator = new MethodCodeGenerator();

    public void genTests(ArrayList<InfoClass> infoClasses) {
        for (int i = 0; i < infoClasses.size(); i++) {
            classTests.put(infoClasses.get(i).getName(), genTest(infoClasses.get(i)));
        }
    }



    public String genTest(InfoClass infoClass) {


        String test = "";
        test = test + "package " + infoClass.getClassPackage() + endOfLine;
        test = test + getAllHeaders();
        test = test + "class " + infoClass.getName() + "Test" + openBracket;
        test = test + consCodeGenerator.genInitCons(infoClass);
        //test = test + methodCodeGenerator.genMthods(infoClass);
        test = test + genMethodTests(infoClass.getMethods());
        test = test + endBracket ;

        String formattedSource ="";
        //System.out.println(test);
        try {
            formattedSource += new com.google.googlejavaformat.java.Formatter().formatSource(test);
        } catch (FormatterException e) {
            e.printStackTrace();
        }

        return formattedSource;
    }

    public String genMethodTests(ArrayList<InfoMethod> infoMethods) {
        String methodTests="";
        for(int i=0;i<infoMethods.size();i++){
            methodTests = methodTests + genMethodTest(infoMethods.get(i));
        }

        return methodTests;
    }

    private String getAllHeaders(){
        String hd = headers;
        for (Iterator<String> it = neededPackages.iterator(); it.hasNext(); ) {
            String h = it.next();
            hd=hd+" import "+h+endOfLine;
        }
        return hd;
    }

    private String getInfoMethodTypeName(InfoMethod method){
        if(!method.getReturnType().getTypePackage().equals(method.getReturnType().getName())){
            neededPackages.add(method.getReturnType().getTypePackage());
        }
        return method.getReturnType().getName();
    }

    private String getInfoFieldTypeName(InfoField field){
        if(!field.getType().getTypePackage().equals(field.getType().getName())){
            neededPackages.add(field.getType().getTypePackage());
        }
        return field.getType().getName();
    }

    public String genMethodTest(InfoMethod infoMethod) {
        String method = "";
        method = method + testAnnotation;
        method = method + "public void " + infoMethod.getName() + "()" + openBracket;


        method = method + endBracket;

        return method;
    }


    public void printTestByNameClass(String className) {
        System.out.println("Test for class '" + className + "'!!!");
        System.out.println(classTests.get(className));
        System.out.println();
    }


}
