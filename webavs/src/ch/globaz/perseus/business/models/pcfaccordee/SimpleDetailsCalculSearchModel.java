/**
 * 
 */
package ch.globaz.perseus.business.models.pcfaccordee;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author DDE
 * 
 */
public class SimpleDetailsCalculSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPCFAccordee = null;
    private String forTypeData = null;

    public String getForIdPCFAccordee() {
        return forIdPCFAccordee;
    }

    public void setForIdPCFAccordee(String forIdPCFAccordee) {
        this.forIdPCFAccordee = forIdPCFAccordee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleDetailsCalcul.class;
    }

    public void setForTypeData(String forTypeData) {
        this.forTypeData = forTypeData;
    }

    public String getForTypeData() {
        return forTypeData;
    }

}
