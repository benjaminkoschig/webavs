package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * <p>
 * Un entity contenant toutes les informations necessaires à l'impression des contrôles à effectuer pour les contrôles
 * employeurs
 * </p>
 * <p>
 * Cet entity est un peu special, il ne peut qu'etre lu. Le seul moyen d'en obtenir une instance correcte est de passer
 * par le manager correspondant.
 * </p>
 * 
 * @author hpe
 * @since Créé le 14 févr. 07
 */

public class CEControlesAEffectuer extends BEntity {

    private static final long serialVersionUID = 4759455063051383610L;
    private String brancheEconomique = "";
    private String codeNoga = "";
    private String dateDebutAffiliation = "";
    private String dateDebutParticularite = "";
    private String dateFinAffiliation = "";
    private String dateFinParticularite = "";
    private String designation1 = "";
    private String designation2 = "";
    private String idAffiliation = "";
    private String idCouverture = "";
    private String idTiers = "";
    private String masse1 = "";
    private String masse2 = "";
    private String masse3 = "";
    private String masse4 = "";
    private String masse5 = "";
    private String nbCI1 = "";
    private String nbCI2 = "";
    private String nbCI3 = "";
    private String nbCI4 = "";
    private String nbCI5 = "";
    private String nomGroupe = "";
    private String numAffilie = "";
    private String idControle = "";
    private String codeSuva = "";
    private String libelleSuva = "";
    private String numeroIDE;

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
        numAffilie = statement.dbReadString("MALNAF");
        idAffiliation = statement.dbReadNumeric("MAIAFF");
        idTiers = statement.dbReadNumeric("HTITIE");
        designation1 = statement.dbReadString("HTLDE1");
        designation2 = statement.dbReadString("HTLDE2");
        nomGroupe = statement.dbReadString("CELGRP");
        dateDebutAffiliation = statement.dbReadDateAMJ("MADDEB");
        dateFinAffiliation = statement.dbReadDateAMJ("MADFIN");
        brancheEconomique = statement.dbReadString("MATBRA");
        codeNoga = statement.dbReadString("MATCDN");
        masse1 = statement.dbReadString("MASSE1");
        masse2 = statement.dbReadString("MASSE2");
        masse3 = statement.dbReadString("MASSE3");
        masse4 = statement.dbReadString("MASSE4");
        masse5 = statement.dbReadString("MASSE5");
        nbCI1 = statement.dbReadNumeric("NBCI1");
        nbCI2 = statement.dbReadNumeric("NBCI2");
        nbCI3 = statement.dbReadNumeric("NBCI3");
        nbCI4 = statement.dbReadNumeric("NBCI4");
        nbCI5 = statement.dbReadNumeric("NBCI5");
        dateDebutParticularite = statement.dbReadDateAMJ("SANSPERSDEB");
        dateFinParticularite = statement.dbReadDateAMJ("SANSPERSFIN");
        idCouverture = statement.dbReadNumeric("CEICOU");
        idControle = statement.dbReadNumeric("MDICON");
        codeSuva = statement.dbReadString("CODESUVA");
        libelleSuva = statement.dbReadString("LIBELLESUVA");
        numeroIDE = statement.dbReadString("MALFED");
    }

    @Override
    protected void _validate(final BStatement statement) throws Exception {
        _addError(statement.getTransaction(), "interdit d'ajouter");
    }

    @Override
    protected void _writePrimaryKey(final BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    @Override
    protected void _writeProperties(final BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    public String getBrancheEconomique() {
        return brancheEconomique;
    }

    public String getCodeNoga() {
        return codeNoga;
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

    public String getDateFinParticularite() {
        return dateFinParticularite;
    }

    public String getDesignation1() {
        return designation1;
    }

    public String getDesignation2() {
        return designation2;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdCouverture() {
        return idCouverture;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMasse1() {
        return masse1;
    }

    public String getMasse2() {
        return masse2;
    }

    public String getMasse3() {
        return masse3;
    }

    public String getMasse4() {
        return masse4;
    }

    public String getMasse5() {
        return masse5;
    }

    public String getNbCI1() {
        return nbCI1;
    }

    public String getNbCI2() {
        return nbCI2;
    }

    public String getNbCI3() {
        return nbCI3;
    }

    public String getNbCI4() {
        return nbCI4;
    }

    public String getNbCI5() {
        return nbCI5;
    }

    public String getNom() {
        String d1 = designation1;
        String d2 = designation2;
        if (d1 == null) {
            d1 = "";
        } else {
            d1 = d1.trim();
        }

        if (d2 == null) {
            d2 = "";
        } else {
            d2 = d2.trim();
        }

        String tmp = d1;
        if (!JadeStringUtil.isBlank(d2)) {

            if (!JadeStringUtil.isBlank(d1)) {
                tmp += " ";
            }
            tmp += d2;
        }

        return tmp;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public void setBrancheEconomique(final String brancheEconomique) {
        this.brancheEconomique = brancheEconomique;
    }

    public void setCodeNoga(final String codeNoga) {
        this.codeNoga = codeNoga;
    }

    public void setDateDebutAffiliation(final String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateDebutParticularite(final String dateDebutParticularite) {
        this.dateDebutParticularite = dateDebutParticularite;
    }

    public void setDateFinAffiliation(final String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setDateFinParticularite(final String dateFinParticularite) {
        this.dateFinParticularite = dateFinParticularite;
    }

    public void setDesignation1(final String designation1) {
        this.designation1 = designation1;
    }

    public void setDesignation2(final String designation2) {
        this.designation2 = designation2;
    }

    public void setIdAffiliation(final String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdCouverture(final String idCouverture) {
        this.idCouverture = idCouverture;
    }

    public void setIdTiers(final String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMasse1(final String masse1) {
        this.masse1 = masse1;
    }

    public void setMasse2(final String masse2) {
        this.masse2 = masse2;
    }

    public void setMasse3(final String masse3) {
        this.masse3 = masse3;
    }

    public void setMasse4(final String masse4) {
        this.masse4 = masse4;
    }

    public void setMasse5(final String masse5) {
        this.masse5 = masse5;
    }

    public void setNbCI1(final String nbCI1) {
        this.nbCI1 = nbCI1;
    }

    public void setNbCI2(final String nbCI2) {
        this.nbCI2 = nbCI2;
    }

    public void setNbCI3(final String nbCI3) {
        this.nbCI3 = nbCI3;
    }

    public void setNbCI4(final String nbCI4) {
        this.nbCI4 = nbCI4;
    }

    public void setNbCI5(final String nbCI5) {
        this.nbCI5 = nbCI5;
    }

    public void setNomGroupe(final String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    public void setNumAffilie(final String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public String getIdControle() {
        return idControle;
    }

    public void setIdControle(final String idControle) {
        this.idControle = idControle;
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

    public String getNumeroIDE() {
        return numeroIDE;
    }
}
