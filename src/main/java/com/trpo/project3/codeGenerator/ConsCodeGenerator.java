package com.trpo.project3.codeGenerator;

import com.thoughtworks.paranamer.Paranamer;
import com.thoughtworks.paranamer.PositionalParanamer;
import com.trpo.project3.dto.InfoClass;
import com.trpo.project3.dto.InfoConstructor;
import com.trpo.project3.dto.InfoParameter;
import com.trpo.project3.generator.PrimitiveGenerator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Random;

public class ConsCodeGenerator {

    PrimitiveGenerator primitiveGenerator = new PrimitiveGenerator();
    private final int repeat = 3;

    public String genInitCons(InfoClass infoClass) {
        Integer max = 0;
        InfoConstructor infoConstructor = null;

        if (infoClass.getConstructors() == null) {
            return "";
        } else {
            infoConstructor = infoClass.getConstructors().get(0);
            if (infoClass.getConstructors().get(0).getParameters() != null) {
                max = infoClass.getConstructors().get(0).getParameters().size();
            }
        }
        for (int i = 1; i < infoClass.getConstructors().size(); i++) {
            if (infoClass.getConstructors().get(i).getParameters() != null &&
                    max <= infoClass.getConstructors().get(i).getParameters().size()) {
                max = infoClass.getConstructors().get(i).getParameters().size();
                infoConstructor = infoClass.getConstructors().get(i);
            }
        }

        String initBefore = "";
        String cons = "" + infoClass.getName() +
                " " + infoClass.getName().substring(0, 1).toLowerCase()
                + infoClass.getName().substring(1) + " = new "
                + infoClass.getName() + "(";

        if (infoConstructor.getParameters() != null && infoConstructor.getParameters().size() != 0) {
            ArrayList<InfoParameter> infoParameters = infoConstructor.getParameters();
            for (int i = 0; i < infoParameters.size(); i++) {
                System.out.println("AAA: " + infoParameters.get(i).getType().getTypePackage() + " ;; " + infoParameters.get(i).getType().getName());
                if (infoParameters.get(i).getType().getTypePackage().equals(infoParameters.get(i).getType().getName())) {
                    System.out.println("FFFF!!!");
                    cons = cons + primitiveGenerator.getGenPrimString(infoParameters.get(i).getType().getName()) + ",";
                } else {
                    initBefore = genForHardObj(infoParameters.get(i).getType().getTypePackage(), infoParameters.get(i).getName());
                    cons = cons + infoParameters.get(i).getName() + ",";
                }
            }
            cons = cons.substring(0, cons.length() - 1) + "); ";
        } else {
            cons = cons + "); ";
        }

        return initBefore + cons;

    }

    private String genForHardObj(String className, String argName) {
        System.out.println("HHH: " + className);
        try {
            Class c = Class.forName(className);
            Constructor[] constructors = c.getConstructors();

            int max = 0;
            int index = -1;

            for (int i = 0; i < constructors.length; i++) {
                if (constructors[i].getParameters() != null && max < constructors[i].getParameters().length) {
                    max = constructors[i].getParameters().length;
                    index = i;
                }
            }


            if (index == -1) return "";

            int innerIndex = index + 1;
            int innerMax = max;
            int counter = 3;//после 3 проходов во избежание зацикливания выходим (по хорошему нужно кидать ошибку)

            while (1 == 1) {
                System.out.println(constructors.length);

                while (constructors.length == 1 || innerIndex % constructors.length != index) {


                    System.out.println("GGGG" + (constructors[innerIndex % constructors.length].getParameters().length == innerMax));
                    if (constructors[innerIndex % constructors.length].getParameters() != null && constructors[innerIndex % constructors.length].getParameters().length == innerMax) {

                        for (int i = 0; i < repeat; i++) {
                            System.out.println("KKKKK");
                            String genStr = genHard(c, constructors[innerIndex % constructors.length], argName);
                            if (!genStr.equals("")) return genStr;
                        }
                    }
                    innerIndex++;
                    //System.out.println("HHHHHH: "+innerIndex + " " +index + " ;; "+constructors.length);
                }

                innerMax--;
                counter--;
                innerIndex++;


                //System.out.println("YYYY "+innerIndex +" ; "+innerIndex % constructors.length+" ; "+index);
                if (innerMax < 0) innerMax = max;
                //if (counter == 0) return "";
            }

            //c.newInstance();
            //System.out.println("HHH1: " + constructors.length);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "";

    }

    private String genHard(Class cl, Constructor constructor, String name) {
        String str = "";

        PrimitiveGenerator primitiveGenerator = new PrimitiveGenerator();
        if (constructor.getParameters() != null) {

            Paranamer paranamer = new PositionalParanamer();
            String[] parameterNames = paranamer.lookupParameterNames(constructor, false);


            for (int i = 0; i < constructor.getParameters().length; i++) {
                if (constructor.getParameters()[i].getType().getSimpleName().equals(constructor.getParameters()[i].getType().getTypeName())) {


                    str = str + constructor.getParameters()[i].getType().getSimpleName() + " " + cl.getSimpleName() + parameterNames[i] +
                            " = " + primitiveGenerator.getGenPrimString(constructor.getParameters()[i].getType().getSimpleName()) + "; ";

                } else {
                    //запуск рекурсии
                }

            }
        } else {
            str = str + cl.getSimpleName() + " " + name + " = new " + cl.getSimpleName() + "(); ";
        }

        str = str + cl.getSimpleName() + " " + cl.getSimpleName().substring(0, 1).toLowerCase()
                + cl.getSimpleName().substring(1) + " = " + "new " + cl.getSimpleName() + "(";
        for (int i = 0; i < constructor.getParameters().length; i++) {
            str = str + cl.getSimpleName() + constructor.getParameters()[i].getName() + ",";
        }

        System.out.println("UUUU: " + name + " ; " + str.substring(0, str.length() - 1) + "); ");
        return str.substring(0, str.length() - 1) + "); ";
    }

    private boolean checkObj() {
        //проверка
        boolean flag = false;
        while (!flag) {
            try {
                System.out.println("RRRRRR0");

                Class[] cArg = new Class[2];
                cArg[0] = int.class;
                cArg[1] = int.class;

                Random random = new Random();
                int count = random.nextInt(4);
                System.out.println("Count: " + count);
                Object[] args = new Object[count];
                for (int i = 0; i < count; i++) {
                    args[i] = i * 10;
                }

                //checkCons(infoClass.getAClass(), cArg, args);
                System.out.println("RRRRRR1");
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
