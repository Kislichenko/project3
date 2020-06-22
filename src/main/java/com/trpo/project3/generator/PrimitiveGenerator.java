package com.trpo.project3.generator;

import com.trpo.project3.dto.StringObject;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class PrimitiveGenerator {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";

    private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static SecureRandom random1 = new SecureRandom();

    Random random = new Random();

    public StringObject getGenPrim(String type) {
        if (type.substring(type.length() - 2).equals("[]")) {
            return genPrimArray(type);
        } else {
            return genSimple(type);
        }
    }


    //генерация примитива
    private StringObject genSimple(String type) {

        if (type.equals("int")) {
            return new StringObject(random.nextInt(), "" + random.nextInt(),null);
        } else if (type.equals("byte")) {
            byte[] bt = new byte[1];
            random.nextBytes(bt);
            return new StringObject("0b10", "0b10", null);
        } else if (type.equals("long")) {
            return new StringObject(random.nextLong(), "" + random.nextLong(), null);
        } else if (type.equals("float")) {
            return new StringObject(random.nextFloat(), "" + random.nextFloat(), null);
        } else if (type.equals("double")) {
            return new StringObject(random.nextDouble(), "" + random.nextDouble(), null);
        } else if (type.equals("boolean")) {
            return new StringObject(random.nextBoolean(), "" + random.nextBoolean(), null);
        } else if (type.equals("char")) {
            return new StringObject((char) (random.nextInt(127 - 32) + 32),
                    "" + (char) (random.nextInt(127 - 32) + 32), null);
        } else if (type.equals("short")) {
            return new StringObject((short) random.nextInt(Short.MAX_VALUE + 1),
                    "" + (short) random.nextInt(Short.MAX_VALUE + 1), null);
        }
        if (type.equals("String")) {
            return new StringObject("\"" + generateRandomString(8) + "\"",
                    "\"" + generateRandomString(8) + "\"", null);
        } else {
            return new StringObject(null, "null", null);
        }
    }

    //IntStream.generate(() -> new Random().nextInt(100)).limit(100).toArray();
    //метод генерации массива примитивов
    private StringObject genPrimArray(String type) {
        int size = random.nextInt(10);

        if (type.equals("int[]")) {
            int[] tmp = new int[size];
            IntStream.range(0, size).forEach(i -> tmp[i] = random.nextInt());

            return new StringObject(tmp, "new " + type + "{" +
                    Arrays.toString(tmp).substring(1, Arrays.toString(tmp).length() - 1) + "}", null);
        } else if (type.equals("byte[]")) {
            byte[] bt = new byte[1];
            random.nextBytes(bt);
            return new StringObject("0b10", "0b10", null);
        } else if (type.equals("long[]")) {
            long[] tmp = new long[size];
            IntStream.range(0, size).forEach(i -> tmp[i] = random.nextLong());
            return new StringObject(tmp, "new " + type + "{" +
                    Arrays.toString(tmp).substring(1, Arrays.toString(tmp).length() - 1) + "}", null);
        } else if (type.equals("float[]")) {
            float[] tmp = new float[size];
            IntStream.range(0, size).forEach(i -> tmp[i] = random.nextFloat());
            return new StringObject(tmp, "new " + type + "{" +
                    Arrays.toString(tmp).substring(1, Arrays.toString(tmp).length() - 1) + "}", null);
        } else if (type.equals("double[]")) {
            double[] tmp = new double[size];
            IntStream.range(0, size).forEach(i -> tmp[i] = random.nextDouble());
            return new StringObject(tmp, "new " + type + "{" +
                    Arrays.toString(tmp).substring(1, Arrays.toString(tmp).length() - 1) + "}", null);
        } else if (type.equals("boolean[]")) {
            boolean[] tmp = new boolean[size];
            IntStream.range(0, size).forEach(i -> tmp[i] = random.nextBoolean());
            return new StringObject(tmp, "new " + type + "{" +
                    Arrays.toString(tmp).substring(1, Arrays.toString(tmp).length() - 1) + "}", null);
        } else if (type.equals("char[]")) {
            return new StringObject((char) (random.nextInt(127 - 32) + 32),
                    "" + (char) (random.nextInt(127 - 32) + 32), null);
        } else if (type.equals("short[]")) {
            short[] tmp = new short[size];
            IntStream.range(0, size).forEach(i -> tmp[i] = (short) random.nextInt(Short.MAX_VALUE + 1));
            return new StringObject(tmp, "new " + type + "{" +
                    Arrays.toString(tmp).substring(1, Arrays.toString(tmp).length() - 1) + "}", null);

        }
        if (type.equals("String[]")) {
            return new StringObject("\"" + generateRandomString(8) + "\"",
                    "\"" + generateRandomString(8) + "\"", null);
        } else {
            return new StringObject(null, "", null);
        }

    }


    //метод генерации строки
    private String generateRandomString(int length) {
        if (length < 1) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {

            int rndCharAt = random1.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

            sb.append(rndChar);

        }

        return sb.toString();
    }

}
