package com.trpo.project3.generator;

import com.trpo.project3.analyze.ClassInformer;
import com.trpo.project3.codeGenerator.CodeGenerator;
import com.trpo.project3.dto.InfoClass;

import java.util.ArrayList;

public class Generator {

    ClassInformer classInformer = new ClassInformer();


    public void run() {
        ArrayList<InfoClass> infoClasses = classInformer.saveAllClasses();
        //classInformer.getInfoClass("Car");


        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.genTests(infoClasses);

        codeGenerator.printTestByNameClass("Car");


        //FileSaver fileSaver = new FileSaver();
        //fileSaver.createFile(infoClasses, "Hello");


    }
}
