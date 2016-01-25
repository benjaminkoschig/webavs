/**
 * 
 */
package ch.globaz.amal.business.models.deductionsfiscalesenfants;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author CBU
 * 
 */
public class SimpleDeductionsFiscalesEnfantsSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeTaxation = null;
    private String forAnneeTaxationGOE = null;
    private String forAnneeTaxationLOE = null;
    private String forNbEnfant = null;
    private String forNbEnfantGOE = null;
    private String forNbEnfantLOE = null;

    /**
     * @return
     */
    public String getForAnneeTaxation() {
        return forAnneeTaxation;
    }

    public String getForAnneeTaxationGOE() {
        return forAnneeTaxationGOE;
    }

    /**
     * @return the forAnneeTaxationLOE
     */
    public String getForAnneeTaxationLOE() {
        return forAnneeTaxationLOE;
    }

    public String getForNbEnfant() {
        return forNbEnfant;
    }

    public String getForNbEnfantGOE() {
        return forNbEnfantGOE;
    }

    /**
     * @return the forNbEnfantLOE
     */
    public String getForNbEnfantLOE() {
        return forNbEnfantLOE;
    }

    /**
     * @param forAnneeTaxation
     */
    public void setForAnneeTaxation(String forAnneeTaxation) {
        this.forAnneeTaxation = forAnneeTaxation;
    }

    public void setForAnneeTaxationGOE(String forAnneeTaxationGOE) {
        this.forAnneeTaxationGOE = forAnneeTaxationGOE;
    }

    /**
     * @param forAnneeTaxationLOE
     *            the forAnneeTaxationLOE to set
     */
    public void setForAnneeTaxationLOE(String forAnneeTaxationLOE) {
        this.forAnneeTaxationLOE = forAnneeTaxationLOE;
    }

    public void setForNbEnfant(String forNbEnfant) {
        this.forNbEnfant = forNbEnfant;
    }

    public void setForNbEnfantGOE(String forNbEnfantGOE) {
        this.forNbEnfantGOE = forNbEnfantGOE;
    }

    /**
     * @param forNbEnfantLOE
     *            the forNbEnfantLOE to set
     */
    public void setForNbEnfantLOE(String forNbEnfantLOE) {
        this.forNbEnfantLOE = forNbEnfantLOE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleDeductionsFiscalesEnfants.class;
    }

}
