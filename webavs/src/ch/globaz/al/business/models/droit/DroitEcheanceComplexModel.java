/**
 * 
 */
package ch.globaz.al.business.models.droit;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.pyxis.business.model.TiersLiaisonComplexModel;

/**
 * Classe modèle des droits arrivant à échéance
 * 
 * @author PTA
 * 
 */
public class DroitEcheanceComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Activite de l'allocataire
     */

    private String activiteAllocataire = null;

    /**
     * Modèle de l'affiliation
     */
    private AffiliationSimpleModel affiliationSimpleModel = null;

    /**
     * date de naissance de l'enfant
     */
    private String dateNaissanceEnfant = null;

    /**
     * RootModel
     */
    private DroitModel droitModel = new DroitModel();

    /**
     * identifiant du tiersAllocataire
     */
    private String idTiersAllocataire = null;

    /**
     * identifiant du tiers beneficiaire défini au niveau du dossier
     */
    private String idTiersBeneficiaire = null;

    /**
     * nom l'allocataire
     */
    private String nomAllocataire = null;

    /**
     * nom de l'enfant
     */
    private String nomEnfant = null;
    /**
     * Numéro de l'affilie
     */
    private String numAffilie = null;

    /**
     * Numéro NSS de l'allocataire
     */
    private String numNss = null;

    /**
     * Numéro nss de l'enfant
     */

    private String numNssEnfant = null;

    /**
     * prénom de l'alllocataire
     */
    private String prenomAllocataire = null;

    /**
     * prénom de l'enfant
     */
    private String prenomEnfant = null;

    /**
     * Modèle complexe de tiersLiaison
     */

    private TiersLiaisonComplexModel tiersLiaisonModel = null;
    /**
     * titre (code système)
     */
    private String titre = null;

    /**
     * Constructeur
     */
    public DroitEcheanceComplexModel() {
        super();
        droitModel = new DroitModel();
        affiliationSimpleModel = new AffiliationSimpleModel();
        tiersLiaisonModel = new TiersLiaisonComplexModel();

    }

    /**
     * @return the activiteAllocataire
     */
    public String getActiviteAllocataire() {
        return activiteAllocataire;
    }

    /**
     * @return the affiliationSimpleModel
     */
    public AffiliationSimpleModel getAffiliationSimpleModel() {
        return affiliationSimpleModel;
    }

    /**
     * @return the dateNaissanceEnfant
     */
    public String getDateNaissanceEnfant() {
        return dateNaissanceEnfant;
    }

    /**
     * @return the droitModel
     */
    public DroitModel getDroitModel() {
        return droitModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {

        return droitModel.getId();
    }

    /**
     * @return the idTiersAllocataire
     */
    public String getIdTiersAllocataire() {
        return idTiersAllocataire;
    }

    /**
     * @return the idTiersBeneficiaire
     */
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    /**
     * @return the nomPrenomAllocataire
     */
    public String getNomAllocataire() {
        return nomAllocataire;
    }

    /**
     * @return the nomPrenomEnfant
     */
    public String getNomEnfant() {
        return nomEnfant;
    }

    /**
     * @return the numAffilie
     */
    public String getNumAffilie() {
        return numAffilie;
    }

    /**
     * @return the numNss
     */
    public String getNumNss() {
        return numNss;
    }

    /**
     * @return the numNssEnfant
     */
    public String getNumNssEnfant() {
        return numNssEnfant;
    }

    /**
     * @return the prenomAllocataire
     */
    public String getPrenomAllocataire() {
        return prenomAllocataire;
    }

    /**
     * @return the prenomEnfant
     */
    public String getPrenomEnfant() {
        return prenomEnfant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {

        return null;
    }

    /**
     * @return the tiersLiaisonModel
     */
    public TiersLiaisonComplexModel getTiersLiaisonComplexModel() {
        return tiersLiaisonModel;
    }

    // /**
    // * @return the dateEcheance
    // */
    // public String getDateEcheance() {
    // return this.dateEcheance;
    // }

    /**
     * @return the titre
     */
    public String getTitre() {
        return titre;
    }

    // /**
    // * @return the idDossier
    // */
    // public String getIdDossier() {
    // return this.idDossier;
    // }

    // /**
    // * @return the motifEcheance
    // */
    // public String getMotifEcheance() {
    // return this.motifEcheance;
    // }

    /**
     * @param activiteAllocataire
     *            the activiteAllocataire to set
     */
    public void setActiviteAllocataire(String activiteAllocataire) {
        this.activiteAllocataire = activiteAllocataire;
    }

    /**
     * @param affiliationSimpleModel
     *            the affiliationSimpleModel to set
     */
    public void setAffiliationSimpleModel(AffiliationSimpleModel affiliationSimpleModel) {
        this.affiliationSimpleModel = affiliationSimpleModel;
    }

    /**
     * @param dateNaissanceEnfant
     *            the dateNaissanceEnfant to set
     */
    public void setDateNaissanceEnfant(String dateNaissanceEnfant) {
        this.dateNaissanceEnfant = dateNaissanceEnfant;
    }

    /**
     * @param droitModel
     *            the droitModel to set
     */
    public void setDroitModel(DroitModel droitModel) {
        this.droitModel = droitModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        droitModel.setId(id);
    }

    // /**
    // * @param dateEcheance
    // * the dateEcheance to set
    // */
    // public void setDateEcheance(String dateEcheance) {
    // this.dateEcheance = dateEcheance;
    // }

    /**
     * @param idTiersAllocataire
     *            the idTiersAllocataire to set
     */
    public void setIdTiersAllocataire(String idTiersAllocataire) {
        this.idTiersAllocataire = idTiersAllocataire;
    }

    /**
     * @param idTiersBeneficiaire
     *            the idTiersBeneficiaire to set
     */
    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    // /**
    // * @param idDossier
    // * the idDossier to set
    // */
    // public void setIdDossier(String idDossier) {
    // this.idDossier = idDossier;
    // }

    // /**
    // * @param motifEcheance
    // * the motifEcheance to set
    // */
    // public void setMotifEcheance(String motifEcheance) {
    // this.motifEcheance = motifEcheance;
    // }

    /**
     * @param nomAllocataire
     *            the nomAllocataire to set
     */
    public void setNomAllocataire(String nomAllocataire) {
        this.nomAllocataire = nomAllocataire;
    }

    /**
     * @param nomEnfant
     *            the nomEnfant to set
     */
    public void setNomEnfant(String nomEnfant) {
        this.nomEnfant = nomEnfant;
    }

    /**
     * @param numAffilie
     *            the numAffilie to set
     */
    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    /**
     * @param numNss
     *            the numNss to set
     */
    public void setNumNss(String numNss) {
        this.numNss = numNss;
    }

    /**
     * @param numNssEnfant
     *            the numNssEnfant to set
     */
    public void setNumNssEnfant(String numNssEnfant) {
        this.numNssEnfant = numNssEnfant;
    }

    /**
     * @param prenomAllocataire
     *            the prenomAllocataire to set
     */
    public void setPrenomAllocataire(String prenomAllocataire) {
        this.prenomAllocataire = prenomAllocataire;
    }

    /**
     * @param prenomEnfant
     *            the prenomEnfant to set
     */
    public void setPrenomEnfant(String prenomEnfant) {
        this.prenomEnfant = prenomEnfant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        // DO NOTHING
    }

    /**
     * @param tiersLiaisonModel
     *            the tiersLiaisonModel to set
     */
    public void setTiersLiaisonComplexModel(TiersLiaisonComplexModel tiersLiaisonModel) {
        this.tiersLiaisonModel = tiersLiaisonModel;
    }

    /**
     * @param titre
     *            the titre to set
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

}
