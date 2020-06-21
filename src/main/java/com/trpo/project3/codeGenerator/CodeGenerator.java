package com.trpo.project3.codeGenerator;


import com.google.googlejavaformat.java.FormatterException;
import com.trpo.project3.dto.InfoClass;
import com.trpo.project3.dto.InfoMethod;

import java.util.*;
import java.util.stream.Collectors;

import static com.trpo.project3.codeGenerator.CodeGenConstants.*;

/**
 * Класс генерации кода тестов по собранной информации.
 */
public class CodeGenerator {

    /**
     * Сгенерированные тесты для каждого тестируемого класса.
     */
    private Map<String, String> classTests = new HashMap<>();

    /**
     * Генерация тестов для всех тестируемых классов.
     *
     * @param infoClasses - массив объектов, содержащих всю необходимую информацию
     *                    тестируемых классах
     */
    public void genTests(ArrayList<InfoClass> infoClasses) {
        for (InfoClass infoClass : infoClasses) {
            classTests.put(infoClass.getName(), genTest(infoClass));
        }
    }

    /**
     * Печать тестов для определенного класса.
     *
     * @param className - класс, для которого нужно вывести тест.
     */
    public void printTestByNameClass(String className) {
        System.out.println("Test for class '" + className + "'!!!");
        System.out.println(classTests.get(className));
        System.out.println();
    }

    /**
     * Генерация набора тестов для одного тестируемого класса.
     *
     * @param infoClass - объект, содержащий всю необходимую инф-цию о тестируемом классе.
     * @return исходный код сгенерированных тестов для одного класса.
     */
    private String genTest(InfoClass infoClass) {

        String test = genPackage(infoClass.getClassPackage())
                + getAllHeaders(infoClass)
                + genClassA(infoClass.getName())
                + genBeforeConstructors(infoClass)
                + genMethodTests(infoClass.getMethods())
                + CLOSE_BLOCK;

        try {
            return new com.google.googlejavaformat.java.Formatter().formatSource(test);
        } catch (FormatterException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Генерация кода для начальной инициализации используемых классов.
     *
     * @param infoClass - объект, содержащий всю необходимую инф-цию о тестируемом классе.
     * @return - код инициализации используемых классов.
     */
    private String genBeforeConstructors(InfoClass infoClass) {
        return (new ConsCodeGenerator()).genInitCons(infoClass);
    }

    /**
     * Генерация строки, означающей принадлежность класса тестов к определнному пакету.
     *
     * @param packageName - имя пакета тестируемого класса.
     * @return строку пакета.
     */
    private String genPackage(String packageName) {
        return PACKAGE + packageName + END_LINE;
    }

    /**
     * Генерация названия класса тестов для определнной сущности.
     *
     * @param className - название тестируемого класса.
     * @return строку класса тестов без внутреннего содержимого.
     */
    private String genClassA(String className) {
        return CLASS + className + TEST + OPEN_BLOCK;
    }

    /**
     * Генерация строки импортов
     *
     * @param importPackageName - название пакета, который необходимо подключить
     *                          для обеспечения работоспособности класса тестов.
     * @return строку импорта пакета.
     */
    private String genImport(String importPackageName) {
        return IMPORT + importPackageName + END_LINE;
    }

    /**
     * Генерация всех импортов необходимых для работы.
     *
     * @return строка всех необходимых импортов.
     */
    private String getAllHeaders(InfoClass infoClass) {
        return getTestHeaders()
                + (new ClassImports())
                .getLinkImports(infoClass)
                .stream()
                .map(this::genImport)
                .collect(Collectors.joining());
    }

    /**
     * Генерация тестов для массива методов.
     *
     * @param infoMethods - массив объектов, содержащих необходимую информацию о тестируемых методах.
     * @return - исходный код тестов для тестирования массива методов.
     */
    private String genMethodTests(ArrayList<InfoMethod> infoMethods) {
        return infoMethods.stream().map(this::genMethodTest).collect(Collectors.joining());
    }

    /**
     * Получение необходимых заголовков для Junit тестов, которые находятся в файле ресурсов.
     *
     * @return - строка импортов заголовков для Junit тестов.
     */
    private String getTestHeaders() {
        return (new Utils()).readFile(TEST_HEADERS_FILE);
    }

    /**
     * Генерация кода теста для метода.
     *
     * @param infoMethod - объект, содержащий всю инф-цию о тестируемом методе.
     * @return - исходный код теста тестирумого метода.
     */
    private String genMethodTest(InfoMethod infoMethod) {
        return TEST_ANNOTATION
                + PUBLIC_VOID_TEST
                + infoMethod.getName()
                + EMPTY_BRACKETS
                + OPEN_BLOCK
                //+ (new MethodCodeGenerator()).genMthods(infoMethod)
                + CLOSE_BLOCK;
    }
}
