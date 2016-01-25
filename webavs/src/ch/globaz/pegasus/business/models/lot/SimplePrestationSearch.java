/**
 * 
 */
package ch.globaz.pegasus.business.models.lot;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

/**
 * @author SCE
 * 
 *         14 juil. 2010
 */
public class SimplePrestationSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat = null;
    private String forDate = null;
    // private String forIdDecisionHeader = null;
    private String forIdLot = null;
    private String forIdPrestation = null;
    private String forIdVersionDroit = null;
    private List<String> inIdPrestation = null;

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForDate() {
        return forDate;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public String getForIdPrestation() {
        return forIdPrestation;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public List<String> getInIdPrestation() {
        return inIdPrestation;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForIdPrestation(String forIdPrestation) {
        this.forIdPrestation = forIdPrestation;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public void setInIdPrestation(List<String> inIdPrestation) {
        this.inIdPrestation = inIdPrestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<SimplePrestation> whichModelClass() {
        return SimplePrestation.class;
    }

}
