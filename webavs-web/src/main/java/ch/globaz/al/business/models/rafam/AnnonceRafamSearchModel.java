package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.Collection;

/**
 * Modèle de recherche pour le modèle {@link AnnonceRafamModel}
 * 
 * @author jts
 */
public class AnnonceRafamSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Recherche sur le code de retour de la centrale
     */
    private String forCodeRetour = null;
    private Boolean forDelegated = null;
    /**
     * Recherche selon l'état de l'annonce
     */
    private String forEtatAnnonce = null;
    /**
     * Recherche sur le genre de prestations
     */
    private String forGenrePrestation = null;
    /** Recherche sur l'id d'annonce */
    private String forIdAnnonce = null;
    /**
     * Recherche sur l'id du droit
     */
    private String forIdDroit = null;
    /**
     * Recherche sur le NSS de l'enfant
     */
    private String forNssEnfant = null;

    /**
     * Recherche sur le record number
     */
    private String forRecordNumber = null;

    /**
     * Recherche sur le type d'annonce
     */
    private String forTypeAnnonce = null;
    private Collection<String> inCodeRetour = null;
    /**
     * Recherche selon une liste d'états
     */
    private Collection<String> inEtatAnnonce = null;

    /** Recherche sur une liste de droits */
    private Collection<String> inIdDroit = null;

    /**
     * Recherche selon une liste de types d'annonces
     */
    private Collection<String> inTypeAnnonce = null;

    private String likeInternalOffice = null;

    private String betweenDateDebut = null;

    private String betweenDateFin = null;

    public String getForCodeRetour() {
        return forCodeRetour;
    }

    public Boolean getForDelegated() {
        return forDelegated;
    }

    public String getForEtatAnnonce() {
        return forEtatAnnonce;
    }

    public String getForGenrePrestation() {
        return forGenrePrestation;
    }

    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForNssEnfant() {
        return forNssEnfant;
    }

    public String getForRecordNumber() {
        return forRecordNumber;
    }

    public String getForTypeAnnonce() {
        return forTypeAnnonce;
    }

    public Collection<String> getInEtatAnnonce() {
        return inEtatAnnonce;
    }

    public Collection<String> getInIdDroit() {
        return inIdDroit;
    }

    public Collection<String> getInTypeAnnonce() {
        return inTypeAnnonce;
    }

    public String getLikeInternalOffice() {
        return likeInternalOffice;
    }

    public void setForCodeRetour(String forCodeRetour) {
        this.forCodeRetour = forCodeRetour;
    }

    public void setForDelegated(Boolean forDelegated) {
        this.forDelegated = forDelegated;
    }

    public void setForEtatAnnonce(String forEtatAnnonce) {
        this.forEtatAnnonce = forEtatAnnonce;
    }

    public void setForGenrePrestation(String forGenrePrestation) {
        this.forGenrePrestation = forGenrePrestation;
    }

    public void setForIdAnnonce(String forIdAnnonce) {
        this.forIdAnnonce = forIdAnnonce;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForNssEnfant(String forNssEnfant) {
        this.forNssEnfant = forNssEnfant;
    }

    public void setForRecordNumber(String forRecordNumber) {
        this.forRecordNumber = forRecordNumber;
    }

    public void setForTypeAnnonce(String forTypeAnnonce) {
        this.forTypeAnnonce = forTypeAnnonce;
    }

    public void setInEtatAnnonce(Collection<String> inEtatAnnonce) {
        this.inEtatAnnonce = inEtatAnnonce;
    }

    public void setInIdDroit(Collection<String> inIdDroit) {
        this.inIdDroit = inIdDroit;
    }

    public void setInTypeAnnonce(Collection<String> inTypeAnnonce) {
        this.inTypeAnnonce = inTypeAnnonce;
    }

    public void setLikeInternalOffice(String likeInternalOffice) {
        this.likeInternalOffice = likeInternalOffice;
    }

    @Override
    public Class<AnnonceRafamModel> whichModelClass() {
        return AnnonceRafamModel.class;
    }

    public Collection<String> getInCodeRetour() {
        return inCodeRetour;
    }

    public void setInCodeRetour(Collection<String> inCodeRetour) {
        this.inCodeRetour = inCodeRetour;
    }

    public String getBetweenDateDebut() {
        return betweenDateDebut;
    }

    public void setBetweenDateDebut(String betweenDateDebut) {
        this.betweenDateDebut = betweenDateDebut;
    }

    public String getBetweenDateFin() {
        return betweenDateFin;
    }

    public void setBetweenDateFin(String betweenDateFin) {
        this.betweenDateFin = betweenDateFin;
    }
}
