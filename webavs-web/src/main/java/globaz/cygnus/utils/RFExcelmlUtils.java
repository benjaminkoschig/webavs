package globaz.cygnus.utils;

import globaz.cygnus.application.RFApplication;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.op.excelml.model.document.ExcelmlBuilder;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author JPA
 * @since JPA 11 juin 2010
 */
public class RFExcelmlUtils {

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
    public static String createDocumentExcel(String modelName, String nomDoc, RFXmlmlContainer container)
            throws Exception {
        ExcelmlWorkbook wk = null;
        nomDoc += "_" + JadeUUIDGenerator.createStringUUID() + ".xls";
        // On va charger le classeur
        wk = RFExcelmlUtils.load(modelName);
        wk.mergeDocument(container);
        // On sauvegarde le document sur persistance avec un nom unique
        return RFExcelmlUtils.save(wk, nomDoc);
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
        String sourceFile = Jade.getInstance().getExternalModelDir() + RFApplication.APPLICATION_CYGNUS_REP + "/"
                + RFApplication.MODELS_EXCELML_CYGNUS + "/" + model;
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
     * Création d'un classeur excel suivant un model et son chemin passé en parametre.
     * 
     * @param path
     * @return
     * @throws Exception
     */
    public static ExcelmlWorkbook loadPath(String path) throws Exception {
        ExcelmlWorkbook wk = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            wk = ExcelmlBuilder.getDocument(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return wk;
    }

    /**
     * Permet de remplir une colonne par la valeur passée en parametre.
     * 
     * @param container
     * @param column
     * @param value
     */
    public static void remplirColumn(RFXmlmlContainer container, String column, String value, String defaultValue) {
        if (!JadeStringUtil.isEmpty(value)) {
            container.put(column, value);
        } else {
            container.put(column, defaultValue);
        }
    }

    /**
     * Permet de remplir une colonne par la valeur passée en parametre.
     * 
     * @param container
     * @param column
     * @param value
     */
    public static void remplirColumnByLibelle(RFXmlmlContainer container, BSession session, String column,
            String codeLibelle, String defaultValue) {
        if (!JadeStringUtil.isEmpty(codeLibelle)) {
            container.put(column, session.getCodeLibelle(codeLibelle));
        } else {
            container.put(column, defaultValue);
        }
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

    /**
     * Constructeur de REExcelmlUtils
     */
    protected RFExcelmlUtils() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }
}
