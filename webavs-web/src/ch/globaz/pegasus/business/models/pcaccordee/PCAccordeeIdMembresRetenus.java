package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;

public class PCAccordeeIdMembresRetenus extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    DroitMembreFamille membreFamille = null;
    SimplePCAccordee simplePCAccordee = null;
    SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul = null;
    SimplePlanDeCalcul simplePlanDeCalcul = null;

    public PCAccordeeIdMembresRetenus() {
        simplePCAccordee = new SimplePCAccordee();
        membreFamille = new DroitMembreFamille();
        simplePlanDeCalcul = new SimplePlanDeCalcul();
        simplePersonneDansPlanCalcul = new SimplePersonneDansPlanCalcul();

    }

    @Override
    public String getId() {
        return simplePCAccordee.getIdPCAccordee();
    }

    public DroitMembreFamille getMembreFamille() {
        return membreFamille;
    }

    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    public SimplePersonneDansPlanCalcul getSimplePersonneDansPlanCalcul() {
        return simplePersonneDansPlanCalcul;
    }

    public SimplePlanDeCalcul getSimplePlanDeCalcul() {
        return simplePlanDeCalcul;
    }

    @Override
    public String getSpy() {
        return simplePCAccordee.getSpy();
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    public void setMembreFamille(DroitMembreFamille membreFamille) {
        this.membreFamille = membreFamille;
    }

    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    public void setSimplePersonneDansPlanCalcul(SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul) {
        this.simplePersonneDansPlanCalcul = simplePersonneDansPlanCalcul;
    }

    public void setSimplePlanDeCalcul(SimplePlanDeCalcul simplePlanDeCalcul) {
        this.simplePlanDeCalcul = simplePlanDeCalcul;
    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

}
