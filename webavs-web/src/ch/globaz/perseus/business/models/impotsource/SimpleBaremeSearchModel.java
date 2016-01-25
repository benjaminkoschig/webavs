package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleBaremeSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = null;
    private String forCsTypeBareme = null;
    private String forIdBareme = null;
    private String forNombrePersonne = null;

    /**
     * @return the forAnnee
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * @return the forCsTypeBareme
     */
    public String getForCsTypeBareme() {
        return forCsTypeBareme;
    }

    /**
     * @return the forIdBareme
     */
    public String getForIdBareme() {
        return forIdBareme;
    }

    /**
     * @return the forNombrePersonne
     */
    public String getForNombrePersonne() {
        return forNombrePersonne;
    }

    /**
     * @param forAnnee
     *            the forAnnee to set
     */
    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    /**
     * @param forCsTypeBareme
     *            the forCsTypeBareme to set
     */
    public void setForCsTypeBareme(String forCsTypeBareme) {
        this.forCsTypeBareme = forCsTypeBareme;
    }

    /**
     * @param forIdBareme
     *            the forIdBareme to set
     */
    public void setForIdBareme(String forIdBareme) {
        this.forIdBareme = forIdBareme;
    }

    /**
     * @param forNombrePersonne
     *            the forNombrePersonne to set
     */
    public void setForNombrePersonne(String forNombrePersonne) {
        this.forNombrePersonne = forNombrePersonne;
    }

    @Override
    public Class<SimpleBareme> whichModelClass() {
        return SimpleBareme.class;
    }

}
