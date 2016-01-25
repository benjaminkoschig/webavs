package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author SCO
 * @since 6 sept. 2010
 */
public class CEAffilieForAttrPts extends BEntity {

    private static final long serialVersionUID = 7227097171361460957L;
    private String dateDebutAffiliation = "";
    private String dateDebutPeriodeControle = "";
    private String dateEffective;
    private String dateFinAffiliation = "";
    private String dateFinPeriodeControle = "";
    private String datePrevue;
    private String idAffiliation = "";
    private String idControle;
    private String idTiers = "";
    private String nom = "";
    private String numAffilie = "";
    private String typeAffiliation = "";
    private String numAffilieExterne = "";
    private String codeNOGA = "";
    private String brancheEconomique = "";
    private String codeSuva = "";
    private String libelleSuva;

    @Override
    protected boolean _allowAdd() {
        return false;
    }

    @Override
    protected boolean _allowDelete() {
        return false;
    }

    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(final BStatement statement) throws Exception {
        idAffiliation = statement.dbReadNumeric(CEAffilieManager.F_IDAFFILIATION);
        numAffilie = statement.dbReadString(CEAffilieManager.F_AFF_NUMAFFILIE);
        idTiers = statement.dbReadNumeric(CEAffilieManager.F_IDTIER);
        nom = statement.dbReadString(CEAffilieManager.F_TIER_NOM) + " "
                + statement.dbReadString(CEAffilieManager.F_TIER_COMPLEMENTNOM);
        typeAffiliation = statement.dbReadNumeric(CEAffilieManager.F_AFF_TYPE);
        dateDebutAffiliation = statement.dbReadDateAMJ(CEAffilieManager.F_AFF_DDEB);
        dateFinAffiliation = statement.dbReadDateAMJ(CEAffilieManager.F_AFF_DFIN);
        dateDebutPeriodeControle = statement.dbReadDateAMJ("MDDCDE");
        dateFinPeriodeControle = statement.dbReadDateAMJ("MDDCFI");
        datePrevue = statement.dbReadDateAMJ("MDDPRE");
        dateEffective = statement.dbReadDateAMJ("MDDEFF");
        idControle = statement.dbReadNumeric("MDICON");
        numAffilieExterne = statement.dbReadString("MDLNAF");
        codeNOGA = statement.dbReadNumeric("MATCDN");
        brancheEconomique = statement.dbReadNumeric("MATBRA");
        // Code suva
        codeSuva = statement.dbReadString("CODESUVA");
        libelleSuva = statement.dbReadString("LIBELLESUVA");
    }

    @Override
    protected void _validate(final BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    @Override
    protected void _writePrimaryKey(final BStatement statement) throws Exception {
        statement.writeKey("MAIAFF", this._dbWriteNumeric(statement.getTransaction(), getIdAffiliation(), ""));
    }

    @Override
    protected void _writeProperties(final BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    /**
     * Getter de dateFinAffiliation
     * 
     * @return the dateFinAffiliation
     */
    public String getDateFinAffiliation() {
        if (JadeStringUtil.isBlankOrZero(dateFinAffiliation)) {
            return "*";
        }
        return dateFinAffiliation;
    }

    /**
     * Getter de brancheEconomique
     * 
     * @return the brancheEconomique
     */
    public String getBrancheEconomique() {
        return brancheEconomique;
    }

    /**
     * Setter de brancheEconomique
     * 
     * @param brancheEconomique the brancheEconomique to set
     */
    public void setBrancheEconomique(final String brancheEconomique) {
        this.brancheEconomique = brancheEconomique;
    }

    /**
     * Getter de dateDebutAffiliation
     * 
     * @return the dateDebutAffiliation
     */
    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    /**
     * Setter de dateDebutAffiliation
     * 
     * @param dateDebutAffiliation the dateDebutAffiliation to set
     */
    public void setDateDebutAffiliation(final String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    /**
     * Getter de dateDebutPeriodeControle
     * 
     * @return the dateDebutPeriodeControle
     */
    public String getDateDebutPeriodeControle() {
        return dateDebutPeriodeControle;
    }

    /**
     * Setter de dateDebutPeriodeControle
     * 
     * @param dateDebutPeriodeControle the dateDebutPeriodeControle to set
     */
    public void setDateDebutPeriodeControle(final String dateDebutPeriodeControle) {
        this.dateDebutPeriodeControle = dateDebutPeriodeControle;
    }

    /**
     * Getter de dateEffective
     * 
     * @return the dateEffective
     */
    public String getDateEffective() {
        return dateEffective;
    }

    /**
     * Setter de dateEffective
     * 
     * @param dateEffective the dateEffective to set
     */
    public void setDateEffective(final String dateEffective) {
        this.dateEffective = dateEffective;
    }

    /**
     * Getter de dateFinPeriodeControle
     * 
     * @return the dateFinPeriodeControle
     */
    public String getDateFinPeriodeControle() {
        return dateFinPeriodeControle;
    }

    /**
     * Setter de dateFinPeriodeControle
     * 
     * @param dateFinPeriodeControle the dateFinPeriodeControle to set
     */
    public void setDateFinPeriodeControle(final String dateFinPeriodeControle) {
        this.dateFinPeriodeControle = dateFinPeriodeControle;
    }

    /**
     * Getter de datePrevue
     * 
     * @return the datePrevue
     */
    public String getDatePrevue() {
        return datePrevue;
    }

    /**
     * Setter de datePrevue
     * 
     * @param datePrevue the datePrevue to set
     */
    public void setDatePrevue(final String datePrevue) {
        this.datePrevue = datePrevue;
    }

    /**
     * Getter de idAffiliation
     * 
     * @return the idAffiliation
     */
    public String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * Setter de idAffiliation
     * 
     * @param idAffiliation the idAffiliation to set
     */
    public void setIdAffiliation(final String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    /**
     * Getter de idControle
     * 
     * @return the idControle
     */
    public String getIdControle() {
        return idControle;
    }

    /**
     * Setter de idControle
     * 
     * @param idControle the idControle to set
     */
    public void setIdControle(final String idControle) {
        this.idControle = idControle;
    }

    /**
     * Getter de idTiers
     * 
     * @return the idTiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Setter de idTiers
     * 
     * @param idTiers the idTiers to set
     */
    public void setIdTiers(final String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * Getter de nom
     * 
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter de nom
     * 
     * @param nom the nom to set
     */
    public void setNom(final String nom) {
        this.nom = nom;
    }

    /**
     * Getter de numAffilie
     * 
     * @return the numAffilie
     */
    public String getNumAffilie() {
        return numAffilie;
    }

    /**
     * Setter de numAffilie
     * 
     * @param numAffilie the numAffilie to set
     */
    public void setNumAffilie(final String numAffilie) {
        this.numAffilie = numAffilie;
    }

    /**
     * Getter de typeAffiliation
     * 
     * @return the typeAffiliation
     */
    public String getTypeAffiliation() {
        return typeAffiliation;
    }

    /**
     * Setter de typeAffiliation
     * 
     * @param typeAffiliation the typeAffiliation to set
     */
    public void setTypeAffiliation(final String typeAffiliation) {
        this.typeAffiliation = typeAffiliation;
    }

    /**
     * Getter de numAffilieExterne
     * 
     * @return the numAffilieExterne
     */
    public String getNumAffilieExterne() {
        return numAffilieExterne;
    }

    /**
     * Setter de numAffilieExterne
     * 
     * @param numAffilieExterne the numAffilieExterne to set
     */
    public void setNumAffilieExterne(final String numAffilieExterne) {
        this.numAffilieExterne = numAffilieExterne;
    }

    /**
     * Getter de codeNOGA
     * 
     * @return the codeNOGA
     */
    public String getCodeNOGA() {
        return codeNOGA;
    }

    /**
     * Setter de codeNOGA
     * 
     * @param codeNOGA the codeNOGA to set
     */
    public void setCodeNOGA(final String codeNOGA) {
        this.codeNOGA = codeNOGA;
    }

    /**
     * Setter de dateFinAffiliation
     * 
     * @param dateFinAffiliation the dateFinAffiliation to set
     */
    public void setDateFinAffiliation(final String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    /**
     * Getter de codeSuva
     * 
     * @return the codeSuva
     */
    public String getCodeSuva() {
        return codeSuva;
    }

    /**
     * Setter de codeSuva
     * 
     * @param codeSuva the codeSuva to set
     */
    public void setCodeSuva(final String codeSuva) {
        this.codeSuva = codeSuva;
    }

    /**
     * Getter de libelleSuva
     * 
     * @return the libelleSuva
     */
    public String getLibelleSuva() {
        return libelleSuva;
    }

    /**
     * Setter de libelleSuva
     * 
     * @param libelleSuva the libelleSuva to set
     */
    public void setLibelleSuva(final String libelleSuva) {
        this.libelleSuva = libelleSuva;
    }

}
