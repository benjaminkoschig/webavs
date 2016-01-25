package ch.globaz.utils.tests;

import globaz.jade.log.JadeLogger;
import java.io.File;
import java.io.IOException;

/**
 * Classe utilitaire founissant des outils de manipulation de fichier pour la g�n�ration des fichiers json de dump
 * 
 * @author sce
 * 
 */
public class FileUtil {

    /**
     * Creation du r�pertoire
     * 
     * @param folder
     * @return
     * @throws IOException
     */
    public static void createFolder(File folder) throws IOException {
        boolean success = folder.mkdirs();
        if (!success) {
            throw new IOException("##### Directory cannot be created: " + folder.getAbsolutePath());
        }
        JadeLogger.info(new FileUtil(), "##### R�pertoire cr�� : " + folder.getAbsolutePath());
    }

}
