package globaz.cygnus.db.demandes;

import globaz.globall.db.BStatement;

/**
 * author jje
 */
public class RFDemandeDev19JointAssDemandeDev19Ftd15 extends RFDemande {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Génération de la clause from pour la requête > Jointure entre la demande de devis 19 et la demande ftd 15
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {

        StringBuffer fromClauseBuffer = new StringBuffer();
        String leftJoint = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(RFDemandeDev19.createFromClause(schema));

        // jointure entre la table des demandes 19 et la table ass demandes ftd
        // 15
        fromClauseBuffer.append(leftJoint);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssDemandeDev19Ftd15.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssDemandeDev19Ftd15.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssDemandeDev19Ftd15.FIELDNAME_ID_DEMANDE_DEV19);
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
    private String idDemandeDevis19 = "";

    private String idDemandeDevis19Ass = "";
    private String idDemandeFtd15Ass = "";

    private String montantAcceptation = "";
    private String montantAssocieAuDevis = "";

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idDemandeDevis19 = statement.dbReadNumeric(RFDemandeDev19.FIELDNAME_ID_DEMANDE_DEVIS_19);
        dateEnvoiMDT = statement.dbReadDateAMJ(RFDemandeDev19.FIELDNAME_DATE_ENVOI_MDT);
        dateEnvoiMDC = statement.dbReadDateAMJ(RFDemandeDev19.FIELDNAME_DATE_ENVOI_MDC);
        dateReceptionPreavis = statement.dbReadDateAMJ(RFDemandeDev19.FIELDNAME_DATE_RECEPTION_PREAVIS);
        montantAcceptation = statement.dbReadNumeric(RFDemandeDev19.FIELDNAME_MONTANT_ACCEPTATION);
        dateEnvoiAcceptation = statement.dbReadDateAMJ(RFDemandeDev19.FIELDNAME_DATE_ENVOI_ACCEPTATION);

        idDemandeDevis19Ass = statement.dbReadNumeric(RFAssDemandeDev19Ftd15.FIELDNAME_ID_DEMANDE_DEV19);
        idDemandeFtd15Ass = statement.dbReadNumeric(RFAssDemandeDev19Ftd15.FIELDNAME_ID_DEMANDE_FTD15);

        montantAssocieAuDevis = statement.dbReadNumeric(RFAssDemandeDev19Ftd15.FIELDNAME_MONTANT_ASSOCIE_AU_DEVIS);

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

    public String getIdDemandeDevis19Ass() {
        return idDemandeDevis19Ass;
    }

    public String getIdDemandeFtd15Ass() {
        return idDemandeFtd15Ass;
    }

    public String getMontantAcceptation() {
        return montantAcceptation;
    }

    public String getMontantAssocieAuDevis() {
        return montantAssocieAuDevis;
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

    public void setIdDemandeDevis19(String idDemandeDevis19) {
        this.idDemandeDevis19 = idDemandeDevis19;
    }

    public void setIdDemandeDevis19Ass(String idDemandeDevis19Ass) {
        this.idDemandeDevis19Ass = idDemandeDevis19Ass;
    }

    public void setIdDemandeFtd15Ass(String idDemandeFtd15Ass) {
        this.idDemandeFtd15Ass = idDemandeFtd15Ass;
    }

    public void setMontantAcceptation(String montantAcceptation) {
        this.montantAcceptation = montantAcceptation;
    }

    public void setMontantAssocieAuDevis(String montantAssocieAuDevis) {
        this.montantAssocieAuDevis = montantAssocieAuDevis;
    }

}
