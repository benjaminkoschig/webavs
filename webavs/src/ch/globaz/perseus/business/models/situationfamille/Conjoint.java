/**
 * 
 */
package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author DDE
 * 
 */
public class Conjoint extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private MembreFamille membreFamille = null;
    private SimpleConjoint simpleConjoint = null;

    public Conjoint() {
        super();
        simpleConjoint = new SimpleConjoint();
        membreFamille = new MembreFamille();
    }

    @Override
    public String getId() {
        return simpleConjoint.getIdConjoint();
    }

    /**
     * @return the membreFamille
     */
    public MembreFamille getMembreFamille() {
        return membreFamille;
    }

    /**
     * @return the simpleConjoint
     */
    public SimpleConjoint getSimpleConjoint() {
        return simpleConjoint;
    }

    @Override
    public String getSpy() {
        return simpleConjoint.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleConjoint.setId(id);
    }

    /**
     * @param membreFamille
     *            the membreFamille to set
     */
    public void setMembreFamille(MembreFamille membreFamille) {
        this.membreFamille = membreFamille;
    }

    /**
     * @param simpleConjoint
     *            the simpleConjoint to set
     */
    public void setSimpleConjoint(SimpleConjoint simpleConjoint) {
        this.simpleConjoint = simpleConjoint;
    }

    @Override
    public void setSpy(String spy) {
        simpleConjoint.setSpy(spy);
    }

}
