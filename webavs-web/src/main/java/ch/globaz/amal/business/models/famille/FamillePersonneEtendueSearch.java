package ch.globaz.amal.business.models.famille;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class FamillePersonneEtendueSearch extends JadeSearchComplexModel {
    private String likeNss = null;
    private String forMembreFinDroit = null;
    private String forMembreActifFromToday = null;

    public String getForMembreFinDroit() {
        return forMembreFinDroit;
    }

    public void setForMembreFinDroit(String forMembreFinDroit) {
        this.forMembreFinDroit = forMembreFinDroit;
    }

    @Override
    public Class whichModelClass() {
        return FamillePersonneEtendue.class;
    }

    public String getLikeNss() {
        return likeNss;
    }

    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
    }

    public String getForMembreActifFromToday() {
        return forMembreActifFromToday;
    }

    public void setForMembreActifFromToday(String forMembreActifFromToday) {
        this.forMembreActifFromToday = forMembreActifFromToday;
    }

}
