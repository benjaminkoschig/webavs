package ch.globaz.vulpecula.comptabilite.importationcg.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper pour gérer un fichier CSV
 * 
 * @since WebBMS 0.5
 */
public class CsvFileHelper {
    private final static Logger LOGGER = LoggerFactory.getLogger(CsvFileHelper.class);

    public static String getResourcePath(String fileName) {
        if (fileName == null || fileName.length() == 0) {
            throw new IllegalArgumentException("Le nom du fichier ne peut pas être null");
        }

        final File f = new File("");
        final String dossierPath = f.getAbsolutePath() + File.separator + fileName;
        return dossierPath;
    }

    public static File getResource(String fileName) {
        if (fileName == null || fileName.length() == 0) {
            throw new IllegalArgumentException("Le nom du fichier ne peut pas être null");
        }

        final String completeFileName = getResourcePath(fileName);
        File file = new File(completeFileName);
        return file;
    }

    /**
     * @param file
     * @return
     */
    public static List<String> readFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException("Le fichier ne peut pas être null");
        }

        if (!file.exists()) {
            throw new IllegalArgumentException("Le fichier " + file.getName() + " n'existe pas.");
        }

        List<String> result = new ArrayList<String>();

        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                result.add(line);
            }
        } catch (IOException e) {
            LOGGER.error("Error while reading csv file.");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LOGGER.error("Error when close buffer.");
                }
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    LOGGER.error("Error when close file.");
                }
            }
        }

        return result;
    }

    public static String readFileAsString(File file) {
        final List<String> lines = readFile(file);
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line);
            sb.append("\n");
        }
        sb.deleteCharAt(sb.lastIndexOf("\n"));

        return sb.toString();
    }
}
