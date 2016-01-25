package globaz.cygnus.db.paiement;

import globaz.corvus.db.lots.RELot;
import globaz.globall.db.BStatement;

/**
 * @author fha
 */
public class RFPrestationJointLot extends RFPrestation {

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
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);

        /************* Faire aussi la jointure sur le gestionnaire *******/
        // jointure entre la table des prestations et la table des lots
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RELot.TABLE_NAME_LOT);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RELot.TABLE_NAME_LOT);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RELot.FIELDNAME_ID_LOT);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestation.TABLE_NAME_PREST);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestation.FIELDNAME_ID_LOT);

        return fromClauseBuffer.toString();
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String csRole = "";
    private transient String fromClause = null;
    private String idDomaineApplication = "";
    private String idExterne = "";
    private String idLot = "";
    private String idPrestation = "";
    private String idRole = "";
    private String idTiers = "";
    private String idTiersAdressePaiement = "";
    private String montant = "";
    private String numeroFacture = "";

    private String prenom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String typeVersement = "";

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFPrestationJointLot.createFromClause(_getCollection()));
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

        idLot = statement.dbReadNumeric(RELot.FIELDNAME_ID_LOT);
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
