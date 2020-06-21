package com.trpo.project3.codeGenerator;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.trpo.project3.analyze.ClassInformer;
import com.trpo.project3.dto.InfoClass;
import com.trpo.project3.dto.InfoConstructor;
import com.trpo.project3.dto.InfoParameter;
import com.trpo.project3.dto.StringObject;
import com.trpo.project3.generator.Generator;
import com.trpo.project3.generator.PrimitiveGenerator;

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
    public String createSimpleObjectCons(Class cl){
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
        boolean isCorrect = false;
        while (infoConstructors.size()>0 && !isCorrect) {
            for (InfoConstructor infoConstructor : infoConstructors) {
                if (infoConstructor.getParameters().size() == maxNumParam) {
                    for (int i = 0; i < ATTEMPT; i++) {
                        //пытаемся сгенерировать корректные параметры для конструтора
                        List<String> args = genArgs(infoConstructor);
                        System.out.println(utils.createConsA(cl.getSimpleName(), args));

                        //проверяем корректность сгенерированных аргументов конструктора
                        //isCorrect = checkObj(infoClass.getAClass(), null, args);

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


        return "";
    }

    private List<String> genArgs(InfoConstructor infoConstructor){
        ArrayList<InfoParameter> infoParameters = infoConstructor.getParameters();
        Object[] objects = new Object[infoParameters.size()];
        List<String> strings = new ArrayList<>();

        for (int i=0;i<infoParameters.size();i++){
            if(primitives.contains(infoParameters.get(i).getType().getName())){
                //генерируем примитивы
                StringObject stringObject = primitiveGenerator.getGenPrim(infoParameters.get(i).getType().getName());
                objects[i] = stringObject.getObject();
                strings.add(stringObject.getStrObject());
                //System.out.println(stringObject.getStrObject());
            }else{
                 //генерация сложных объектов
            }
        }

        return strings;
    }

    //проверка того, что созданный коснтруктор не ломается
    private boolean checkObj(Class cl, Parameter[] parameters, Object[] args) {
        //проверка
        boolean flag = false;

        try {
            Class[] cArg = new Class[parameters.length];

            for(int i=0;i<parameters.length;i++){
                System.out.println("YYYY: "+parameters[i].getType().getTypeName());
                if(parameters[i].getType().getTypeName().substring(parameters[i].getType().getTypeName().length()-2).equals("[]")){
                    cArg[i] = byte[].class;
                } else if(parameters[i].getType().getTypeName().equals("int")){
                    cArg[i] = int.class;
                }
                else{
                    cArg[i] = Class.forName(parameters[i].getType().getTypeName());
                }
            }

            checkCons(cl, cArg, args);
            System.out.println("RRRRRR1");
            flag = true;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        //}
        if(flag) return true;
        else return false;
    }

    private void checkCons(Class cl, Class[] consArgs, Object ... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        cl.getDeclaredConstructor(consArgs).newInstance(args);

    }
}
