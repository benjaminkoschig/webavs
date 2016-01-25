/*
 * Créé le 24 mars 2010
 */
package globaz.cygnus.db.conventions;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author fha
 */

public class RFConvention extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_BOOL_ACTIF = "ESBACT";
    public static final String FIELDNAME_DATE_CREATION = "ESDCRE";
    public static final String FIELDNAME_ID_CONVENTION = "ESICON";
    public static final String FIELDNAME_ID_GESTIONNAIRE = "ESIGES";
    public static final String FIELDNAME_TEXT_LIBELLE = "ESLLIB";

    public static final String TABLE_NAME = "RFCONVE";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public static final RFConvention loadConvention(BSession session, BITransaction transaction, String idConvention)
            throws Exception {
        RFConvention retValue;

        retValue = new RFConvention();
        retValue.setIdConvention(idConvention);
        retValue.setSession(session);

        if (transaction == null) {
            retValue.retrieve();
        } else {
            retValue.retrieve(transaction);
        }

        return retValue;
    }

    private String dateCreation = "";
    private String idConvention = "";
    private String idGestionnaire = "";
    private Boolean isActif = Boolean.FALSE;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String textLibelle = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFConvention.
     */
    public RFConvention() {
        super();
    }

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdConvention(_incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des dossiers
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idConvention = statement.dbReadNumeric(FIELDNAME_ID_CONVENTION);
        textLibelle = statement.dbReadString(FIELDNAME_TEXT_LIBELLE);
        isActif = statement.dbReadBoolean(FIELDNAME_BOOL_ACTIF);
        dateCreation = statement.dbReadDateAMJ(FIELDNAME_DATE_CREATION);
        idGestionnaire = statement.dbReadString(FIELDNAME_ID_GESTIONNAIRE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_CONVENTION,
                _dbWriteNumeric(statement.getTransaction(), idConvention, "idConvention"));

    }

    /**
     * Méthode d'écriture des champs dans la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_CONVENTION,
                _dbWriteNumeric(statement.getTransaction(), idConvention, "idConvention"));
        statement.writeField(FIELDNAME_TEXT_LIBELLE,
                _dbWriteString(statement.getTransaction(), textLibelle, "textLibelle"));
        statement.writeField(FIELDNAME_BOOL_ACTIF,
                _dbWriteBoolean(statement.getTransaction(), isActif, BConstants.DB_TYPE_BOOLEAN_CHAR, "isActif"));
        statement.writeField(FIELDNAME_DATE_CREATION,
                _dbWriteDateAMJ(statement.getTransaction(), dateCreation, "dateCreation"));
        statement.writeField(FIELDNAME_ID_GESTIONNAIRE,
                _dbWriteString(statement.getTransaction(), idGestionnaire, "idGestionnaire"));
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getIdConvention() {
        return idConvention;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public Boolean getIsActif() {
        return isActif;
    }

    public String getTextLibelle() {
        return textLibelle;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIsActif(Boolean isActif) {
        this.isActif = isActif;
    }

    public void setTextLibelle(String textLibelle) {
        this.textLibelle = textLibelle;
    }

}
