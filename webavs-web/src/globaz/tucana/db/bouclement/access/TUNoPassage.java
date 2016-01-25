package globaz.tucana.db.bouclement.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri May 05 15:07:58 CEST 2006
 */
public class TUNoPassage extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int KEY_ALTERNATE_BOUCLEMENT = 1;
    /** csApplication - cs application tu_appli (CSAPPL) */
    private String csApplication = new String();
    /** Table : TUBPNP */
    /** idBouclement - clé primaire du fichier bouclement (BBOUID) */
    private String idBouclement = new String();
    /**
     * idNoPassage - représentera la clé primaire du fichier une fois générée (BPNPID)
     */
    private String idNoPassage = new String();
    /** noPassage - numéro de passage (BPNPNP) */
    private String noPassage = new String();

    /**
     * Constructeur de la classe TUNoPassage
     */
    public TUNoPassage() {
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
        setIdNoPassage(_incCounter(transaction, "0"));
    }

    /**
     * Renvoie le nom de la table TUBPNP
     * 
     * @return String TUBPNP
     */
    @Override
    protected String _getTableName() {
        return ITUNoPassageDefTable.TABLE_NAME;
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
        idBouclement = statement.dbReadNumeric(ITUNoPassageDefTable.ID_BOUCLEMENT);
        idNoPassage = statement.dbReadNumeric(ITUNoPassageDefTable.ID_NO_PASSAGE);
        noPassage = statement.dbReadNumeric(ITUNoPassageDefTable.NO_PASSAGE);
        csApplication = statement.dbReadNumeric(ITUNoPassageDefTable.CS_APPLICATION);
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeAlternateKey(globaz.globall.db.BStatement , int)
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        switch (alternateKey) {
            case 1:
                // Clé alternée numéro 1 : idBouclement, idPassage
                statement.writeKey(ITUNoPassageDefTable.ID_BOUCLEMENT,
                        _dbWriteNumeric(statement.getTransaction(), getIdBouclement(), ""));
                statement.writeKey(ITUNoPassageDefTable.CS_APPLICATION,
                        _dbWriteNumeric(statement.getTransaction(), getCsApplication(), ""));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * Indique la clé principale UNoPassage() du fichier TUBPNP
     * 
     * @param statement
     *            L'objet d'accès à la base
     * @throws Exception
     *             si problème lors de l'écriture de la clé
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(
                ITUNoPassageDefTable.ID_NO_PASSAGE,
                _dbWriteNumeric(statement.getTransaction(), getIdNoPassage(),
                        "idNoPassage - représentera la clé primaire du fichier une fois générée"));
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
                ITUNoPassageDefTable.ID_BOUCLEMENT,
                _dbWriteNumeric(statement.getTransaction(), getIdBouclement(),
                        "idBouclement - clé primaire du fichier bouclement"));
        statement.writeField(
                ITUNoPassageDefTable.ID_NO_PASSAGE,
                _dbWriteNumeric(statement.getTransaction(), getIdNoPassage(),
                        "idNoPassage - représentera la clé primaire du fichier une fois générée"));
        statement.writeField(ITUNoPassageDefTable.NO_PASSAGE,
                _dbWriteNumeric(statement.getTransaction(), getNoPassage(), "noPassage - numéro de passage"));
        statement.writeField(
                ITUNoPassageDefTable.CS_APPLICATION,
                _dbWriteNumeric(statement.getTransaction(), getCsApplication(),
                        "csApplication - cs application tu_appli"));
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
     * Renvoie la zone idBouclement - clé primaire du fichier bouclement (BBOUID)
     * 
     * @return String idBouclement - clé primaire du fichier bouclement
     */
    public String getIdBouclement() {
        return idBouclement;
    }

    /**
     * Renvoie la zone idNoPassage - représentera la clé primaire du fichier une fois générée (BPNPID)
     * 
     * @return String idNoPassage - représentera la clé primaire du fichier une fois générée
     */
    public String getIdNoPassage() {
        return idNoPassage;
    }

    /**
     * Renvoie la zone noPassage - numéro de passage (BPNPNP)
     * 
     * @return String noPassage - numéro de passage
     */
    public String getNoPassage() {
        return noPassage;
    }

    /**
     * Modifie la zone csApplication - cs application tu_appli (CSAPPL)
     * 
     * @param newCsApplication
     *            - cs application tu_appli String
     */
    public void setCsApplication(String newCsApplication) {
        csApplication = newCsApplication;
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
     * Modifie la zone idNoPassage - représentera la clé primaire du fichier une fois générée (BPNPID)
     * 
     * @param newIdNoPassage
     *            - représentera la clé primaire du fichier une fois générée String
     */
    public void setIdNoPassage(String newIdNoPassage) {
        idNoPassage = newIdNoPassage;
    }

    /**
     * Modifie la zone noPassage - numéro de passage (BPNPNP)
     * 
     * @param newNoPassage
     *            - numéro de passage String
     */
    public void setNoPassage(String newNoPassage) {
        noPassage = newNoPassage;
    }
}
