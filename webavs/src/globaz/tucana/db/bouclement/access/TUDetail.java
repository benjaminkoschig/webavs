package globaz.tucana.db.bouclement.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri May 12 07:35:57 CEST 2006
 */
public class TUDetail extends BEntity {
    /** Table : TUBPDET */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** canton - canton (BDETCA) */
    private String canton = new String();
    /** csApplication - cs application tu_appli (CSAPPL) */
    private String csApplication = new String();
    /** csRubrique - cs rubrique tu_rubr (CSRUBR) */
    private String csRubrique = new String();
    /** csTypeRubrique - cs type de rubr tu_tyrubr (CSTYRU) */
    private String csTypeRubrique = new String();
    /** dateImport - date importation (BDETIM) */
    private String dateImport = new String();
    /** idBouclement - clé primaire du fichier bouclement (BBOUID) */
    private String idBouclement = new String();
    /** idDetail - id détail (BDETID) */
    private String idDetail = new String();
    /** nombreMontant - nombre ou montant (BDETNM) */
    private String nombreMontant = new String();
    /** noPassage - numéro de passage (BDETNP) */
    private String noPassage = new String();

    /**
     * Constructeur de la classe TUDetail
     */
    public TUDetail() {
        super();
    }

    /**
     * Méthode qui incrémente la clé primaire
     * 
     * @param transaction
     *            BTransaction transaction
     * @throws Exception
     *             exception
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdDetail(_incCounter(transaction, "0"));
    }

    /**
     * Renvoie le nom de la table TUBPDET
     * 
     * @return String TUBPDET
     */
    @Override
    protected String _getTableName() {
        return ITUDetailDefTable.TABLE_NAME;
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @param statement
     *            L'objet d'accès à la base
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idBouclement = statement.dbReadNumeric(ITUDetailDefTable.ID_BOUCLEMENT);
        canton = statement.dbReadString(ITUDetailDefTable.CANTON);
        idDetail = statement.dbReadNumeric(ITUDetailDefTable.ID_DETAIL);
        dateImport = statement.dbReadDateAMJ(ITUDetailDefTable.DATE_IMPORT);
        nombreMontant = statement.dbReadNumeric(ITUDetailDefTable.NOMBRE_MONTANT);
        noPassage = statement.dbReadNumeric(ITUDetailDefTable.NO_PASSAGE);
        csApplication = statement.dbReadNumeric(ITUDetailDefTable.CS_APPLICATION);
        csRubrique = statement.dbReadNumeric(ITUDetailDefTable.CS_RUBRIQUE);
        csTypeRubrique = statement.dbReadNumeric(ITUDetailDefTable.CS_TYPE_RUBRIQUE);
    }

    /**
     * Valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**
     * Indique la clé principale UDetail() du fichier TUBPDET
     * 
     * @param statement
     *            L'objet d'accès à la base
     * @throws Exception
     *             si problème lors de l'écriture de la clé
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(ITUDetailDefTable.ID_DETAIL,
                _dbWriteNumeric(statement.getTransaction(), getIdDetail(), "idDetail - id détail"));
    }

    /**
     * Ecriture des propriétés
     * 
     * @param statement
     *            L'objet d'accès à la base
     * @throws Exception
     *             si problème lors de l'écritrues des propriétés
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(
                ITUDetailDefTable.ID_BOUCLEMENT,
                _dbWriteNumeric(statement.getTransaction(), getIdBouclement(),
                        "idBouclement - clé primaire du fichier bouclement"));
        statement.writeField(ITUDetailDefTable.CANTON,
                _dbWriteString(statement.getTransaction(), getCanton(), "canton - canton"));
        statement.writeField(ITUDetailDefTable.ID_DETAIL,
                _dbWriteNumeric(statement.getTransaction(), getIdDetail(), "idDetail - id détail"));
        statement.writeField(ITUDetailDefTable.DATE_IMPORT,
                _dbWriteDateAMJ(statement.getTransaction(), getDateImport(), "dateImport - date importation"));
        statement.writeField(ITUDetailDefTable.NOMBRE_MONTANT,
                _dbWriteNumeric(statement.getTransaction(), getNombreMontant(), "nombreMontant - nombre ou montant"));
        statement.writeField(ITUDetailDefTable.NO_PASSAGE,
                _dbWriteNumeric(statement.getTransaction(), getNoPassage(), "noPassage - numéro de passage"));
        statement.writeField(
                ITUDetailDefTable.CS_APPLICATION,
                _dbWriteNumeric(statement.getTransaction(), getCsApplication(),
                        "csApplication - cs application tu_appli"));
        statement.writeField(ITUDetailDefTable.CS_RUBRIQUE,
                _dbWriteNumeric(statement.getTransaction(), getCsRubrique(), "csRubrique - cs rubrique tu_rubr"));
        statement.writeField(
                ITUDetailDefTable.CS_TYPE_RUBRIQUE,
                _dbWriteNumeric(statement.getTransaction(), getCsTypeRubrique(),
                        "csTypeRubrique - cs type de rubr tu_tyrubr"));
    }

    /**
     * Renvoie la zone canton - canton (BDETCA)
     * 
     * @return String canton - canton
     */
    public String getCanton() {
        return canton;
    }

    /**
     * Renvoie la zone csApplication - cs application tu_appli (CSAPPL)
     * 
     * @return String csApplication - cs application tu_appli
     */
    public String getCsApplication() {
        return csApplication;
    }

    /**
     * Renvoie la zone csRubrique - cs rubrique tu_rubr (CSRUBR)
     * 
     * @return String csRubrique - cs rubrique tu_rubr
     */
    public String getCsRubrique() {
        return csRubrique;
    }

    /**
     * Renvoie la zone csTypeRubrique - cs type de rubr tu_tyrubr (CSTYRU)
     * 
     * @return String csTypeRubrique - cs type de rubr tu_tyrubr
     */
    public String getCsTypeRubrique() {
        return csTypeRubrique;
    }

    /**
     * Renvoie la zone dateImport - date importation (BDETIM)
     * 
     * @return String dateImport - date importation
     */
    public String getDateImport() {
        return dateImport;
    }

    /**
     * Renvoie la zone idBouclement - clé primaire du fichier bouclement (BBOUID)
     * 
     * @return String idBouclement - clé primaire du fichier bouclement
     */
    public String getIdBouclement() {
        return idBouclement;
    }

    /**
     * Renvoie la zone idDetail - id détail (BDETID)
     * 
     * @return String idDetail - id détail
     */
    public String getIdDetail() {
        return idDetail;
    }

    /**
     * Renvoie la zone nombreMontant - nombre ou montant (BDETNM)
     * 
     * @return String nombreMontant - nombre ou montant
     */
    public String getNombreMontant() {
        return nombreMontant;
    }

    /**
     * Renvoie la zone noPassage - numéro de passage (BDETNP)
     * 
     * @return String noPassage - numéro de passage
     */
    public String getNoPassage() {
        return noPassage;
    }

    /**
     * Modifie la zone canton - canton (BDETCA)
     * 
     * @param newCanton
     *            - canton String
     */
    public void setCanton(String newCanton) {
        canton = newCanton;
    }

    /**
     * Modifie la zone csApplication - cs application tu_appli (CSAPPL)
     * 
     * @param newCsAplication
     *            - cs application tu_appli String
     */
    public void setCsApplication(String newCsAplication) {
        csApplication = newCsAplication;
    }

    /**
     * Modifie la zone csRubrique - cs rubrique tu_rubr (CSRUBR)
     * 
     * @param newCsRubrique
     *            - cs rubrique tu_rubr String
     */
    public void setCsRubrique(String newCsRubrique) {
        csRubrique = newCsRubrique;
    }

    /**
     * Modifie la zone csTypeRubrique - cs type de rubr tu_tyrubr (CSTYRU)
     * 
     * @param newCsTypeRubrique
     *            - cs type de rubr tu_tyrubr String
     */
    public void setCsTypeRubrique(String newCsTypeRubrique) {
        csTypeRubrique = newCsTypeRubrique;
    }

    /**
     * Modifie la zone dateImport - date importation (BDETIM)
     * 
     * @param newDateImport
     *            - date importation String
     */
    public void setDateImport(String newDateImport) {
        dateImport = newDateImport;
    }

    /**
     * Modifie la zone idBouclement - clé primaire du fichier bouclement (BBOUID)
     * 
     * @param newIdBouclement
     *            - clé primaire du fichier bouclement String
     */
    public void setIdBouclement(String newIdBouclement) {
        idBouclement = newIdBouclement;
    }

    /**
     * Modifie la zone idDetail - id détail (BDETID)
     * 
     * @param newIdDetail
     *            - id détail String
     */
    public void setIdDetail(String newIdDetail) {
        idDetail = newIdDetail;
    }

    /**
     * Modifie la zone nombreMontant - nombre ou montant (BDETNM)
     * 
     * @param newNombreMontant
     *            - nombre ou montant String
     */
    public void setNombreMontant(String newNombreMontant) {
        nombreMontant = newNombreMontant;
    }

    /**
     * Modifie la zone noPassage - numéro de passage (BDETNP)
     * 
     * @param newNoPassage
     *            - numéro de passage String
     */
    public void setNoPassage(String newNoPassage) {
        noPassage = newNoPassage;
    }
}
