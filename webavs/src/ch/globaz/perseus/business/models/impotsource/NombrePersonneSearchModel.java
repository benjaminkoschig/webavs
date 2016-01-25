package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class NombrePersonneSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = null;
    private String forIdNombrePersonne = null;

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForIdNombrePersonne() {
        return forIdNombrePersonne;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForIdNombrePersonne(String forIdNombrePersonne) {
        this.forIdNombrePersonne = forIdNombrePersonne;
    }

    @Override
    public Class whichModelClass() {
        return NombrePersonne.class;
    }

}
