package com.trpo.project3.generator;

import com.trpo.project3.analyze.ClassInformer;
import com.trpo.project3.codeGenerator.CodeGenerator;
import com.trpo.project3.dto.InfoClass;
import com.trpo.project3.utils.FileSaver;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class Generator {

    ClassInformer classInformer = new ClassInformer();


    public void run(){
        ArrayList<InfoClass> infoClasses = classInformer.saveAllClasses();
        //classInformer.getInfoClass("Car");



        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.genTests(infoClasses);

        codeGenerator.printTestByNameClass("Car");



        //FileSaver fileSaver = new FileSaver();
        //fileSaver.createFile(infoClasses, "Hello");



    }
}
