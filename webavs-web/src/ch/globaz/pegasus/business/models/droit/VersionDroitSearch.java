package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class VersionDroitSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String CURRENT_VERSION = "currentVersion";
    public final static String DERNIERE_VERSION_HISTORISEE_ORDER_KEY = "derniereVersionHistorisee";
    public final static String DERNIERE_VERSION_HISTORISEE_WHERE_KEY = "derniereVersionHistorisee";
    public static final String OLD_DEMANDE = "withOldDemande";

    private String forCsEtatDroit = null;
    private List<String> forCsEtatDroitIn = null;
    private String forCsModif = null;
    private String forCsSexe = null;
    private String forDateFinDemande = null;
    private String forDateNaissance = null;
    private String forIdDemandePc = null;
    private String forIdDroit = null;
    private String forIdTiers = null;
    private String forIdVersionDroit = null;
    private String forNoVersionDroit = null;
    private String forNss = null;
    private List<String> inIdDemandes = null;
    private String likeNom = null;
    private String likeNss = null;
    private String likePrenom = null;

    public String getForCsEtatDroit() {
        return forCsEtatDroit;
    }

    public List<String> getForCsEtatDroitIn() {
        return forCsEtatDroitIn;
    }

    public String getForCsModif() {
        return forCsModif;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForDateFinDemande() {
        return forDateFinDemande;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForIdDemandePc() {
        return forIdDemandePc;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public String getForNoVersionDroit() {
        return forNoVersionDroit;
    }

    public String getForNss() {
        return forNss;
    }

    public List<String> getInIdDemandes() {
        return inIdDemandes;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikeNss() {
        return likeNss;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    public void setForCsEtatDroit(String forCsEtatDroit) {
        this.forCsEtatDroit = forCsEtatDroit;
    }

    public void setForCsEtatDroitIn(List<String> forCsEtatDroitIn) {
        this.forCsEtatDroitIn = forCsEtatDroitIn;
    }

    public void setForCsModif(String forCsModif) {
        this.forCsModif = forCsModif;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForDateFinDemande(String forDateFinDemande) {
        this.forDateFinDemande = forDateFinDemande;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForIdDemandePc(String forIdDemandePc) {
        this.forIdDemandePc = forIdDemandePc;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public void setForNoVersionDroit(String forNoVersionDroit) {
        this.forNoVersionDroit = forNoVersionDroit;
    }

    public void setForNss(String forNss) {
        this.forNss = forNss;
    }

    public void setInIdDemandes(List<String> inIdDemandes) {
        this.inIdDemandes = inIdDemandes;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

    @Override
    public Class<VersionDroit> whichModelClass() {
        return VersionDroit.class;
    }

}
