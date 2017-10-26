package ch.globaz.pegasus.rpc.business.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class PersonneAnnonceSearch extends JadeSearchComplexModel {

    private String likeNss;
    private String likeNom;
    private String likePrenom;
    private String forIdAnnonce;
    private String forEtat;
    private String forCodeTraitement;
    private String forPeriodeDateDebut;
    private String forPeriodeDateFin;
    private static final String RECHERCHE_FAMILLE = "rechercheFamille";

    public String getLikeNss() {
        return (isWereKeyRechercheFamille()) ? likeNss + "%" : likeNss;
    }

    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
    }

    public String getLikeNom() {
        return (isWereKeyRechercheFamille()) ? likeNom + "%" : likeNom;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public String getLikePrenom() {
        return (isWereKeyRechercheFamille()) ? likePrenom + "%" : likePrenom;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

    public String getForEtat() {
        return forEtat;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public String getForCodeTraitement() {
        return forCodeTraitement;
    }

    public void setForCodeTraitement(String forCodeTraitement) {
        this.forCodeTraitement = forCodeTraitement;
    }

    public String getForPeriodeDateDebut() {
        return forPeriodeDateDebut;
    }

    public void setForPeriodeDateDebut(String forPeriodeDateDebut) {
        this.forPeriodeDateDebut = forPeriodeDateDebut;
    }

    public String getForPeriodeDateFin() {
        return forPeriodeDateFin;
    }

    public void setForPeriodeDateFin(String forPeriodeDateFin) {
        this.forPeriodeDateFin = forPeriodeDateFin;
    }

    public void whereKeyRechercheFamille() {
        setWhereKey(RECHERCHE_FAMILLE);
    }

    public boolean isWereKeyRechercheFamille() {
        return RECHERCHE_FAMILLE.equals(getWhereKey());
    }

    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    public void setForIdAnnonce(String forIdAnnonce) {
        this.forIdAnnonce = forIdAnnonce;
    }

    @Override
    public Class<PersonneAnnonce> whichModelClass() {
        return PersonneAnnonce.class;
    }

    @Override
    public String toString() {
        return "PersonneAnnonceSearch [likeNss=" + likeNss + ", likeNom=" + likeNom + ", likePrenom=" + likePrenom
                + ", forEtat=" + forEtat + ", forCodeTraitement=" + forCodeTraitement + ", forPeriodeDateDebut="
                + forPeriodeDateDebut + ", forPeriodeDateFin=" + forPeriodeDateFin + "]";
    }

}
