package com.trpo.project3.generator;

import java.util.Random;

public class PrimitiveGenerator {

    Random random = new Random();

    private String genSimple(String type){

        if(type.equals("int")){
            return ""+random.nextInt();
        }else if(type.equals("byte")){
            byte[] bt = new byte[1];
            random.nextBytes(bt);
            return "0b10";
        }else if(type.equals("long")){
            return ""+random.nextLong();
        }else if(type.equals("float")){
            return ""+random.nextFloat();
        }else if(type.equals("double")){
            return ""+random.nextDouble();
        }else if(type.equals("boolean")){
            return ""+random.nextBoolean();
        }else if(type.equals("char")){
            return ""+(char)(random.nextInt(127 - 32) + 32);
        }else if(type.equals("short")){
            return ""+(short) random.nextInt(Short.MAX_VALUE + 1);
        }else{
            return "";
        }

    }

    private Object genSimpleObj(String type){

        if(type.equals("int")){
            return random.nextInt();
        }else if(type.equals("byte")){
            byte[] bt = new byte[1];
            random.nextBytes(bt);
            return "0b10";
        }else if(type.equals("long")){
            return random.nextLong();
        }else if(type.equals("float")){
            return random.nextFloat();
        }else if(type.equals("double")){
            return random.nextDouble();
        }else if(type.equals("boolean")){
            return random.nextBoolean();
        }else if(type.equals("char")){
            return (char)(random.nextInt(127 - 32) + 32);
        }else if(type.equals("short")){
            return (short) random.nextInt(Short.MAX_VALUE + 1);
        }else{
            return null;
        }

    }

    private Object[] genPrimArrayObj(String type){
        int size = random.nextInt(10);
        //String genStr="new "+type+"{"+genSimple(type.substring(0,type.length()-2));
        Object[] objects = new Object[size];
        for(int i=1;i<size;i++) {
            objects[i] = genSimpleObj(type.substring(0,type.length()-2));
        }

        return objects;
    }

    private String genPrimArray(String type){
        int size = random.nextInt(10);
        String genStr="new "+type+"{"+genSimple(type.substring(0,type.length()-2));
        for(int i=1;i<size;i++) {
            genStr=genStr+","+genSimple(type.substring(0,type.length()-2));
        }

        return genStr+"}";
    }

    public Object getGenPrimObj(String type){
        if(type.substring(type.length()-2).equals("[]")){
            return genPrimArrayObj(type);
        }else {
            return genSimpleObj(type);
        }
    }

    public String getGenPrimString(String type){
        if(type.substring(type.length()-2).equals("[]")){
            return genPrimArray(type);
        }else {
            return genSimple(type);
        }
    }

}
