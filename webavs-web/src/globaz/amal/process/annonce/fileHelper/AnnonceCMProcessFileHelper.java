/**
 * 
 */
package globaz.amal.process.annonce.fileHelper;

import globaz.amal.process.annonce.cosama.AMCosamaRecord;
import globaz.jade.common.Jade;
import java.util.List;

/**
 * @author dhi
 * 
 */
public abstract class AnnonceCMProcessFileHelper {

    /**
     * Type de fichier cosama
     */
    public static String FILE_TYPE_COSAMA = "cosama";
    /**
     * Extension du type de fichier cosama
     */
    public static String FILE_TYPE_COSAMA_EXTENSION = "";
    /**
     * Type de fichier csv
     */
    public static String FILE_TYPE_CSV = "csv";
    /**
     * Extension du type de fichier csv
     */
    public static String FILE_TYPE_CSV_EXTENSION = ".csv";

    /**
     * Récupération d'une instance de type AnnonceCMProcessFileHelper
     * 
     * @param fileType
     *            AnnonceCMProcessFileHelper.FILE_TYPE_COSAMA,
     *            AnnonceCMProcessFileHelper.FILE_TYPE_CSV
     * @return
     *         un objet instancer de type AnnonceCMProcessFileHelper
     */
    public static AnnonceCMProcessFileHelper getInstance(String fileType) {
        if (fileType.equals(AnnonceCMProcessFileHelper.FILE_TYPE_COSAMA)) {
            return new AnnonceCMProcessCosamaFileHelper();
        } else if (fileType.equals(AnnonceCMProcessFileHelper.FILE_TYPE_CSV)) {
            return new AnnonceCMProcessCsvFileHelper();
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
    public void writeFile(List<AMCosamaRecord> records) {

    }

}
