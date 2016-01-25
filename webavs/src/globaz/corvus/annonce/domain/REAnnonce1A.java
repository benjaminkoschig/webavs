package globaz.corvus.annonce.domain;

import globaz.corvus.annonce.RENSS;
import globaz.corvus.annonce.REPrefixPourReferenceInterneCaisseProvider;
import globaz.globall.util.JADate;

/**
 * REAAL1A
 * Représente le niveau 1A des annonces
 * 
 * @author lga
 * 
 */
public abstract class REAnnonce1A extends REEnteteAnnonce {

    private Integer cantonEtatDomicile;
    private Integer codeMutation;
    private JADate debutDroit;
    private Integer etatCivil;
    private JADate finDroit;
    private JADate moisRapport;
    private Integer genrePrestation;
    private Long idRenteAccordee;
    private Long idTiers;
    private Long numeroAnnonce;
    private Boolean isRefugie;
    private Integer mensualitePrestationsFrancs;
    private RENSS noAssAyantDroit;
    private RENSS premierNoAssComplementaire;
    private String utilisateurPourReferenceCaisseInterne;
    private RENSS secondNoAssComplementaire;

    /**
     * Retourne un objet capable de fournir le préfixe qui sera utilisé pour le champs REAAL1A.YXLREI
     * 
     * @return un objet capable de fournir le préfixe qui sera utilisé pour le champs REAAL1A.YXLREI
     */
    public abstract REPrefixPourReferenceInterneCaisseProvider getPrefixPourReferenceInterneCaisseProvider();

    /**
     * @return the cantonEtatDomicile
     */
    public final Integer getCantonEtatDomicile() {
        return cantonEtatDomicile;
    }

    /**
     * @param cantonEtatDomicile the cantonEtatDomicile to set
     */
    public final void setCantonEtatDomicile(Integer cantonEtatDomicile) {
        this.cantonEtatDomicile = cantonEtatDomicile;
    }

    /**
     * @return the codeMutation
     */
    public final Integer getCodeMutation() {
        return codeMutation;
    }

    /**
     * @param codeMutation the codeMutation to set
     */
    public final void setCodeMutation(Integer codeMutation) {
        this.codeMutation = codeMutation;
    }

    /**
     * @return the debutDroit
     */
    public final JADate getDebutDroit() {
        return debutDroit;
    }

    /**
     * @param debutDroit the debutDroit to set
     */
    public final void setDebutDroit(JADate debutDroit) {
        this.debutDroit = debutDroit;
    }

    /**
     * @return the etatCivil
     */
    public final Integer getEtatCivil() {
        return etatCivil;
    }

    /**
     * @param etatCivil the etatCivil to set
     */
    public final void setEtatCivil(Integer etatCivil) {
        this.etatCivil = etatCivil;
    }

    /**
     * @return the finDroit
     */
    public final JADate getFinDroit() {
        return finDroit;
    }

    /**
     * @param finDroit the finDroit to set
     */
    public final void setFinDroit(JADate finDroit) {
        this.finDroit = finDroit;
    }

    /**
     * @return the genrePrestation
     */
    public final Integer getGenrePrestation() {
        return genrePrestation;
    }

    /**
     * @param genrePrestation the genrePrestation to set
     */
    public final void setGenrePrestation(Integer genrePrestation) {
        this.genrePrestation = genrePrestation;
    }

    /**
     * @return the idTiers
     */
    public final Long getIdTiers() {
        return idTiers;
    }

    /**
     * @param idTiers the idTiers to set
     */
    public final void setIdTiers(Long idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @return the isRefugie
     */
    public final Boolean isRefugie() {
        return isRefugie;
    }

    /**
     * @param isRefugie the isRefugie to set
     */
    public final void setRefugie(Boolean isRefugie) {
        this.isRefugie = isRefugie;
    }

    /**
     * @return the mensualitePrestationsFrancs
     */
    public final Integer getMensualitePrestationsFrancs() {
        return mensualitePrestationsFrancs;
    }

    /**
     * @param mensualitePrestationsFrancs the mensualitePrestationsFrancs to set
     */
    public final void setMensualitePrestationsFrancs(Integer mensualitePrestationsFrancs) {
        this.mensualitePrestationsFrancs = mensualitePrestationsFrancs;
    }

    /**
     * @return the noAssAyantDroit
     */
    public final RENSS getNoAssAyantDroit() {
        return noAssAyantDroit;
    }

    /**
     * @param noAssAyantDroit the noAssAyantDroit to set
     */
    public final void setNoAssAyantDroit(RENSS noAssAyantDroit) {
        this.noAssAyantDroit = noAssAyantDroit;
    }

    /**
     * @return the premierNoAssComplementaire
     */
    public final RENSS getPremierNoAssComplementaire() {
        return premierNoAssComplementaire;
    }

    /**
     * @param premierNoAssComplementaire the premierNoAssComplementaire to set
     */
    public final void setPremierNoAssComplementaire(RENSS premierNoAssComplementaire) {
        this.premierNoAssComplementaire = premierNoAssComplementaire;
    }

    // /**
    // * Retourne l'utilisateur qui est utilisé pour la référence interne de la caisse
    // *
    // * @return l'utilisateur qui est utilisé pour la référence interne de la caisse
    // */
    // public final String getReferenceCaisseInterne() {
    // return utilisateurPourReferenceCaisseInterne;
    // }

    /**
     * Définit l'utilisateur qui est utilisé pour la référence interne de la caisse
     * 
     * @param utilisateurPourReferenceCaisseInterne l'utilisateur qui est utilisé pour la référence interne de la caisse
     */
    public final void setReferenceCaisseInterne(String utilisateurPourReferenceCaisseInterne) {
        this.utilisateurPourReferenceCaisseInterne = utilisateurPourReferenceCaisseInterne;
    }

    /**
     * @return the secondNoAssComplementaire
     */
    public final RENSS getSecondNoAssComplementaire() {
        return secondNoAssComplementaire;
    }

    /**
     * @param secondNoAssComplementaire the secondNoAssComplementaire to set
     */
    public final void setSecondNoAssComplementaire(RENSS secondNoAssComplementaire) {
        this.secondNoAssComplementaire = secondNoAssComplementaire;
    }

    /**
     * @return the utilisateurPourReferenceCaisseInterne
     */
    public final String getUtilisateurPourReferenceCaisseInterne() {
        return utilisateurPourReferenceCaisseInterne;
    }

    /**
     * @param utilisateurPourReferenceCaisseInterne the utilisateurPourReferenceCaisseInterne to set
     */
    public final void setUtilisateurPourReferenceCaisseInterne(String utilisateurPourReferenceCaisseInterne) {
        this.utilisateurPourReferenceCaisseInterne = utilisateurPourReferenceCaisseInterne;
    }

    /**
     * @return the idRenteAccordee
     */
    public final Long getIdRenteAccordee() {
        return idRenteAccordee;
    }

    /**
     * @param idRenteAccordee the idRenteAccordee to set
     */
    public final void setIdRenteAccordee(Long idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    /**
     * @return the moisRapport
     */
    public final JADate getMoisRapport() {
        return moisRapport;
    }

    /**
     * @param moisRapport the moisRapport to set
     */
    public final void setMoisRapport(JADate moisRapport) {
        this.moisRapport = moisRapport;
    }

    /**
     * @return the numeroAnnonce
     */
    public final Long getNumeroAnnonce() {
        return numeroAnnonce;
    }

    /**
     * @param numeroAnnonce the numeroAnnonce to set
     */
    public final void setNumeroAnnonce(Long numeroAnnonce) {
        this.numeroAnnonce = numeroAnnonce;
    }

}
