package com.trpo.project3.codeGenerator;

import com.thoughtworks.paranamer.Paranamer;
import com.thoughtworks.paranamer.PositionalParanamer;
import com.trpo.project3.dto.InfoClass;
import com.trpo.project3.dto.InfoMethod;
import com.trpo.project3.dto.InfoMethod;
import com.trpo.project3.dto.InfoParameter;
import com.trpo.project3.generator.PrimitiveGenerator;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

public class MethodCodeGenerator {

    PrimitiveGenerator primitiveGenerator = new PrimitiveGenerator();
    private final int repeat = 3;

    public String genMthods(InfoClass infoClass) {
        Integer max = 0;
        InfoMethod infoMethod = null;

        if (infoClass.getMethods() == null || infoClass.getMethods().size()==0) {
            return "";
        } else {
            infoMethod = infoClass.getMethods().get(0);
            if (infoClass.getMethods().get(0).getParameters() != null) {
                max = infoClass.getMethods().get(0).getParameters().size();
            }
        }
        for (int i = 1; i < infoClass.getMethods().size(); i++) {
            if (infoClass.getMethods().get(i).getParameters() != null &&
                    max <= infoClass.getMethods().get(i).getParameters().size()) {
                max = infoClass.getMethods().get(i).getParameters().size();
                infoMethod = infoClass.getMethods().get(i);
            }
        }

        String initBefore = "";
        String cons = ""+ infoClass.getName().substring(0, 1).toLowerCase()
                + infoClass.getName().substring(1) + "."
                + infoMethod.getName() + "(";

        if (infoMethod.getParameters() != null && infoMethod.getParameters().size() != 0) {
            ArrayList<InfoParameter> infoParameters = infoMethod.getParameters();
            for (int i = 0; i < infoParameters.size(); i++) {
                System.out.println("AAA: " + infoParameters.get(i).getType().getTypePackage() + " ;; " + infoParameters.get(i).getType().getName());
                if (infoParameters.get(i).getType().getTypePackage().equals(infoParameters.get(i).getType().getName())) {
                    System.out.println("FFFF!!!");
                    cons = cons + primitiveGenerator.getGenPrimString(infoParameters.get(i).getType().getName()) + ",";
                } else {
                    //initBefore = initBefore+genForHardObj(infoParameters.get(i).getType().getTypePackage(), infoParameters.get(i).getName());
                    System.out.println("INIT BEFORE "+ initBefore);
                    cons = cons + infoParameters.get(i).getName() + ",";
                }
            }
            cons = cons.substring(0, cons.length() - 1) + "); ";
        } else {
            cons = cons + "); ";
        }
        return cons;
        //System.out.println("INIT BEFORE9 "+ cons);
        //System.out.println("INIT BEFORE1 "+ initBefore);

        //return initBefore + cons;

    }

    private String genForHardObj(String className, String argName) {
        //System.out.println("HHH: " + className);
        try {
            Class c = Class.forName(className);
            Method[] Methods = c.getMethods();

            int max = 0;
            int index = -1;

            for (int i = 0; i < Methods.length; i++) {
                if (Methods[i].getParameters() != null && max < Methods[i].getParameters().length) {
                    max = Methods[i].getParameters().length;
                    index = i;
                }
            }


            if (index == -1) return "";

            int innerIndex = index + 1;
            int innerMax = max;
            int counter = 3;//после 3 проходов во избежание зацикливания выходим (по хорошему нужно кидать ошибку)

            while (1 == 1) {
                System.out.println(Methods.length);

                while (Methods.length == 1 || innerIndex % Methods.length != index) {


                    //System.out.println("GGGG" + (Methods[innerIndex % Methods.length].getParameters().length == innerMax));
                    if (Methods[innerIndex % Methods.length].getParameters() != null && Methods[innerIndex % Methods.length].getParameters().length == innerMax) {

                        for (int i = 0; i < repeat; i++) {
                            //System.out.println("KKKKK");
                            //System.out.println("DDDD: "+Methods.length);
                            String genStr = genHard(c, Methods[innerIndex % Methods.length], argName);
                            if (!genStr.equals("")) return genStr;
                        }
                    }
                    innerIndex++;
                    //System.out.println("HHHHHH: "+innerIndex + " " +index + " ;; "+Methods.length);
                }

                innerMax--;
                counter--;
                innerIndex++;


                //System.out.println("YYYY "+innerIndex +" ; "+innerIndex % Methods.length+" ; "+index);
                if (innerMax < 0) innerMax = max;
                //if (counter == 0) return "";
            }

            //c.newInstance();
            //System.out.println("HHH1: " + Methods.length);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "";

    }

    private String genHard(Class cl, Method Method, String name) {
        String str = "";

        PrimitiveGenerator primitiveGenerator = new PrimitiveGenerator();
        if (Method.getParameters() != null) {

            Paranamer paranamer = new PositionalParanamer();
            String[] parameterNames = paranamer.lookupParameterNames(Method, false);


            Object[] args = new Object[Method.getParameters().length];
            for (int i = 0; i < Method.getParameters().length; i++) {
                if (Method.getParameters()[i].getType().getSimpleName().equals(Method.getParameters()[i].getType().getTypeName())) {

                    args[i] = primitiveGenerator.getGenPrimObj(Method.getParameters()[i].getType().getSimpleName());

//                    str = str + Method.getParameters()[i].getType().getSimpleName() + " " + cl.getSimpleName() + parameterNames[i] +
//                            " = " + primitiveGenerator.getGenPrimString(Method.getParameters()[i].getType().getSimpleName()) + "; ";

                } else {
                    System.out.println("TTTTT0: "+Method.getParameters()[i].getType().getTypeName() + " ; "+Method.getParameters()[i].getName());
                    //String str1 = genForHardObj(Method.getParameters()[i].getType().getTypeName(), Method.getParameters()[i].getName());
                    //System.out.println("TTTTT1: "+str1);
                    //запуск рекурсии
                }

            }
            if(checkObj(cl, Method.getParameters(), args)){
                for(int i=0;i<Method.getParameters().length;i++){
                    str = str + Method.getParameters()[i].getType().getSimpleName() + " " + cl.getSimpleName() + parameterNames[i] +
                            " = " + String.valueOf(args[i]) + "; ";
                }
            }else {
                return "";
            }

        } else {
            str = str + cl.getSimpleName() + " " + name + " = new " + cl.getSimpleName() + "(); ";
        }


        str = str + cl.getSimpleName() + " " + name + " = " + "new " + cl.getSimpleName() + "(";

        for (int i = 0; i < Method.getParameters().length; i++) {
            str = str + cl.getSimpleName() + Method.getParameters()[i].getName() + ",";
        }
        if(Method.getParameters().length>0){
            System.out.println("UUUU: " + name + " ; " + str.substring(0, str.length() - 1) + "); ");
            return str.substring(0, str.length() - 1) + "); ";
        }else{
            System.out.println("UUUU: " + name + " ; " +str + "); ");
            return str+ "); ";
        }


        //return
    }

    private boolean checkObj(Class cl, Parameter[] parameters, Object[] args) {
        return true;
//        //проверка
//        boolean flag = false;
//        //while (!flag) {
//        try {
//            System.out.println("RRRRRR0");
//
//            Class[] cArg = new Class[parameters.length];
//            for(int i=0;i<parameters.length;i++){
//                System.out.println("YYYY: "+parameters[i].getType().getTypeName());
//                if(parameters[i].getType().getTypeName().substring(parameters[i].getType().getTypeName().length()-2).equals("[]")){
//                    cArg[i] = byte[].class;
//                } else if(parameters[i].getType().getTypeName().equals("int")){
//                    cArg[i] = int.class;
//                }
//                else{
//                    cArg[i] = Class.forName(parameters[i].getType().getTypeName());
//                }
//            }
//
//            //checkCons(cl, cArg, args);
//            System.out.println("RRRRRR1");
//            flag = true;
//        } catch (Exception e) {
//            //e.printStackTrace();
//        }
//        //}
//        if(flag) return true;
//        else return false;
    }

//    private void checkCons(Class cl, Class[] consArgs, Object ... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        cl.getDeclaredMethod(consArgs).newInstance(args);
//
//    }


}
