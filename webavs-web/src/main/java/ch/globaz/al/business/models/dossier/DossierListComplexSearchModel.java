package ch.globaz.al.business.models.dossier;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * modèle complexe de recherche de dossier
 * 
 * @author PTA
 * 
 */
public class DossierListComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Recherche sur la date de naissance de l'enfant
     */
    private String forDateEnfant = null;
    /**
     * Recherche par l'identifiant de l'allocataire
     */
    private String forIdAllocataire = null;
    /**
     * Recherche par l'identifiant du dossier
     */
    private String forIdDossier = null;
    /**
     * Recherche par l'idntifiant de l'enfant
     */
    private String forIdEnfant = null;
    /**
     * Recherche par l'identifaint du tiers allocataire
     */
    private String forIdTiersAllocataire = null;
    /**
     * Recherche par l'identifiant du tiers enfant
     */
    private String forIdTiersEnfant = null;
    /**
     * Recherche par le numéro d'affilié
     */
    private String forNumAffilie = null;
    /**
     * Recherche par le numéro du dossier
     */
    private String forNumeroDossier = null;
    /**
     * Recherche selon une liste d'activité alloc de dossier
     */
    private Collection<String> inActivitesDossier = null;
    /**
     * Recherche selon une liste d'états de dossier
     */
    private Collection<String> inEtatsDossier = null;

    /**
     * Recherche selon une liste de statuts de dossier
     */
    private Collection<String> inStatutsDossier = null;
    /**
     * Recherche par le nom partiel de l'affilié
     */
    private String likeNomAffilie = null;

    /**
     * Recherche par le nom partiel de l'allocataire
     */
    private String likeNomAllocataire = null;

    /**
     * Recherche par le nom partiel de l'enfnat
     */
    private String likeNomEnfant = null;
    /**
     * Recherche par le numéro partiel du NSS de l'allocataire
     */
    private String likeNssAllocataire = null;
    /**
     * Recherche par le numéro partiel du NSS de l'enfant
     */
    private String likeNssEnfant = null;
    /**
     * Recherche par le numéro partiel du dossier
     */
    private String likeNumeroDossier = null;

    /**
     * Recherche par le prénom partiel de l'allocataire
     */
    private String likePrenomAllocataire = null;
    /**
     * Recherche par le prénom partiel de l'enfnat
     */
    private String likePrenomEnfant = null;

    /**
     * Recherche pour les dossiers dont la date de validité est plus grande que forFinValiditeGreater ou null
     */
    private String forFinValiditeGreater = null;

    /**
     * 
     * @return forDateEnfant
     */
    public String getForDateEnfant() {
        return forDateEnfant;
    }

    /**
     * 
     * @return the forIdAllocataire
     */
    public String getForIdAllocataire() {
        return forIdAllocataire;
    }

    /**
     * 
     * @return forIdDossier
     */

    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * 
     * @return forIdEnfant
     */
    public String getForIdEnfant() {
        return forIdEnfant;
    }

    /**
     * 
     * @return the forIdTiersAllocataire
     */
    public String getForIdTiersAllocataire() {
        return forIdTiersAllocataire;
    }

    /**
     * 
     * @return the forIdTiersEnfant
     */
    public String getForIdTiersEnfant() {
        return forIdTiersEnfant;
    }

    /**
     * @return the forNumAffilie
     */
    public String getForNumAffilie() {
        return forNumAffilie;
    }

    /**
     * @return the forNumeroDossier
     */
    public String getForNumeroDossier() {
        return forNumeroDossier;
    }

    public Collection<String> getInActivitesDossier() {
        return inActivitesDossier;
    }

    /**
     * 
     * @return inEtatsDossier liste contenant les états de dossier à rechercher
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_ETAT
     */
    public Collection<String> getInEtatsDossier() {
        return inEtatsDossier;
    }

    /**
     * @return inStatutsDossier liste des statuts de dossier recherchés
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_STATUT
     */
    public Collection<String> getInStatutsDossier() {
        return inStatutsDossier;
    }

    /**
     * @return likeNomAffilie
     */
    public String getLikeNomAffilie() {
        return likeNomAffilie;
    }

    /**
     * 
     * @return the likeNomAllocataire
     */
    public String getLikeNomAllocataire() {
        return likeNomAllocataire;
    }

    /**
     * 
     * @return the likeNomEnfant
     */
    public String getLikeNomEnfant() {
        return likeNomEnfant;
    }

    /**
     * 
     * @return the likeNssAllocataire
     */
    public String getLikeNssAllocataire() {
        return likeNssAllocataire;
    }

    /**
     * 
     * @return the likeNssEnfant
     */
    public String getLikeNssEnfant() {
        return likeNssEnfant;
    }

    /**
     * @return the likeNumeroDossier
     */
    public String getLikeNumeroDossier() {
        return likeNumeroDossier;
    }

    /**
     * @return the likePrenomAllocataire
     */
    public String getLikePrenomAllocataire() {
        return likePrenomAllocataire;
    }

    /**
     * 
     * @return the likePrenomEnfant
     */
    public String getLikePrenomEnfant() {
        return likePrenomEnfant;
    }

    /**
     * @param forDateEnfant
     *            La date de naissance enfant à rechercher
     */
    public void setForDateEnfant(String forDateEnfant) {
        this.forDateEnfant = forDateEnfant;
    }

    /**
     * 
     * @param forIdAllocataire
     *            the forIdAllocataire to set
     */
    public void setForIdAllocataire(String forIdAllocataire) {
        this.forIdAllocataire = forIdAllocataire;
    }

    /**
     * @param forIdDossier
     *            L'id dossier à rechercher
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * @param forIdEnfant
     *            l'id enfant à rechercher
     */
    public void setForIdEnfant(String forIdEnfant) {
        this.forIdEnfant = forIdEnfant;
    }

    /**
     * 
     * @param forIdTiersAllocataire
     *            the forIdTiersAllocataire to set
     */
    public void setForIdTiersAllocataire(String forIdTiersAllocataire) {
        this.forIdTiersAllocataire = forIdTiersAllocataire;
    }

    /**
     * 
     * @param forIdTiersEnfant
     *            the forIdTiersEnfant to set
     */
    public void setForIdTiersEnfant(String forIdTiersEnfant) {
        this.forIdTiersEnfant = forIdTiersEnfant;
    }

    /**
     * @param forNumAffilie
     *            the forNumAffilie to set
     */
    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    /**
     * @param forNumeroDossier
     *            the forNumeroDossier to set
     */
    public void setForNumeroDossier(String forNumeroDossier) {
        this.forNumeroDossier = forNumeroDossier;
    }

    public void setInActivitesDossier(Collection<String> inActivitesDossier) {
        this.inActivitesDossier = inActivitesDossier;
    }

    /**
     * @param inEtatsDossier
     *            liste contenant les états de dossier à rechercher
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_ETAT
     */
    public void setInEtatsDossier(Collection<String> inEtatsDossier) {
        this.inEtatsDossier = inEtatsDossier;
    }

    /**
     * @param inStatutsDossier
     *            liste contenant les statut de dossier à rechercher
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_STATUT
     */
    public void setInStatutsDossier(Collection<String> inStatutsDossier) {
        this.inStatutsDossier = inStatutsDossier;
    }

    /**
     * 
     * @param likeNomAffilie
     *            : the likeNomAffilie to set
     */
    public void setLikeNomAffilie(String likeNomAffilie) {
        if (!JadeStringUtil.isEmpty(likeNomAffilie)) {
            this.likeNomAffilie = JadeStringUtil.convertSpecialChars(likeNomAffilie).toUpperCase();
        }
    }

    /**
     * 
     * @param likeNomAllocataire
     *            the likeNomAllocataire to set
     */
    public void setLikeNomAllocataire(String likeNomAllocataire) {
        if (!JadeStringUtil.isEmpty(likeNomAllocataire)) {
            this.likeNomAllocataire = JadeStringUtil.convertSpecialChars(likeNomAllocataire).toUpperCase();
        }
    }

    /**
     * 
     * @param likeNomEnfant
     *            the likeNomEnfant to set
     */
    public void setLikeNomEnfant(String likeNomEnfant) {
        if (!JadeStringUtil.isEmpty(likeNomEnfant)) {
            this.likeNomEnfant = JadeStringUtil.convertSpecialChars(likeNomEnfant).toUpperCase();
        }
    }

    /**
     * 
     * @param likeNssAllocataire
     *            the likeNssAllocataire to set
     */
    public void setLikeNssAllocataire(String likeNssAllocataire) {
        this.likeNssAllocataire = likeNssAllocataire;
    }

    /**
     * 
     * @param likeNssEnfant
     *            the likeNssEnfant to set
     */
    public void setLikeNssEnfant(String likeNssEnfant) {
        this.likeNssEnfant = likeNssEnfant;
    }

    /**
     * @param likeNumeroDossier
     *            the likeNumeroDossier to set
     */
    public void setLikeNumeroDossier(String likeNumeroDossier) {
        this.likeNumeroDossier = likeNumeroDossier;
    }

    /**
     * @param likePrenomAllocataire
     *            the likePrenomAllocataire to set
     */
    public void setLikePrenomAllocataire(String likePrenomAllocataire) {
        if (!JadeStringUtil.isEmpty(likePrenomAllocataire)) {
            this.likePrenomAllocataire = JadeStringUtil.convertSpecialChars(likePrenomAllocataire).toUpperCase();
        }
    }

    /**
     * 
     * @param likePrenomEnfant
     *            the likePrenomEnfant to set
     */
    public void setLikePrenomEnfant(String likePrenomEnfant) {
        if (!JadeStringUtil.isEmpty(likePrenomEnfant)) {
            this.likePrenomEnfant = JadeStringUtil.convertSpecialChars(likePrenomEnfant).toUpperCase();
        }
    }

    public String getForFinValiditeGreater() {
        return forFinValiditeGreater;
    }

    public void setForFinValiditeGreater(String forFinValiditeGreater) {
        this.forFinValiditeGreater = forFinValiditeGreater;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<DossierListComplexModel> whichModelClass() {
        return DossierListComplexModel.class;
    }
}
