package com.trpo.project3.codeGenerator;

import com.trpo.project3.analyze.ClassInformer;
import com.trpo.project3.analyze.ClassScanner;
import com.trpo.project3.dto.*;
import com.trpo.project3.generator.PrimitiveGenerator;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Попытки создать корректный конструтор будут предприниматься, начиная с конструктора
 * с самым большим числом параметров, чтобы максимально изменить состояние объекта.
 * Попыток подобрать корректные параметры для каждого конструктора будут предниматься
 * ATTEMPT раз. Если для конструктора не удалось создать новый объект, то и тест/метод
 * с этим объектом создаваться не будет.
 */
public class ObjectCreator {
    private final int ATTEMPT = 3;
    private Utils utils;
    private List<String> primitives;

    public ObjectCreator(){
        utils = new Utils();
        primitives = Arrays.asList("int", "float", "byte", "long", "double",
                "char", "short", "boolean", "String", "int[]", "float[]", "byte[]", "long[]", "double[]",
                "char[]", "short[]", "boolean[]", "Class");
    }


    /**
     * Генерация строки кода инициализации конструктора. !!! обновить
     *
     * @param infoClass - объект, содержащий всю необходимую инф-цию классе.
     * @return - строка кода инициализации конструктора.
     */
    public StringObject createCons(InfoClass infoClass) {
        return createObjectCons(infoClass);
    }

    /**
     * Генерация объектов и строки исходного кода инциализации конструктора.
     *
     * @param cl - класс, для которого производится генерация.
     * @return - сгенерированные объекты и строка кода инициализации.
     */
    public StringObject createObjectConsByClass(Class cl) {
        return createObjectCons((new ClassInformer()).genFromClass(cl));
    }

    /**
     * Генерация строки исходного кода для вызова метода с рекурсивным заполнением параметров.
     *
     * @param infoMethod - объект, содержащий всю необходимую инф-цию о методе, для которого
     *                   производится генерация кода.
     * @return строка исходного кода для вызова метода с рекурсивным заполнением параметров
     */
    public GenArgs createObjectMethods(InfoMethod infoMethod) {
        if(infoMethod.getModifiers().contains("public")) {
            //рекурсивно генерируем аргументы метода
            return genArgs(infoMethod.getParameters());
        }else{
            return null;
        }
    }

    /**
     * Метод для создания объекта с параметрами через конструктор.
     *
     * @param infoClass - объект, содержащий всю необходимую инф-цию классе.
     * @return объект и строка инициализации конструктора.
     */
    public StringObject createObjectCons(InfoClass infoClass) {

        //получаем список всех конструторов класса
        ArrayList<InfoConstructor> infoConstructors = infoClass.getConstructors();

        // находим конструктор с наибольшим числом параметров
        int maxNumParam = 0;
        for (InfoConstructor infoConstructor : infoConstructors) {
            if (maxNumParam < infoConstructor.getParameters().size()) {
                maxNumParam = infoConstructor.getParameters().size();
            }
        }

        //перебираем все конструкторы до тех пор, пока не останется ни одного конструктора
        //к которому не были применено ATTEMPT попыток создать объект
        Object isCorrect = null;
        GenArgs args = null;
        while (infoConstructors.size() > 0 && isCorrect == null) {
            for (InfoConstructor infoConstructor : infoConstructors) {
                if (infoConstructor.getParameters().size() == maxNumParam) {
                    for (int i = 0; i < ATTEMPT; i++) {
                        //пытаемся сгенерировать корректные параметры для конструтора
                        args = genArgs(infoConstructor.getParameters());

                        //проверяем корректность сгенерированных аргументов конструктора
                        isCorrect = checkObj(infoClass.getAClass(), infoConstructor.getParameters(), args.getObjects());

                        if (isCorrect != null) break;
                    }

                    //после ATTEMPT попыток создать объект с конструтором, удаляем его из списка доступных
                    //и начниаем по новой перебирать список доступных конструкторов
                    infoConstructors.remove(infoConstructor);

                    //мы, возможно, еще не прошли полностью все конструкторы с кол-вом параметров maxNumParam
                    //поэтому плюсуем единицу, чтобы декрементация после прерванного цикла не учитывалась
                    //Если мы нашли нужный конструктор, то while останосится по не нулевому параметру isCorrect
                    maxNumParam++;
                    break;
                }
            }
            maxNumParam--;
        }

        if (isCorrect != null) { //если нужный конструктор был найден
            return new StringObject(isCorrect, utils.createConsA(infoClass.getName(), args.getGenArgs()), args.getHeaders());
        } else { //если конструктор не удалось найти/создать
            return new StringObject(null, "", null);
        }
    }


    /**
     * Метод рекурсивной генерации и заполнения аргументов конструктора/метода.
     * Рекурсия идет до тех пор, пока не появится объект или без параметров, или с примитивными параеметрами,
     * или не не получится этот объект создать.
     *
     * @param infoParameters - набор параметров метода/конструктора.
     * @return - сгенерированные параметры и строка исходного кода, которая соотвествует сгенерированным параметрам.
     */
    private GenArgs genArgs(ArrayList<InfoParameter> infoParameters) {

        //массив объектов, в которые будут скалдываться сгенерированные рекурсивно аргументы, для
        //проверки корректности создания анализируемого объекта.
        Object[] objects = new Object[infoParameters.size()];

        //сгенерированные объекты в виде строки
        List<String> strings = new ArrayList<>();

        Set<String> headers = new HashSet<>();

        //перебираем все параметры
        for (int i = 0; i < infoParameters.size(); i++) {
            StringObject stringObject = null;

            //проверяем, является ли параметр примитивом (задан список примтивов)
            if (primitives.contains(infoParameters.get(i).getType().getName())) {

                //генерируем примитив (объект + строка исходного кода)
                stringObject = (new PrimitiveGenerator()).getGenPrim(infoParameters.get(i).getType().getName());
            } else { //рекурсивно генерируем сложные объекты

                //получаем имя пакета
                List<String> strName = new ArrayList<>();
                strName.add(infoParameters.get(i).getType().getTypePackage());

                //если аргумент метода/конструктора является интерфейсом или абстрактным классом,
                //то его нельзя инициализировать напрямую. Нужно сначала найти класс, который
                //имплементирует данный интерфейс, затем подменить интерфейс на
                //имплементирующий класс.
                if (infoParameters.get(i).getModifiers().contains("interface") ||
                        infoParameters.get(i).getModifiers().contains("abstract")) {

                    //ищем имплементирующий класс и заменяем пакет
                    try {
                        if (strName.get(0).contains("[]")) {
                            strName = findImplForInterface(getArrayClass(Class.forName(strName.get(0).substring(0, strName.get(0).indexOf("[]")))));
                        }else {
                            strName = findImplForInterface(Class.forName(strName.get(0)));
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                //если интерфейс, то перебираем все имеплемнтирующие классы, пока не найдем корректный
                for (int j = 0; j < strName.size(); j++) {
                    try {
                        if (strName.get(j).contains("[]")) {

                            //генерация сложного аргумента через конструктор, если аргумент еще и простой массив
                            stringObject = createObjectConsByClass(getArrayClass(Class.forName(strName.get(j).substring(0, strName.get(j).indexOf("[]")))));
                        } else {

                            //генерация сложного аргумента через конструктор, если аргумент не является простым массиовм
                            stringObject = createObjectConsByClass(Class.forName(strName.get(j)));
                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (stringObject.getStrObject() != null&&!strName.get(j).contains("[]")) {
                        headers.add(strName.get(j));
                        break;
                    }
                }
            }

            if(stringObject!=null) {
                objects[i] = stringObject.getObject();
                strings.add(stringObject.getStrObject());
                if(stringObject.getHeaders()!=null) {
                    headers.addAll(stringObject.getHeaders());
                }
            }
        }

        return new GenArgs(strings, objects, headers);
    }

    /**
     * если аргумент метода/конструктора является интерфейсом или абстрактным классом,
     * то его нельзя инициализировать напрямую. Нужно сначала найти класс, который
     * имплементирует данный интерфейс, затем подменить интерфейс на
     * имплементирующий класс.
     *
     * @return - новый пакет, для которого будем генеировать.
     */
    private List<String> findImplForInterface(Class aClass) {

        List<String> classesTobeReturned = new ArrayList<String>();
        if (aClass.getProtectionDomain().getCodeSource()==null || aClass.getProtectionDomain().getCodeSource().getLocation().getPath().contains("target/classes")) {
            ClassScanner classScanner = new ClassScanner();
            classScanner.scanPath();
            List<Class> classes = classScanner.getScannedClasses();

            for (int i = 0; i < classes.size(); i++) {
                if (aClass.isAssignableFrom(classes.get(i))) {
                    if (!Modifier.toString(classes.get(i).getModifiers()).contains("abstract") &&
                            !Modifier.toString(classes.get(i).getModifiers()).contains("interface")) {

                        classesTobeReturned.add(classes.get(i).getName());
                    }
                }
            }

            return classesTobeReturned;
        } else {
            try {
                return utils.findClassesInJar(aClass, aClass.getProtectionDomain().getCodeSource().getLocation().getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
        //собираем список классов, которые исплементируют нужный нам интерфейс
    }

    /**
     * Проверка того, что созданный коснтруктор не ломается.
     *
     * @param cl             - класс, для которого проверяется конструктор
     * @param infoParameters - параметры целевого класса
     * @param args           - массив сгенерированных аргументов (объекты)
     * @return - объект сгенеированного класса.
     */
    private Object checkObj(Class cl, ArrayList<InfoParameter> infoParameters, Object[] args) {
        //собираем массив классов
        try {
            Class[] cArg = new Class[infoParameters.size()];

            for (int i = 0; i < infoParameters.size(); i++) {
                cArg[i] = getClass(infoParameters.get(i).getType().getTypePackage());
            }

            //проверяем корретность проверки путем
            return checkCons(cl, cArg, args);

        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }

    /**
     * получаем класс для простых массивов любого типа.
     *
     * @param cl - класс типа (не массива)
     * @return - класс массива входного класса
     */
    private Class getArrayClass(Class cl) {
        return Array.newInstance(cl, 0).getClass();
    }

    private Class getClass(String typeName) throws ClassNotFoundException {
        String simpleType = typeName.substring(0, typeName.length() - 2);

        if (typeName.contains("[]") && getPrimitiveClass(simpleType) != null) {
            return getArrayClass(getPrimitiveClass(simpleType));
        } else if (typeName.contains("[]")) {
            return getArrayClass(Class.forName(simpleType));
        } else if (primitives.contains(typeName) && !typeName.contains("[]")) {
            return getPrimitiveClass(typeName);
        } else {
            return Class.forName(typeName);
        }
    }

    /**
     * Метод для получения примтивных классов.
     *
     * @param typeName - имя примитивного класса.
     * @return - класс необходимого примитивного типа
     * @throws ClassNotFoundException
     */
    private Class getPrimitiveClass(String typeName) throws ClassNotFoundException {
        if (typeName.equals("int")) return int.class;
        else if (typeName.equals("long")) return long.class;
        else if (typeName.equals("float")) return float.class;
        else if (typeName.equals("double")) return double.class;
        else if (typeName.equals("short")) return short.class;
        else if (typeName.equals("boolean")) return boolean.class;
        else if (typeName.equals("char")) return char.class;
        else if (typeName.equals("byte")) return byte.class;
        return null;
    }

    /**
     * Проверяется, что объект создается с входными сгенерированными аргументами.
     *
     * @param cl       - класс, для которого производится проверка
     * @param consArgs - массив классов(типов) для каждого аргумента, чтобы определить нужный конструктор
     * @param args     - сгенерированные аргументы(должны соотвествовать по типу consArgs)
     * @return в случае успех сгенерированный объект с входными аргументами, в противном случае null
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private Object checkCons(Class cl, Class[] consArgs, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //плохая защита от переаолнения массивов
//        if (cl.getSimpleName().contains("List")) {
//            return null;
//        }

        return cl.getDeclaredConstructor(consArgs).newInstance(args);
    }
}
