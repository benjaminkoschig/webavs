/**
 * 
 */
package ch.globaz.pegasus.business.models.dessaisissement;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

/**
 * @author ECO
 * 
 */
public class DessaisissementFortune extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private MembreFamilleEtendu membreFamilleEtendu = null;
    private SimpleDessaisissementFortune simpleDessaisissementFortune = null;

    /**
	 * 
	 */
    public DessaisissementFortune() {
        super();
        simpleDessaisissementFortune = new SimpleDessaisissementFortune();
        membreFamilleEtendu = new MembreFamilleEtendu();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleDessaisissementFortune.getId();
    }

    /**
     * @return the membreFamilleEtendu
     */
    public MembreFamilleEtendu getMembreFamilleEtendu() {
        return membreFamilleEtendu;
    }

    /**
     * @return the simpleDessaisissementFortune
     */
    public SimpleDessaisissementFortune getSimpleDessaisissementFortune() {
        return simpleDessaisissementFortune;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleDessaisissementFortune.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleDessaisissementFortune.setId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel #setIsNew()
     */
    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleDessaisissementFortune.setId(null);
        simpleDessaisissementFortune.setSpy(null);
    }

    /**
     * @param membreFamilleEtendu
     *            the membreFamilleEtendu to set
     */
    public void setMembreFamilleEtendu(MembreFamilleEtendu membreFamilleEtendu) {
        this.membreFamilleEtendu = membreFamilleEtendu;
    }

    /**
     * @param simpleDessaisissementFortune
     *            the simpleDessaisissementFortune to set
     */
    public void setSimpleDessaisissementFortune(SimpleDessaisissementFortune simpleDessaisissementFortune) {
        this.simpleDessaisissementFortune = simpleDessaisissementFortune;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleDessaisissementFortune.setIdDonneeFinanciereHeader(simpleDonneeFinanciereHeader.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleDessaisissementFortune.setSpy(spy);
    }

}
