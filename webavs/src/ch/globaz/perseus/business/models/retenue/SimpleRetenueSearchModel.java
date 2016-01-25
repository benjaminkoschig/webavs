/**
 * 
 */
package ch.globaz.perseus.business.models.retenue;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author dde
 * 
 */
public class SimpleRetenueSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String WITH_MOIS_VALABLE = "withMoisValable";

    private String forCsTypeRetenue = null;
    private String forIdPcfAccordee = null;
    private String forMoisValable = null;
    private String forNotCsTypeRetenue = null;

    /**
     * @return the forCsTypeRetenue
     */
    public String getForCsTypeRetenue() {
        return forCsTypeRetenue;
    }

    /**
     * @return the forIdPcfAccordee
     */
    public String getForIdPcfAccordee() {
        return forIdPcfAccordee;
    }

    /**
     * @return the forMoisValable
     */
    public String getForMoisValable() {
        return forMoisValable;
    }

    /**
     * 
     * @return the forNotCsTypeRetenue
     */
    public String getForNotCsTypeRetenue() {
        return forNotCsTypeRetenue;
    }

    /**
     * @param forCsTypeRetenue
     *            the forCsTypeRetenue to set
     */
    public void setForCsTypeRetenue(String forCsTypeRetenue) {
        this.forCsTypeRetenue = forCsTypeRetenue;
    }

    /**
     * @param forIdPcfAccordee
     *            the forIdPcfAccordee to set
     */
    public void setForIdPcfAccordee(String forIdPcfAccordee) {
        this.forIdPcfAccordee = forIdPcfAccordee;
    }

    /**
     * @param forMoisValable
     *            the forMoisValable to set
     */
    public void setForMoisValable(String forMoisValable) {
        if (!JadeStringUtil.isEmpty(forMoisValable)) {
            setWhereKey(SimpleRetenueSearchModel.WITH_MOIS_VALABLE);
            this.forMoisValable = forMoisValable;
        }
    }

    /**
     * 
     * @param forNotCsTypeRetenue
     *            the forNotCsTypeRetenue to set
     */
    public void setForNotCsTypeRetenue(String forNotCsTypeRetenue) {
        this.forNotCsTypeRetenue = forNotCsTypeRetenue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleRetenue.class;
    }

}
