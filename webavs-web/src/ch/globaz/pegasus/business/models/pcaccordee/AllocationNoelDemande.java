package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;

public class AllocationNoelDemande extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleAllocationNoel simpleAllocationNoel = null;
    private SimpleDemande simpleDemande = null;

    public AllocationNoelDemande() {
        super();
        simpleAllocationNoel = new SimpleAllocationNoel();
        simpleDemande = new SimpleDemande();
    }

    @Override
    public String getId() {
        return simpleAllocationNoel.getId();
    }

    public SimpleAllocationNoel getSimpleAllocationNoel() {
        return simpleAllocationNoel;
    }

    public SimpleDemande getSimpleDemande() {
        return simpleDemande;
    }

    @Override
    public String getSpy() {
        return simpleAllocationNoel.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleAllocationNoel.setId(id);
    }

    public void setSimpleAllocationNoel(SimpleAllocationNoel simpleAllocationNoel) {
        this.simpleAllocationNoel = simpleAllocationNoel;
    }

    public void setSimpleDemande(SimpleDemande simpleDemande) {
        this.simpleDemande = simpleDemande;
    }

    @Override
    public void setSpy(String spy) {
        simpleAllocationNoel.setSpy(spy);
    }
}
