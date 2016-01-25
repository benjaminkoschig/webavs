/**
 * 
 */
package ch.globaz.amal.business.models.annonce;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author DHI
 * 
 */
public class SimpleAnnonceSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeHistorique = null;
    private String forDateAvisRIP = null;
    private String forIdDetailAnnonce = null;
    private String forIdDetailFamille = null;
    private String forNoCaisseMaladie = null;

    /**
     * @return the forAnneeHistorique
     */
    public String getForAnneeHistorique() {
        return forAnneeHistorique;
    }

    /**
     * @return the forDateAvisRIP
     */
    public String getForDateAvisRIP() {
        return forDateAvisRIP;
    }

    /**
     * @return the forIdDetailAnnonce
     */
    public String getForIdDetailAnnonce() {
        return forIdDetailAnnonce;
    }

    /**
     * @return the forIdDetailFamille
     */
    public String getForIdDetailFamille() {
        return forIdDetailFamille;
    }

    /**
     * @return the forNoCaisseMaladie
     */
    public String getForNoCaisseMaladie() {
        return forNoCaisseMaladie;
    }

    /**
     * @param forAnneeHistorique
     *            the forAnneeHistorique to set
     */
    public void setForAnneeHistorique(String forAnneeHistorique) {
        this.forAnneeHistorique = forAnneeHistorique;
    }

    /**
     * @param forDateAvisRIP
     *            the forDateAvisRIP to set
     */
    public void setForDateAvisRIP(String forDateAvisRIP) {
        this.forDateAvisRIP = forDateAvisRIP;
    }

    /**
     * @param forIdDetailAnnonce
     *            the forIdDetailAnnonce to set
     */
    public void setForIdDetailAnnonce(String forIdDetailAnnonce) {
        this.forIdDetailAnnonce = forIdDetailAnnonce;
    }

    /**
     * @param forIdDetailFamille
     *            the forIdDetailFamille to set
     */
    public void setForIdDetailFamille(String forIdDetailFamille) {
        this.forIdDetailFamille = forIdDetailFamille;
    }

    /**
     * @param forNoCaisseMaladie
     *            the forNoCaisseMaladie to set
     */
    public void setForNoCaisseMaladie(String forNoCaisseMaladie) {
        this.forNoCaisseMaladie = forNoCaisseMaladie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleAnnonce.class;
    }

}
