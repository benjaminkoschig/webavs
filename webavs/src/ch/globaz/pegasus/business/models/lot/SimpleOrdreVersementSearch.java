/**
 * 
 */
package ch.globaz.pegasus.business.models.lot;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author BSC
 * 
 */
public class SimpleOrdreVersementSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String SUPPRESSION_WHERE_KEY = "suppressionOrdresVersement";

    private String forIdOrdreVersement = null;
    private List<String> forInIdPrestation = new ArrayList<String>();

    public String getForIdOrdreVersement() {
        return forIdOrdreVersement;
    }

    public List<String> getForInIdPrestation() {
        return forInIdPrestation;
    }

    public void setForIdOrdreVersement(String forIdOrdreVersement) {
        this.forIdOrdreVersement = forIdOrdreVersement;
    }

    public void setForInIdPrestation(List<String> forInIdPrestation) {
        this.forInIdPrestation = forInIdPrestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleOrdreVersement.class;
    }

}
