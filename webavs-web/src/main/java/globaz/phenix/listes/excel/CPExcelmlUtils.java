package globaz.phenix.listes.excel;

import globaz.globall.db.BSession;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.mappingXmlml.ICEListeColumns;
import globaz.hercule.service.CETiersService;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.op.excelml.model.document.ExcelmlBuilder;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.phenix.application.CPApplication;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.CommonExcelmlContainer;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author JPA
 * @since JPA 11 juin 2010
 */
public class CPExcelmlUtils {

    /**
     * Cr?ation d'un document excel ? partir d'un container des donn?es
     * 
     * @param modelName
     *            le nom du model
     * @param nomDoc
     *            le nom du document
     * @param container
     *            le container des donn?es ? ins?rer dans le document
     * @return filePath le chemin complet o? ? ?t? sauvegard? le document
     * @throws Exception
     */
    public static String createDocumentExcel(String modelName, String nomDoc, CommonExcelmlContainer container)
            throws Exception {
        ExcelmlWorkbook wk = null;
        nomDoc = JadeFilenameUtil.addOrReplaceFilenameSuffixUID(nomDoc + ".xml");
        // On va charger le classeur
        wk = CPExcelmlUtils.load(modelName);
        wk.mergeDocument(container);
        // On sauvegarde le document sur persistance avec un nom unique
        return CPExcelmlUtils.save(wk, nomDoc);
    }

    /**
     * Cr?ation d'un classeur excel suivant un model
     * 
     * @param model
     *            le model ? utiliser pour cr?er le classeur
     * @return wk un classeur excel
     * @throws Exception
     */
    public static ExcelmlWorkbook load(String model) throws Exception {
        String sourceFile = Jade.getInstance().getExternalModelDir() + CPApplication.APPLICATION_PHENIX_REP + "/"
                + "model" + "/" + model;
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
     * Cr?ation d'un classeur excel suivant un model et son chemin pass? en parametre.
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
     * Permet de remplir une colonne par la valeur pass?e en parametre.
     * 
     * @param container
     * @param column
     * @param value
     */
    public static void remplirColumn(CommonExcelmlContainer container, String column, String value, String defaultValue) {
        if (!JadeStringUtil.isEmpty(value)) {
            container.put(column, value);
        } else {
            container.put(column, defaultValue);
        }
    }

    /**
     * Permet de remplir une colonne par la valeur pass?e en parametre.
     * 
     * @param container
     * @param column
     * @param value
     */
    public static void remplirColumnByLibelle(CommonExcelmlContainer container, BSession session, String column,
            String codeLibelle, String defaultValue) {
        if (!JadeStringUtil.isEmpty(codeLibelle)) {
            container.put(column, session.getCodeLibelle(codeLibelle));
        } else {
            container.put(column, defaultValue);
        }
    }

    /**
     * Permet de renseigner l'adresse d'un tiers
     * 
     * @param session
     * @param typeAdresse
     * @param container
     * @param idTiers
     * @throws HerculeException
     * @throws Exception
     */
    public static void renseigneAdresse(BSession session, String typeAdresse, CommonExcelmlContainer container,
            String idTiers, String numAffilie) throws HerculeException {

        String ville = "";
        String rue = "";
        String numRue = "";
        String npa = "";
        String casePostale = "";

        TITiers tiers = CETiersService.retrieveTiers(session, idTiers);
        TIAdresseDataSource d;

        try {
            d = CETiersService.retrieveAdresseDataSource(typeAdresse, tiers, numAffilie);
        } catch (Exception e) {
            throw new HerculeException("Technical Exception, Unabled to retrieve the adresse ( idTiers = " + idTiers
                    + ")", e);
        }

        if (d != null) {
            ville = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
            rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
            numRue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
            npa = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
            casePostale = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_CASE_POSTALE);
        }

        // Rue
        container.put(ICEListeColumns.RUE, rue + " " + numRue);
        // Case postale
        container.put(ICEListeColumns.CASE_POSTALE, casePostale);
        // NPA
        container.put(ICEListeColumns.NPA, npa);
        // Localit?
        container.put(ICEListeColumns.LOCALITE, ville);
    }

    /**
     * Sauvegarde du document excel sur persistence
     * 
     * @param wk
     *            le classeur ? sauver
     * @param nomDoc
     *            le nom du document
     * @return filePath le chemin complet o? ? ?t? sauvegard? le document
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
            throw new HerculeException("Technical Exception, unable to save the document at the emplacement : "
                    + filePath, e);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return filePath;
    }

    /**
     * Constructeur de CEAffiliationUtils
     */
    protected CPExcelmlUtils() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
