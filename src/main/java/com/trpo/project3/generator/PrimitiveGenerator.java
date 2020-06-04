package com.trpo.project3.generator;

import java.security.SecureRandom;
import java.util.Random;

public class PrimitiveGenerator {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";

    private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static SecureRandom random1 = new SecureRandom();

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
        }if(type.equals("String")){
            return "\""+generateRandomString(8)+"\"";
        }
        else{
            return "";
        }


    }
    public static String generateRandomString(int length) {
        if (length < 1) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {

            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random1.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

            sb.append(rndChar);

        }

        return sb.toString();

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
