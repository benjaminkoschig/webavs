package globaz.cygnus.db.recapitulation;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.cygnus.db.paiement.RFPrestationAccordee;
import globaz.globall.db.BStatement;
import globaz.prestation.tools.PRDateFormater;

public class RFPrestationAccordeeJointREPrestationAccordeeRecapitulation extends RFPrestationAccordee {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Génération de la clause from pour la requête
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
        fromClauseBuffer.append(RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE);

        // jointure entre la table des prestations accordées RFM et la table des prestations accordées RENTE
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationAccordee.FIELDNAME_ID_RFM_ACCORDEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        return fromClauseBuffer.toString();
    }

    private String dateDebutDroit = null;
    private String dateFinDroit = null;
    private String idTiers = "";
    private String montant = "";
    private String montantPrestation = "";

    private String typePrestationAccordee = "";

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        montant = statement
                .dbReadNumeric(RFPrestationAccordeeJointREPrestationAccordeeRecapitulationManager.ALIAS_SOMME_MONTANT);
        dateFinDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT));
        dateDebutDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT));
        idTiers = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        montantPrestation = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    public String getTypePrestationAccordee() {
        return typePrestationAccordee;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public void setTypePrestationAccordee(String typePrestationAccordee) {
        this.typePrestationAccordee = typePrestationAccordee;
    }

}
