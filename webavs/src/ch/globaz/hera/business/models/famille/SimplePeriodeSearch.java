/**
 * 
 */
package ch.globaz.hera.business.models.famille;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

/**
 * @author BSC
 * 
 */
public class SimplePeriodeSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdMembreFamille = null;
    private List forIdMembreFamilleIn = null;
    private String forType = null;
    private List forTypeIn = null;

    /**
     * getter pour l'attribut forIdMembreFamille
     * 
     * @return
     */
    public String getForIdMembreFamille() {
        return forIdMembreFamille;
    }

    /**
     * getter pour l'attribut forIdMembreFamilleIn
     * 
     * @return
     */
    public List getForIdMembreFamilleIn() {
        return forIdMembreFamilleIn;
    }

    /**
     * getter pour l'attribut forType
     * 
     * @return
     */
    public String getForType() {
        return forType;
    }

    /**
     * getter pour l'attribut forTypeIn
     * 
     * @return
     */
    public List getForTypeIn() {
        return forTypeIn;
    }

    /**
     * setter pour l'attribut forIdMembreFamille
     * 
     * @param forIdMembreFamille
     */
    public void setForIdMembreFamille(String forIdMembreFamille) {
        this.forIdMembreFamille = forIdMembreFamille;
    }

    /**
     * setter pour l'attribut forIdMembreFamilleIn
     * 
     * @param forIdMembreFamilleIn
     */
    public void setForIdMembreFamilleIn(List forIdMembreFamilleIn) {
        this.forIdMembreFamilleIn = forIdMembreFamilleIn;
    }

    /**
     * setter pour l'attribut forType
     * 
     * @param forType
     */
    public void setForType(String forType) {
        this.forType = forType;
    }

    /**
     * setter pour l'attribut forTypeIn
     * 
     * @param forTypeIn
     */
    public void setForTypeIn(List forTypeIn) {
        this.forTypeIn = forTypeIn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimplePeriode.class;
    }

}
