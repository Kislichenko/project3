package com.trpo.project3.analyze;

public class InfoClass {
    ClassScanner classScanner = new ClassScanner();

    public void getInfoClass(String nameClass){
        classScanner.scanPath();
        Class testClass = classScanner.getClassByName(nameClass);

        //инфо о загруженном классе
        System.out.println("Name of class "+nameClass+" "+ testClass.getName());

    }


}
