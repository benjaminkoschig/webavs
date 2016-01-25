/*
 * Créé le 06 janvier 2010
 */
package globaz.cygnus.db.demandes;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author jje
 */
public class RFDemandeMoy5_6_7 extends RFDemande {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_DECISION_OAI = "EKDOAI";
    public static final String FIELDNAME_ID_DEMANDE_MOYENS_AUX = "EKIDEM";
    public static final String FIELDNAME_MONTANT_FACTURE_44 = "EKMF44";
    public static final String FIELDNAME_MONTANT_VERSE_OAI = "EKMOAI";

    public static final String TABLE_NAME = "RFDMAUX";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateDecisionOAI = "";

    private boolean hasCreationSpy = false;
    private boolean hasSpy = false;
    private String idDemandeMoyensAux = "";
    private String montantFacture44 = "";
    private String montantVerseOAI = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDemandeMoy5.
     */
    public RFDemandeMoy5_6_7() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return faux
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // super._beforeAdd(transaction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String getFrom = "";

        getFrom += _getCollection();
        getFrom += RFDemande.TABLE_NAME;
        getFrom += " INNER JOIN ";
        getFrom += _getCollection();
        getFrom += RFDemandeMoy5_6_7.TABLE_NAME;
        getFrom += " ON ";
        getFrom += RFDemandeMoy5_6_7.FIELDNAME_ID_DEMANDE_MOYENS_AUX;
        getFrom += "=";
        getFrom += RFDemande.FIELDNAME_ID_DEMANDE;

        return getFrom;
    }

    /**
     * getter pour le nom de la table des demandes Moyen aux.
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFDemandeMoy5_6_7.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des demandes Moyen aux.
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idDemandeMoyensAux = statement.dbReadNumeric(RFDemandeMoy5_6_7.FIELDNAME_ID_DEMANDE_MOYENS_AUX);
        dateDecisionOAI = statement.dbReadDateAMJ(RFDemandeMoy5_6_7.FIELDNAME_DATE_DECISION_OAI);
        montantVerseOAI = statement.dbReadNumeric(RFDemandeMoy5_6_7.FIELDNAME_MONTANT_VERSE_OAI);
        montantFacture44 = statement.dbReadNumeric(RFDemandeMoy5_6_7.FIELDNAME_MONTANT_FACTURE_44);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des demandes Moyen aux.
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // statement.writeKey(FIELDNAME_ID_DEMANDE_MOYENS_AUX,
        // _dbWriteNumeric(statement.getTransaction(), getIdDemande(),
        // "idDemande"));
        statement.writeKey(RFDemandeMoy5_6_7.FIELDNAME_ID_DEMANDE_MOYENS_AUX,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeMoyensAux, "idDemandeMoyensAux5"));
    }

    /**
     * Méthode d'écriture des champs dans la table des demandes Moyen aux.
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // super._writeProperties(statement);
        // statement.writeField(FIELDNAME_ID_DEMANDE_MOYENS_AUX,
        // _dbWriteNumeric(statement.getTransaction(), getIdDemande(),
        // "idDemande"));
        statement.writeField(RFDemandeMoy5_6_7.FIELDNAME_ID_DEMANDE_MOYENS_AUX,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeMoyensAux, "idDemandeMoyensAux5"));
        statement.writeField(RFDemandeMoy5_6_7.FIELDNAME_DATE_DECISION_OAI,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDecisionOAI, "dateDecisionOAI"));
        statement.writeField(RFDemandeMoy5_6_7.FIELDNAME_MONTANT_VERSE_OAI,
                this._dbWriteNumeric(statement.getTransaction(), montantVerseOAI, "montantVerseOAI"));
        statement.writeField(RFDemandeMoy5_6_7.FIELDNAME_MONTANT_FACTURE_44,
                this._dbWriteNumeric(statement.getTransaction(), montantFacture44, "montantFacture44"));

    }

    public String getDateDecisionOAI() {
        return dateDecisionOAI;
    }

    public String getIdDemandeMoyensAux() {
        return idDemandeMoyensAux;
    }

    public String getMontantFacture44() {
        return montantFacture44;
    }

    public String getMontantVerseOAI() {
        return montantVerseOAI;
    }

    @Override
    public boolean hasCreationSpy() {
        return hasCreationSpy;
    }

    @Override
    public boolean hasSpy() {
        return hasSpy;
    }

    public void setDateDecisionOAI(String dateDecisionOAI) {
        this.dateDecisionOAI = dateDecisionOAI;
    }

    public void setHasCreationSpy(boolean hasCreationSpy) {
        this.hasCreationSpy = hasCreationSpy;
    }

    public void setHasSpy(boolean hasSpy) {
        this.hasSpy = hasSpy;
    }

    public void setIdDemandeMoyensAux(String idDemandeMoyensAux) {
        this.idDemandeMoyensAux = idDemandeMoyensAux;
    }

    public void setMontantFacture44(String montantFacture44) {
        this.montantFacture44 = montantFacture44;
    }

    public void setMontantVerseOAI(String montantVerseOAI) {
        this.montantVerseOAI = montantVerseOAI;
    }

}