package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * @author jts
 * 
 */
public class AnnonceRafamComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Recherche sur le code de retour
     */
    private String forCodeRetour = null;

    /**
     * Recherche sur la date de debut de l'annonce
     */
    private String forDebutDroit = null;

    /**
     * Recherche sur le champ annonce délégué ou non
     */
    private Boolean forDelegated = null;

    /**
     * Recherche sur l'état des annonces
     */
    private String forEtat = null;

    /**
     * Recherche sur l'id de l'annonce
     */
    private String forIdAnnonce = null;

    /**
     * Recherche sur l'id du dossier
     */
    private String forIdDossier = null;

    /**
     * Recherche sur l'id du droit
     */
    private String forIdDroit = null;

    /**
     * Recherche par record Number
     */
    private String forRecordNumber = null;

    /**
     * Recherche selon un type d'annonce
     */
    private String forTypeAnnonce = null;

    /**
     * Recherche selon une liste de code de retour
     */
    private Collection<String> inCodeRetour = null;
    /**
     * Recherche selon une liste de type d'annonce
     */
    private Collection<String> inTypeAnnonce = null;

    /**
     * Recherche par NSS de l'enfant
     */
    private String likeNssEnfant = null;

    public String getForCodeRetour() {
        return forCodeRetour;
    }

    public String getForDebutDroit() {
        return forDebutDroit;
    }

    public Boolean getForDelegated() {
        return forDelegated;
    }

    public String getForEtat() {
        return forEtat;
    }

    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForRecordNumber() {
        return forRecordNumber;
    }

    public String getForTypeAnnonce() {
        return forTypeAnnonce;
    }

    public Collection<String> getInCodeRetour() {
        return inCodeRetour;
    }

    public Collection<String> getInTypeAnnonce() {
        return inTypeAnnonce;
    }

    public String getLikeNssEnfant() {
        return likeNssEnfant;
    }

    public void setForCodeRetour(String forCodeRetour) {
        this.forCodeRetour = forCodeRetour;
    }

    public void setForDebutDroit(String forDebutDroit) {
        this.forDebutDroit = forDebutDroit;
    }

    public void setForDelegated(Boolean forDelegated) {
        this.forDelegated = forDelegated;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public void setForIdAnnonce(String forIdAnnonce) {
        this.forIdAnnonce = forIdAnnonce;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForRecordNumber(String forRecordNumber) {
        this.forRecordNumber = forRecordNumber;
    }

    public void setForTypeAnnonce(String forTypeAnnonce) {
        this.forTypeAnnonce = forTypeAnnonce;
    }

    public void setInCodeRetour(Collection<String> inCodeRetour) {
        this.inCodeRetour = inCodeRetour;
    }

    public void setInTypeAnnonce(Collection<String> inTypeAnnonce) {
        this.inTypeAnnonce = inTypeAnnonce;
    }

    public void setLikeNssEnfant(String likeNssEnfant) {
        this.likeNssEnfant = likeNssEnfant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<AnnonceRafamComplexModel> whichModelClass() {
        return AnnonceRafamComplexModel.class;
    }
}
