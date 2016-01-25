package globaz.cygnus.db.ordresversements;

import globaz.cygnus.db.paiement.RFPrestation;
import globaz.globall.db.BStatement;

/**
 * @author fha
 */
public class RFPrestationJointOrdreVersement extends RFPrestation {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusque dans les tiers (Nom et AVS)
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);

        /************* Faire aussi la jointure sur le gestionnaire *******/
        // jointure entre la table des prestations et la table ordre versement
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFOrdresVersements.TABLE_NAME_ORVER);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFOrdresVersements.TABLE_NAME_ORVER);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFOrdresVersements.FIELDNAME_ID_PRESTATION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestation.FIELDNAME_ID_PRESTATION);

        return fromClauseBuffer.toString();
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String csRole = "";
    private transient String fromClause = null;
    private String idDomaineApplication = "";
    private String idExterne = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String idOrdreVersement = "";
    private String idPrestation = "";
    private String idRole = "";
    private String idTiers = "";
    private String idTiersAdressePaiement = "";
    private String montant = "";
    private String montantDepassementQD = "";
    private String numeroFacture = "";

    private String prenom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String typeVersement = "";

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFPrestationJointOrdreVersement.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idOrdreVersement = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT);
        idPrestation = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_PRESTATION);
        typeVersement = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_TYPE_VERSEMENT);
        numeroFacture = statement.dbReadString(RFOrdresVersements.FIELDNAME_NUMERO_FACTURE);
        idExterne = statement.dbReadString(RFOrdresVersements.FIELDNAME_ID_EXTERNE);
        csRole = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_CS_ROLE);
        idTiersAdressePaiement = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_TIERS_ADRESSE_PAIEMENT);
        idDomaineApplication = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_DOMAINE_APPLICATION);
        montant = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_MONTANT);
        montantDepassementQD = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_MONTANT_DEPASSEMENT_QD);
        idTiers = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_TIERS);
        idRole = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_ROLE);

    }

    public String getCsRole() {
        return csRole;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getIdDomaineApplication() {
        return idDomaineApplication;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdOrdreVersement() {
        return idOrdreVersement;
    }

    @Override
    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdRole() {
        return idRole;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantDepassementQD() {
        return montantDepassementQD;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTypeVersement() {
        return typeVersement;
    }

    public void setCsRole(String csRole) {
        this.csRole = csRole;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setIdDomaineApplication(String idDomaineApplication) {
        this.idDomaineApplication = idDomaineApplication;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = idOrdreVersement;
    }

    @Override
    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantDepassementQD(String montantDepassementQD) {
        this.montantDepassementQD = montantDepassementQD;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setTypeVersement(String typeVersement) {
        this.typeVersement = typeVersement;
    }

}
