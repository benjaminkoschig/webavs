package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author SCO
 * @since 30 juillet 2010
 */
public class CEEmployeurSansPersonnel extends BEntity {

    private static final long serialVersionUID = 1303901768679790918L;
    private String dateDebutAffiliation = "";
    private String dateDebutParticularite = "";
    private String dateFinAffiliation = "";
    private String idAffiliation = "";
    private String idTiers = "";
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
        idAffiliation = statement.dbReadNumeric(CEEmployeurSansPersonnelManager.F_IDAFFILIATION);
        numAffilie = statement.dbReadString(CEEmployeurSansPersonnelManager.F_AFF_NUMAFFILIE);
        idTiers = statement.dbReadNumeric(CEEmployeurSansPersonnelManager.F_IDTIER);
        nom = statement.dbReadString(CEEmployeurSansPersonnelManager.F_TIER_NOM) + " "
                + statement.dbReadString(CEEmployeurSansPersonnelManager.F_TIER_COMPLEMENTNOM);
        dateDebutAffiliation = statement.dbReadDateAMJ(CEEmployeurSansPersonnelManager.F_AFF_DDEB);
        dateFinAffiliation = statement.dbReadDateAMJ(CEEmployeurSansPersonnelManager.F_AFF_DFIN);
        dateDebutParticularite = statement.dbReadDateAMJ(CEEmployeurSansPersonnelManager.F_PARTICULARITE_DDEB);
        nomGroupe = statement.dbReadString(CEEmployeurSansPersonnelManager.F_NOMGROUPE);
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

    public String getDateDebutParticularite() {
        return dateDebutParticularite;
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

    public void setDateDebutParticularite(final String newDateDebutParticularite) {
        dateDebutParticularite = newDateDebutParticularite;
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
