package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleNombrePersonneSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = null;
    private String forIdNombrePersonne = null;
    private String forNombrePersonne = null;

    private String forNomCategorie = null;

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForIdNombrePersonne() {
        return forIdNombrePersonne;
    }

    public String getForNombrePersonne() {
        return forNombrePersonne;
    }

    public String getForNomCategorie() {
        return forNomCategorie;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForIdNombrePersonne(String forIdNombrePersonne) {
        this.forIdNombrePersonne = forIdNombrePersonne;
    }

    public void setForNombrePersonne(String forNombrePersonne) {
        this.forNombrePersonne = forNombrePersonne;
    }

    public void setForNomCategorie(String forNomCategorie) {
        this.forNomCategorie = forNomCategorie;
    }

    @Override
    public Class whichModelClass() {
        return SimpleNombrePersonne.class;
    }

}
