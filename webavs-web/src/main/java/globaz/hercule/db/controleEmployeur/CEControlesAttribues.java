package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.hercule.utils.CEUtils;

/**
 * <H1>Description</H1>
 * <p>
 * Un entity contenant toutes les informations necessaires à l'impression des contrôles attribués pour les contrôles
 * employeurs
 * </p>
 * <p>
 * Cet entity est un peu special, il ne peut qu'etre lu. Le seul moyen d'en obtenir une instance correcte est de passer
 * par le manager correspondant.
 * </p>
 */
public class CEControlesAttribues extends BEntity {

    private static final long serialVersionUID = -6776156148427454616L;
    private String dateDebutAffiliation = "";
    private String dateDebutControle = "";
    private String dateEffective = "";
    private String dateFinAffiliation = "";
    private String dateFinControle = "";
    private String datePrecControle = "";
    private String designation1 = "";
    private String designation2 = "";
    private String idAffilie = "";
    private String idTiers = "";
    private String libelleGroupe = "";
    private String montantMasse_1 = "";
    private String montantMasse_2 = "";
    private String montantMasse_3 = "";
    private String montantMasse_4 = "";
    private String montantMasse_5 = "";
    private String nbInscCI = "";
    private String numAffilie = "";
    private String tempsJour = "";
    private String typeControle = "";
    private String visaReviseur = "";
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
        idAffilie = statement.dbReadNumeric("MAIAFF");
        idTiers = statement.dbReadNumeric("HTITIE");
        numAffilie = statement.dbReadString("MALNAF");
        dateDebutControle = statement.dbReadDateAMJ("MDDCDE");
        dateFinControle = statement.dbReadDateAMJ("MDDCFI");
        datePrecControle = statement.dbReadDateAMJ("MDDPRC");
        montantMasse_1 = statement.dbReadNumeric("MASSE1");
        montantMasse_2 = statement.dbReadNumeric("MASSE2");
        montantMasse_3 = statement.dbReadNumeric("MASSE3");
        montantMasse_4 = statement.dbReadNumeric("MASSE4");
        montantMasse_5 = statement.dbReadNumeric("MASSE5");
        nbInscCI = statement.dbReadNumeric("NBCI");
        tempsJour = statement.dbReadNumeric("MDNTJO");
        visaReviseur = statement.dbReadString("milvis");
        typeControle = statement.dbReadNumeric("MDTGEN");
        designation1 = statement.dbReadString("HTLDE1");
        designation2 = statement.dbReadString("HTLDE2");
        dateDebutAffiliation = statement.dbReadDateAMJ("MADDEB");
        dateFinAffiliation = statement.dbReadDateAMJ("MADFIN");
        dateEffective = statement.dbReadDateAMJ("MDDEFF");
        libelleGroupe = statement.dbReadString("CELGRP");
        codeSuva = statement.dbReadString("CODESUVA");
        libelleSuva = statement.dbReadString("LIBELLESUVA");
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

    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public String getDateDebutControle() {
        return dateDebutControle;
    }

    public String getDateEffective() {
        return dateEffective;
    }

    public String getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    public String getDateFinControle() {
        return dateFinControle;
    }

    public String getDatePrecControle() {
        return datePrecControle;
    }

    public String getDesignation1() {
        return designation1;
    }

    public String getDesignation2() {
        return designation2;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getLibelleGroupe() {
        return libelleGroupe;
    }

    public String getMontantMasse_1() {
        return montantMasse_1;
    }

    public String getMontantMasse_2() {
        return montantMasse_2;
    }

    public String getMontantMasse_3() {
        return montantMasse_3;
    }

    public String getMontantMasse_4() {
        return montantMasse_4;
    }

    public String getMontantMasse_5() {
        return montantMasse_5;
    }

    public String getNbInscCI() {
        return nbInscCI;
    }

    public String getNom() {
        return CEUtils.formatNomTiers(getDesignation1(), getDesignation2());
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getTempsJour() {
        return tempsJour;
    }

    public String getTypeControle() {
        return typeControle;
    }

    public String getVisaReviseur() {
        return visaReviseur;
    }

    public void setDateDebutAffiliation(final String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateDebutControle(final String string) {
        dateDebutControle = string;
    }

    public void setDateEffective(final String dateEffective) {
        this.dateEffective = dateEffective;
    }

    public void setDateFinAffiliation(final String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setDateFinControle(final String string) {
        dateFinControle = string;
    }

    public void setDatePrecControle(final String string) {
        datePrecControle = string;
    }

    public void setDesignation1(final String designation1) {
        this.designation1 = designation1;
    }

    public void setDesignation2(final String designation2) {
        this.designation2 = designation2;
    }

    public void setIdAffilie(final String string) {
        idAffilie = string;
    }

    public void setIdTiers(final String string) {
        idTiers = string;
    }

    public void setLibelleGroupe(final String libelleGroupe) {
        this.libelleGroupe = libelleGroupe;
    }

    public void setMontantMasse_1(final String string) {
        montantMasse_1 = string;
    }

    public void setMontantMasse_2(final String string) {
        montantMasse_2 = string;
    }

    public void setMontantMasse_3(final String string) {
        montantMasse_3 = string;
    }

    public void setMontantMasse_4(final String string) {
        montantMasse_4 = string;
    }

    public void setMontantMasse_5(final String string) {
        montantMasse_5 = string;
    }

    public void setNbInscCI(final String string) {
        nbInscCI = string;
    }

    public void setNumAffilie(final String string) {
        numAffilie = string;
    }

    public void setTempsJour(final String string) {
        tempsJour = string;
    }

    public void setTypeControle(final String string) {
        typeControle = string;
    }

    public void setVisaReviseur(final String string) {
        visaReviseur = string;
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
