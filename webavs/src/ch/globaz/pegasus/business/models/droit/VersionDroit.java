/**
 * 
 */
package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.demande.Demande;

/**
 * @author SCE
 * 
 *         19 août 2010
 */
public class VersionDroit extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Demande demande = null;
    private SimpleDroit simpleDroit = null;
    private SimpleVersionDroit simpleVersionDroit = null;

    /**
	 * 
	 */
    public VersionDroit() {
        super();
        simpleDroit = new SimpleDroit();
        simpleVersionDroit = new SimpleVersionDroit();
        demande = new Demande();
    }

    /**
     * @return the demande
     */
    public Demande getDemande() {
        return demande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleVersionDroit.getId();
    }

    /**
     * @return the simpleDroit
     */
    public SimpleDroit getSimpleDroit() {
        return simpleDroit;
    }

    /**
     * @return the simpleVersionDroit
     */
    public SimpleVersionDroit getSimpleVersionDroit() {
        return simpleVersionDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleVersionDroit.getSpy();
    }

    /**
     * @param demande
     *            the demande to set
     */
    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleVersionDroit.setId(id);

    }

    /**
     * @param simpleDroit
     *            the simpleDroit to set
     */
    public void setSimpleDroit(SimpleDroit simpleDroit) {
        this.simpleDroit = simpleDroit;
    }

    /**
     * @param simpleVersionDroit
     *            the simpleVersionDroit to set
     */
    public void setSimpleVersionDroit(SimpleVersionDroit simpleVersionDroit) {
        this.simpleVersionDroit = simpleVersionDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleVersionDroit.setSpy(spy);

    }

    public boolean isInitial() {
        return simpleVersionDroit.isInitial();
    }

}
