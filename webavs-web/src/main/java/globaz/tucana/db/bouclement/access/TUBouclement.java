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
    /** anneeComptable - ann�e comptable (BBOUAN) */
    private String anneeComptable = new String();
    /** csAgence - code syst�me agence (CSAGEN) */
    private String csAgence = new String();
    /** csEtat - code syst�me etat (CSETAT) */
    private String csEtat = new String();
    /** dateCreation - date de cr�ation (BBOUCR) */
    private String dateCreation = new String();
    /** dateEtat - date �tat (BBOUET) */
    private String dateEtat = new String();
    /** idBouclement - cl� primaire du fichier bouclement (BBOUID) */
    private String idBouclement = new String();
    /** idImportation - id importation cl� �trang�re (BBOUIM) */
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
     * M�thode qui incr�mente la cl� primaire
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
            // Des lignes de d�tail existent encore
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
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     * @exception Exception
     *                si la lecture des propri�t�s �choue
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
                // Cl� altern�e num�ro 1 : idType et idValeur
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
     * Indique la cl� principale UBouclement() du fichier TUBPBOU
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     * @throws Exception
     *             si probl�me lors de l'�criture de la cl�
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(ITUBouclementDefTable.ID_BOUCLEMENT,
                _dbWriteNumeric(statement.getTransaction(), getIdBouclement(), "idBouclement - id bouclement"));
    }

    /**
     * Ecriture des propri�t�s
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     * @throws Exception
     *             si probl�me lors de l'�critrues des propri�t�s
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(ITUBouclementDefTable.ANNEE_COMPTABLE,
                _dbWriteNumeric(statement.getTransaction(), getAnneeComptable(), "anneeComptable - ann�e comptable"));
        statement.writeField(ITUBouclementDefTable.DATE_CREATION,
                _dbWriteDateAMJ(statement.getTransaction(), getDateCreation(), "dateCreation - date de cr�ation"));
        statement.writeField(ITUBouclementDefTable.DATE_ETAT,
                _dbWriteDateAMJ(statement.getTransaction(), getDateEtat(), "dateEtat - date �tat"));
        statement.writeField(
                ITUBouclementDefTable.ID_BOUCLEMENT,
                _dbWriteNumeric(statement.getTransaction(), getIdBouclement(),
                        "idBouclement - cl� primaire du fichier bouclement"));
        statement.writeField(
                ITUBouclementDefTable.ID_IMPORTATION,
                _dbWriteNumeric(statement.getTransaction(), getIdImportation(),
                        "idImportation - id importation cl� �trang�re"));
        statement.writeField(ITUBouclementDefTable.MOIS_COMPTABLE,
                _dbWriteNumeric(statement.getTransaction(), getMoisComptable(), "moisComptable - mois comptable"));
        statement
                .writeField(
                        ITUBouclementDefTable.SOLDE_BOUCLEMENT,
                        _dbWriteNumeric(statement.getTransaction(), getSoldeBouclement(),
                                "soldeBouclement - solde bouclement"));
        statement.writeField(ITUBouclementDefTable.CS_AGENCE,
                _dbWriteNumeric(statement.getTransaction(), getCsAgence(), "csAgence - code syst�me agence"));
        statement.writeField(ITUBouclementDefTable.CS_ETAT,
                _dbWriteNumeric(statement.getTransaction(), getCsEtat(), "csEtat - code syst�me etat"));
    }

    /**
     * Renvoie la zone anneeComptable - ann�e comptable (BBOUAN)
     * 
     * @return String anneeComptable - ann�e comptable
     */
    public String getAnneeComptable() {
        return anneeComptable;
    }

    /**
     * Renvoie la zone csAgence - code syst�me agence (CSAGEN)
     * 
     * @return String csAgence - code syst�me agence
     */
    public String getCsAgence() {
        return csAgence;
    }

    /**
     * Renvoie la zone csEtat - code syst�me etat (CSETAT)
     * 
     * @return String csEtat - code syst�me etat
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * Renvoie la zone dateCreation - date de cr�ation (BBOUCR)
     * 
     * @return String dateCreation - date de cr�ation
     */
    public String getDateCreation() {
        return dateCreation;
    }

    /**
     * Renvoie la zone dateEtat - date �tat (BBOUET)
     * 
     * @return String dateEtat - date �tat
     */
    public String getDateEtat() {
        return dateEtat;
    }

    /**
     * Renvoie la zone idBouclement - cl� primaire du fichier bouclement (BBOUID)
     * 
     * @return String idBouclement - cl� primaire du fichier bouclement
     */
    public String getIdBouclement() {
        return idBouclement;
    }

    /**
     * Renvoie la zone idImportation - id importation cl� �trang�re (BBOUIM)
     * 
     * @return String idImportation - id importation cl� �trang�re
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
     * Modifie la zone anneeComptable - ann�e comptable (BBOUAN)
     * 
     * @param newAnneeComptable
     *            - ann�e comptable String
     */
    public void setAnneeComptable(String newAnneeComptable) {
        anneeComptable = newAnneeComptable;
    }

    /**
     * Modifie la zone csAgence - code syst�me agence (CSAGEN)
     * 
     * @param newCsAgence
     *            - code syst�me agence String
     */
    public void setCsAgence(String newCsAgence) {
        csAgence = newCsAgence;
    }

    /**
     * Modifie la zone csEtat - code syst�me etat (CSETAT)
     * 
     * @param newCsEtat
     *            - code syst�me etat String
     */
    public void setCsEtat(String newCsEtat) {
        csEtat = newCsEtat;
    }

    /**
     * Modifie la zone dateCreation - date de cr�ation (BBOUCR)
     * 
     * @param newDateCreation
     *            - date de cr�ation String
     */
    public void setDateCreation(String newDateCreation) {
        dateCreation = newDateCreation;
    }

    /**
     * Modifie la zone dateEtat - date �tat (BBOUET)
     * 
     * @param newDateEtat
     *            - date �tat String
     */
    public void setDateEtat(String newDateEtat) {
        dateEtat = newDateEtat;
    }

    /**
     * Modifie la zone idBouclement - cl� primaire du fichier bouclement (BBOUID)
     * 
     * @param newIdBouclement
     *            - cl� primaire du fichier bouclement String
     */
    public void setIdBouclement(String newIdBouclement) {
        idBouclement = newIdBouclement;
    }

    /**
     * Modifie la zone idImportation - id importation cl� �trang�re (BBOUIM)
     * 
     * @param newIdImportation
     *            - id importation cl� �trang�re String
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
