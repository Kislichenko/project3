package com.trpo.project3.codeGenerator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

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

    public String firstLetterToLowCase(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    //генерация кода инициализации через конструктор
    public String createConsA(String className, List<String> params) {

        String cons = " new "
                + className + "(";

        if (params.size() > 0) {
            for (int i = 0; i < params.size(); i++) {
                cons += params.get(i) + ",";
            }
            cons = cons.substring(0, cons.length() - 1);
        }

        cons += ")";

        return cons;
    }

    //метод для нахождения классов, который имплементируют интерфейс или абстрактный класс
    public boolean findClassesInJar(final Class<?> baseInterface, final String jarName) throws IOException {
        final List<String> classesTobeReturned = new ArrayList<String>();
        if (!StringUtils.isBlank(jarName)) {
            //jarName is relative location of jar wrt.
            final String jarFullPath = File.separator + jarName;
            final ClassLoader classLoader = this.getClass().getClassLoader();
            JarInputStream jarFile = null;
            URLClassLoader ucl = null;
            final URL url = new URL("jar:file:" + jarFullPath + "!/");
            ucl = new URLClassLoader(new URL[]{url}, classLoader);
            jarFile = new JarInputStream(new FileInputStream(jarFullPath));
            JarEntry jarEntry;
            while (true) {
                jarEntry = jarFile.getNextJarEntry();
                if (jarEntry == null)
                    break;
                if (jarEntry.getName().endsWith(".class")) {
                    String classname = jarEntry.getName().replaceAll("/", "\\.");
                    classname = classname.substring(0, classname.length() - 6);
                    if (!classname.contains("$")) {
                        try {
                            final Class<?> myLoadedClass = Class.forName(classname, true, ucl);
                            if (baseInterface.isAssignableFrom(myLoadedClass)) {
                                System.out.println(myLoadedClass.getSimpleName());
                                return true;
                            }
                        } catch (final ClassNotFoundException e) {

                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

}
