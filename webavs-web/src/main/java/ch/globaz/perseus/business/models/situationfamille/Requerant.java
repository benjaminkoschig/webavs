/**
 * 
 */
package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author DDE
 * 
 */
public class Requerant extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private MembreFamille membreFamille = null;
    private SimpleRequerant simpleRequerant = null;

    public Requerant() {
        super();
        simpleRequerant = new SimpleRequerant();
        membreFamille = new MembreFamille();
    }

    @Override
    public String getId() {
        return simpleRequerant.getId();
    }

    /**
     * @return the membreFamille
     */
    public MembreFamille getMembreFamille() {
        return membreFamille;
    }

    /**
     * @return the simpleRequerant
     */
    public SimpleRequerant getSimpleRequerant() {
        return simpleRequerant;
    }

    @Override
    public String getSpy() {
        return simpleRequerant.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleRequerant.setId(id);
    }

    /**
     * @param membreFamille
     *            the membreFamille to set
     */
    public void setMembreFamille(MembreFamille membreFamille) {
        this.membreFamille = membreFamille;
    }

    /**
     * @param simpleRequerant
     *            the simpleRequerant to set
     */
    public void setSimpleRequerant(SimpleRequerant simpleRequerant) {
        this.simpleRequerant = simpleRequerant;
    }

    @Override
    public void setSpy(String spy) {
        simpleRequerant.setSpy(spy);
    }

}
