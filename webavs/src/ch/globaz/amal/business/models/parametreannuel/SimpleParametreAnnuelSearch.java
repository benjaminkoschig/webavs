/**
 * 
 */
package ch.globaz.amal.business.models.parametreannuel;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

/**
 * @author CBU
 * 
 */
public class SimpleParametreAnnuelSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeParametre = null;
    private String forCodeTypeParametre = null;
    private String fromAnneeParametre = null;
    private List<String> inCodeTypeParametre = null;
    private String untilAnneeParametre = null;

    public String getForAnneeParametre() {
        return forAnneeParametre;
    }

    public String getForCodeTypeParametre() {
        return forCodeTypeParametre;
    }

    public String getFromAnneeParametre() {
        return fromAnneeParametre;
    }

    public List<String> getInCodeTypeParametre() {
        return inCodeTypeParametre;
    }

    public String getUntilAnneeParametre() {
        return untilAnneeParametre;
    }

    public void setForAnneeParametre(String forAnneeParametre) {
        this.forAnneeParametre = forAnneeParametre;
    }

    public void setForCodeTypeParametre(String forCodeTypeParametre) {
        this.forCodeTypeParametre = forCodeTypeParametre;
    }

    public void setFromAnneeParametre(String fromAnneeParametre) {
        this.fromAnneeParametre = fromAnneeParametre;
    }

    public void setInCodeTypeParametre(List<String> inCodeTypeParametre) {
        this.inCodeTypeParametre = inCodeTypeParametre;
    }

    public void setUntilAnneeParametre(String untilAnneeParametre) {
        this.untilAnneeParametre = untilAnneeParametre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleParametreAnnuel.class;
    }

}
