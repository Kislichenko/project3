package com.trpo.project3.analyze;

import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

public class InfoClass {
    ClassScanner classScanner = new ClassScanner();

    public Class getClassByName(String className){
        classScanner.scanPath();
        return classScanner.getClassByName(className);
    }

    public void getInfoClass(String nameClass){
        classScanner.scanPath();
        Class testClass = classScanner.getClassByName(nameClass);

        //инфо о загруженном классе
        System.out.println("Name of class "+nameClass+" "+ testClass.getName());
        System.out.println("Package of class: "+testClass.getPackage().getName());
        System.out.println(" ");

        //поля
        Field[] fields = testClass.getDeclaredFields();
        for(int i=0;i<fields.length;i++){
            System.out.println("Field name: "+ fields[i].getName());
            System.out.println("Field modifier: "+ Modifier.toString(fields[i].getModifiers()));
            System.out.println("Field type: "+ fields[i].getGenericType());
            System.out.println(" ");
        }


        //методы
        Method[] methods = testClass.getDeclaredMethods();
        for(int i=0;i<methods.length;i++){
            System.out.println("Method name: "+ methods[i].getName());
            System.out.println("Method return type: "+ methods[i].getReturnType().toString());
            System.out.println("Method modifier: "+ Modifier.toString(methods[i].getModifiers()));


            //получаем названия параметров метода и их типы
            Paranamer paranamer = new BytecodeReadingParanamer();
            String [] parameterNames = paranamer.lookupParameterNames(methods[i], false);
            Parameter[] parameters = methods[i].getParameters();

            for(int k=0;k<parameterNames.length;k++){
                System.out.println("Parameter's names for Method "+methods[i].getName() +": "+parameterNames[k]);
                System.out.println("Parameter: "+parameters[k].getType());
            }

            System.out.println("");
        }


    }


}
