package globaz.orion.utils;

import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.op.excelml.model.document.ExcelmlBuilder;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import ch.globaz.orion.EBApplication;

/**
 * @author JPA
 * @since JPA 11 juin 2010
 */
public class EBExcelmlUtils {

    /**
     * Création d'un document excel à partir d'un container des données
     * 
     * @param modelName
     *            le nom du model
     * @param nomDoc
     *            le nom du document
     * @param container
     *            le container des données à insérer dans le document
     * @return filePath le chemin complet où à été sauvegardé le document
     * @throws Exception
     */
    public static String createDocumentExcel(String modelName, String nomDoc, OrionContainer container)
            throws Exception {
        ExcelmlWorkbook wk = null;
        nomDoc += "-" + JadeUUIDGenerator.createStringUUID() + ".xml";
        // On va charger le classeur
        wk = EBExcelmlUtils.load(modelName);
        wk.mergeDocument(container);
        // On sauvegarde le document sur persistance avec un nom unique
        return EBExcelmlUtils.save(wk, nomDoc);
    }

    /**
     * Création d'un classeur excel suivant un model
     * 
     * @param model
     *            le model à utiliser pour créer le classeur
     * @return wk un classeur excel
     * @throws Exception
     */
    public static ExcelmlWorkbook load(String model) throws Exception {
        String sourceFile = Jade.getInstance().getHomeDir() + EBApplication.APPLICATION_ROOT + "/"
                + EBApplication.MODELS_EXCELML + "/" + model;
        ExcelmlWorkbook wk = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(sourceFile);
            wk = ExcelmlBuilder.getDocument(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return wk;
    }

    /**
     * Sauvegarde du document excel sur persistence
     * 
     * @param wk
     *            le classeur à sauver
     * @param nomDoc
     *            le nom du document
     * @return filePath le chemin complet où à été sauvegardé le document
     * @throws Exception
     */
    public static String save(ExcelmlWorkbook wk, String nomDoc) throws Exception {
        String filePath = Jade.getInstance().getPersistenceDir() + nomDoc;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            wk.write(fos);
            fos.flush();
        } catch (Exception e) {
            throw new Exception("Technical Exception, unable to save the document at the emplacement : " + filePath, e);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return filePath;
    }

    // constructeurs
    protected EBExcelmlUtils() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
