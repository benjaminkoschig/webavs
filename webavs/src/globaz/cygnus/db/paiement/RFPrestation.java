/*
 * Créé le 10 décembre 2010
 */
package globaz.cygnus.db.paiement;

import globaz.cygnus.db.ordresversements.RFOrdresVersements;
import globaz.cygnus.db.ordresversements.RFOrdresVersementsManager;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author FHA
 * 
 */
public class RFPrestation extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_DATE_MOIS_ANNEE = "FODMAN";

    public static final String FIELDNAME_ETAT = "FOTETA";
    public static final String FIELDNAME_ID_ADRESSE_PAIEMENT = "FOIADP";
    public static final String FIELDNAME_ID_DECISION = "FOIDEC";
    public static final String FIELDNAME_ID_LOT = "FOILOT";
    public static final String FIELDNAME_ID_PRESTATION = "FOIPRE";
    public static final String FIELDNAME_ID_TIERS_BENEFICIAIRE = "FOIBEN";
    public static final String FIELDNAME_IS_LAPRAMS = "FOBILA";
    public static final String FIELDNAME_IS_RI = "FOBIRI";
    public static final String FIELDNAME_MONTANT_TOTAL = "FOMTOT";
    public static final String FIELDNAME_REFERENCE_PAIEMENT = "FOLRPA";
    public static final String FIELDNAME_REMBOURSEMENT_CONJOINT = "FOTREC";
    public static final String FIELDNAME_REMBOURSEMENT_REQUERANT = "FOTRER";
    public static final String FIELDNAME_TYPE_PRESTATION = "FOTGPR";

    public static final String TABLE_NAME_PREST = "RFPREST";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csEtatPrestation = "";
    private String dateMoisAnnee = "";
    private String idAdressePaiement = "";
    private String idDecision = "";
    private String idLot = "";
    private String idPrestation = null;
    private String idTiersBeneficiaire = "";
    private Boolean isLAPRAMS = Boolean.FALSE;
    private Boolean isRI = Boolean.FALSE;
    private String montantTotal = "";
    private String referencePaiement = "";
    private String remboursementConjoint = "";
    private String remboursementRequerant = "";
    private String typePrestation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdPrestation(this._incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return RFPrestation.TABLE_NAME_PREST;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idPrestation = statement.dbReadNumeric(RFPrestation.FIELDNAME_ID_PRESTATION);
        dateMoisAnnee = statement.dbReadDateAMJ(RFPrestation.FIELDNAME_DATE_MOIS_ANNEE);
        montantTotal = statement.dbReadNumeric(RFPrestation.FIELDNAME_MONTANT_TOTAL);
        csEtatPrestation = statement.dbReadNumeric(RFPrestation.FIELDNAME_ETAT);
        idDecision = statement.dbReadNumeric(RFPrestation.FIELDNAME_ID_DECISION);
        idLot = statement.dbReadNumeric(RFPrestation.FIELDNAME_ID_LOT);
        idAdressePaiement = statement.dbReadNumeric(RFPrestation.FIELDNAME_ID_ADRESSE_PAIEMENT);
        typePrestation = statement.dbReadNumeric(RFPrestation.FIELDNAME_TYPE_PRESTATION);
        referencePaiement = statement.dbReadString(RFPrestation.FIELDNAME_REFERENCE_PAIEMENT);
        remboursementRequerant = statement.dbReadNumeric(RFPrestation.FIELDNAME_REMBOURSEMENT_REQUERANT);
        remboursementConjoint = statement.dbReadNumeric(RFPrestation.FIELDNAME_REMBOURSEMENT_CONJOINT);
        isRI = statement.dbReadBoolean(RFPrestation.FIELDNAME_IS_RI);
        isLAPRAMS = statement.dbReadBoolean(RFPrestation.FIELDNAME_IS_LAPRAMS);
        idTiersBeneficiaire = statement.dbReadNumeric(RFPrestation.FIELDNAME_ID_TIERS_BENEFICIAIRE);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(RFPrestation.FIELDNAME_ID_PRESTATION,
                this._dbWriteNumeric(statement.getTransaction(), idPrestation, "idPrestation"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RFPrestation.FIELDNAME_ID_PRESTATION,
                this._dbWriteNumeric(statement.getTransaction(), idPrestation, "idPrestation"));
        statement.writeField(RFPrestation.FIELDNAME_DATE_MOIS_ANNEE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateMoisAnnee, "dateMoisAnnee"));
        statement.writeField(RFPrestation.FIELDNAME_MONTANT_TOTAL,
                this._dbWriteNumeric(statement.getTransaction(), montantTotal, "montantTotal"));
        statement.writeField(RFPrestation.FIELDNAME_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), csEtatPrestation, "csEtatPrestation"));
        statement.writeField(RFPrestation.FIELDNAME_ID_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
        statement.writeField(RFPrestation.FIELDNAME_ID_LOT,
                this._dbWriteNumeric(statement.getTransaction(), idLot, "idLot"));
        statement.writeField(RFPrestation.FIELDNAME_ID_ADRESSE_PAIEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idAdressePaiement, "idAdressePaiement"));
        statement.writeField(RFPrestation.FIELDNAME_TYPE_PRESTATION,
                this._dbWriteNumeric(statement.getTransaction(), typePrestation, "typePrestation"));
        statement.writeField(RFPrestation.FIELDNAME_REFERENCE_PAIEMENT,
                this._dbWriteString(statement.getTransaction(), referencePaiement, "referencePaiement"));
        statement.writeField(RFPrestation.FIELDNAME_REMBOURSEMENT_REQUERANT,
                this._dbWriteNumeric(statement.getTransaction(), remboursementRequerant, "remboursementRequerant"));
        statement.writeField(RFPrestation.FIELDNAME_REMBOURSEMENT_CONJOINT,
                this._dbWriteNumeric(statement.getTransaction(), remboursementConjoint, "remboursementConjoint"));
        statement.writeField(RFPrestation.FIELDNAME_IS_RI,
                this._dbWriteBoolean(statement.getTransaction(), isRI, BConstants.DB_TYPE_BOOLEAN_CHAR, "isRI"));
        statement.writeField(RFPrestation.FIELDNAME_IS_LAPRAMS, this._dbWriteBoolean(statement.getTransaction(),
                isLAPRAMS, BConstants.DB_TYPE_BOOLEAN_CHAR, "isLAPRAMS"));

        statement.writeField(RFPrestation.FIELDNAME_ID_TIERS_BENEFICIAIRE,
                this._dbWriteNumeric(statement.getTransaction(), idTiersBeneficiaire, "idTiersBeneficiaire"));
    }

    public String getCsEtatPrestation() {
        return csEtatPrestation;
    }

    public String getDateMoisAnnee() {
        return dateMoisAnnee;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public Boolean getIsLAPRAMS() {
        return isLAPRAMS;
    }

    public Boolean getIsRI() {
        return isRI;
    }

    public String getMontantTotal() {
        return montantTotal;
    }

    public RFOrdresVersements[] getOrdresVersement(BTransaction transaction) throws Exception {

        RFOrdresVersementsManager mgr = new RFOrdresVersementsManager();
        mgr.setSession(getSession());
        mgr.setForIdPrestation(getIdPrestation());
        mgr.setForIdDecision(idDecision);
        mgr.find(transaction, BManager.SIZE_NOLIMIT);

        return (RFOrdresVersements[]) mgr.getContainer().toArray(new RFOrdresVersements[mgr.size()]);
    }

    public String getReferencePaiement() {
        return referencePaiement;
    }

    public String getRemboursementConjoint() {
        return remboursementConjoint;
    }

    public String getRemboursementRequerant() {
        return remboursementRequerant;
    }

    public String getTypePrestation() {
        return typePrestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasCreationSpy()
     */
    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setCsEtatPrestation(String csEtatPrestation) {
        this.csEtatPrestation = csEtatPrestation;
    }

    public void setDateMoisAnnee(String dateMoisAnnee) {
        this.dateMoisAnnee = dateMoisAnnee;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setIsLAPRAMS(Boolean isLAPRAMS) {
        this.isLAPRAMS = isLAPRAMS;
    }

    public void setIsRI(Boolean isRI) {
        this.isRI = isRI;
    }

    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

    public void setRemboursementConjoint(String remboursementConjoint) {
        this.remboursementConjoint = remboursementConjoint;
    }

    public void setRemboursementRequerant(String remboursementRequerant) {
        this.remboursementRequerant = remboursementRequerant;
    }

    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }

}
