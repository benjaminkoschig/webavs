package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * 
 * @author PTA
 * 
 */

public class RecapitulatifEntrepriseImpressionComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * activite de l'allocataire
     */
    private String activiteAllocataire = null;

    /**
     * agence communale
     */
    private String agenceCommunale = null;
    /**
     * identifiant de l'affilié
     */
    private String idAffilie = null;

    /**
     * identifiant du dossier
     */

    private String idDossier = null;

    /**
     * identifaint de l'entête
     */

    private String idEntete = null;

    /**
     * désingation:Agence Communale
     */
    private String intituleAgenceComm = null;

    /**
     * montant du détail de la recap
     */
    private String montant = null;

    /**
     * nombre d'enfants
     */
    private String nbrEnfant = null;

    /**
     * nombre d'unité
     */
    private String nbreUnite = null;

    /**
     * nom de l'allocataire
     */
    private String nomAllocataire = null;

    /**
     * numéro nss de l'allocataire
     */
    private String numNSS = null;

    /**
     * numéro du salarié externe
     */

    private String numSalarieExterne = null;

    /**
     * fin période pour l'entete de prestation
     */
    private String periodeAEntete = null;

    /**
     * début période pour l'entete de prestation
     */
    private String periodeDeEntete = null;
    /**
     * prenom de l'allocataire
     */
    private String prenomAllocataire = null;
    /**
     * RootModel
     */
    private RecapitulatifEntrepriseModel recapEntrepriseModel = null;
    /**
     * type de dossier
     */
    private String statutDossier = null;
    /**
     * type d'unité (heure, jour mois)
     */
    private String typeUnite = null;

    /**
     * Constructeur
     */
    public RecapitulatifEntrepriseImpressionComplexModel() {
        super();
        recapEntrepriseModel = new RecapitulatifEntrepriseModel();

    }

    /**
     * @return the activiteAllocataire
     */
    public String getActiviteAllocataire() {
        return activiteAllocataire;
    }

    /**
     * @return the agenceCommunale
     */
    public String getAgenceCommunale() {
        return agenceCommunale;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {

        return null;
    }

    /**
     * @return the idAffilie
     */
    public String getIdAffilie() {
        return idAffilie;
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    public String getIdEntete() {
        return idEntete;
    }

    /**
     * @return the intituleAgenceComm
     */
    public String getIntituleAgenceComm() {
        return intituleAgenceComm;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return the nbrEnfant
     */
    public String getNbrEnfant() {
        return nbrEnfant;
    }

    /**
     * @return the nbreUnite
     */
    public String getNbreUnite() {
        return nbreUnite;
    }

    /**
     * @return the nomAllocataire
     */
    public String getNomAllocataire() {
        return nomAllocataire;
    }

    /**
     * @return the nSS
     */
    public String getNumNSS() {
        return numNSS;
    }

    /**
     * @return the numSalarieExterne
     */
    public String getNumSalarieExterne() {
        return numSalarieExterne;
    }

    /**
     * @return the periodeAEntete
     */
    public String getPeriodeAEntete() {
        return periodeAEntete;
    }

    /**
     * @return the periodeDeEntete
     */
    public String getPeriodeDeEntete() {
        return periodeDeEntete;
    }

    /**
     * @return the prenomAllocataire
     */
    public String getPrenomAllocataire() {
        return prenomAllocataire;
    }

    /**
     * @return the recapEntrepriseModel
     */
    public RecapitulatifEntrepriseModel getRecapEntrepriseModel() {
        return recapEntrepriseModel;
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

    public String getStatutDossier() {
        return statutDossier;
    }

    /**
     * @return the typeUnite
     */
    public String getTypeUnite() {
        return typeUnite;
    }

    /**
     * @param activiteAllocataire
     *            the activiteAllocataire to set
     */
    public void setActiviteAllocataire(String activiteAllocataire) {
        this.activiteAllocataire = activiteAllocataire;
    }

    /**
     * @param agenceCommunale
     *            the agenceCommunale to set
     */
    public void setAgenceCommunale(String agenceCommunale) {
        this.agenceCommunale = agenceCommunale;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */

    @Override
    public void setId(String id) {
        // DO NOTHING

    }

    /**
     * @param idAffilie
     *            the idAffilie to set
     */
    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdEntete(String idEntete) {
        this.idEntete = idEntete;
    }

    /**
     * @param intituleAgenceComm
     *            the intituleAgenceComm to set
     */
    public void setIntituleAgenceComm(String intituleAgenceComm) {
        this.intituleAgenceComm = intituleAgenceComm;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * @param nbrEnfant
     *            the nbrEnfant to set
     */
    public void setNbrEnfant(String nbrEnfant) {
        this.nbrEnfant = nbrEnfant;
    }

    /**
     * @param nbreUnite
     *            the nbreUnite to set
     */
    public void setNbreUnite(String nbreUnite) {
        this.nbreUnite = nbreUnite;
    }

    /**
     * @param nomAllocataire
     *            the nomAllocataire to set
     */
    public void setNomAllocataire(String nomAllocataire) {
        this.nomAllocataire = nomAllocataire;
    }

    /**
     * @param numNss
     *            the nSS to set
     */
    public void setNumNSS(String numNss) {
        numNSS = numNss;
    }

    /**
     * @param numSalarieExterne
     *            the numSalarieExterne to set
     */
    public void setNumSalarieExterne(String numSalarieExterne) {
        this.numSalarieExterne = numSalarieExterne;
    }

    /**
     * @param periodeAEntete
     *            the periodeAEntete to set
     */
    public void setPeriodeAEntete(String periodeAEntete) {
        this.periodeAEntete = periodeAEntete;
    }

    /**
     * @param periodeDeEntete
     *            the periodeDeEntete to set
     */
    public void setPeriodeDeEntete(String periodeDeEntete) {
        this.periodeDeEntete = periodeDeEntete;
    }

    /**
     * @param prenomAllocataire
     *            the prenomAllocataire to set
     */
    public void setPrenomAllocataire(String prenomAllocataire) {
        this.prenomAllocataire = prenomAllocataire;
    }

    /**
     * @param recapEntrepriseModel
     *            the recapEntrepriseModel to set
     */
    public void setRecapEntrepriseModel(RecapitulatifEntrepriseModel recapEntrepriseModel) {
        this.recapEntrepriseModel = recapEntrepriseModel;
    }

    @Override
    public void setSpy(String spy) {
        // DO NOTHING

    }

    public void setStatutDossier(String statutDossier) {
        this.statutDossier = statutDossier;
    }

    /**
     * @param typeUnite
     *            the typeUnite to set
     */
    public void setTypeUnite(String typeUnite) {
        this.typeUnite = typeUnite;
    }

}
