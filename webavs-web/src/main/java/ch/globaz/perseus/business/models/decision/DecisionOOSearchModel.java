/**
 * 
 */
package ch.globaz.perseus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author MBO
 * 
 */
public class DecisionOOSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAdressCourrier = null;
    private String forIdDecision = null;
    private String forNumeroDecision = null;
    private String forNumeroNss = null;

    public String getForIdAdressCourrier() {
        return forIdAdressCourrier;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForNumeroDecision() {
        return forNumeroDecision;
    }

    public String getForNumeroNss() {
        return forNumeroNss;
    }

    public void setForIdAdressCourrier(String forIdAdressCourrier) {
        this.forIdAdressCourrier = forIdAdressCourrier;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForNumeroDecision(String forNumeroDecision) {
        this.forNumeroDecision = forNumeroDecision;
    }

    public void setForNumeroNss(String forNumeroNss) {
        this.forNumeroNss = forNumeroNss;
    }

    @Override
    public Class whichModelClass() {
        return DecisionOO.class;
    }

}
