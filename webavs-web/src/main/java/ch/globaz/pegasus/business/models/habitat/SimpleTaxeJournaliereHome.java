package ch.globaz.pegasus.business.models.habitat;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DMA
 * @date 28 juil. 2010
 */
public class SimpleTaxeJournaliereHome extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csDestinationSortie = null;
    private String dateEcheance = null;
    private String dateEntreeHome = null;
    private String prixJournalier = null;
    private String idAssureurMaladie = null;
    private String idDonneeFinanciereHeader = null;
    private String idHome = null;
    private String idTaxeJournaliereHome = null;
    private String idTypeChambre = null;
    private Boolean isParticipationLCA = Boolean.FALSE;
    private String montantJournalierLCA = null;
    private String primeAPayer = null;
    private Boolean isDeplafonner = null;
    private String montantFraisLongueDuree = null;
    private Boolean isVersementDirect = Boolean.FALSE;
    private Boolean isCopie = Boolean.FALSE;

    private String idAdressePaiement = null;

    public Boolean getIsDeplafonner() {
        return isDeplafonner;
    }

    public void setIsDeplafonner(Boolean isDeplafonner) {
        this.isDeplafonner = isDeplafonner;
    }

    public String getCsDestinationSortie() {
        return csDestinationSortie;
    }

    /**
     * @return the dateEcheance
     */
    public String getDateEcheance() {
        return dateEcheance;
    }

    public String getDateEntreeHome() {
        return dateEntreeHome;
    }

    @Override
    public String getId() {
        return idTaxeJournaliereHome;
    }

    /**
     * @return the idAssureurMaladie
     */
    public String getIdAssureurMaladie() {
        return idAssureurMaladie;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the idHome
     */
    public String getIdHome() {
        return idHome;
    }

    /**
     * @return the idTaxeJournaliereHome
     */
    public String getIdTaxeJournaliereHome() {
        return idTaxeJournaliereHome;
    }

    /**
     * @return the csTypeChambre
     */
    public String getIdTypeChambre() {
        return idTypeChambre;
    }

    /**
     * @return the isParticipationLCA
     */
    public Boolean getIsParticipationLCA() {
        return isParticipationLCA;
    }

    /**
     * @return the montantJournalierLCA
     */
    public String getMontantJournalierLCA() {
        return montantJournalierLCA;
    }

    /**
     * @return the primeAPayer
     */
    public String getPrimeAPayer() {
        return primeAPayer;
    }

    public void setCsDestinationSortie(String csDestinationSortie) {
        this.csDestinationSortie = csDestinationSortie;
    }

    /**
     * @param dateEcheance
     *            the dateEcheance to set
     */
    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setDateEntreeHome(String dateEntreeHome) {
        this.dateEntreeHome = dateEntreeHome;
    }

    @Override
    public void setId(String id) {
        idTaxeJournaliereHome = id;
    }

    /**
     * @param idAssureurMaladie
     *            the idAssureurMaladie to set
     */
    public void setIdAssureurMaladie(String idAssureurMaladie) {
        this.idAssureurMaladie = idAssureurMaladie;
    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    /**
     * @param idHome
     *            the idHome to set
     */
    public void setIdHome(String idHome) {
        this.idHome = idHome;
    }

    /**
     * @param idTaxeJournaliereHome
     *            the idTaxeJournaliereHome to set
     */
    public void setIdTaxeJournaliereHome(String idTaxeJouranliereHome) {
        idTaxeJournaliereHome = idTaxeJouranliereHome;
    }

    /**
     * @param csTypeChambre
     *            the csTypeChambre to set
     */
    public void setIdTypeChambre(String csTypeChambre) {
        idTypeChambre = csTypeChambre;
    }

    /**
     * @param isParticipationLCA
     *            the isParticipationLCA to set
     */
    public void setIsParticipationLCA(Boolean isParticipationLCA) {
        this.isParticipationLCA = isParticipationLCA;
    }

    /**
     * @param montantJournalierLCA
     *            the montantJournalierLCA to set
     */
    public void setMontantJournalierLCA(String montantJournalierLCA) {
        this.montantJournalierLCA = montantJournalierLCA;
    }

    /**
     * @param primeAPayer
     *            the primeAPayer to set
     */
    public void setPrimeAPayer(String primeAPayer) {
        this.primeAPayer = primeAPayer;
    }

    public String getMontantFraisLongueDuree() {
        return montantFraisLongueDuree;
    }

    public void setMontantFraisLongueDuree(String montantFraisLongueDuree) {
        this.montantFraisLongueDuree = montantFraisLongueDuree;
    }
    public Boolean getIsVersementDirect() {
        return isVersementDirect;
    }

    public void setIsVersementDirect(Boolean isVersementDirect) {
        this.isVersementDirect = isVersementDirect;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public Boolean getCopie() {
        return isCopie;
    }

    public void setCopie(Boolean copie) {
        isCopie = copie;
    }

    public String getPrixJournalier() {
        return prixJournalier;
    }

    public void setPrixJournalier(String prixJournalier) {
        this.prixJournalier = prixJournalier;
    }
}
