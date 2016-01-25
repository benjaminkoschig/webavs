package globaz.aquila.ts;

import globaz.globall.util.JADate;

/**
 * Si fichier a g�n�r� => quel est le nom standardis� de se fichier.<br/>
 * 
 * @see FWPARP
 * @author DDA
 */
public interface COTSFileUtil {

    public static final String OUTPUT_FILE_WORK_DIR = "work";

    public String getOutputFileName(JADate dateCreation) throws Exception;

}
