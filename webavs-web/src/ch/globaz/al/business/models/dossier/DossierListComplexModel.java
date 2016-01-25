package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Mod�le utilisant par la recherche de dossier permet une jointure sur un droit pour la recherche par enfant ainsi
 * qu'une jointure sur l'affiliation pour recherche par nom affili�
 * 
 * @author GMO
 * 
 */
public class DossierListComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * activit�
     */
    private String activiteDossier = null;

    /**
     * �tat du dossier
     */
    private String etatDossier = null;
    /**
     * date de fin de validit�
     */
    private String finValidite = null;
    /**
     * identifiant du dossier
     */
    private String idDossier = null;

    /**
     * Nom de l'allocataire
     */
    private String nomAllocataire = null;
    /**
     * Num�ro de NSS
     */
    private String nss = null;
    /**
     * Num�ro de l'affili�
     */
    private String numeroAffilie = null;

    /**
     * Pr�nom de l'allocatarie
     */
    private String prenomAllocataire = null;

    /**
     * Raison sociale de l'affili�
     */
    private String raisonSocialeAffilie = null;
    /**
	 * 
	 */
    private String spy = null;
    /**
     * Statut du dossier
     */
    private String statutDossier = null;

    /**
     * Constructeur de DossierListComplexModel - cr�e les objets mod�le contenus dans ce mod�le
     */
    public DossierListComplexModel() {
        super();
    }

    /**
     * 
     * @return activiteDossier
     */
    public String getActiviteDossier() {
        return activiteDossier;
    }

    /**
     * 
     * @return etatDossier
     */
    public String getEtatDossier() {
        return etatDossier;
    }

    @Override
    public String getId() {
        return idDossier;
    }

    /**
     * 
     * @return idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * 
     * @return nomAllocataire
     */
    public String getNomAllocataire() {
        return nomAllocataire;
    }

    /**
     * 
     * @return nss
     */
    public String getNss() {
        return nss;
    }

    /**
     * 
     * @return numeroAffilie
     */
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    /**
     * 
     * @return prenomAllocataire
     */
    public String getPrenomAllocataire() {
        return prenomAllocataire;
    }

    /**
     * 
     * @return date de fin de validit�
     */
    public String getFinValidite() {
        return finValidite;
    }

    /**
     * 
     * @return raisonSocialeAffilie
     */
    public String getRaisonSocialeAffilie() {
        return raisonSocialeAffilie;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    /**
     * 
     * @return statutDossier
     */
    public String getStatutDossier() {
        return statutDossier;
    }

    /**
     * 
     * @param activiteDossier
     *            : the activiteDossier to set
     */
    public void setActiviteDossier(String activiteDossier) {
        this.activiteDossier = activiteDossier;
    }

    /**
     * 
     * @param etatDossier
     *            : the etatDossier to set
     */
    public void setEtatDossier(String etatDossier) {
        this.etatDossier = etatDossier;
    }

    @Override
    public void setId(String id) {
        setIdDossier(id);
    }

    /**
     * 
     * @param idDossier
     *            : the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * 
     * @param nomAllocataire
     *            : the nomAllocataire to set
     */
    public void setNomAllocataire(String nomAllocataire) {
        this.nomAllocataire = nomAllocataire;
    }

    /**
     * 
     * @param nss
     *            : the nss to set
     */
    public void setNss(String nss) {
        this.nss = nss;
    }

    /**
     * d�finit la date de fin validit�
     * 
     * @param finValidite
     */
    public void setFinValidite(String finValidite) {
        this.finValidite = finValidite;
    }

    /**
     * 
     * @param numeroAffilie
     *            the numeroAffilie to set
     */
    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    /**
     * 
     * @param prenomAllocataire
     *            : the prenomAllocataire to set
     */
    public void setPrenomAllocataire(String prenomAllocataire) {
        this.prenomAllocataire = prenomAllocataire;
    }

    /**
     * 
     * @param raisonSocialeAffilie
     *            : the raisonSocialeAffilie to set
     */
    public void setRaisonSocialeAffilie(String raisonSocialeAffilie) {
        this.raisonSocialeAffilie = raisonSocialeAffilie;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    /**
     * 
     * @param statutDossier
     *            : the statutAffilie to set
     */
    public void setStatutDossier(String statutDossier) {
        this.statutDossier = statutDossier;
    }

}
