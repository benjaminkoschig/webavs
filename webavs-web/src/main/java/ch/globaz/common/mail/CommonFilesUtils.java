package ch.globaz.common.mail;

import com.sun.star.util.FileIOException;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommonFilesUtils {
    private static final String ZIP_EXTENSION = ".zip";


    public static List<String> selectFilesToJoin(String filesPath, String numAvs) throws IOException {
        List<String> filesToJoin = new ArrayList<>();
        List<String> repositoryELP = null;
        try {
            repositoryELP = JadeFsFacade.getFolderChildren(filesPath);

            for (String nomFichier : repositoryELP) {
                if(nomFichier.endsWith(ZIP_EXTENSION)){
                    if (nomFichier.contains(numAvs.replace(".",""))) {
                        filesToJoin.add(nomFichier);
                    }
                }
            }
        } catch (JadeServiceLocatorException | JadeServiceActivatorException | JadeClassCastException e) {
            throw  new IOException("Erreur lors de la lecture des fichiers", e);
        }

        return filesToJoin;
    }

    public static String readPathFiles(String numAvs, String path){

        StringBuilder pathFiles = new StringBuilder();
        // On élimine les "." s'il y en a.
        numAvs = numAvs.replace(".", "");
        numAvs = numAvs.substring(3, numAvs.length());

        pathFiles.append(path);

        int j=0;
        for(char i : numAvs.toCharArray()) {
            if (j>1) break;
            pathFiles.append(i + "\\");
            j++;
        }

        return pathFiles.toString();
    }

    public static String createPathFiles(String numAvs, String path){

        StringBuilder pathFiles = new StringBuilder();
        if ((Objects.nonNull(numAvs) || !numAvs.isEmpty()) && numAvs.length()>10) {
            pathFiles.append(path);
            // On élimine les "." s'il y en a.
            numAvs = numAvs.replace(".", "");
            numAvs = numAvs.substring(3, numAvs.length());
            int j=0;
            for(char i : numAvs.toCharArray()) {
                if (j>1) break;
                pathFiles.append(i + "/");
                new File(pathFiles.toString()).mkdir();
                j++;
            }
        }
        return pathFiles.toString();
    }
}
