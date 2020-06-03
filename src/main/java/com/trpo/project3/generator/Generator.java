package com.trpo.project3.generator;

import com.trpo.project3.analyze.InfoClass;
import com.trpo.project3.utils.FileSaver;

public class Generator {

    public void run(){
        InfoClass infoClass = new InfoClass();
        infoClass.getInfoClass("Car");

        FileSaver fileSaver = new FileSaver();
        fileSaver.createFile(infoClass.getClassByName("Car"), "Hello");



    }
}
