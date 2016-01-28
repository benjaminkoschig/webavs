package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class DroitMembreFamilleEtenduSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> forCsRoletMembreFamilleIn = null;
    private String forIdDemande = null;
    private String forIdDroit = null;

    /**
     * @return the forCsRoletMembreFamilleIn
     */
    public List<String> getForCsRoletMembreFamilleIn() {
        return forCsRoletMembreFamilleIn;
    }

    /**
     * @return the forIdDemande
     */
    public String getForIdDemande() {
        return forIdDemande;
    }

    /**
     * @return the forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @param forCsRoletMembreFamilleIn
     *            the forCsRoletMembreFamilleIn to set
     */
    public void setForCsRoletMembreFamilleIn(List<String> forCsRoletMembreFamilleIn) {
        this.forCsRoletMembreFamilleIn = forCsRoletMembreFamilleIn;
    }

    /**
     * @param forIdDemande
     *            the forIdDemande to set
     */
    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    /**
     * @param forIdDroit
     *            the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    @Override
    public Class<DroitMembreFamilleEtendu> whichModelClass() {
        return DroitMembreFamilleEtendu.class;
    }

}
