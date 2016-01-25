package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * Modèle de recherche pour les modèles complexes de dossier
 * 
 * @author jts
 * 
 */
public class DossierComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String ETATACTIF = "etatActif";
    public static final String ETATRADIE = "etatRadie";

    public static final String ORDER_NUM_AFFILIE_ALLOC = "numAffilieAlloc";

    public static final String ORDER_AFFILIE_ALLOC = "numAffilieAlloc";
    public static final String ORDER_ALLOC = "OrderAlloc";
    public static final String ORDER_DIRECT_AFFILIE = "OrderDirectAffilie";
    public static final String ORDER_DIRECT_ALLOC = "OrderDirectAlloc";

    public static final String SEARCH_DOSSIERS_LIES = "lienDossier";
    public static final String SEARCH_LIST_DOSSIER = "listDossier";
    public static final String SEARCH_LIST_DOSSIER_RADIE = "listDossierRadie";
    public static final String SEARCH_LIST_DOSSIER_ACTIF = "listDossierActif";
    public static final String SEARCH_RADIATION_AFFILIE = "RadiationAffilie";
    public static final String SEARCH_DOSSIERS_ACTIF = "dossiersActifs";

    /**
     * Recherche selon une liste d'état
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_ETAT
     */
    private String forEtat = null;
    /**
     * Recherche sur la date de fin de validité du dossier
     */
    private String forFinValidite = null;
     /**
     * Recherche sur la date de debut de validité du dossier
     */
    private String forDebutValidite = null;
    /**
     * recherche between date de fin de validité
     */
    private String forFinValiditeSmaller = null;
    private String forFinValiditeGreater = null;
    /**
     * recherche between date de début de validité
     */
    private String forValiditeSmaller = null;
    private String forValiditeGreater = null;
    /**
     * Recherche par id du dossier
     */
    private String forIdDossier = null;

    /** Recherche sur l'idTiers */
    private String forIdTiersAllocataire = null;
    /**
     * Recherche par numéro d'affilié
     */
    private String forNumeroAffilie = null;
    /**
     * Recherche sur une ou plusieurs activité(s)
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_ACTIVITE_ALLOC
     */
    private Collection<String> inActivites = null;
    /**
     * Recherche selon une liste d'état
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_ETAT
     */
    private Collection<String> inEtat = null;
    /**
     * Recherche selon unne liste de dossier
     */
    private Collection<String> inIdDossier = null;
    /**
     * Recherche selon une liste de numéro d'affiliés
     */
    private Collection<String> inNumeroAffilie = null;
    /**
     * Recherche selon une liste de statut
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_STATUT
     */
    private Collection<String> inStatut = null;
    /**
     * Recherche par n° affilié
     */
    private String likeNumeroAffilie = null;

    /**
     * @return the forEtat
     */
    public String getForEtat() {
        return forEtat;
    }

    /**
     * @return the forFinValidite
     */
    public String getForFinValidite() {
        return forFinValidite;
    }

    public String getForFinValiditeSmaller() {
        return forFinValiditeSmaller;
    }

    public String getForFinValiditeGreater() {
        return forFinValiditeGreater;
    }

    public String getForValiditeSmaller() {
        return forValiditeSmaller;
    }

    public String getForValiditeGreater() {
        return forValiditeGreater;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdTiersAllocataire() {
        return forIdTiersAllocataire;
    }

    /**
     * @return the forNumeroAffilie
     */
    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    public Collection<String> getInActivites() {
        return inActivites;
    }

    /**
     * @return the inEtat
     */
    public Collection<String> getInEtat() {
        return inEtat;
    }

    /**
     * @return the inIdDossier
     */
    public Collection<String> getInIdDossier() {
        return inIdDossier;
    }

    /**
     * @return the inStatut
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_STATUT
     */
    public Collection<String> getInStatut() {
        return inStatut;
    }

    public String getLikeNumeroAffilie() {
        return likeNumeroAffilie;
    }

    public Collection<String> getInNumeroAffilie() {
        return inNumeroAffilie;
    }

    public void setInNumeroAffilie(Collection<String> inNumeroAffilie) {
        this.inNumeroAffilie = inNumeroAffilie;
    }

    /**
     * @param forEtat
     *            the forEtat to set
     */
    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    /**
     * @param forFinValidite
     *            the forFinValidite to set
     */
    public void setForFinValidite(String forFinValidite) {
        this.forFinValidite = forFinValidite;
    }

    public void setForFinValiditeSmaller(String forFinValiditeSmaller) {
        this.forFinValiditeSmaller = forFinValiditeSmaller;
    }

    public void setForFinValiditeGreater(String forFinValiditeGreater) {
        this.forFinValiditeGreater = forFinValiditeGreater;
    }

    public void setForValiditeSmaller(String forValiditeSmaller) {
        this.forValiditeSmaller = forValiditeSmaller;
    }

    public void setForValiditeGreater(String forValiditeGreater) {
        this.forValiditeGreater = forValiditeGreater;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdTiersAllocataire(String forIdTiersAllocataire) {
        this.forIdTiersAllocataire = forIdTiersAllocataire;
    }

    /**
     * @param forNumeroAffilie
     *            the forNumeroAffilie to set
     */
    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    public void setInActivites(Collection<String> inActivites) {
        this.inActivites = inActivites;
    }

    /**
     * @param inEtat
     *            the inEtat to set
     */
    public void setInEtat(Collection<String> inEtat) {
        this.inEtat = inEtat;
    }

    /**
     * @param inIdDossier
     *            the inIdDossier to set
     */
    public void setInIdDossier(Collection<String> inIdDossier) {
        this.inIdDossier = inIdDossier;
    }

    /**
     * @param inStatut
     *            the inStatut to set
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_STATUT
     */
    public void setInStatut(Collection<String> inStatut) {
        this.inStatut = inStatut;
    }

    public void setLikeNumeroAffilie(String likeNumeroAffilie) {
        this.likeNumeroAffilie = likeNumeroAffilie;
    }

    @Override
    public Class<DossierComplexModel> whichModelClass() {
        return DossierComplexModel.class;
    }

    public String getForDebutValidite() {
        return forDebutValidite;
    }

    public void setForDebutValidite(String forDebutValidite) {
        this.forDebutValidite = forDebutValidite;
    }
}