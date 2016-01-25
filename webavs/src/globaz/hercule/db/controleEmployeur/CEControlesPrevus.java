package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author SCO
 * @since 12 oct. 2010
 */
public class CEControlesPrevus extends BEntity {

    private static final long serialVersionUID = 2304409169651255151L;
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
    private String montantMasse = "";
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
        return null;
    }

    @Override
    protected void _readProperties(final BStatement statement) throws Exception {
        idAffilie = statement.dbReadNumeric("MAIAFF");
        idTiers = statement.dbReadNumeric("HTITIE");
        numAffilie = statement.dbReadString("MALNAF");
        dateDebutControle = statement.dbReadDateAMJ("MDDCDE");
        dateFinControle = statement.dbReadDateAMJ("MDDCFI");
        datePrecControle = statement.dbReadDateAMJ("MDDEFF");
        montantMasse = statement.dbReadNumeric("MASSE");
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
        // ON NE FAIT RIEN
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

    public String getMontantMasse() {
        return montantMasse;
    }

    public String getNbInscCI() {
        return nbInscCI;
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

    public void setDateDebutControle(final String dateDebutControle) {
        this.dateDebutControle = dateDebutControle;
    }

    public void setDateEffective(final String dateEffective) {
        this.dateEffective = dateEffective;
    }

    public void setDateFinAffiliation(final String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setDateFinControle(final String dateFinControle) {
        this.dateFinControle = dateFinControle;
    }

    public void setDatePrecControle(final String datePrecControle) {
        this.datePrecControle = datePrecControle;
    }

    public void setDesignation1(final String designation1) {
        this.designation1 = designation1;
    }

    public void setDesignation2(final String designation2) {
        this.designation2 = designation2;
    }

    public void setIdAffilie(final String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIdTiers(final String idTiers) {
        this.idTiers = idTiers;
    }

    public void setLibelleGroupe(final String libelleGroupe) {
        this.libelleGroupe = libelleGroupe;
    }

    public void setMontantMasse(final String montantMasse) {
        this.montantMasse = montantMasse;
    }

    public void setNbInscCI(final String nbInscCI) {
        this.nbInscCI = nbInscCI;
    }

    public void setNumAffilie(final String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setTempsJour(final String tempsJour) {
        this.tempsJour = tempsJour;
    }

    public void setTypeControle(final String typeControle) {
        this.typeControle = typeControle;
    }

    public void setVisaReviseur(final String visaReviseur) {
        this.visaReviseur = visaReviseur;
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
