package com.trpo.project3.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileSaver {

    public void createFile(Class c, String srcCode) {
        System.out.println(srcCode);
        String baseDir = System.getProperty("user.dir");

        String middleDir = "src" + File.separator + "test" +
                File.separator + "java";
        String dir = c.getCanonicalName().replace(".", File.separator);
        System.out.println(dir);
        String filePath = baseDir + File.separator + middleDir + File.separator + dir + "Test.java";
        File theFile = new File(filePath);
        File fullDirFile = theFile.getParentFile();
        if (!fullDirFile.exists()) {
            fullDirFile.mkdirs();
        }
        try (PrintWriter writer = new PrintWriter(filePath, "utf-8")) {
            writer.print(srcCode);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public  void printFile(String srcCode) {
        System.out.println(srcCode);
    }

    private String getPackageName(String path, String basePath, String className) {
        String packageName = path
                .replace(basePath, "")
                .replace(className, "")
                .replaceFirst("\\/", "")
                .replaceFirst("\\\\", "")
                .replaceAll("\\\\", ".")
                .replaceAll("\\/", ".");
        return packageName;
    }
}
