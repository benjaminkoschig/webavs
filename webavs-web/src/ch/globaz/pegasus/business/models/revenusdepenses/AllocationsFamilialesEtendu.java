package ch.globaz.pegasus.business.models.revenusdepenses;

import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;

public class AllocationsFamilialesEtendu extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AllocationsFamiliales allocationsFamiliales = null;
    private DroitMembreFamille droitMembreFamille = null;
    private DroitMembreFamille droitMembreFamilleRequerant = null;
    private SimpleDemande simpleDemande = null;
    private SimpleDroit simpleDroit = null;

    /**
	 * 
	 */
    public AllocationsFamilialesEtendu() {
        super();
        allocationsFamiliales = new AllocationsFamiliales();
        droitMembreFamille = new DroitMembreFamille();
        droitMembreFamilleRequerant = new DroitMembreFamille();
        simpleDroit = new SimpleDroit();
        simpleDemande = new SimpleDemande();
    }

    public AllocationsFamiliales getAllocationsFamiliales() {
        return allocationsFamiliales;
    }

    public DroitMembreFamille getDroitMembreFamille() {
        return droitMembreFamille;
    }

    public DroitMembreFamille getDroitMembreFamilleRequerant() {
        return droitMembreFamilleRequerant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return allocationsFamiliales.getSimpleAllocationsFamiliales().getId();
    }

    public SimpleDemande getSimpleDemande() {
        return simpleDemande;
    }

    public SimpleDroit getSimpleDroit() {
        return simpleDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return allocationsFamiliales.getSimpleAllocationsFamiliales().getSpy();
    }

    public void setAllocationsFamiliales(AllocationsFamiliales allocationsFamiliales) {
        this.allocationsFamiliales = allocationsFamiliales;
    }

    public void setDroitMembreFamille(DroitMembreFamille droitMembreFamille) {
        this.droitMembreFamille = droitMembreFamille;
    }

    public void setDroitMembreFamilleRequerant(DroitMembreFamille droitMembreFamilleRequerant) {
        this.droitMembreFamilleRequerant = droitMembreFamilleRequerant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        allocationsFamiliales.getSimpleAllocationsFamiliales().setId(id);
    }

    @Override
    public void setIsNew() {
        super.setIsNew();
        allocationsFamiliales.getSimpleAllocationsFamiliales().setId(null);
        allocationsFamiliales.getSimpleAllocationsFamiliales().setSpy(null);
    }

    public void setSimpleDemande(SimpleDemande simpleDemande) {
        this.simpleDemande = simpleDemande;
    }

    public void setSimpleDroit(SimpleDroit simpleDroit) {
        this.simpleDroit = simpleDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        allocationsFamiliales.getSimpleAllocationsFamiliales().setSpy(spy);
    }

}
