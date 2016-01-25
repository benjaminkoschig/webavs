package globaz.tucana.db.bouclement.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.tucana.application.TUApplication;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Wed May 03 13:47:48 CEST 2006
 */
public class TUBouclement extends BEntity {
    /** Table : TUBPBOU */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** anneeComptable - année comptable (BBOUAN) */
    private String anneeComptable = new String();
    /** csAgence - code système agence (CSAGEN) */
    private String csAgence = new String();
    /** csEtat - code système etat (CSETAT) */
    private String csEtat = new String();
    /** dateCreation - date de création (BBOUCR) */
    private String dateCreation = new String();
    /** dateEtat - date état (BBOUET) */
    private String dateEtat = new String();
    /** idBouclement - clé primaire du fichier bouclement (BBOUID) */
    private String idBouclement = new String();
    /** idImportation - id importation clé étrangère (BBOUIM) */
    private String idImportation = new String();
    /** moisComptable - mois comptable (BBOUMO) */
    private String moisComptable = new String();
    /** soldeBouclement - solde bouclement (BBOUSO) */
    private String soldeBouclement = new String();

    /**
     * Constructeur de la classe TUBouclement
     */
    public TUBouclement() {
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
        setIdBouclement(_incCounter(transaction, "0"));
        // Teste la valeur du code agence, si vide prend la valeur du fichier de
        // properties
        if (JadeStringUtil.isIntegerEmpty(csAgence)) {
            BSession sessionTmp = new BSession();
            sessionTmp.setApplication(TUApplication.DEFAULT_APPLICATION_TUCANA);
            setCsAgence(sessionTmp.getApplication().getProperty(TUApplication.CS_AGENCE));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        TUDetailManager detailManager = new TUDetailManager();
        detailManager.setSession(transaction.getSession());
        detailManager.setForIdBouclement(getIdBouclement());
        if (detailManager.getCount() > 0) {
            // Des lignes de détail existent encore
            _addError(transaction, transaction.getSession().getLabel("ERR_DETAIL_TROUVE"));
        }
    }

    /**
     * Renvoie le nom de la table TUBPBOU
     * 
     * @return String TUBPBOU
     */
    @Override
    protected String _getTableName() {
        return ITUBouclementDefTable.TABLE_NAME;
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
        anneeComptable = statement.dbReadNumeric(ITUBouclementDefTable.ANNEE_COMPTABLE);
        dateCreation = statement.dbReadDateAMJ(ITUBouclementDefTable.DATE_CREATION);
        dateEtat = statement.dbReadDateAMJ(ITUBouclementDefTable.DATE_ETAT);
        idBouclement = statement.dbReadNumeric(ITUBouclementDefTable.ID_BOUCLEMENT);
        idImportation = statement.dbReadNumeric(ITUBouclementDefTable.ID_IMPORTATION);
        moisComptable = statement.dbReadNumeric(ITUBouclementDefTable.MOIS_COMPTABLE);
        soldeBouclement = statement.dbReadNumeric(ITUBouclementDefTable.SOLDE_BOUCLEMENT, 2);
        csAgence = statement.dbReadNumeric(ITUBouclementDefTable.CS_AGENCE);
        csEtat = statement.dbReadNumeric(ITUBouclementDefTable.CS_ETAT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeAlternateKey(globaz.globall.db.BStatement , int)
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        switch (alternateKey) {
            case 1:
                // Clé alternée numéro 1 : idType et idValeur
                statement.writeKey(ITUBouclementDefTable.ANNEE_COMPTABLE,
                        _dbWriteNumeric(statement.getTransaction(), getAnneeComptable(), ""));
                statement.writeKey(ITUBouclementDefTable.MOIS_COMPTABLE,
                        _dbWriteNumeric(statement.getTransaction(), getMoisComptable(), ""));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * Indique la clé principale UBouclement() du fichier TUBPBOU
     * 
     * @param statement
     *            L'objet d'accès à la base
     * @throws Exception
     *             si problème lors de l'écriture de la clé
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(ITUBouclementDefTable.ID_BOUCLEMENT,
                _dbWriteNumeric(statement.getTransaction(), getIdBouclement(), "idBouclement - id bouclement"));
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
        statement.writeField(ITUBouclementDefTable.ANNEE_COMPTABLE,
                _dbWriteNumeric(statement.getTransaction(), getAnneeComptable(), "anneeComptable - année comptable"));
        statement.writeField(ITUBouclementDefTable.DATE_CREATION,
                _dbWriteDateAMJ(statement.getTransaction(), getDateCreation(), "dateCreation - date de création"));
        statement.writeField(ITUBouclementDefTable.DATE_ETAT,
                _dbWriteDateAMJ(statement.getTransaction(), getDateEtat(), "dateEtat - date état"));
        statement.writeField(
                ITUBouclementDefTable.ID_BOUCLEMENT,
                _dbWriteNumeric(statement.getTransaction(), getIdBouclement(),
                        "idBouclement - clé primaire du fichier bouclement"));
        statement.writeField(
                ITUBouclementDefTable.ID_IMPORTATION,
                _dbWriteNumeric(statement.getTransaction(), getIdImportation(),
                        "idImportation - id importation clé étrangère"));
        statement.writeField(ITUBouclementDefTable.MOIS_COMPTABLE,
                _dbWriteNumeric(statement.getTransaction(), getMoisComptable(), "moisComptable - mois comptable"));
        statement
                .writeField(
                        ITUBouclementDefTable.SOLDE_BOUCLEMENT,
                        _dbWriteNumeric(statement.getTransaction(), getSoldeBouclement(),
                                "soldeBouclement - solde bouclement"));
        statement.writeField(ITUBouclementDefTable.CS_AGENCE,
                _dbWriteNumeric(statement.getTransaction(), getCsAgence(), "csAgence - code système agence"));
        statement.writeField(ITUBouclementDefTable.CS_ETAT,
                _dbWriteNumeric(statement.getTransaction(), getCsEtat(), "csEtat - code système etat"));
    }

    /**
     * Renvoie la zone anneeComptable - année comptable (BBOUAN)
     * 
     * @return String anneeComptable - année comptable
     */
    public String getAnneeComptable() {
        return anneeComptable;
    }

    /**
     * Renvoie la zone csAgence - code système agence (CSAGEN)
     * 
     * @return String csAgence - code système agence
     */
    public String getCsAgence() {
        return csAgence;
    }

    /**
     * Renvoie la zone csEtat - code système etat (CSETAT)
     * 
     * @return String csEtat - code système etat
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * Renvoie la zone dateCreation - date de création (BBOUCR)
     * 
     * @return String dateCreation - date de création
     */
    public String getDateCreation() {
        return dateCreation;
    }

    /**
     * Renvoie la zone dateEtat - date état (BBOUET)
     * 
     * @return String dateEtat - date état
     */
    public String getDateEtat() {
        return dateEtat;
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
     * Renvoie la zone idImportation - id importation clé étrangère (BBOUIM)
     * 
     * @return String idImportation - id importation clé étrangère
     */
    public String getIdImportation() {
        return idImportation;
    }

    /**
     * Renvoie la zone moisComptable - mois comptable (BBOUMO)
     * 
     * @return String moisComptable - mois comptable
     */
    public String getMoisComptable() {
        return moisComptable;
    }

    /**
     * Renvoie la zone soldeBouclement - solde bouclement (BBOUSO)
     * 
     * @return String soldeBouclement - solde bouclement
     */
    public String getSoldeBouclement() {
        return soldeBouclement;
    }

    /**
     * Modifie la zone anneeComptable - année comptable (BBOUAN)
     * 
     * @param newAnneeComptable
     *            - année comptable String
     */
    public void setAnneeComptable(String newAnneeComptable) {
        anneeComptable = newAnneeComptable;
    }

    /**
     * Modifie la zone csAgence - code système agence (CSAGEN)
     * 
     * @param newCsAgence
     *            - code système agence String
     */
    public void setCsAgence(String newCsAgence) {
        csAgence = newCsAgence;
    }

    /**
     * Modifie la zone csEtat - code système etat (CSETAT)
     * 
     * @param newCsEtat
     *            - code système etat String
     */
    public void setCsEtat(String newCsEtat) {
        csEtat = newCsEtat;
    }

    /**
     * Modifie la zone dateCreation - date de création (BBOUCR)
     * 
     * @param newDateCreation
     *            - date de création String
     */
    public void setDateCreation(String newDateCreation) {
        dateCreation = newDateCreation;
    }

    /**
     * Modifie la zone dateEtat - date état (BBOUET)
     * 
     * @param newDateEtat
     *            - date état String
     */
    public void setDateEtat(String newDateEtat) {
        dateEtat = newDateEtat;
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
     * Modifie la zone idImportation - id importation clé étrangère (BBOUIM)
     * 
     * @param newIdImportation
     *            - id importation clé étrangère String
     */
    public void setIdImportation(String newIdImportation) {
        idImportation = newIdImportation;
    }

    /**
     * Modifie la zone moisComptable - mois comptable (BBOUMO)
     * 
     * @param newMoisComptable
     *            - mois comptable String
     */
    public void setMoisComptable(String newMoisComptable) {
        moisComptable = newMoisComptable;
    }

    /**
     * Modifie la zone soldeBouclement - solde bouclement (BBOUSO)
     * 
     * @param newSoldeBouclement
     *            - solde bouclement String
     */
    public void setSoldeBouclement(String newSoldeBouclement) {
        soldeBouclement = newSoldeBouclement;
    }
}
