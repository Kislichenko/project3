package com.trpo.project3.codeGenerator;

import com.trpo.project3.dto.InfoClass;
import com.trpo.project3.dto.InfoConstructor;
import com.trpo.project3.dto.InfoParameter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class ConsCodeGenerator {

    public void genInitCons(InfoClass infoClass, ArrayList<InfoClass> infoClasses){
        Integer max = 0;
        InfoConstructor infoConstructor = null;
        for(int i=0;i<infoClass.getConstructors().size();i++){
            if(max<infoClass.getConstructors().get(i).getParameters().size()){
                max = infoClass.getConstructors().get(i).getParameters().size();
                infoConstructor = infoClass.getConstructors().get(i);

            }
        }

        ArrayList<InfoParameter> infoParameters = new ArrayList<>();
        for(int i=0;i<infoParameters.size();i++){
            if(infoParameters.get(i).getType().getTypePackage().equals(infoParameters.get(i).getType().getName())){
                genSimpleValue(infoParameters.get(i).getType().getName());
            }else {

            }
        }

    }


    private String genSimpleValue(String type){
        Integer integer = new Integer(12);
        return "";

    }
}
