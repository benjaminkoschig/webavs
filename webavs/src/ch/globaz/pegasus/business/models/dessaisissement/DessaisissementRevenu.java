/**
 * 
 */
package ch.globaz.pegasus.business.models.dessaisissement;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

/**
 * @author BSC
 * 
 */
public class DessaisissementRevenu extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private MembreFamilleEtendu membreFamilleEtendu = null;
    private SimpleDessaisissementRevenu simpleDessaisissementRevenu = null;

    /**
	 * 
	 */
    public DessaisissementRevenu() {
        super();
        simpleDessaisissementRevenu = new SimpleDessaisissementRevenu();
        membreFamilleEtendu = new MembreFamilleEtendu();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleDessaisissementRevenu.getId();
    }

    /**
     * @return the membreFamilleEtendu
     */
    public MembreFamilleEtendu getMembreFamilleEtendu() {
        return membreFamilleEtendu;
    }

    /**
     * @return the simpleDessaisissementRevenu
     */
    public SimpleDessaisissementRevenu getSimpleDessaisissementRevenu() {
        return simpleDessaisissementRevenu;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleDessaisissementRevenu.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleDessaisissementRevenu.setId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel #setIsNew()
     */
    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleDessaisissementRevenu.setId(null);
        simpleDessaisissementRevenu.setSpy(null);
    }

    /**
     * @param membreFamilleEtendu
     *            the membreFamilleEtendu to set
     */
    public void setMembreFamilleEtendu(MembreFamilleEtendu membreFamilleEtendu) {
        this.membreFamilleEtendu = membreFamilleEtendu;
    }

    /**
     * @param simpleDessaisissementRevenu
     *            the simpleDessaisissementRevenu to set
     */
    public void setSimpleDessaisissementRevenu(SimpleDessaisissementRevenu simpleDessaisissementRevenu) {
        this.simpleDessaisissementRevenu = simpleDessaisissementRevenu;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleDessaisissementRevenu.setIdDonneeFinanciereHeader(simpleDonneeFinanciereHeader.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleDessaisissementRevenu.setSpy(spy);
    }

}
