package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author MMO
 * @since 2 aout. 2010
 */
public class CEEmployeurMasseSalariale extends BEntity {

    private static final long serialVersionUID = 5725221331319057835L;
    private String dateDebutAffiliation = "";
    private String dateFinAffiliation = "";
    private String idTiers = "";
    private String libelleGroupe = "";
    private String masseSalarialeAnneeMoins1 = "";
    private String masseSalarialeAnneeMoins2 = "";
    private String masseSalarialeAnneeMoins3 = "";
    private String masseSalarialeAnneeMoins4 = "";
    private String masseSalarialeAnneeMoins5 = "";
    private String nbCI1 = "";
    private String nbCI2 = "";
    private String nbCI3 = "";
    private String nbCI4 = "";
    private String nbCI5 = "";
    private String masseSalarialeAF = "";
    private String nom = "";
    private String numAffilie = "";
    private String caisseAVS = "";
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
                + statement.dbReadString(CEEmployeurMasseSalarialeManager.F_TIER_COMPLEMENTNOM);
        dateDebutAffiliation = statement.dbReadDateAMJ("MADDEB");
        dateFinAffiliation = statement.dbReadDateAMJ("MADFIN");
        masseSalarialeAnneeMoins1 = statement.dbReadNumeric("CUMULMASSEAN1", 2);
        masseSalarialeAnneeMoins2 = statement.dbReadNumeric("CUMULMASSEAN2", 2);
        masseSalarialeAnneeMoins3 = statement.dbReadNumeric("CUMULMASSEAN3", 2);
        masseSalarialeAnneeMoins4 = statement.dbReadNumeric("CUMULMASSEAN4", 2);
        masseSalarialeAnneeMoins5 = statement.dbReadNumeric("CUMULMASSEAN5", 2);
        nbCI1 = statement.dbReadNumeric("NBCI1");
        nbCI2 = statement.dbReadNumeric("NBCI2");
        nbCI3 = statement.dbReadNumeric("NBCI3");
        nbCI4 = statement.dbReadNumeric("NBCI4");
        nbCI5 = statement.dbReadNumeric("NBCI5");
        masseSalarialeAF = statement.dbReadNumeric("CUMULMASSEAF", 2);
        libelleGroupe = statement.dbReadString("CELGRP");
        caisseAVS = statement.dbReadString("NUMCAISSE");
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

    /**
     * Getter de dateDebutAffiliation
     * 
     * @return the dateDebutAffiliation
     */
    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    /**
     * Getter de dateFinAffiliation
     * 
     * @return the dateFinAffiliation
     */
    public String getDateFinAffiliation() {
        return dateFinAffiliation;
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
     * Getter de libelleGroupe
     * 
     * @return the libelleGroupe
     */
    public String getLibelleGroupe() {
        return libelleGroupe;
    }

    /**
     * Getter de masseSalarialeAF
     * 
     * @return the masseSalarialeAF
     */
    public String getMasseSalarialeAF() {
        return masseSalarialeAF;
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
     * Getter de numAffilie
     * 
     * @return the numAffilie
     */
    public String getNumAffilie() {
        return numAffilie;
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
     * Setter de dateFinAffiliation
     * 
     * @param dateFinAffiliation the dateFinAffiliation to set
     */
    public void setDateFinAffiliation(final String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
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
     * Setter de libelleGroupe
     * 
     * @param libelleGroupe the libelleGroupe to set
     */
    public void setLibelleGroupe(final String libelleGroupe) {
        this.libelleGroupe = libelleGroupe;
    }

    /**
     * Setter de masseSalarialeAF
     * 
     * @param masseSalarialeAF the masseSalarialeAF to set
     */
    public void setMasseSalarialeAF(final String newMasseSalarialeAF) {
        masseSalarialeAF = newMasseSalarialeAF;
    }

    /**
     * Setter de newNom
     * 
     * @param newNom the newNom to set
     */
    public void setNom(final String newNom) {
        nom = newNom;
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
     * Getter de caisseAVS
     * 
     * @return the caisseAVS
     */
    public String getCaisseAVS() {
        return caisseAVS;
    }

    /**
     * Setter de caisseAVS
     * 
     * @param caisseAVS the caisseAVS to set
     */
    public void setCaisseAVS(final String caisseAVS) {
        this.caisseAVS = caisseAVS;
    }

    /**
     * Getter de masseSalarialeAnneeMoins1
     * 
     * @return the masseSalarialeAnneeMoins1
     */
    public String getMasseSalarialeAnneeMoins1() {
        return masseSalarialeAnneeMoins1;
    }

    /**
     * Setter de masseSalarialeAnneeMoins1
     * 
     * @param masseSalarialeAnneeMoins1 the masseSalarialeAnneeMoins1 to set
     */
    public void setMasseSalarialeAnneeMoins1(final String masseSalarialeAnneeMoins1) {
        this.masseSalarialeAnneeMoins1 = masseSalarialeAnneeMoins1;
    }

    /**
     * Getter de masseSalarialeAnneeMoins2
     * 
     * @return the masseSalarialeAnneeMoins2
     */
    public String getMasseSalarialeAnneeMoins2() {
        return masseSalarialeAnneeMoins2;
    }

    /**
     * Setter de masseSalarialeAnneeMoins2
     * 
     * @param masseSalarialeAnneeMoins2 the masseSalarialeAnneeMoins2 to set
     */
    public void setMasseSalarialeAnneeMoins2(final String masseSalarialeAnneeMoins2) {
        this.masseSalarialeAnneeMoins2 = masseSalarialeAnneeMoins2;
    }

    /**
     * Getter de masseSalarialeAnneeMoins3
     * 
     * @return the masseSalarialeAnneeMoins3
     */
    public String getMasseSalarialeAnneeMoins3() {
        return masseSalarialeAnneeMoins3;
    }

    /**
     * Setter de masseSalarialeAnneeMoins3
     * 
     * @param masseSalarialeAnneeMoins3 the masseSalarialeAnneeMoins3 to set
     */
    public void setMasseSalarialeAnneeMoins3(final String masseSalarialeAnneeMoins3) {
        this.masseSalarialeAnneeMoins3 = masseSalarialeAnneeMoins3;
    }

    /**
     * Getter de masseSalarialeAnneeMoins4
     * 
     * @return the masseSalarialeAnneeMoins4
     */
    public String getMasseSalarialeAnneeMoins4() {
        return masseSalarialeAnneeMoins4;
    }

    /**
     * Setter de masseSalarialeAnneeMoins4
     * 
     * @param masseSalarialeAnneeMoins4 the masseSalarialeAnneeMoins4 to set
     */
    public void setMasseSalarialeAnneeMoins4(final String masseSalarialeAnneeMoins4) {
        this.masseSalarialeAnneeMoins4 = masseSalarialeAnneeMoins4;
    }

    /**
     * Getter de masseSalarialeAnneeMoins5
     * 
     * @return the masseSalarialeAnneeMoins5
     */
    public String getMasseSalarialeAnneeMoins5() {
        return masseSalarialeAnneeMoins5;
    }

    /**
     * Setter de masseSalarialeAnneeMoins5
     * 
     * @param masseSalarialeAnneeMoins5 the masseSalarialeAnneeMoins5 to set
     */
    public void setMasseSalarialeAnneeMoins5(final String masseSalarialeAnneeMoins5) {
        this.masseSalarialeAnneeMoins5 = masseSalarialeAnneeMoins5;
    }

    /**
     * Getter de nbCI1
     * 
     * @return the nbCI1
     */
    public String getNbCI1() {
        return nbCI1;
    }

    /**
     * Setter de nbCI1
     * 
     * @param nbCI1 the nbCI1 to set
     */
    public void setNbCI1(final String nbCI1) {
        this.nbCI1 = nbCI1;
    }

    /**
     * Getter de nbCI2
     * 
     * @return the nbCI2
     */
    public String getNbCI2() {
        return nbCI2;
    }

    /**
     * Setter de nbCI2
     * 
     * @param nbCI2 the nbCI2 to set
     */
    public void setNbCI2(final String nbCI2) {
        this.nbCI2 = nbCI2;
    }

    /**
     * Getter de nbCI3
     * 
     * @return the nbCI3
     */
    public String getNbCI3() {
        return nbCI3;
    }

    /**
     * Setter de nbCI3
     * 
     * @param nbCI3 the nbCI3 to set
     */
    public void setNbCI3(final String nbCI3) {
        this.nbCI3 = nbCI3;
    }

    /**
     * Getter de nbCI4
     * 
     * @return the nbCI4
     */
    public String getNbCI4() {
        return nbCI4;
    }

    /**
     * Setter de nbCI4
     * 
     * @param nbCI4 the nbCI4 to set
     */
    public void setNbCI4(final String nbCI4) {
        this.nbCI4 = nbCI4;
    }

    /**
     * Getter de nbCI5
     * 
     * @return the nbCI5
     */
    public String getNbCI5() {
        return nbCI5;
    }

    /**
     * Setter de nbCI5
     * 
     * @param nbCI5 the nbCI5 to set
     */
    public void setNbCI5(final String nbCI5) {
        this.nbCI5 = nbCI5;
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
}
