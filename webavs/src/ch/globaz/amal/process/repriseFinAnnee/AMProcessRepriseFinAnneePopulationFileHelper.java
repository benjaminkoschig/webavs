/**
 * 
 */
package ch.globaz.amal.process.repriseFinAnnee;

import globaz.jade.common.Jade;
import java.util.List;

/**
 * @author dhi
 * 
 */
public class AMProcessRepriseFinAnneePopulationFileHelper {

    /**
     * Séparateur de champ fichier csv
     */
    public static String CSV_SEPARATOR = ";";
    /**
     * Type de fichier csv
     */
    public static String FILE_TYPE_CSV = "csv";
    /**
     * Extension du type de fichier csv
     */
    public static String FILE_TYPE_CSV_EXTENSION = ".csv";

    /**
     * 
     * @param fileType
     * @return
     */
    public static AMProcessRepriseFinAnneePopulationCsvFileHelper getInstance(String fileType) {
        if (fileType.equals(AMProcessRepriseFinAnneePopulationFileHelper.FILE_TYPE_CSV)) {
            return new AMProcessRepriseFinAnneePopulationCsvFileHelper();
        } else {
            return null;
        }
    }

    /**
     * Nom du fichier court. le path est défini par l'application
     */
    private String shortFileName = null;

    /**
     * Récupération du nom du fichier complet
     * 
     * @return
     */
    public String getFullFileName() {
        return Jade.getInstance().getPersistenceDir() + getShortFileName();
    }

    /**
     * @return the shortFileName
     */
    public String getShortFileName() {
        return shortFileName;
    }

    /**
     * @param shortFileName
     *            the shortFileName to set
     */
    public void setShortFileName(String shortFileName) {
        this.shortFileName = shortFileName;
    }

    /**
     * A surcharger par les enfants
     * 
     * Provoque l'écriture du fichier
     * 
     */
    public void writeFile(List<String> records) {

    }

}
