package ch.globaz.vulpecula.external.fichierPlat;

import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class FichierPlatExporterAbstract {

    protected final static String EXTENSION = ".txt";
    protected final static String SEPARATOR = "\t";
    protected final static String NEW_LINE = "\r\n";

    protected void createExportFile(String content, String path, String fileName) throws IOException,
            JadeServiceLocatorException, JadeServiceActivatorException, NullPointerException, ClassCastException,
            JadeClassCastException {

        FileWriter fileWriter = new FileWriter(fileName, true);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        PrintWriter printWriter = new PrintWriter(writer);
        printWriter.println(content);
        printWriter.close();

        JadeFsFacade.copyFile(fileName, path + fileName);
        JadeFsFacade.delete(fileName);
    }
}
