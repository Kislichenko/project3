package com.trpo.project3.analyze;

import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import com.trpo.project3.dto.*;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Класс для обработки и анализа всех пользовательских классов.
 */
public class ClassInformer {

    /**
     * Считываем все классы из classpath и получаем (сохраняем) всю необходимую информацию
     *
     * @return список объектов, содержащих необходимую информацию о классах.
     */
    public ArrayList<InfoClass> saveAllClasses() {
        ArrayList<InfoClass> infoClasses = new ArrayList<>();
        ClassScanner classScanner = new ClassScanner();
        classScanner.scanPath();

        classScanner.getScannedClasses().stream().forEach(cl -> infoClasses.add(saveClass(cl)));

        return infoClasses;
    }

    /**
     * Получение всей необходимой информации из любого (в том числе не пользовательского)
     * @param cl - анализируемый класс
     * @return - InfoClass
     */
    public InfoClass genFromClass(Class cl){
        return saveClass(cl);
    }

    /**
     * Метод для извлечения нужной информации (поля, методы, конструкторы) о классе.
     *
     * @param cl - анализируемый класс.
     * @return InfoClass.
     */
    private InfoClass saveClass(Class cl) {
        InfoClass infoClass = new InfoClass();

        //сохранение имени класса
        infoClass.setName(cl.getSimpleName());

        //сохранение пакета класса
        infoClass.setClassPackage(cl.getPackage().getName());

        //сохранение информации о конструкторах
        infoClass.setConstructors(analyzeConstructors(cl));

        //сохранение информации о полях
        infoClass.setFields(analyzeFields(cl));

        //сохранение информации о методах
        infoClass.setMethods(analyzeMethods(cl));

        //сохранение класса на всякий случай
        infoClass.setAClass(cl);

        return infoClass;
    }

    /**
     * метод для сохранения информации о полях класса.
     *
     * @param cl - анализируемый класс.
     * @return список объектов, содержащих всю необходимую инф-цию о полях анализируемого класса.
     */
    private ArrayList<InfoField> analyzeFields(Class cl) {
        ArrayList<InfoField> infoFields = new ArrayList<>();

        Arrays.stream(cl.getDeclaredFields()).forEach(field -> infoFields.add(InfoField.builder()
                .name(field.getName())
                .type(getFieldInfoType(field))
                .modifiers(Modifier.toString(field.getModifiers()))
                .build()
        ));

        return infoFields;
    }

    /**
     * Метод для сохранения всей необходимой информации о методах аналируемого класса.
     *
     * @param cl - анализируемый класс.
     * @return список обхектов, содержащих всю необходимую инф-цию о методах.
     */
    private ArrayList<InfoMethod> analyzeMethods(Class cl) {
        ArrayList<InfoMethod> infoMethods = new ArrayList<>();

        Arrays.stream(cl.getDeclaredMethods()).forEach(method -> infoMethods.add(InfoMethod.builder()
                .name(method.getName())
                .returnType(getReturnMethodType(method))
                .modifiers(Modifier.toString(method.getModifiers()))
                .nameClass(cl.getSimpleName())
                .parameters(analyzeParameters(method))
                .build()

        ));

        return infoMethods;
    }

    /**
     * Метод для сохранения всей необходимой инф-ции о конструкторах анализируемого класса.
     *
     * @param cl - анализируемый класс.
     * @return список объектов, которые содержат необходимую инф-цию о конструкторах анализируемого класса.
     */
    private ArrayList<InfoConstructor> analyzeConstructors(Class cl) {
        ArrayList<InfoConstructor> infoConstructors = new ArrayList<>();

        Arrays.stream(cl.getConstructors()).forEach(constructor -> infoConstructors.add(InfoConstructor.builder()
                .name(cl.getSimpleName())
                .modifiers(Modifier.toString(constructor.getModifiers()))
                .parameters(analyzeParameters(constructor))
                .build()

        ));

        return infoConstructors;
    }

    /**
     * Метод для анализа и сохранения всей необходимой информации о параметрах методов
     * и конструкторов.
     *
     * @param obj - метод или конструктор класса через reflection
     * @return - набор объектов с нужной информацией о параметрах.
     */
    private ArrayList<InfoParameter> analyzeParameters(Executable obj) {

        ArrayList<InfoParameter> infoParameters = new ArrayList<>();
        Parameter[] parameters = obj.getParameters();

        //анализ имен параметров метода или конструктора из байт-кода
        String[] parameterNames = (new BytecodeReadingParanamer())
                .lookupParameterNames(obj, false);

        IntStream.range(0, parameters.length).forEach(i -> {
            infoParameters.add(InfoParameter
                    .builder()
                    .name(parameterNames[i])
                    .type(getParameterInfoType(parameters[i]))
                    .build());
        });

        return infoParameters;
    }

    private InfoType getFieldInfoType(Field field) {
        InfoType infoType = new InfoType();
        infoType.setName(field.getType().getSimpleName());

        if (field.getType().getPackage() != null) {
            infoType.setTypePackage(field.getType().getTypeName());
        } else {
            infoType.setTypePackage("");
        }
        return infoType;
    }

    private InfoType getReturnMethodType(Method method) {
        InfoType infoType = new InfoType();
        infoType.setName(method.getReturnType().getSimpleName());

        if (method.getReturnType().getPackage() != null) {
            infoType.setTypePackage(method.getReturnType().getName());
        } else {
            infoType.setTypePackage("");
        }
        return infoType;
    }

    private InfoType getParameterInfoType(Parameter param) {
        InfoType infoType = new InfoType();
        infoType.setName(param.getType().getSimpleName());

        if (param.getType().getPackage() != null) {
            infoType.setTypePackage(param.getType().getTypeName());
        } else {
            infoType.setTypePackage("");
        }

        infoType.setTypePackage(param.getType().getTypeName());

        return infoType;
    }

    public void getInfoClass(String nameClass) {
        ClassScanner classScanner = new ClassScanner();
        classScanner.scanPath();
        Class testClass = classScanner.getClassByName(nameClass);

        //инфо о загруженном классе
        System.out.println("Name of class " + nameClass + " " + testClass.getName());
        System.out.println("Package of class: " + testClass.getPackage().getName());
        System.out.println(" ");

        Constructor[] constructors = testClass.getConstructors();
        for (int i = 0; i < constructors.length; i++) {
            System.out.println("Con name: " + constructors[i].getName());
            System.out.println("Con str: " + constructors[i].toString());

            Paranamer paranamer = new BytecodeReadingParanamer();
            String[] parameterNames = paranamer.lookupParameterNames(constructors[i], false);
            Parameter[] parameters = constructors[i].getParameters();
            for (int j = 0; j < parameters.length; j++) {
                System.out.println("Con Param name0: " + parameterNames[j]);
                System.out.println("Con Param name1: " + parameters[j].getName());
                System.out.println("Con Param type: " + parameters[j].getType().getTypeName());
                System.out.println("Con Param type: " + parameters[j].getType().getSimpleName());
                System.out.println();
            }
        }


        //поля
        Field[] fields = testClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            System.out.println("Field name: " + fields[i].getName());
            System.out.println("Field modifier: " + Modifier.toString(fields[i].getModifiers()));
            System.out.println("Field type: " + fields[i].getGenericType());
            System.out.println(" ");
        }


        //методы
        Method[] methods = testClass.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            System.out.println("Method name: " + methods[i].getName());
            System.out.println("Method return type: " + methods[i].getReturnType().toString());
            System.out.println("Method modifier: " + Modifier.toString(methods[i].getModifiers()));


            //получаем названия параметров метода и их типы
            Paranamer paranamer = new BytecodeReadingParanamer();
            String[] parameterNames = paranamer.lookupParameterNames(methods[i], false);
            Parameter[] parameters = methods[i].getParameters();

            for (int k = 0; k < parameterNames.length; k++) {
                System.out.println("Parameter's names for Method " + methods[i].getName() + ": " + parameterNames[k]);
                System.out.println("Parameter: " + parameters[k].getType());
            }

            System.out.println("");
        }


    }


}
