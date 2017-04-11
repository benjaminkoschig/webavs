/**
 * 
 */
package ch.globaz.amal.business.models.annoncesedexco;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;

/**
 * @author lbe
 * 
 */
public class ComplexAnnonceSedexCO2Search extends JadeSearchComplexModel {

    private static final long serialVersionUID = 1L;
    private String forIdContribuable = null;
    private ArrayList<String> inSDXMessageSubType = null;

    /**
     * Default constructor
     */
    public ComplexAnnonceSedexCO2Search() {
    }

    /**
     * @return the IdContribuable
     */
    public String getForIdContribuable() {
        return forIdContribuable;
    }

    /**
     * @param forIdContribuable
     *            the forIdContribuable to set
     */
    public void setForIdContribuable(String forIdContribuable) {
        this.forIdContribuable = forIdContribuable;
    }

    /**
     * @return the inSDXMessageSubType
     */
    public ArrayList<String> getInSDXMessageSubType() {
        return inSDXMessageSubType;
    }

    /**
     * @param inSDXMessageSubType the inSDXMessageSubType to set
     */
    public void setInSDXMessageSubType(ArrayList<String> inSDXMessageSubType) {
        this.inSDXMessageSubType = inSDXMessageSubType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return ComplexAnnonceSedexCO2.class;
    }

}
