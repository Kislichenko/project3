package com.trpo.project3.generator;

import com.trpo.project3.analyze.ClassInformer;
import com.trpo.project3.codeGenerator.CodeGenerator;
import com.trpo.project3.dto.InfoClass;
import com.trpo.project3.utils.FileSaver;

import java.util.ArrayList;
import java.util.Map;

public class Generator {

    ClassInformer classInformer = new ClassInformer();


    public void run() {
        ArrayList<InfoClass> infoClasses = classInformer.saveAllClasses();
        //classInformer.getInfoClass("Car");


        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.genTests(infoClasses);
        Map<String, String> classTests = codeGenerator.getClassTests();
        FileSaver fileSaver = new FileSaver();

        for (Map.Entry<String, String> entry : classTests.entrySet()) {
            //System.out.println(entry.getKey());
            //codeGenerator.printTestByNameClass("Man");
            fileSaver.createFile(classInformer.getClassScanner().getClassByName(entry.getKey()), entry.getValue());

        }

    }
}
