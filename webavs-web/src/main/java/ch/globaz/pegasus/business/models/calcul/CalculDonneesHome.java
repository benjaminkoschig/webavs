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
    private String prixJournalier = null;
    private Boolean isDeplafonner = null;
    private String montantFraisLongueDuree = null;
    private Boolean isVersementDirect = null;
    private String idAdressePaiement = null;
    private String idTiersHome = null;
    private String csRoleFamille = null;
    private String idDemande = null;
    private String idTiersRegroupement = null;
    //LOCAL
    private boolean doitDiviserMontantPCMensuel;

    public String getCsCategorieArgentPoche() {
        return csCategorieArgentPoche;
    }

    public void setCsCategorieArgentPoche(String csCategorieArgentPoche) {
        this.csCategorieArgentPoche = csCategorieArgentPoche;
    }

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

    public Boolean getIsDeplafonner() {
        return isDeplafonner;
    }

    public void setIsDeplafonner(Boolean isDeplafonner) {
        this.isDeplafonner = isDeplafonner;
    }

    public String getMontantFraisLongueDuree() {
        return montantFraisLongueDuree;
    }

    public void setMontantFraisLongueDuree(String montantFraisLongueDuree) {
        this.montantFraisLongueDuree = montantFraisLongueDuree;
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

    public Boolean getIsVersementDirect() {
        return isVersementDirect;
    }

    public void setIsVersementDirect(Boolean versementDirect) {
        isVersementDirect = versementDirect;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public String getCsRoleFamille() {
        return csRoleFamille;
    }

    public void setCsRoleFamille(String csRoleFamille) {
        this.csRoleFamille = csRoleFamille;
    }

    public String getIdTiersHome() {
        return idTiersHome;
    }

    public void setIdTiersHome(String idTiersHome) {
        this.idTiersHome = idTiersHome;
    }
    public String getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }
    public String getIdTiersRegroupement() {
        return idTiersRegroupement;
    }

    public void setIdTiersRegroupement(String idTiersRegroupement) {
        this.idTiersRegroupement = idTiersRegroupement;
    }

    public boolean isDoitDiviserMontantPCMensuel() {
        return doitDiviserMontantPCMensuel;
    }

    public void setDoitDiviserMontantPCMensuel(boolean doitDiviserMontantPCMensuel) {
        this.doitDiviserMontantPCMensuel = doitDiviserMontantPCMensuel;
    }
}
