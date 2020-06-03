package com.trpo.project3.codeGenerator;

import com.trpo.project3.dto.InfoClass;
import com.trpo.project3.dto.InfoConstructor;
import com.trpo.project3.dto.InfoParameter;
import com.trpo.project3.dto.InfoType;
import com.trpo.project3.generator.PrimitiveGenerator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class ConsCodeGenerator {

    PrimitiveGenerator primitiveGenerator = new PrimitiveGenerator();

    public String genInitCons(InfoClass infoClass){
        Integer max = 0;
        InfoConstructor infoConstructor = null;

        if(infoClass.getConstructors()==null){
            return "";
        }else {
            infoConstructor = infoClass.getConstructors().get(0);
            if(infoClass.getConstructors().get(0).getParameters()!=null) {
                max = infoClass.getConstructors().get(0).getParameters().size();
            }
        }
        for(int i=1;i<infoClass.getConstructors().size();i++){
            if(infoClass.getConstructors().get(i).getParameters()!=null &&
                    max<=infoClass.getConstructors().get(i).getParameters().size()){
                max = infoClass.getConstructors().get(i).getParameters().size();
                infoConstructor = infoClass.getConstructors().get(i);
            }
        }

        String cons = ""+infoClass.getName()+
                " "+infoClass.getName().substring(0, 1).toLowerCase()
                + infoClass.getName().substring(1)+" = new "
                + infoClass.getName()+"(";

        if(infoConstructor.getParameters()!=null&&infoConstructor.getParameters().size()!=0) {
            ArrayList<InfoParameter> infoParameters = infoConstructor.getParameters();
            for (int i = 0; i < infoParameters.size(); i++) {
                System.out.println("AAA: "+infoParameters.get(i).getType().getTypePackage()+" ;; "+infoParameters.get(i).getType().getName());
                if (infoParameters.get(i).getType().getTypePackage().equals(infoParameters.get(i).getType().getName())) {
                    System.out.println("FFFF!!!");
                    cons = cons + primitiveGenerator.getGenPrimString(infoParameters.get(i).getType().getName()) + ",";
                } else {
                    //рекурсивное заполнение объектов
                }
            }
            cons = cons.substring(0,cons.length()-1)+"); ";
        }else{
            cons = cons+"); ";
        }

        return cons;

    }



}
