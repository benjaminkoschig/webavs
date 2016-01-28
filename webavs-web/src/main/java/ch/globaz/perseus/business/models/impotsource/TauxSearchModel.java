package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class TauxSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String WITH_TAUX_VALABLE = "withTauxValable";

    private String forAnnee = null;
    private String forCsTypeBareme = null;
    private String forIdTaux = null;
    private String forNombrePersonne = null;
    private String forSalaireBrut = null;

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
     * @return the forIdTaux
     */
    public String getForIdTaux() {
        return forIdTaux;
    }

    /**
     * @return the forNombrePersonne
     */
    public String getForNombrePersonne() {
        return forNombrePersonne;
    }

    /**
     * @return the forSalaireBrut
     */
    public String getForSalaireBrut() {
        return forSalaireBrut;
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
     * @param forIdTaux
     *            the forIdTaux to set
     */
    public void setForIdTaux(String forIdTaux) {
        this.forIdTaux = forIdTaux;
    }

    /**
     * @param forNombrePersonne
     *            the forNombrePersonne to set
     */
    public void setForNombrePersonne(String forNombrePersonne) {
        this.forNombrePersonne = forNombrePersonne;
    }

    /**
     * @param forSalaireBrut
     *            the forSalaireBrut to set
     */
    public void setForSalaireBrut(String forSalaireBrut) {
        this.forSalaireBrut = forSalaireBrut;
    }

    @Override
    public Class whichModelClass() {
        return Taux.class;
    }

}
