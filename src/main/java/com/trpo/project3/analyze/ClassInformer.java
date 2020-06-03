package com.trpo.project3.analyze;

import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import com.trpo.project3.dto.*;

import java.lang.reflect.*;
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

        //сохранение конструкторов класса
        ArrayList<InfoConstructor> infoConstructors = new ArrayList<>();
        Constructor[] constructors = cl.getConstructors();
        for(int i=0;i<constructors.length;i++){
            InfoConstructor infoConstructor = new InfoConstructor();
            infoConstructor.setName(cl.getSimpleName());
            infoConstructor.setModifiers(Modifier.toString(constructors[i].getModifiers()));

            ArrayList<InfoParameter> infoParameters = new ArrayList<>();
            Paranamer paranamer = new BytecodeReadingParanamer();
            String[] parameterNames = paranamer.lookupParameterNames(constructors[i], false);
            Parameter[] parameters = constructors[i].getParameters();
            for(int j=0;j<parameters.length;j++){
                InfoParameter infoParameter = new InfoParameter();
                infoParameter.setName(parameterNames[j]);
                infoParameter.setType(getInfoTypeForParameters(parameters[j]));
                infoParameters.add(infoParameter);
            }
            infoConstructor.setParameters(infoParameters);
            infoConstructors.add(infoConstructor);

        }


        infoClass.setConstructors(infoConstructors);

        //сохранение полей класса
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

        //сохранение методов класса
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
                infoParameter.setType(getInfoTypeForParameters(parameters[k]));
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
            infoType.setTypePackage(field.getType().getTypeName());
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

    private InfoType getInfoTypeForParameters(Parameter param){
        InfoType infoType = new InfoType();
        infoType.setName(param.getType().getSimpleName());
        infoType.setTypePackage(param.getType().getTypeName());

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

        Constructor[] constructors = testClass.getConstructors();
        for(int i=0;i<constructors.length;i++){
            System.out.println("Con name: "+constructors[i].getName());
            System.out.println("Con str: "+constructors[i].toString());

            Paranamer paranamer = new BytecodeReadingParanamer();
            String[] parameterNames = paranamer.lookupParameterNames(constructors[i], false);
            Parameter[] parameters = constructors[i].getParameters();
            for(int j=0;j<parameters.length;j++){
                System.out.println("Con Param name0: "+parameterNames[j]);
                System.out.println("Con Param name1: "+parameters[j].getName());
                System.out.println("Con Param type: "+parameters[j].getType().getTypeName());
                System.out.println("Con Param type: "+parameters[j].getType().getSimpleName());
                System.out.println();
            }
        }


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
