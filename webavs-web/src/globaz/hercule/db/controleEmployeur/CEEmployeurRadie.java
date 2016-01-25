package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author MMO
 * @since 4 aout 2010
 */
public class CEEmployeurRadie extends BEntity {

    private static final long serialVersionUID = 8999682989067454131L;
    private String dateDebutAffiliation = "";
    private String dateFinAffiliation = "";
    private String idAffiliation = "";
    private String idTiers = "";
    private String masseSalariale = "";
    private String motifRadiation = "";
    private String nom = "";
    private String nomGroupe = "";
    private String numAffilie = "";
    private String codeSuva = "";
    private String libelleSuva = "";

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
        return null; // PAS DE TABLES !!!
    }

    @Override
    protected void _readProperties(final BStatement statement) throws Exception {
        idAffiliation = statement.dbReadNumeric(CEEmployeurRadieManager.F_IDAFFILIATION);
        numAffilie = statement.dbReadString(CEEmployeurRadieManager.F_AFF_NUMAFFILIE);
        idTiers = statement.dbReadNumeric(CEEmployeurRadieManager.F_IDTIER);
        nom = statement.dbReadString(CEEmployeurRadieManager.F_TIER_NOM) + " "
                + statement.dbReadString(CEEmployeurRadieManager.F_TIER_COMPLEMENTNOM);
        dateDebutAffiliation = statement.dbReadDateAMJ(CEEmployeurRadieManager.F_AFF_DDEB);
        dateFinAffiliation = statement.dbReadDateAMJ(CEEmployeurRadieManager.F_AFF_DFIN);
        masseSalariale = statement.dbReadNumeric(CEEmployeurRadieManager.F_CACPTR_CUMULMASSE, 2);
        nomGroupe = statement.dbReadString(CEEmployeurRadieManager.F_NOMGROUPE);
        motifRadiation = statement.dbReadNumeric("MATMOT");
        codeSuva = statement.dbReadString("CODESUVA");
        libelleSuva = statement.dbReadString("LIBELLESUVA");
    }

    @Override
    protected void _validate(final BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(final BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    @Override
    protected void _writeProperties(final BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public String getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMasseSalariale() {
        return masseSalariale;
    }

    public String getMotifRadiation() {
        return motifRadiation;
    }

    public String getNom() {
        return nom;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public void setDateDebutAffiliation(final String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateFinAffiliation(final String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setIdAffiliation(final String newIdAffiliation) {
        idAffiliation = newIdAffiliation;
    }

    public void setIdTiers(final String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMasseSalariale(final String newMasseSalariale) {
        masseSalariale = newMasseSalariale;
    }

    public void setMotifRadiation(final String motifRadiation) {
        this.motifRadiation = motifRadiation;
    }

    public void setNom(final String newNom) {
        nom = newNom;
    }

    public void setNomGroupe(final String newNomGroupe) {
        nomGroupe = newNomGroupe;
    }

    public void setNumAffilie(final String numAffilie) {
        this.numAffilie = numAffilie;
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
