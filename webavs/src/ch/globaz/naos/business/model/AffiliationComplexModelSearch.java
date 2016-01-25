package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class AffiliationComplexModelSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAffiliation;
    private String forNumeroAffilie;
    private String forNumeroAffilieLike;

    /**
     * @return the forIdAffiliation
     */
    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    /**
     * @return the forNumeroAffilie
     */
    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    /**
     * @return the forNumeroAffilieLike
     */
    public String getForNumeroAffilieLike() {
        return forNumeroAffilieLike;
    }

    /**
     * @param forIdAffiliation
     *            the forIdAffiliation to set
     */
    public void setForIdAffiliation(String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    /**
     * @param forNumeroAffilie
     *            the forNumeroAffilie to set
     */
    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    /**
     * @param forNumeroAffilieLike
     *            the forNumeroAffilieLike to set
     */
    public void setForNumeroAffilieLike(String forNumeroAffilieLike) {
        this.forNumeroAffilieLike = forNumeroAffilieLike;
    }

    @Override
    public Class<AffiliationComplexModel> whichModelClass() {
        return AffiliationComplexModel.class;
    }

}
