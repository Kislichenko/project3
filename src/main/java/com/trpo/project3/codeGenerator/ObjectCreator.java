package com.trpo.project3.codeGenerator;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.trpo.project3.analyze.ClassInformer;
import com.trpo.project3.analyze.ClassScanner;
import com.trpo.project3.dto.*;
import com.trpo.project3.generator.Generator;
import com.trpo.project3.generator.PrimitiveGenerator;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 *  Попыткки создать корректный конструтор будут предприниматься, начиная с конструктора
 *  с самым большим числом параметров, чтобы максимально изменить состояние объекта.
 *  Попыток подобрать корректные параметры для каждого конструктора будут предниматься
 *  ATTEMPT раз. Если для конструктора не удалось создать новый объект, то и тест/метод
 *  с этим объектом создаваться не будет.
 */
public class ObjectCreator {
    private final int ATTEMPT = 3;
    private List<String> primitives = Arrays.asList("int", "float", "byte", "long", "double",
            "char", "short", "boolean", "String", "int[]", "float[]", "byte[]", "long[]", "double[]",
            "char[]", "chort[]", "boolean[]", "String[]");

    Utils utils = new Utils();
    PrimitiveGenerator primitiveGenerator = new PrimitiveGenerator();


    // метод для создание объекта с простыми параметрами через конструктор
    public StringObject createSimpleObjectCons(Class cl){
        InfoClass infoClass = (new ClassInformer()).genFromClass(cl);

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
        while (infoConstructors.size()>0 && isCorrect == null) {
            for (InfoConstructor infoConstructor : infoConstructors) {
                if (infoConstructor.getParameters().size() == maxNumParam) {
                    for (int i = 0; i < ATTEMPT; i++) {
                        //пытаемся сгенерировать корректные параметры для конструтора
                        args = genArgs(infoConstructor);
                        System.out.println("TTTTTTT: "+args.getObjects());
                        System.out.println("TTTTTTT2: "+args.getGenArgs());

                        //проверяем корректность сгенерированных аргументов конструктора
                        isCorrect = checkObj(infoClass.getAClass(), infoConstructor.getParameters(), args.getObjects());

                        if(isCorrect != null) break;
                        System.out.println(isCorrect!=null);
                    }

                    //после ATTEMPT попыток создать объект с конструтором, удаляем его из списка доступных
                    //и начниаем по новой перебирать список доступных конструкторов
                    infoConstructors.remove(infoConstructor);

                    //мы, возможно, еще не прошли полностью все конструкторы с кол-вом параметров maxNumParam
                    //поэтому плюсуем единицу, чтобы декрементация после прерванного цикла не учитывалась
                    maxNumParam++;
                    break;
                }
            }
            maxNumParam--;
        }


        if(isCorrect!=null) {
            return new StringObject(isCorrect, utils.createConsA(cl.getSimpleName(), args.getGenArgs()));
        }else{
            return new StringObject(null, "");
        }
    }

    private GenArgs genArgs(InfoConstructor infoConstructor){
        ArrayList<InfoParameter> infoParameters = infoConstructor.getParameters();
        Object[] objects = new Object[infoParameters.size()];
        List<String> strings = new ArrayList<>();

        for (int i=0;i<infoParameters.size();i++){
            StringObject stringObject = null;

            if(primitives.contains(infoParameters.get(i).getType().getName())){
                //генерируем примитивы
                System.out.println("SIMPLE: " + infoParameters.get(i).getType().getName());
                stringObject = primitiveGenerator.getGenPrim(infoParameters.get(i).getType().getName());


            }else{

                //рекурсивно генерируем сложные объекты
                System.out.println("HARD: "+infoParameters.get(i).getType().getTypePackage());
                try {
                    stringObject  = createSimpleObjectCons(Class.forName(infoParameters.get(i).getType().getTypePackage()));

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            objects[i] = stringObject.getObject();

            //System.out.println("FFFF: "+stringObject.getObject().getClass().getSimpleName());
            strings.add(stringObject.getStrObject());
        }

        return new GenArgs(strings, objects);
    }

    //проверка того, что созданный коснтруктор не ломается
    private Object checkObj(Class cl, ArrayList<InfoParameter> infoParameters, Object[] args) {
        //собираем массив классов
        try {
            Class[] cArg = new Class[infoParameters.size()];

            for(int i=0;i<infoParameters.size();i++){
                System.out.println("CHECK: "+infoParameters.get(i).getType().getTypePackage());
                cArg[i] = getClass(infoParameters.get(i).getType().getTypePackage());
            }
            System.out.println();

            return checkCons(cl, cArg, args);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class getArrayClass(Class cl){
        return Array.newInstance(cl, 0).getClass();
    }

    private Class getClass(String typeName) throws ClassNotFoundException {
        if(typeName.contains("[]") && getPrimitiveClass(typeName.substring(0, typeName.length()-2))!=null) {
            return getArrayClass(getPrimitiveClass(typeName.substring(0, typeName.length()-2)));
        }else if (typeName.contains("[]")){
            return getArrayClass(Class.forName(typeName.substring(0, typeName.length()-2)));
        }else if(primitives.contains(typeName)&&!typeName.contains("[]")){
            return getPrimitiveClass(typeName);
        }else {
            return Class.forName(typeName);
        }
    }

    private Class getPrimitiveClass(String typeName) throws ClassNotFoundException {
        if(typeName.equals("int")) return int.class;
        else if(typeName.equals("long")) return long.class;
        else if(typeName.equals("float")) return float.class;
        else if(typeName.equals("double")) return double.class;
        else if(typeName.equals("short")) return short.class;
        else if(typeName.equals("boolean")) return boolean.class;
        else if(typeName.equals("char")) return char.class;
        else if(typeName.equals("byte")) return byte.class;
        return null;
    }

    private Object checkCons(Class cl, Class[] consArgs, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Arrays.stream(consArgs).forEach(arg -> System.out.println(arg.getSimpleName()));
        Arrays.stream(args).forEach(arg -> System.out.println("DD: "+arg));

        return cl.getDeclaredConstructor(consArgs).newInstance(args);

    }
}
