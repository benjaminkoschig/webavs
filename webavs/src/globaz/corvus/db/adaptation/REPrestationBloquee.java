package globaz.corvus.db.adaptation;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 * <p>
 * Entité dédié à la recherche des prestations bloquées pour la liste de vérification du paiement mensuel.
 * </p>
 * 
 * @author PBA
 * @see REPrestationBloqueeManager
 */
public class REPrestationBloquee extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codePrestation;
    private String csSexeBeneficiaire;
    private String dateDebutDroit;
    private String dateEcheance;
    private String dateNaissanceBeneficiaire;
    private String idPaysBeneficiaire;
    private String idPrestationAccordee;
    private String idTiersBeneficiaire;
    private String montant;
    private String nomBeneficiaire;
    private String numeroAvsBeneficiaire;
    private String prenomBeneficiaire;

    public REPrestationBloquee() {
        super();

        codePrestation = "";
        csSexeBeneficiaire = "";
        dateDebutDroit = "";
        dateEcheance = "";
        dateNaissanceBeneficiaire = "";
        idPaysBeneficiaire = "";
        idPrestationAccordee = "";
        idTiersBeneficiaire = "";
        montant = "";
        nomBeneficiaire = "";
        numeroAvsBeneficiaire = "";
        prenomBeneficiaire = "";
    }

    @Override
    protected String _getFields(BStatement statement) {

        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = _getCollection() + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;

        StringBuilder sql = new StringBuilder();

        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_ECHEANCE)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION)
                .append(",");

        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_1).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_2).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_PAYS).append(",");

        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.CS_SEXE).append(",");
        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.DATE_NAISSANCE).append(",");

        sql.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {

        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = _getCollection() + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;

        StringBuilder sql = new StringBuilder();

        sql.append(tablePrestationAccordee);

        sql.append(" INNER JOIN ");
        sql.append(tableTiers);
        sql.append(" ON ");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append("=");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" INNER JOIN ");
        sql.append(tablePersonne);
        sql.append(" ON ");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);
        sql.append("=");
        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS);

        sql.append(" INNER JOIN ");
        sql.append(tablePersonneAvs);
        sql.append(" ON ");
        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS);
        sql.append("=");
        sql.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        codePrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        csSexeBeneficiaire = statement.dbReadNumeric(ITIPersonneDefTable.CS_SEXE);
        dateDebutDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT));
        dateEcheance = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_ECHEANCE));
        dateNaissanceBeneficiaire = statement.dbReadDateAMJ(ITIPersonneDefTable.DATE_NAISSANCE);
        idPaysBeneficiaire = statement.dbReadNumeric(ITITiersDefTable.ID_PAYS);
        idPrestationAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        idTiersBeneficiaire = statement.dbReadNumeric(ITITiersDefTable.ID_TIERS);
        montant = statement.dbReadString(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
        nomBeneficiaire = statement.dbReadString(ITITiersDefTable.DESIGNATION_1);
        numeroAvsBeneficiaire = statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);
        prenomBeneficiaire = statement.dbReadString(ITITiersDefTable.DESIGNATION_2);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // rien
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), idPrestationAccordee, "idPrestationAccordee"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        throw new UnsupportedOperationException("can't be saved");
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    public String getCsSexeBeneficiaire() {
        return csSexeBeneficiaire;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public String getDateNaissanceBeneficiaire() {
        return dateNaissanceBeneficiaire;
    }

    public String getIdPaysBeneficiaire() {
        return idPaysBeneficiaire;
    }

    public String getIdPrestationAccordee() {
        return idPrestationAccordee;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getMontant() {
        return montant;
    }

    public String getNomBeneficiaire() {
        return nomBeneficiaire;
    }

    public String getNumeroAvsBeneficiaire() {
        return numeroAvsBeneficiaire;
    }

    public String getPrenomBeneficiaire() {
        return prenomBeneficiaire;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setCsSexeBeneficiaire(String csSexeBeneficiaire) {
        this.csSexeBeneficiaire = csSexeBeneficiaire;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setDateNaissanceBeneficiaire(String dateNaissanceBeneficiaire) {
        this.dateNaissanceBeneficiaire = dateNaissanceBeneficiaire;
    }

    public void setIdPaysBeneficiaire(String idPaysBeneficiaire) {
        this.idPaysBeneficiaire = idPaysBeneficiaire;
    }

    public void setIdPrestationAccordee(String idPrestationAccordee) {
        this.idPrestationAccordee = idPrestationAccordee;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setNomBeneficiaire(String nomBeneficiaire) {
        this.nomBeneficiaire = nomBeneficiaire;
    }

    public void setNumeroAvsBeneficiaire(String numeroAvsBeneficiaire) {
        this.numeroAvsBeneficiaire = numeroAvsBeneficiaire;
    }

    public void setPrenomBeneficiaire(String prenomBeneficiaire) {
        this.prenomBeneficiaire = prenomBeneficiaire;
    }
}
