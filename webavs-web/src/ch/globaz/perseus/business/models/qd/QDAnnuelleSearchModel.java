/**
 * 
 */
package ch.globaz.perseus.business.models.qd;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author DDE
 * 
 */
public class QDAnnuelleSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = null;
    private String forIdDossier = null;

    /**
     * @return the forAnnee
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @param forAnnee
     *            the forAnnee to set
     */
    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return QDAnnuelle.class;
    }

}
