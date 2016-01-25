package ch.globaz.pegasus.business.models.revenusdepenses;

import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;

public class RevenuActiviteLucrativeIndependanteEtendu extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DroitMembreFamille droitMembreFamille = null;
    private RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante = null;
    private SimpleDemande simpleDemande = null;
    private SimpleDroit simpleDroit = null;

    /**
	 * 
	 */
    public RevenuActiviteLucrativeIndependanteEtendu() {
        super();
        droitMembreFamille = new DroitMembreFamille();
        simpleDroit = new SimpleDroit();
        simpleDemande = new SimpleDemande();
        revenuActiviteLucrativeIndependante = new RevenuActiviteLucrativeIndependante();
    }

    public DroitMembreFamille getDroitMembreFamille() {
        return droitMembreFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return revenuActiviteLucrativeIndependante.getId();
    }

    public RevenuActiviteLucrativeIndependante getRevenuActiviteLucrativeIndependante() {
        return revenuActiviteLucrativeIndependante;
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
        return revenuActiviteLucrativeIndependante.getSpy();
    }

    public void setDroitMembreFamille(DroitMembreFamille droitMembreFamille) {
        this.droitMembreFamille = droitMembreFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        revenuActiviteLucrativeIndependante.setId(id);
    }

    public void setRevenuActiviteLucrativeIndependante(
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante) {
        this.revenuActiviteLucrativeIndependante = revenuActiviteLucrativeIndependante;
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
        revenuActiviteLucrativeIndependante.setSpy(spy);
    }

}