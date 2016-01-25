/**
 * 
 */
package ch.globaz.perseus.business.models.parametres;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author DDE
 * 
 */
public class LoyerSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String WITH_DATE_VALABLE_LE = "withDateValable";

    private String forCsTypeLoyer = null;
    private String forDateValable = null;
    private String forIdZone = null;

    public LoyerSearchModel() {
        super();
    }

    public String getForCsTypeLoyer() {
        return forCsTypeLoyer;
    }

    public String getForDateValable() {
        return forDateValable;
    }

    public String getForIdZone() {
        return forIdZone;
    }

    public void setForCsTypeLoyer(String forCsTypeLoyer) {
        this.forCsTypeLoyer = forCsTypeLoyer;
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
        if (!JadeStringUtil.isEmpty(forDateValable)) {
            setWhereKey(LoyerSearchModel.WITH_DATE_VALABLE_LE);
        }
    }

    public void setForIdZone(String forIdZone) {
        this.forIdZone = forIdZone;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return Loyer.class;
    }

}
