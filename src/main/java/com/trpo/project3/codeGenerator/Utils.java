package com.trpo.project3.codeGenerator;

import com.google.common.collect.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    // получение файла из ресурсов
    private File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("File is not found!");
        } else {
            return new File(resource.getFile());
        }
    }

    public String readFile(String fileName) {
        try {
            return FileUtils.readFileToString(getFileFromResources(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String firstLetterToLowCase(String str){
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    //генерация кода инициализации через конструктор
    public String createConsA(String className, List<String> params){

        String cons = "" + className
                + " "
                +firstLetterToLowCase(className)
                + " = new "
                + className + "(";

        if(params.size()>0) {
            for (int i = 0; i < params.size(); i++) {
                cons += params.get(i) + ",";
            }
            cons = cons.substring(0, cons.length()-1);
        }

        cons+=");";

        return cons;
    }

}
