package globaz.naos.listes.excel.util;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.hercule.exception.HerculeException;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.naos.application.AFApplication;
import globaz.op.excelml.model.document.ExcelmlBuilder;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author JPA
 * @since JPA 11 juin 2010
 */
public class AFExcelmlUtils {

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
    public static String createDocumentExcel(String modelName, String nomDoc, NaosContainer container) throws Exception {
        ExcelmlWorkbook wk = null;
        nomDoc = JadeFilenameUtil.addOrReplaceFilenameSuffixUID(nomDoc + ".xml");
        // On va charger le classeur
        wk = AFExcelmlUtils.load(modelName);
        wk.mergeDocument(container);
        // On sauvegarde le document sur persistance avec un nom unique
        return AFExcelmlUtils.save(wk, nomDoc);
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
        String sourceFile = Jade.getInstance().getExternalModelDir() + AFApplication.DEFAULT_APPLICATION_NAOS_REP + "/"
                + AFApplication.MODELS_EXCELML_NAOS + "/" + model;
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
    public static void remplirColumn(NaosContainer container, String column, String value, String defaultValue) {
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
    public static void remplirColumnByLibelle(NaosContainer container, BSession session, String column,
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
    public static void renseigneAdresse(BSession session, String typeAdresse, NaosContainer container, String idTiers)
            throws Exception {

        String ville = "";
        String rue = "";
        String numRue = "";
        String npa = "";
        String casePostale = "";

        TITiers tiers = AFApplication.retrieveTiers(session, idTiers);
        TIAdresseDataSource d;

        try {
            d = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, typeAdresse,
                    JACalendar.todayJJsMMsAAAA(), true);
        } catch (Exception e) {
            throw new Exception("Technical Exception, Unabled to retrieve the adresse ( idTiers = " + idTiers + ")", e);
        }

        if (d != null) {
            ville = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
            rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
            numRue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
            npa = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
            casePostale = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_CASE_POSTALE);
        }

        // Rue
        container.put(IAFListeColumns.RUE, rue + " " + numRue);
        // Case postale
        container.put(IAFListeColumns.CASE_POSTALE, casePostale);
        // NPA
        container.put(IAFListeColumns.NPA, npa);
        // Localité
        container.put(IAFListeColumns.LOCALITE, ville);
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
            e.printStackTrace();
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
    protected AFExcelmlUtils() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
