package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

public class RERenteJoinPersonneAvs extends BEntity implements Comparable<RERenteJoinPersonneAvs> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codePrestation;
    private String csSexeBeneficiaire;
    private String dateDebutDroit;
    private String dateFinDroit;
    private String dateNaissanceBeneficiaire;
    private String fractionRente;
    private String idPaysBeneficiaire;
    private String idPrestationAccordee;
    private String idTiers;
    private String montantPrestation;
    private String nomBeneficiaire;
    private String nomMajBeneficiaire;
    private String nssBeneficiaire;
    private String prenomBeneficiaire;
    private String prenomMajBeneficaire;

    public RERenteJoinPersonneAvs() {
        super();

        codePrestation = "";
        csSexeBeneficiaire = "";
        dateDebutDroit = "";
        dateFinDroit = "";
        dateNaissanceBeneficiaire = "";
        fractionRente = "";
        idPaysBeneficiaire = "";
        idPrestationAccordee = "";
        idTiers = "";
        montantPrestation = "";
        nomBeneficiaire = "";
        nssBeneficiaire = "";
        prenomBeneficiaire = "";
    }

    @Override
    protected String _getTableName() {
        return REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        codePrestation = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        csSexeBeneficiaire = statement.dbReadNumeric(ITIPersonneDefTable.CS_SEXE);
        dateDebutDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadString(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT));
        dateFinDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadString(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT));
        dateNaissanceBeneficiaire = statement.dbReadDateAMJ(ITIPersonneDefTable.DATE_NAISSANCE);
        fractionRente = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_FRACTION_RENTE);
        idPaysBeneficiaire = statement.dbReadNumeric(ITITiersDefTable.ID_PAYS);
        idPrestationAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        idTiers = statement.dbReadNumeric(ITITiersDefTable.ID_TIERS);
        montantPrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
        nomBeneficiaire = statement.dbReadString(ITITiersDefTable.DESIGNATION_1);
        nomMajBeneficiaire = statement.dbReadString(ITITiersDefTable.DESIGNATION_1_MAJ);
        nssBeneficiaire = statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);
        prenomBeneficiaire = statement.dbReadString(ITITiersDefTable.DESIGNATION_2);
        prenomMajBeneficaire = statement.dbReadString(ITITiersDefTable.DESIGNATION_2_MAJ);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), idPrestationAccordee, "idPrestationAccordee"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        throw new UnsupportedOperationException("Can't save this entity in DB (complex entity)");
    }

    @Override
    public int compareTo(RERenteJoinPersonneAvs uneRente) {

        int compareNom = getNomMajBeneficiaire().compareTo(uneRente.getNomMajBeneficiaire());
        if (compareNom != 0) {
            return compareNom;
        }

        int comparePrenom = getPrenomMajBeneficaire().compareTo(uneRente.getPrenomMajBeneficaire());
        if (comparePrenom != 0) {
            return comparePrenom;
        }

        return Integer.valueOf(getCodePrestation()).compareTo(Integer.valueOf(uneRente.getCodePrestation()));
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

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getDateNaissanceBeneficiaire() {
        return dateNaissanceBeneficiaire;
    }

    public String getFractionRente() {
        return fractionRente;
    }

    public String getIdPaysBeneficiaire() {
        return idPaysBeneficiaire;
    }

    public String getIdPrestationAccordee() {
        return idPrestationAccordee;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    public String getNomBeneficiaire() {
        return nomBeneficiaire;
    }

    public String getNomMajBeneficiaire() {
        return nomMajBeneficiaire;
    }

    public String getNssBeneficiaire() {
        return nssBeneficiaire;
    }

    public String getPrenomBeneficiaire() {
        return prenomBeneficiaire;
    }

    public String getPrenomMajBeneficaire() {
        return prenomMajBeneficaire;
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

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setDateNaissanceBeneficiaire(String dateNaissanceBeneficiaire) {
        this.dateNaissanceBeneficiaire = dateNaissanceBeneficiaire;
    }

    public void setFractionRente(String fractionRente) {
        this.fractionRente = fractionRente;
    }

    public void setIdPaysBeneficiaire(String idPaysBeneficiaire) {
        this.idPaysBeneficiaire = idPaysBeneficiaire;
    }

    public void setIdPrestationAccordee(String idPrestationAccordee) {
        this.idPrestationAccordee = idPrestationAccordee;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public void setNomBeneficiaire(String nomBeneficiaire) {
        this.nomBeneficiaire = nomBeneficiaire;
    }

    public void setNomMajBeneficiaire(String nomMajBeneficiaire) {
        this.nomMajBeneficiaire = nomMajBeneficiaire;
    }

    public void setNssBeneficiaire(String nssBeneficiaire) {
        this.nssBeneficiaire = nssBeneficiaire;
    }

    public void setPrenomBeneficiaire(String prenomBeneficiaire) {
        this.prenomBeneficiaire = prenomBeneficiaire;
    }

    public void setPrenomMajBeneficaire(String prenomMajBeneficaire) {
        this.prenomMajBeneficaire = prenomMajBeneficaire;
    }
}
