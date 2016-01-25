/**
 * 
 */
package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author ECO
 * 
 */
public class CalculDonneesHome extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csServiceEtatPeriode = null;
    private String csTypeChambre = null;
    private String dateDebutDFH = null;
    private String dateDebutPeriode = null;
    private String dateDebutPrixChambre = null;
    private String dateEntreeHome = null;
    private String dateFinDFH = null;
    private String dateFinPeriode = null;
    private String dateFinPrixChambre = null;
    private String idDDF = null;
    private String idHome = null;
    private String idMembreFamille = null;
    private String idTaxesJournaliere = null;
    private String idTypeChambre = null;
    private String idVersionDroit = null;
    private Boolean isApiFactureParHome = null;
    private Boolean isHorsCanton = null;
    private String numeroIdentification = null;
    private String csCategorieArgentPoche = null;

    public String getCsCategorieArgentPoche() {
        return csCategorieArgentPoche;
    }

    public void setCsCategorieArgentPoche(String csCategorieArgentPoche) {
        this.csCategorieArgentPoche = csCategorieArgentPoche;
    }

    private String prixJournalier = null;

    public String getCsServiceEtatPeriode() {
        return csServiceEtatPeriode;
    }

    public String getCsTypeChambre() {
        return csTypeChambre;
    }

    public String getDateDebutDFH() {
        return dateDebutDFH;
    }

    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public String getDateDebutPrixChambre() {
        return dateDebutPrixChambre;
    }

    public String getDateEntreeHome() {
        return dateEntreeHome;
    }

    public String getDateFinDFH() {
        return dateFinDFH;
    }

    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    public String getDateFinPrixChambre() {
        return dateFinPrixChambre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdDDF() {
        return idDDF;
    }

    public String getIdHome() {
        return idHome;
    }

    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    public String getIdTaxesJournaliere() {
        return idTaxesJournaliere;
    }

    public String getIdTypeChambre() {
        return idTypeChambre;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public Boolean getIsApiFactureParHome() {
        return isApiFactureParHome;
    }

    public Boolean getIsHorsCanton() {
        return isHorsCanton;
    }

    public String getNumeroIdentification() {
        return numeroIdentification;
    }

    public String getPrixJournalier() {
        return prixJournalier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setCsServiceEtatPeriode(String csServiceEtatPeriode) {
        this.csServiceEtatPeriode = csServiceEtatPeriode;
    }

    public void setCsTypeChambre(String csTypeChambre) {
        this.csTypeChambre = csTypeChambre;
    }

    public void setDateDebutDFH(String dateDebutDFH) {
        this.dateDebutDFH = dateDebutDFH;
    }

    public void setDateDebutPeriode(String dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    public void setDateDebutPrixChambre(String dateDebutPrixChambre) {
        this.dateDebutPrixChambre = dateDebutPrixChambre;
    }

    public void setDateEntreeHome(String dateEntreeHome) {
        this.dateEntreeHome = dateEntreeHome;
    }

    public void setDateFinDFH(String dateFinDFH) {
        this.dateFinDFH = dateFinDFH;
    }

    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    public void setDateFinPrixChambre(String dateFinPrixChambre) {
        this.dateFinPrixChambre = dateFinPrixChambre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    public void setIdDDF(String idDDF) {
        this.idDDF = idDDF;
    }

    public void setIdHome(String idHome) {
        this.idHome = idHome;
    }

    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

    public void setIdTaxesJournaliere(String idTaxesJournaliere) {
        this.idTaxesJournaliere = idTaxesJournaliere;
    }

    public void setIdTypeChambre(String idTypeChambre) {
        this.idTypeChambre = idTypeChambre;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setIsApiFactureParHome(Boolean isApiFactureParHome) {
        this.isApiFactureParHome = isApiFactureParHome;
    }

    public void setIsHorsCanton(Boolean isHorsCanton) {
        this.isHorsCanton = isHorsCanton;
    }

    public void setNumeroIdentification(String numeroIdentification) {
        this.numeroIdentification = numeroIdentification;
    }

    public void setPrixJournalier(String prixJournalier) {
        this.prixJournalier = prixJournalier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

}
