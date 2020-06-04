package com.trpo.project3.codeGenerator;

import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import com.thoughtworks.paranamer.PositionalParanamer;
import com.trpo.project3.dto.*;
import com.trpo.project3.dto.InfoMethod;
import com.trpo.project3.generator.PrimitiveGenerator;

import java.lang.reflect.*;
import java.util.ArrayList;

public class MethodCodeGenerator {

    PrimitiveGenerator primitiveGenerator = new PrimitiveGenerator();
    ConsCodeGenerator consCodeGenerator = new ConsCodeGenerator();
    ArrayList<String> beforeInits = new ArrayList<>();
    private final int repeat = 3;

    public String genMthods(InfoMethod infoMethod) {
        String methodInv = "";
        methodInv = methodInv + infoMethod.getNameClass().substring(0, 1).toLowerCase()+
                infoMethod.getNameClass().substring(1)+"."+infoMethod.getName()+"( ";

        String params="";

        if(infoMethod.getParameters()==null || infoMethod.getParameters().size() ==0){
            return methodInv+params+"); ";
        }

        for(int i=0;i<infoMethod.getParameters().size();i++){
            String pr = getGenParams(infoMethod.getParameters().get(i));
            if(pr.equals("1")) {
                params = params + infoMethod.getParameters().get(i).getName() + ",";
            }else{
                params = params + pr + ",";
            }
        }

        methodInv = methodInv+params.substring(0,params.length()-1)+"); ";

        String tmp = "";
        for(int i=0;i<beforeInits.size();i++){
            tmp = tmp + beforeInits.get(i);
        }

        beforeInits.clear();
        return tmp + methodInv;


    }

    public String getGenParams(InfoParameter infoPar){

        String str = "1";
        if(infoPar.getType().getName().equals(infoPar.getType().getTypePackage())){
            str = primitiveGenerator.getGenPrimString(infoPar.getType().getName());

        }else{
            Class c = null;
            try {
                System.out.println("FFFF: "+infoPar.getType().getTypePackage());

                c = Class.forName(infoPar.getType().getTypePackage());
            } catch (ClassNotFoundException e) {
                return "null";
                //e.printStackTrace();

            }
            InfoClass infoClass = new InfoClass();
            infoClass.setName(c.getSimpleName());
            infoClass.setAClass(c);

            //сохранение конструкторов класса
            ArrayList<InfoConstructor> infoConstructors = new ArrayList<>();
            Constructor[] constructors = c.getConstructors();
            for(int i=0;i<constructors.length;i++){
                InfoConstructor infoConstructor = new InfoConstructor();
                infoConstructor.setName(c.getSimpleName());
                infoConstructor.setModifiers(Modifier.toString(constructors[i].getModifiers()));

                ArrayList<InfoParameter> infoParameters = new ArrayList<>();
                Parameter[] parameters = constructors[i].getParameters();
                for(int j=0;j<parameters.length;j++){
                    InfoParameter infoParameter = new InfoParameter();
                    infoParameter.setName(parameters[j].getName());
                    infoParameter.setType(getInfoTypeForParameters(parameters[j]));
                    infoParameters.add(infoParameter);
                }
                infoConstructor.setParameters(infoParameters);
                infoConstructors.add(infoConstructor);

            }

            infoClass.setConstructors(infoConstructors);
            String before = consCodeGenerator.genInitCons(infoClass);
            beforeInits.add(before);
            //System.out.println("BEFORE: "+before+" ; "+infoClass.getName()+"kkk");
        }

        return str;
    }

    private InfoType getInfoTypeForParameters(Parameter param){
        InfoType infoType = new InfoType();
        infoType.setName(param.getType().getSimpleName());
        infoType.setTypePackage(param.getType().getTypeName());

        return infoType;
    }

}
