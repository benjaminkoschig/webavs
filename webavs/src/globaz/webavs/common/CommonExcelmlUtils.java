package globaz.webavs.common;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlBuilder;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author MMO
 * @since 30.05.2011
 */
public class CommonExcelmlUtils {

    /**
     * Cr�ation d'un document excel � partir d'un mod�le xml et d'un container des donn�es
     * 
     * @param modelXMLPath
     *            le chemin o� se trouve le mod�le XML � charger
     * @param xlsFilePath
     *            le chemin o� sauvegarder le document excel cr��
     * @param container
     *            le container des donn�es � ins�rer dans le document
     * 
     * @return le chemin complet o� � �t� sauvegard� le document excel cr��
     * 
     * @throws Exception
     */
    public static String createDocumentExcel(String modelXMLPath, String xlsFilePath, IMergingContainer container)
            throws Exception {

        StringBuffer wrongArguments = new StringBuffer();

        if (JadeStringUtil.isEmpty(modelXMLPath)) {
            wrongArguments.append(" modelXMLPath is empty ");
        }

        if (JadeStringUtil.isEmpty(xlsFilePath)) {
            wrongArguments.append(" xlsFilePath is empty ");
        }

        if (container == null) {
            wrongArguments.append(" container is null ");
        }

        if (wrongArguments.length() >= 1) {
            throw new Exception("Can't create excel document due to wrong arguments : " + wrongArguments.toString());
        }

        ExcelmlWorkbook wk = null;

        // On charge le mod�le xml
        wk = CommonExcelmlUtils.loadModelXML(modelXMLPath);

        // On ins�re les donn�es dans le document excel
        wk.mergeDocument(container);

        // On sauvegarde le document excel
        return CommonExcelmlUtils.saveAs(wk, xlsFilePath);
    }

    /**
     * Cr�ation d'un classeur excel � partir d'un model xml
     * 
     * @param modelPath
     *            le chemin du model � utiliser pour cr�er le classeur
     * 
     * @return un classeur excel
     * 
     * @throws Exception
     */
    public static ExcelmlWorkbook loadModelXML(String modelXMLPath) throws Exception {

        if (JadeStringUtil.isEmpty(modelXMLPath)) {
            throw new Exception("Can't load xml model due to wrong arguments : modelXMLPath is empty ");
        }

        ExcelmlWorkbook wk = null;
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(modelXMLPath);
            wk = ExcelmlBuilder.getDocument(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return wk;
    }

    /**
     * Sauvegarde du document excel � l'endroit sp�cifi� par le chemin re�u en param�tre
     * 
     * @param wk
     *            le classeur � sauver
     * @param filePath
     *            le chemin o� sauvegarder le document excel
     * 
     * @return le chemin complet o� � �t� sauvegard� le document excel
     * 
     * @throws Exception
     */
    public static String saveAs(ExcelmlWorkbook wk, String filePath) throws Exception {

        StringBuffer wrongArguments = new StringBuffer();

        if (wk == null) {
            wrongArguments.append(" wk is null ");
        }

        if (JadeStringUtil.isEmpty(filePath)) {
            wrongArguments.append(" filePath is empty ");
        }

        if (wrongArguments.length() >= 1) {
            throw new Exception("Can't save excel document due to wrong arguments : " + wrongArguments.toString());
        }

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
     * Sauvegarde du document excel sur persistence
     * 
     * @param wk
     *            le classeur � sauver
     * @param nomDoc
     *            le nom du fichier excel � sauver
     * 
     * @return le chemin complet o� � �t� sauvegard� le document excel
     * 
     * @throws Exception
     */
    public static String saveInPersistenceDir(ExcelmlWorkbook wk, String nomDoc) throws Exception {

        StringBuffer wrongArguments = new StringBuffer();

        if (wk == null) {
            wrongArguments.append(" wk is null ");
        }

        if (JadeStringUtil.isEmpty(nomDoc)) {
            wrongArguments.append(" nomDoc is empty ");
        }

        if (wrongArguments.length() >= 1) {
            throw new Exception("Can't save excel document in persistence dir due to wrong arguments : "
                    + wrongArguments.toString());
        }

        return CommonExcelmlUtils.saveAs(wk, Jade.getInstance().getPersistenceDir() + nomDoc);
    }

    /**
     * Constructeur de CommonExcelmlUtils
     * 
     * prevents calls from subclass
     */
    protected CommonExcelmlUtils() {
        throw new UnsupportedOperationException();
    }

}
