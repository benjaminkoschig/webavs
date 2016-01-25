package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author MMO
 * @since 27 juillet 2010
 */
public class CEEmployeurChangementMasseSalariale extends BEntity {

    private static final long serialVersionUID = 5218919953203331363L;
    private String ancienneMasseSalariale = "";
    private String dateDebutAffiliation = "";
    private String dateFinAffiliation = "";
    private String idTiers = "";
    private String libelleGroupe = "";
    private String masseSalariale = "";
    private String nom = "";
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
        numAffilie = statement.dbReadString("MALNAF");
        idTiers = statement.dbReadNumeric("HTITIE");
        nom = statement.dbReadString("HTLDE1") + " "
                + statement.dbReadString(CEEmployeurChangementMasseSalarialeManager.F_TIER_COMPLEMENTNOM);
        dateDebutAffiliation = statement.dbReadDateAMJ("MADDEB");
        dateFinAffiliation = statement.dbReadDateAMJ("MADFIN");
        masseSalariale = statement.dbReadNumeric("CUMULMASSE", 2);
        ancienneMasseSalariale = statement.dbReadNumeric("ANCIENCUMULMASSE", 2);
        libelleGroupe = statement.dbReadString("CELGRP");
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

    public String getAncienneMasseSalariale() {
        return ancienneMasseSalariale;
    }

    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public String getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getLibelleGroupe() {
        return libelleGroupe;
    }

    public String getMasseSalariale() {
        return masseSalariale;
    }

    public String getNom() {
        return nom;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public void setAncienneMasseSalariale(final String newAncienneMasseSalariale) {
        ancienneMasseSalariale = newAncienneMasseSalariale;
    }

    public void setDateDebutAffiliation(final String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateFinAffiliation(final String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setIdTiers(final String idTiers) {
        this.idTiers = idTiers;
    }

    public void setLibelleGroupe(final String libelleGroupe) {
        this.libelleGroupe = libelleGroupe;
    }

    public void setMasseSalariale(final String newMasseSalariale) {
        masseSalariale = newMasseSalariale;
    }

    public void setNom(final String newNom) {
        nom = newNom;
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
