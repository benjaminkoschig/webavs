package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.hercule.utils.CEUtils;

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

public class CEControles5PourCent extends BEntity {

    private static final long serialVersionUID = -7003880061966477656L;
    private String dateDebutAffiliation = "";
    private String dateEffective = "";
    private String dateFinAffiliation = "";
    private String datePrevue = "";
    private String designation1 = "";
    private String designation2 = "";
    private String idAffiliation = "";
    private String idTiers = "";
    private String libelleGroupe = "";
    private String numAffilie = "";
    private String typeControle = "";
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
        idAffiliation = statement.dbReadNumeric("MAIAFF");
        idTiers = statement.dbReadNumeric("HTITIE");
        designation1 = statement.dbReadString("HTLDE1");
        designation2 = statement.dbReadString("HTLDE2");
        dateDebutAffiliation = statement.dbReadDateAMJ("MADDEB");
        dateFinAffiliation = statement.dbReadDateAMJ("MADFIN");
        typeControle = statement.dbReadNumeric("MDTGEN");
        datePrevue = statement.dbReadDateAMJ("MDDPRE");
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

    public String getDateEffective() {
        return dateEffective;
    }

    public String getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    public String getDatePrevue() {
        return datePrevue;
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

    public String getIdTiers() {
        return idTiers;
    }

    public String getLibelleGroupe() {
        return libelleGroupe;
    }

    public String getNom() {
        return CEUtils.formatNomTiers(designation1, designation2);
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getTypeControle() {
        return typeControle;
    }

    public void setDateDebutAffiliation(final String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateEffective(final String dateEffective) {
        this.dateEffective = dateEffective;
    }

    public void setDateFinAffiliation(final String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setDatePrevue(final String datePrevue) {
        this.datePrevue = datePrevue;
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

    public void setIdTiers(final String idTiers) {
        this.idTiers = idTiers;
    }

    public void setLibelleGroupe(final String libelleGroupe) {
        this.libelleGroupe = libelleGroupe;
    }

    public void setNumAffilie(final String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setTypeControle(final String typeControle) {
        this.typeControle = typeControle;
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
