package ch.globaz.vulpecula.business.models.congepaye;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Permet d'effecteur des recherches de compteur
 * 
 * @since WebBMS 0.01.04
 */
public class CompteurSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 4239817472918677281L;

    private String forId;
    private String forAnnee;
    private String forIdPosteTravail;

    @Override
    public Class<CompteurSimpleModel> whichModelClass() {
        return CompteurSimpleModel.class;
    }

    /**
     * @return the forId
     */
    public String getForId() {
        return forId;
    }

    /**
     * @return the forIdPosteTravail
     */
    public String getForIdPosteTravail() {
        return forIdPosteTravail;
    }

    /**
     * @param forId the forId to set
     */
    public void setForId(String forId) {
        this.forId = forId;
    }

    /**
     * @param forIdPosteTravail the forIdPosteTravail to set
     */
    public void setForIdPosteTravail(String forIdPosteTravail) {
        this.forIdPosteTravail = forIdPosteTravail;
    }

    /**
     * @return the forAnnee
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * @param forAnnee the forAnnee to set
     */
    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

}
