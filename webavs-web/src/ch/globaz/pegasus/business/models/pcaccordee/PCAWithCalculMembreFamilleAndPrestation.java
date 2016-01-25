package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;

/**
 * @author DMA
 * 
 */
public class PCAWithCalculMembreFamilleAndPrestation extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDossier = null;
    PlanDeCalculWitMembreFamille planDeCalculWitMembreFamille = null;
    SimplePCAccordee simplePCAccordee = null;
    SimplePrestationsAccordees simplePrestationsAccordees = null;

    public PCAWithCalculMembreFamilleAndPrestation() {
        super();
        simplePCAccordee = new SimplePCAccordee();
        simplePrestationsAccordees = new SimplePrestationsAccordees();
        planDeCalculWitMembreFamille = new PlanDeCalculWitMembreFamille();
    }

    @Override
    public String getId() {
        return simplePCAccordee.getId();
    }

    public String getIdDossier() {
        return idDossier;
    }

    public PlanDeCalculWitMembreFamille getPlanDeCalculWitMembreFamille() {
        return planDeCalculWitMembreFamille;
    }

    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    public SimplePrestationsAccordees getSimplePrestationsAccordees() {
        return simplePrestationsAccordees;
    }

    @Override
    public String getSpy() {
        return simplePCAccordee.getSpy();
    }

    @Override
    public void setId(String id) {
        simplePCAccordee.setId(id);
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setPlanDeCalculWitMembreFamille(PlanDeCalculWitMembreFamille planDeCalculWitMembreFamille) {
        this.planDeCalculWitMembreFamille = planDeCalculWitMembreFamille;
    }

    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    public void setSimplePrestationsAccordees(SimplePrestationsAccordees simplePrestationsAccordees) {
        this.simplePrestationsAccordees = simplePrestationsAccordees;
    }

    @Override
    public void setSpy(String spy) {
        simplePCAccordee.setSpy(spy);
    }

}
