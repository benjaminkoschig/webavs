package globaz.aquila.ts;

import globaz.globall.util.JADate;

/**
 * Si fichier a généré => quel est le nom standardisé de se fichier.<br/>
 * 
 * @see FWPARP
 * @author DDA
 */
public interface COTSFileUtil {

    public static final String OUTPUT_FILE_WORK_DIR = "work";

    public String getOutputFileName(JADate dateCreation) throws Exception;

}
