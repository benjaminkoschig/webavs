/**
 * 
 */
package ch.globaz.amal.business.models.revenu;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;

/**
 * @author CBU
 * 
 */
public class RevenuSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeTaxation = null;
    private String forDateAvisTaxation = null;
    private String forIdContribuable = null;
    private String forIdRevenu = null;
    private String forTypeRevenu = null;
    private String forTypeTaxation = null;
    private ArrayList<String> inTypeTaxation = null;

    /**
	 * 
	 */
    public RevenuSearch() {
        forIdRevenu = new String();
        forIdContribuable = new String();
        forAnneeTaxation = new String();
    }

    /**
     * @return the forIdAnneeTaxation
     */
    public String getForAnneeTaxation() {
        return forAnneeTaxation;
    }

    public String getForDateAvisTaxation() {
        return forDateAvisTaxation;
    }

    /**
     * @return forIdContribuable
     */
    public String getForIdContribuable() {
        return forIdContribuable;
    }

    /**
     * @return forIdRevenu
     */
    public String getForIdRevenu() {
        return forIdRevenu;
    }

    public String getForTypeRevenu() {
        return forTypeRevenu;
    }

    public String getForTypeTaxation() {
        return forTypeTaxation;
    }

    public ArrayList<String> getInTypeTaxation() {
        return inTypeTaxation;
    }

    /**
     * @param forIdAnneeTaxation
     *            the forIdAnneeTaxation to set
     */
    public void setForAnneeTaxation(String forAnneeTaxation) {
        this.forAnneeTaxation = forAnneeTaxation;
    }

    public void setForDateAvisTaxation(String forDateAvisTaxation) {
        this.forDateAvisTaxation = forDateAvisTaxation;
    }

    /**
     * @param forIdContribuable
     */
    public void setForIdContribuable(String forIdContribuable) {
        this.forIdContribuable = forIdContribuable;
    }

    /**
     * @param forIdRevenu
     */
    public void setForIdRevenu(String forIdRevenu) {
        this.forIdRevenu = forIdRevenu;
    }

    public void setForTypeRevenu(String forTypeRevenu) {
        this.forTypeRevenu = forTypeRevenu;
    }

    public void setForTypeTaxation(String forTypeTaxation) {
        this.forTypeTaxation = forTypeTaxation;
    }

    public void setInTypeTaxation(ArrayList<String> inTypeTaxation) {
        this.inTypeTaxation = inTypeTaxation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return Revenu.class;
    }

}
