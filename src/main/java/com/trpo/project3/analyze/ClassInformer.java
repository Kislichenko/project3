package com.trpo.project3.analyze;

import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import com.trpo.project3.dto.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

public class ClassInformer {


    public Class getClassByName(String className){
        ClassScanner classScanner = new ClassScanner();
        classScanner.scanPath();
        return classScanner.getClassByName(className);
    }

    public ArrayList<InfoClass> saveAllClasses(){
        ClassScanner classScanner = new ClassScanner();
        classScanner.scanPath();
        ArrayList<InfoClass> infoClasses = new ArrayList<>();
        for(int i=0;i<classScanner.getScannedClasses().size();i++){
            infoClasses.add(saveClass(classScanner.getScannedClasses().get(i)));
        }
        return infoClasses;
    }

    public InfoClass saveClass(Class cl){
        InfoClass infoClass = new InfoClass();
        infoClass.setName(cl.getSimpleName());
        infoClass.setClassPackage(cl.getPackage().getName());

        ArrayList<InfoField> infoFields = new ArrayList<>();
        Field[] fields = cl.getDeclaredFields();
        for(int i=0;i<fields.length;i++){
            InfoField infoField = new InfoField();
            infoField.setName(fields[i].getName());
            infoField.setModifiers(Modifier.toString(fields[i].getModifiers()));
            infoField.setType(getInfoTypeForField(fields[i]));
            infoFields.add(infoField);
        }
        infoClass.setFields(infoFields);

        ArrayList<InfoMethod> infoMethods = new ArrayList<>();
        Method[] methods = cl.getDeclaredMethods();
        for(int i=0;i<methods.length;i++) {
            InfoMethod infoMethod = new InfoMethod();
            infoMethod.setName(methods[i].getName());
            infoMethod.setModifiers(Modifier.toString(methods[i].getModifiers()));
            infoMethod.setReturnType(getInfoTypeForMethod(methods[i]));

            ArrayList<InfoParameter> infoParameters = new ArrayList<>();

            Paranamer paranamer = new BytecodeReadingParanamer();
            String[] parameterNames = paranamer.lookupParameterNames(methods[i], false);
            Parameter[] parameters = methods[i].getParameters();

            for (int k = 0; k < parameterNames.length; k++) {
                InfoParameter infoParameter = new InfoParameter();
                infoParameter.setName(parameterNames[k]);
                infoParameter.setType(parameters[k].getType().toString());
                infoParameters.add(infoParameter);
            }
            infoMethod.setParameters(infoParameters);
            infoMethods.add(infoMethod);
        }

        //System.out.println("AAAA: "+infoMethods.size());
        infoClass.setMethods(infoMethods);

        return infoClass;
    }

    private InfoType getInfoTypeForField(Field field){
        InfoType infoType = new InfoType();
        infoType.setName(field.getType().getSimpleName());
        if(field.getType().getPackage()!=null){
            infoType.setTypePackage(field.getType().getName());
        }else{
            infoType.setTypePackage("");
        }
        return infoType;
    }

    private InfoType getInfoTypeForMethod(Method method){
        InfoType infoType = new InfoType();
        infoType.setName(method.getReturnType().getSimpleName());
        if(method.getReturnType().getPackage()!=null){
            infoType.setTypePackage(method.getReturnType().getName());
        }else{
            infoType.setTypePackage("");
        }
            return infoType;
    }

    public void getInfoClass(String nameClass){
        ClassScanner classScanner = new ClassScanner();
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
