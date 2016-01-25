package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class MembreFamilleSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCSTypeQD = null;
    private String forDate = null;
    private String forIdDossier = null;

    /**
     * @return the forCSTypeQD
     */
    public String getForCSTypeQD() {
        return forCSTypeQD;
    }

    /**
     * @return the forDate
     */
    public String getForDate() {
        return forDate;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @param forCSTypeQD
     *            the forCSTypeQD to set
     */
    public void setForCSTypeQD(String forCSTypeQD) {
        this.forCSTypeQD = forCSTypeQD;
    }

    /**
     * @param forDate
     *            the forDate to set
     */
    public void setForDate(String forDate) {
        this.forDate = forDate;
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
        return MembreFamille.class;
    }

}
