/*
 * Créé le 06 janvier 2010
 */
package globaz.cygnus.db.demandes;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author jje
 */
public class RFDemandeDev19 extends RFDemande {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_ENVOI_ACCEPTATION = "EJDACC";
    public static final String FIELDNAME_DATE_ENVOI_MDC = "EJDMDC";
    public static final String FIELDNAME_DATE_ENVOI_MDT = "EJDMDT";
    public static final String FIELDNAME_DATE_RECEPTION_PREAVIS = "EJDRPR";
    public static final String FIELDNAME_ID_DEMANDE_DEVIS_19 = "EJIDEM";
    public static final String FIELDNAME_MONTANT_ACCEPTATION = "EJMACC";

    public static final String TABLE_NAME = "RFDEV19";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Génération de la clause from pour la requête > Jointure depuis les demande jusqu'au demande 19
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);

        // jointure entre la table des demandes RFM et la table des demandes 19
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeDev19.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeDev19.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemandeDev19.FIELDNAME_ID_DEMANDE_DEVIS_19);

        return fromClauseBuffer.toString();
    }

    private String dateEnvoiAcceptation = "";

    private String dateEnvoiMDC = "";
    private String dateEnvoiMDT = "";
    private String dateReceptionPreavis = "";
    private boolean hasCreationSpy = false;
    private boolean hasSpy = false;
    private String idDemandeDevis19 = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String montantAcceptation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDemandeDev19
     */
    public RFDemandeDev19() {
        super();
    }

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
        getFrom += RFDemandeDev19.TABLE_NAME;
        getFrom += " ON ";
        getFrom += RFDemandeDev19.FIELDNAME_ID_DEMANDE_DEVIS_19;
        getFrom += "=";
        getFrom += RFDemande.FIELDNAME_ID_DEMANDE;

        return getFrom;
    }

    /**
     * getter pour le nom de la table des demandes de devis
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFDemandeDev19.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des demandes de devis
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idDemandeDevis19 = statement.dbReadNumeric(RFDemandeDev19.FIELDNAME_ID_DEMANDE_DEVIS_19);
        dateEnvoiMDT = statement.dbReadDateAMJ(RFDemandeDev19.FIELDNAME_DATE_ENVOI_MDT);
        dateEnvoiMDC = statement.dbReadDateAMJ(RFDemandeDev19.FIELDNAME_DATE_ENVOI_MDC);
        dateReceptionPreavis = statement.dbReadDateAMJ(RFDemandeDev19.FIELDNAME_DATE_RECEPTION_PREAVIS);
        montantAcceptation = statement.dbReadNumeric(RFDemandeDev19.FIELDNAME_MONTANT_ACCEPTATION);
        dateEnvoiAcceptation = statement.dbReadDateAMJ(RFDemandeDev19.FIELDNAME_DATE_ENVOI_ACCEPTATION);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des demandes de devis
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFDemandeDev19.FIELDNAME_ID_DEMANDE_DEVIS_19,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeDevis19, "idDemandeDevis19"));
    }

    /**
     * Méthode d'écriture des champs dans la table des demandes de devis
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // super._writeProperties(statement);
        statement.writeField(RFDemandeDev19.FIELDNAME_ID_DEMANDE_DEVIS_19,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeDevis19, "idDemandeDevis19"));
        statement.writeField(RFDemandeDev19.FIELDNAME_DATE_ENVOI_MDT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateEnvoiMDT, "dateEnvoiMDT"));
        statement.writeField(RFDemandeDev19.FIELDNAME_DATE_ENVOI_MDC,
                this._dbWriteDateAMJ(statement.getTransaction(), dateEnvoiMDC, "dateEnvoiMDC"));
        statement.writeField(RFDemandeDev19.FIELDNAME_DATE_RECEPTION_PREAVIS,
                this._dbWriteDateAMJ(statement.getTransaction(), dateReceptionPreavis, "dateReceptionPreavis"));
        statement.writeField(RFDemandeDev19.FIELDNAME_MONTANT_ACCEPTATION,
                this._dbWriteNumeric(statement.getTransaction(), montantAcceptation, "montantAcceptation"));
        statement.writeField(RFDemandeDev19.FIELDNAME_DATE_ENVOI_ACCEPTATION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateEnvoiAcceptation, "dateEnvoiAcceptation"));

    }

    public String getDateEnvoiAcceptation() {
        return dateEnvoiAcceptation;
    }

    public String getDateEnvoiMDC() {
        return dateEnvoiMDC;
    }

    public String getDateEnvoiMDT() {
        return dateEnvoiMDT;
    }

    public String getDateReceptionPreavis() {
        return dateReceptionPreavis;
    }

    public String getIdDemandeDevis19() {
        return idDemandeDevis19;
    }

    public String getMontantAcceptation() {
        return montantAcceptation;
    }

    @Override
    public boolean hasCreationSpy() {
        return hasCreationSpy;
    }

    @Override
    public boolean hasSpy() {
        return hasSpy;
    }

    public void setDateEnvoiAcceptation(String dateEnvoiAcceptation) {
        this.dateEnvoiAcceptation = dateEnvoiAcceptation;
    }

    public void setDateEnvoiMDC(String dateEnvoiMDC) {
        this.dateEnvoiMDC = dateEnvoiMDC;
    }

    public void setDateEnvoiMDT(String dateEnvoiMDT) {
        this.dateEnvoiMDT = dateEnvoiMDT;
    }

    public void setDateReceptionPreavis(String dateReceptionPreavis) {
        this.dateReceptionPreavis = dateReceptionPreavis;
    }

    public void setHasCreationSpy(boolean hasCreationSpy) {
        this.hasCreationSpy = hasCreationSpy;
    }

    public void setHasSpy(boolean hasSpy) {
        this.hasSpy = hasSpy;
    }

    public void setIdDemandeDevis19(String idDemandeDevis19) {
        this.idDemandeDevis19 = idDemandeDevis19;
    }

    public void setMontantAcceptation(String montantAcceptation) {
        this.montantAcceptation = montantAcceptation;
    }

}